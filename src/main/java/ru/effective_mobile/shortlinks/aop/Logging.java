package ru.effective_mobile.shortlinks.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class Logging {

    @Around("""
            ru.effective_mobile.shortlinks.aop.PointCuts.serviceLayerExecution()
            ||
            ru.effective_mobile.shortlinks.aop.PointCuts.repositoryLayerExecution()
            """)
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String methodClass = joinPoint.getSignature().getDeclaringType().getSimpleName();
        Object[] methodArgs = joinPoint.getArgs();
        Object result;

        log.info("Начало работы метода {} класса {} с входными параметрами: {}", methodName, methodClass, methodArgs);

        try {
            result = joinPoint.proceed();

        } catch (Exception e) {
            log.error("Ошибка в работе метода {} класса {}\n{}\n", methodName, methodClass, e.getMessage());
            throw e;
        }

        log.info("Успешное завершение работы метода {} класса {}", methodName, methodClass);
        return result;
    }
}