package com.hotelcorp.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Component
@Aspect
@Slf4j(topic = "request-log")
public class LogRequestHandler {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMappingMethods() {
    }

    @Pointcut("requestMappingMethods() || " +
            "getMappingMethods() || " +
            "postMappingMethods() || " +
            "putMappingMethods() || " +
            "deleteMappingMethods()")
    public void allRequestMappingMethods() {
    }

    @Before("allRequestMappingMethods()")
    public void logRequest(final JoinPoint joinPoint) {
        final var request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();

        if (request != null) {
            log.info("User {} request: URI={} params={} handled by method: {} with args={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getParameterMap().entrySet()
                            .stream()
                            .map(e -> e.getKey() + "=" + Arrays.toString(e.getValue()))
                            .collect(joining(",")),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs())
                    );
        }
    }
}