package com.example.designmodedemo.proxy;

/**
 * 代理模式中的真实对象
 */
public class AFactory implements AToolsFactory {
    @Override
    public void sellATools(String name) {
        System.out.println("批量订制" + name + "产品");
    }
}
