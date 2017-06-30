package ksbysample.webapp.lending.aspect.logging;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ???
 */
@Aspect
@Component
public class MethodLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* ksbysample.webapp.lending.web..*.*(..))"
            + "&& @within(org.springframework.stereotype.Controller)")
    private void pointcutControllerMethod() {
    }

    @Pointcut("execution(* ksbysample.webapp.lending.service..*.*(..))"
            + "&& @within(org.springframework.stereotype.Service)")
    private void pointcutServiceMethod() {
    }

    /**
     * @param pjp ???
     * @return ???
     * @throws Throwable
     */
    @Around(value = "pointcutControllerMethod() || pointcutServiceMethod()")
    public Object logginMethod(ProceedingJoinPoint pjp)
            throws Throwable {
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();

        logginBeginMethod(className, methodName, pjp.getArgs());
        Object ret = pjp.proceed();
        logginEndMethod(className, methodName, ret);

        return ret;
    }

    private void logginBeginMethod(String className, String methodName, Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("call : ");
        sb.append(className);
        sb.append("#");
        sb.append(methodName);
        sb.append("(");
        sb.append(ToStringBuilder.reflectionToString(args, ToStringStyle.SIMPLE_STYLE));
        sb.append(")");
        logger.info(sb.toString());
    }

    private void logginEndMethod(String className, String methodName, Object ret) {
        StringBuilder sb = new StringBuilder();
        sb.append("ret = ");
        sb.append(ret);
        sb.append(" : ");
        sb.append(className);
        sb.append("#");
        sb.append(methodName);
        logger.info(sb.toString());
    }

}
