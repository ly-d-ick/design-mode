package com.example.designmodedemo.singleton;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式
 */
public class SingletonMode {

    public static void main(String[] args) {
        /*Singleton1 instance = Singleton1.getInstance();
        System.out.println("instance: " + instance);
        Singleton1 instance1 = Singleton1.getInstance();
        System.out.println("instance1: " + instance1);
        Singleton1 instance2 = Singleton1.getInstance();
        System.out.println("instance2: " + instance2);*/

        for (int i = 0; i < 100; i++) {
            final int index = i;
            new Thread(){
                @Override
                public void run() {
//                    System.out.println("index = " + index + ", instance: " + Singleton1.getInstance());
//                    System.out.println("index = " + index + ", instance: " + Singleton2.getInstance());
//                    System.out.println("index = " + index + ", instance: " + Singleton3.getInstance());
//                    System.out.println("index = " + index + ", instance: " + EnumDemo.INSTANCE.getInstance());
                    SingletonManager.putClass("Singleton5", new Singleton5());
                    System.out.println("index = " + index + ", instance: " + SingletonManager.getClass("Singleton5"));
                }
            }.start();
        }
    }

    // 1.懒汉式单例
    static class Singleton1 {

        // 私有静态变量
        private volatile static Singleton1 sInstance = null;

        // 私有构造方法
        private Singleton1() {
            System.out.println("懒汉式单例");
        }

        // 暴露公有静态方法(线程不安全)
        /*public static Singleton1 getInstance() {
            if (sInstance == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sInstance = new Singleton1();
            }
            return sInstance;
        }*/

        // 暴露公有静态方法(线程安全)，同步锁粒度大，耗性能
        /*public static synchronized Singleton1 getInstance() {
            if (sInstance == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sInstance = new Singleton1();
            }
            return sInstance;
        }*/

        // 双重校验DCL
        public static Singleton1 getInstance() {
            if (sInstance == null) {// 第一重校验
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized(Singleton1.class) {
                    if (sInstance == null) {// 第二重校验
                        sInstance = new Singleton1();
                        // 1.sInstance实例分配对象
                        // 2.调用sInstance构造方法，初始化成员字段
                        // 3.Singleton1对象赋值给sInstance
                    }
                }
            }
            return sInstance;
        }

        /**
         * 确保反序列时不重创对象
         */
        private Object readResolve() throws ObjectStreamException {
            return sInstance;
        }
    }

    // 2.饿汉式单例(线程安全)
    static class Singleton2 {

        private static Singleton2 sInstance = new Singleton2();

        private Singleton2() {}

        public static Singleton2 getInstance() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return sInstance;
        }
    }

    // 3.静态内部类单例(线程安全)
    static class Singleton3 {

        private Singleton3() {}

        private static class Holder {
            private static final Singleton3 INSTANCE = new Singleton3();
        }

        // 延时加载
        public final static Singleton3 getInstance() {
            return Holder.INSTANCE;
        }
    }

    // 4.使用枚举(线程安全)
    static class Singleton4 {}

    enum EnumDemo {
        INSTANCE;

        private Singleton4 instance;

        EnumDemo() {
            instance = new Singleton4();
        }

        public Singleton4 getInstance() {
            return instance;
        }
    }

    // 5.使用容器,如源码中类SystemServiceRegistry
    static class Singleton5 {}

    static class SingletonManager {
        private static Map<String, Object> map = new HashMap<>();

        public static void putClass(String key, Object instace) {
            if (!map.containsKey(key)) {
                map.put(key, instace);
            }
        }

        public static Object getClass(String key) {
            return map.get(key);
        }
    }
}
