package com.project.Interceptor;

import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;


@Aspect
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public Object getParameterByName(JoinPoint proceedingJoinPoint, String parameterName) {
        MethodSignature methodSig = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        String[] parametersName = methodSig.getParameterNames();

        int idx = Arrays.asList(parametersName).indexOf(parameterName);

        if(args.length > idx && args.length > 0) { // parameter exist
            return args[idx];
        } // otherwise your parameter does not exist by given name
        return null;

    }
    @AfterReturning(
            pointcut = "(execution(* com.project.Controllers.ResidenceController.*(..))) ||" +
                    "(execution(* com.project.Controllers.ColoniesController.*(..))) ||" +
                    "(execution(* com.project.Controllers.StreetsController.*(..))) ||" +
                    "(execution(* com.project.Controllers.TransactionsController.*(..))) ||" +
                    "(execution(* com.project.Controllers.UsersController.*(..))) ",
            returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof BasicResponseModel) {
            BasicResponseModel responseModel = (BasicResponseModel) result;
            AuthUser authUserParameter = (AuthUser) getParameterByName(joinPoint, "authUser");
            responseModel.setAuthUser(authUserParameter);
        }
    }

}
