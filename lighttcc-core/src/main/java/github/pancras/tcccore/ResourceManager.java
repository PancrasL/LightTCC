package github.pancras.tcccore;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
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
    private final String address;
    private HashMap<String, Object> resources = new HashMap<>();
    /**
     * socket server
     */
    private ServerSocket server;
    private ExecutorService threadPool;

    ResourceManager() {
        try {
            server = new ServerSocket(0);
            int localPort = server.getLocalPort();
            address = "127.0.0.1:" + localPort;
            this.threadPool = Executors.newCachedThreadPool();
            Thread serverThread = new Thread(() -> listen());
            serverThread.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void listen() {
        try {
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
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void registBranch(TccActionContext context, String commitMethod, String rollbackMethod, Object resource) {
        String resourceId = resource.getClass().getCanonicalName();
        resources.put(resourceId, resource);
        BranchTx branchTx = new BranchTx();
        branchTx.setBranchId(UUID.randomUUID().toString());
        branchTx.setResourceId(resourceId);
        branchTx.setResourceAddress(address);
        branchTx.setCommitMethod(commitMethod);
        branchTx.setRollbackMethod(rollbackMethod);
        branchTx.setXid(context.getXid());

        txStore.writeBranchTx(branchTx);
    }

    private JSONObject handle(JSONObject jsonObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONObject ret = new JSONObject();
        String command = (String) jsonObject.get("command");
        BranchTx branchTx = jsonObject.getObject("branchTx", BranchTx.class);
        TccActionContext context = jsonObject.getObject("context", TccActionContext.class);
        switch (command) {
            case "commit":
                doCommit(branchTx, context);
                ret.put("result", "success");
                break;
            case "cancel":
                doCancel(branchTx, context);
                ret.put("result", "success");
                break;
            default:
                throw new IllegalStateException("Illegal command: " + command);
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
