package io.liang.jsidekiq.client.aop;

import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.pojo.Element;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.liang.jsidekiq.client.config.Config;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * sidekiq 拦截器
 * Created by zhangyouliang on 17/3/5.
 */
@Aspect
@Component
@Configuration
@Order(-1)
public class JSidekiqAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(io.liang.jsidekiq.client.aop.JSidekiqLabel)")
    public void jsidekiqAspect() {

    }

    @Around(value = "jsidekiqAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object obj = null;
        if(JSidekiqRole.isConsumer()) {//如果是consumer执行正常的业务
            obj = joinPoint.proceed();
        }else{
            handleSidekiq(joinPoint);
        }

        return obj;
    }

    private boolean handleSidekiq(JoinPoint joinPoint) {
        boolean flag = false;
        try {
            Signature signature = joinPoint.getSignature();

            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();

            //实现类的方法信息
            Method targetMethod = joinPoint.getTarget().getClass().getMethod(method.getName(),method.getParameterTypes());
            JSidekiqLabel label = targetMethod.getAnnotation(JSidekiqLabel.class);
            if(label != null) {
                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] argValues = joinPoint.getArgs();

                Element element = new Element();

                Class targetClass = joinPoint.getTarget().getClass();

//                Service serviceLabel = (Service)targetClass.getAnnotation(Service.class);
//                System.out.println(serviceLabel.value());

                String targetClassName = targetClass.getName();

                element.setInterfaceClassName(method.getDeclaringClass().getName());
                element.setClassName(targetClassName);
                element.setMethodName(method.getName());


                element.setParams(argValues);
                element.setParamTypes(paramTypes);

                element.setCreatedAt(new Date());


                if (label != null) {
                    String description = label.description();
                    String queue = label.queue();
                    int retry = label.retry();
                    long at = label.at();

                    retry = (retry == -1) ? Config.getInstance().getMaxRetry() : retry;

                    element.setMaxRetry(retry);
                    element.setQueue(queue);
                    if(at > 0){
                        element.setAt(new Date(System.currentTimeMillis() + at));
                    }
                    ClientManager.getInstance().push(element);
                    flag = true;

//                    log.debug("add jsidekiq:{}",element.toString());
                }
            }else{
                log.debug("please config sidekiq @interface to interface class method");

            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return flag;
    }

//    private static JSidekiqLabel giveLabel(JoinPoint joinPoint) throws Exception {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//
////        method.getReturnType(); ResultDO
//
//        Annotation[] ans = joinPoint.getTarget().getClass().getAnnotations();
//        System.out.println("11 ans:"+ans.length);
//        for(int i=0;i<ans.length;i++){
//            Annotation an = ans[i];
//            System.out.println(i+":"+an.toString());
//        }
//
//        Annotation[][] anss = method.getParameterAnnotations();
//
//        ans = method.getAnnotations();
//        System.out.println("22 ans:"+ans.length);
//        for(int i=0;i<ans.length;i++){
//            Annotation an = ans[i];
//            System.out.println(i+":"+an.toString());
//        }
//
//        System.out.println("giveLabel："+method.getAnnotation(JSidekiqLabel.class));
//        if (method != null) {
//            return method.getAnnotation(JSidekiqLabel.class);
//        }
//        return null;
//    }

//
//    public void insertLogSuccess(JoinPoint jp, JSidekiqLabel logger) {}
//
//    public void writeLogInfo(JoinPoint joinPoint, JSidekiqLabel opLogger)
//            throws Exception, IllegalAccessException {}
//    @Before("jsidekiqAspect()")
//    public void doBefore(JoinPoint joinPoint) {
//        System.out.println("=====jsidekiqAspect before=====");
//        handleSideiq(joinPoint, null);
//    }
//
//    @AfterReturning(pointcut="jsidekiqAspect()")
//    public  void doAfter(JoinPoint joinPoint) {
//        System.out.println("=====jsidekiqAspect after=====");
//        handleSideiq(joinPoint,null);
//    }
//
//    @AfterThrowing(value="jsidekiqAspect()",throwing="e")
//    public void doAfter(JoinPoint joinPoint, Exception e) {
//        System.out.println("=====jsidekiqAspect exception=====");
//        handleSideiq(joinPoint, e);
//    }

}
