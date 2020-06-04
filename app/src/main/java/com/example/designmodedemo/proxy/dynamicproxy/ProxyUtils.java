package com.example.designmodedemo.proxy.dynamicproxy;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;


public class ProxyUtils {

    public static void generateClassFile(Class clazz, String proxyName) {
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
                proxyName, new Class[]{clazz});
        String paths = clazz.getResource(".").getPath();
        System.out.println(paths);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(paths + proxyName + ".class");
            out.write(proxyClassFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
