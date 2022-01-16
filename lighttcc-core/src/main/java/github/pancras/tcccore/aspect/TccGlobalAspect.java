package github.pancras.tcccore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

import github.pancras.tcccore.TxManager;
import github.pancras.tcccore.dto.BranchTx;
import github.pancras.tcccore.dto.TccActionContext;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class TccGlobalAspect {
    private final TxManager txManager = TxManager.INSTANCE;

    @Around("@annotation(github.pancras.tcccore.annotation.TccGlobal)")
    public Object invoke(ProceedingJoinPoint point) {
        log.info("切入TccGloabl");
        // 1. 创建全局事务ID
        String xid = txManager.newGlobalTransaction();
        Object result = null;
        // 2. 执行全局事务TccGloabl
        try {
            // 2.1 注入TccActionContext
            Object[] args = point.getArgs();
            args[0] = new TccActionContext(xid, null);
            result = point.proceed(args);
            // 2.2 @TccGlobal方法执行成功，获取参与的所有分支事务，执行commit()方法
            List<BranchTx> branches = txManager.getBranches(xid);
            System.out.println("需要commit的分支数：" + branches.size());
            for (BranchTx branch : branches) {
                doCommit(branch);
            }
            txManager.removeGlobalTransaction(xid);
        } catch (Throwable e) {
            // 2.2 @TccGlobal方法执行失败，获取参与的所有分支事务，执行rollback()方法
            List<BranchTx> branches = txManager.getBranches(xid);
            System.out.println("需要rollback的分支数：" + branches.size());
            for (BranchTx branch : branches) {
                doCancel(branch);
            }
            e.printStackTrace();
        }
        return result;
    }

    private void doCommit(BranchTx branch) {
        System.out.println("提交事务：" + branch);
    }

    private void doCancel(BranchTx branch) {
        System.out.println("回滚事务" + branch);
    }
}
