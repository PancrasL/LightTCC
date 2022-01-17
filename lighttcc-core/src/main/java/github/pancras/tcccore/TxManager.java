package github.pancras.tcccore;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import github.pancras.tcccore.dto.BranchTx;
import github.pancras.tcccore.dto.TccActionContext;
import github.pancras.tcccore.store.TxStore;
import github.pancras.tcccore.store.ZkTxStore;
import lombok.extern.slf4j.Slf4j;

/**
 * 被TccGlobalAspect依赖
 */
@Slf4j
public enum TxManager {
    /**
     * 单例
     */
    INSTANCE;
    private final TxStore txStore = ZkTxStore.INSTANCE;

    TxManager() {
    }

    /**
     * 生成全局事务ID
     *
     * @return Xid
     */
    public String newXid() {
        String xid = UUID.randomUUID().toString();
        txStore.writeXid(xid);
        log.info("创建全局事务ID：" + xid);
        return xid;
    }

    public List<BranchTx> getBranches(String xid) {
        return txStore.readBranches(xid);
    }

    public void removeGlobalTransaction(String xid) {
        txStore.deleteXid(xid);
    }

    public boolean branchCommit(BranchTx branchTx, TccActionContext context) {
        JSONObject sendMsg = new JSONObject();
        sendMsg.put("command", "commit");
        sendMsg.put("branchTx", branchTx);
        sendMsg.put("context", context);
        String address = branchTx.getResourceAddress();
        JSONObject recvMsg = sendMessage(address, sendMsg);
        return "success".equals(recvMsg.get("result"));
    }

    public boolean branchCancel(BranchTx branchTx, TccActionContext context) {
        JSONObject sendMsg = new JSONObject();
        sendMsg.put("command", "cancel");
        sendMsg.put("branchTx", branchTx);
        sendMsg.put("context", context);
        String address = branchTx.getResourceAddress();
        JSONObject recvMsg = sendMessage(address, sendMsg);
        return "success".equals(recvMsg.get("result"));
    }

    private JSONObject sendMessage(String address, JSONObject jsonObject) {
        Socket socket = new Socket();
        String[] ipAndPort = address.split(":");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
        JSONObject ret;
        try {
            socket.connect(inetSocketAddress);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(jsonObject);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ret = (JSONObject) in.readObject();
            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return ret;
    }
}
