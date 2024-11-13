package com.example.bookingService.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@EnableAspectJAutoProxy
@Slf4j
@Component
public class Helper {

        @Pointcut("execution(* com.example.bookingService.service.*.*(..))")
        public void serviceMethods(){}


    @Before("serviceMethods()")
    public void logBefore(){
            log.info("Before the method execution");
    }

    @AfterReturning("serviceMethods()")
    public void logAfter(){
            log.info("After the method execution");
    }

    @AfterThrowing(pointcut = "serviceMethods()",throwing = "ex")
    public void logError(Exception ex){
            log.info("Exception occurred"+ex.getMessage(),ex);
    }
}
