package com.example.designmodedemo.proxy.staticproxy;

import com.example.designmodedemo.proxy.AToolsFactory;
import com.example.designmodedemo.proxy.BToolsFactory;

/**
 * 代理模式中的代理对象
 * 代理对象实现作为抽象对象的接口，并持有真实对象
 */
public class ASaler implements AToolsFactory, BToolsFactory {

    private AToolsFactory aToolsFactory;
    private BToolsFactory bToolsFactory;

    public ASaler(AToolsFactory factory) {
        this.aToolsFactory = factory;
    }

    public ASaler(BToolsFactory bToolsFactory) {
        this.bToolsFactory = bToolsFactory;
    }

    /* 前置处理 */
    private void sellBefor() {
        System.out.println("提出需求，市场调研，产品分析");
    }

    /* 后置处理 */
    private void sellAffer() {
        System.out.println("售后服务，用户满意调查问卷");
    }

    /* 前置处理 */
    private void sellBToolsBefor() {
        System.out.println("交付时间，物流运输方式");
    }

    /* 后置处理 */
    private void sellBToolsAffer() {
        System.out.println("售后服务，用户满意调查问卷");
    }

    @Override
    public void sellATools(String name) {
        sellBefor();
        aToolsFactory.sellATools(name);
        sellAffer();
    }

    @Override
    public void sellBTools(float size) {
        sellBToolsBefor();
        bToolsFactory.sellBTools(size);
        sellBToolsAffer();
    }
}
