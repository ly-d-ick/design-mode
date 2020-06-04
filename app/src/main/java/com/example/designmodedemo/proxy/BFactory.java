package com.example.designmodedemo.proxy;

public class BFactory implements BToolsFactory {
    @Override
    public void sellBTools(float size) {
        System.out.println("批量订制规模薄度为" + size + "mm钢材");
    }
}
