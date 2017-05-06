package ksbysample.webapp.lending.aspect.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAndEventNameLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param pjp              ???
     * @param loggingEventName ???
     * @return ???
     * @throws Throwable
     */
    @Around(value = "execution(* ksbysample.webapp.lending.web..*.*(..)) && @annotation(loggingEventName)"
            , argNames = "pjp, loggingEventName")
    public Object logginControllerAndEventName(ProceedingJoinPoint pjp, LoggingEventName loggingEventName)
            throws Throwable {
        StringBuilder logBegin = new StringBuilder("***** begin *****  ");
        StringBuilder logEnd = new StringBuilder("***** end   *****  ");

        LoggingControllerName loggingGamenName = pjp.getTarget().getClass().getAnnotation(LoggingControllerName.class);
        if (loggingGamenName != null) {
            appendControllerName(logBegin, loggingGamenName.value());
            appendControllerName(logEnd, loggingGamenName.value());
        }
        appendEventName(logBegin, loggingEventName.value());
        appendEventName(logEnd, loggingEventName.value());

        logger.info(logBegin.toString());
        Object ret = pjp.proceed();
        logger.info(logEnd.toString());

        return ret;
    }

    private void appendControllerName(StringBuilder sb, String loggingGamenName) {
        sb.append("ControllerName = ");
        sb.append(loggingGamenName);
        sb.append(", ");
    }

    private void appendEventName(StringBuilder sb, String loggingEventName) {
        sb.append("EventName = ");
        sb.append(loggingEventName);
    }

}
