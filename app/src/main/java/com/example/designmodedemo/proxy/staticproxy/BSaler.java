package com.example.designmodedemo.proxy.staticproxy;

import com.example.designmodedemo.proxy.BToolsFactory;

public class BSaler implements BToolsFactory {

    private BToolsFactory factory;

    public BSaler(BToolsFactory factory) {
        this.factory = factory;
    }

    /* 前置处理 */
    private void sellBefor() {
        System.out.println("交付时间，物流运输方式");
    }

    /* 后置处理 */
    private void sellAffer() {
        System.out.println("售后服务，用户满意调查问卷");
    }

    @Override
    public void sellBTools(float size) {
        sellBefor();
        factory.sellBTools(size);
        sellAffer();
    }
}
