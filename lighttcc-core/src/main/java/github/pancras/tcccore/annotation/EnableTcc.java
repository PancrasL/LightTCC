package github.pancras.tcccore.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import github.pancras.tcccore.aspect.TccGlobalAspect;
import github.pancras.tcccore.aspect.TccTryAspect;

/**
 * 开启TCC切面支持
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({TccGlobalAspect.class, TccTryAspect.class})
public @interface EnableTcc {
}
