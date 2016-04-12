package com.avorona.web;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by avorona on 12.04.16.
 */
@Component
@Order
public class AuditAnnotationBeanPostProcessor implements BeanPostProcessor {

    private Logger logger = Logger.getLogger(this.getClass());

    private static Map<String, Set<String>> methodMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        for (Method m : o.getClass().getMethods()) {
            if (m.isAnnotationPresent(Audit.class)) {
                Set<String> methods = methodMap.get(s);
                if (methods == null) {
                    methods = new HashSet<>();
                    methodMap.put(s, methods);
                }
                methods.add(m.getName());
            }
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Set<String> methods = methodMap.get(s);
        if (methods != null) {
            return Proxy.newProxyInstance(o.getClass().getClassLoader(),
                    o.getClass().getInterfaces(), (proxy,
                                                   method,
                                                   args) -> {
                        if (methods.contains(method.getName())) {
                            final long start = System.currentTimeMillis();
                            Object val = method.invoke(proxy, args);
                            logger.info("Method \"" + method.getName() + "\" invocation takes: " + (System
                                    .currentTimeMillis() - start) + " second");
                            return val;
                        }
                        return method.invoke(proxy);
                    });
        }
        return o;
    }
}
