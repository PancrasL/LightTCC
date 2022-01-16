package github.pancras.tcccore;

import com.alibaba.fastjson.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import github.pancras.tcccore.dto.BranchTx;
import github.pancras.tcccore.dto.TccActionContext;
import github.pancras.tcccore.store.TxStore;
import github.pancras.tcccore.store.ZkTxStore;

/**
 * 被TccTryAspect依赖，具有如下功能： 1. 注册分支事务 2. 接收TxManager发来的提交/回滚请求并执行
 */
public enum ResourceManager {
    /**
     * 单例
     */
    INSTANCE;
    private final TxStore txStore = ZkTxStore.INSTANCE;
    private final String address = "127.0.0.1:8010";
    private HashMap<String, Object> resources = new HashMap<>();
    /**
     * socket server
     */
    private ServerSocket server;
    private ExecutorService threadPool;

    ResourceManager() {
        new Thread(() -> {
            try {
                server = new ServerSocket();
                server.bind(new InetSocketAddress("127.0.0.1", 8010));
                this.threadPool = Executors.newCachedThreadPool();
                Socket socket;
                while ((socket = server.accept()) != null) {
                    Socket finalSocket = socket;
                    threadPool.execute(() -> {
                        try (ObjectInputStream in = new ObjectInputStream(finalSocket.getInputStream());
                             ObjectOutputStream out = new ObjectOutputStream(finalSocket.getOutputStream())) {
                            JSONObject jsonObj = (JSONObject) in.readObject();
                            out.writeObject(handle(jsonObj));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void writeBranchTx(BranchTx branchTx) {
        txStore.writeBranchTx(branchTx);
    }

    public void registResource(Object obj) {
        resources.put(obj.getClass().getCanonicalName(), obj);
    }

    public String getAddress() {
        return address;
    }

    private JSONObject handle(JSONObject jsonObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONObject ret = new JSONObject();
        String command = (String) jsonObject.get("command");
        switch (command) {
            case "commit":
                doCommit(jsonObject.getObject("branchTx", BranchTx.class), jsonObject.getObject("context", TccActionContext.class));
                ret.put("result", "commit");
                break;
            case "cancel":
                doCancel(jsonObject.getObject("branchTx", BranchTx.class), jsonObject.getObject("context", TccActionContext.class));
                ret.put("result", "cancel");
                break;
            default:
                break;
        }
        return ret;
    }

    private void doCommit(BranchTx branchTx, TccActionContext context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object r = resources.get(branchTx.getResourceId());
        Method method = r.getClass().getMethod(branchTx.getCommitMethod(), TccActionContext.class);
        method.invoke(r, context);
    }

    private void doCancel(BranchTx branchTx, TccActionContext context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object r = resources.get(branchTx.getResourceId());
        Method method = r.getClass().getMethod(branchTx.getRollbackMethod(), TccActionContext.class);
        method.invoke(r, context);
    }
}
