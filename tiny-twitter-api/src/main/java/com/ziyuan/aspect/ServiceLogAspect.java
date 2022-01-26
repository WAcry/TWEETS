package com.ziyuan.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger log =
            LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * This around type aspect calls when service.impl start and end to record time cost.
     */
    @Around("execution(* com.ziyuan.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("====== Start: {}.{} ======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        // record start time
        long begin = System.currentTimeMillis();

        // do service
        Object result = joinPoint.proceed();

        // record end time
        long end = System.currentTimeMillis();
        long takeTime = end - begin;

        if (takeTime > 3000) {
            log.error("====== End，costs：{} ms ======", takeTime);
        } else if (takeTime > 2000) {
            log.warn("====== End，costs：{} ms ======", takeTime);
        } else {
            log.info("====== End，costs：{} ms ======", takeTime);
        }

        return result;
    }

}
