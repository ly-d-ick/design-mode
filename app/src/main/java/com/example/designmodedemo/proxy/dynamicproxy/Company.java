package com.example.designmodedemo.proxy.dynamicproxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Company implements InvocationHandler {

    /* 持有的真实对象 */
    private Object factory;

    public Object getFactory() {
        return factory;
    }

    public void setFactory(Object factory) {
        this.factory = factory;
    }

    /* 通过Proxy获得动态代理对象 */
    public Object getProxyInstance() {
        /* Proxy#newProxyInstance参数说明
         * @loader     真实对象的类加载器
         * @interfaces 真实对象实现的接口
         * @h          InvocationHandler对象
         */
        return Proxy.newProxyInstance(factory.getClass().getClassLoader(),
                factory.getClass().getInterfaces(), this);
    }

    /* 通过动态代理对象方法进行增强 */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        sellBefor();
        // 调用真实对象的方法
        Object result = method.invoke(factory, args);
        sellAffer();
        return result;
    }

    /* 前置处理 */
    private void sellBefor() {
        System.out.println("提出需求，市场调研，产品分析");
    }

    /* 后置处理 */
    private void sellAffer() {
        System.out.println("售后服务，用户满意调查问卷");
    }

}
