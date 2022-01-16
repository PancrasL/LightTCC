package github.pancras.tcccore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

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
        String xid = txManager.newXid();
        Object result = null;
        // 2. 执行全局事务TccGloabl
        TccActionContext context = new TccActionContext();
        context.setXid(xid);

        // 2.1 注入TccActionContext
        Object[] args = point.getArgs();
        args[0] = context;
        // 2.2 将Try方法的参数注入到TccActionContext中，传递给commit或cancel方法。
        MethodSignature signature = (MethodSignature) point.getSignature();
        String[] paramNames = signature.getParameterNames();
        for (int i = 1; i < paramNames.length; i++) {
            context.setContext(paramNames[i], args[i]);
        }
        try {
            result = point.proceed(args);
            // 2.3 @TccGlobal方法执行成功，获取参与的所有分支事务，执行commit()方法
            List<BranchTx> branches = txManager.getBranches(xid);
            System.out.println("需要commit的分支数：" + branches.size());
            for (BranchTx branch : branches) {
                // TODO 提交失败，进行日志记录，可以考虑失败重试等补救措施
                if (!txManager.branchCommit(branch, context)) {
                    log.error(branch + " commit 失败");
                }
            }
            txManager.removeGlobalTransaction(xid);
            log.info("Global tx success:" + xid);
        } catch (Throwable e) {
            // 2.3 @TccGlobal方法执行失败，获取参与的所有分支事务，执行rollback()方法
            List<BranchTx> branches = txManager.getBranches(xid);
            System.out.println("需要rollback的分支数：" + branches.size());
            for (BranchTx branch : branches) {
                // TODO 回滚失败，进行日志记录，可以考虑失败重试等补救措施
                if (!txManager.branchCancel(branch, context)) {
                    log.error(branch + " cancel 失败");
                }
            }
            e.printStackTrace();
        }
        return result;
    }
}
