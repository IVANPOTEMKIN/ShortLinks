package ru.effective_mobile.shortlinks.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {

    @Pointcut("execution(* ru.effective_mobile.shortlinks.service.impl.LinkServiceImpl.*(..))")
    public void serviceLayerExecution() {
    }

    @Pointcut("execution(* ru.effective_mobile.shortlinks.repository.LinkRepository.*(..))")
    public void repositoryLayerExecution() {
    }
}