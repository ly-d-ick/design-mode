package com.example.designmodedemo.proxy;

import com.example.designmodedemo.proxy.dynamicproxy.Company;
import com.example.designmodedemo.proxy.dynamicproxy.ProxyUtils;
import com.example.designmodedemo.proxy.staticproxy.ASaler;
import com.example.designmodedemo.proxy.staticproxy.BSaler;

import java.lang.reflect.Method;

public class Client {

    public static void main(String[] args) {
        /* 静态模式 */
        System.out.println("-------------- 静态代理 --------------");
        AToolsFactory aFactory = new AFactory();
        ASaler aSaler = new ASaler(aFactory);
        aSaler.sellATools("AA");

        BToolsFactory bFactory = new BFactory();
        ASaler aaSaler = new ASaler(bFactory);
        aaSaler.sellBTools(0.3f);

        BSaler bSaler = new BSaler(bFactory);
        bSaler.sellBTools(0.35f);

        System.out.println(" ");
        System.out.println("-------------- 动态代理 --------------");
        Company company = new Company();

        AToolsFactory aaFactory = new AFactory();
        company.setFactory(aaFactory);
        AToolsFactory employee1 = (AToolsFactory) company.getProxyInstance();
        employee1.sellATools("CC");

        System.out.println(" ");

        BToolsFactory bbFactory = new BFactory();
        company.setFactory(bbFactory);
        BToolsFactory employee2 = (BToolsFactory) company.getProxyInstance();
        employee2.sellBTools(0.5f);

        ProxyUtils.generateClassFile(aaFactory.getClass(), employee1.getClass().getSimpleName());
        ProxyUtils.generateClassFile(bbFactory.getClass(), employee2.getClass().getSimpleName());
        Method[] methods = aaFactory.getClass().getMethods();
        for (Method method: methods) {
            System.out.println(method.getName());
        }
    }

}

