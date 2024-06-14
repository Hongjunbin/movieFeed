package com.sparta.moviefeed.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Pointcut("execution(* com.sparta.moviefeed.controller.BoardController.*(..))")
    private void board() {}
    @Pointcut("execution(* com.sparta.moviefeed.controller.CommentController.*(..))")
    private void comment() {}
    @Pointcut("execution(* com.sparta.moviefeed.controller.EmailController.*(..))")
    private void email() {}
    @Pointcut("execution(* com.sparta.moviefeed.controller.MypageController.*(..))")
    private void mypage() {}
    @Pointcut("execution(* com.sparta.moviefeed.controller.UserController.*(..))")
    private void user() {}

    @Around("board() || comment() || email() || mypage() || user()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();

        log.info("[uri : {} ], [httpMethod : {}]", httpMethod, uri);

        return joinPoint.proceed();
    }
}