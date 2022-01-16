package github.pancras.tcccore;

import java.util.List;
import java.util.UUID;

import github.pancras.tcccore.dto.BranchTx;
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
    private TxStore txStore = ZkTxStore.INSTANCE;

    TxManager() {
    }

    public TxStore getTxStore() {
        return txStore;
    }

    public String newGlobalTransaction() {
        String xid = UUID.randomUUID().toString();
        txStore.writeXid(xid);
        log.info("创建全局事务：" + xid);
        return xid;
    }

    public List<BranchTx> getBranches(String xid) {
        return txStore.readBranches(xid);
    }

    public void removeGlobalTransaction(String xid) {
        txStore.deleteXid(xid);
    }
}
