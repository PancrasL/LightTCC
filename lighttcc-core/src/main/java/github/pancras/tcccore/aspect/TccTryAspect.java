package github.pancras.tcccore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import github.pancras.tcccore.ResourceManager;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

/**
 * 在执行分支事务前将分支事务的状态上报到
 */
@Aspect
public class TccTryAspect {
    private final ResourceManager resourceManager = ResourceManager.INSTANCE;

    @Around("@annotation(github.pancras.tcccore.annotation.TccTry)")
    public Object invoke(ProceedingJoinPoint point) {
        // 1. 获取方法的注解信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        TccTry tccTry = method.getAnnotation(TccTry.class);

        // 2. 注册分支事务
        TccActionContext context = (TccActionContext) point.getArgs()[0];
        String commitMethod = tccTry.commitMethod();
        String rollbackMethod = tccTry.rollbackMethod();
        Object target = point.getTarget();
        resourceManager.registBranch(context, commitMethod, rollbackMethod, target);
        try {
            Object result = point.proceed();
            // 2.1 tccTry执行成功
            System.out.println("TccTry success:" + point.getTarget().getClass().getCanonicalName());
            return result;
        } catch (Throwable throwable) {
            System.out.println("TccTry fail:" + point.getTarget().getClass().getCanonicalName());
            throwable.printStackTrace();
            // 2.2 tccTry执行失败，返回false
            return false;
        }
    }
}
