# 设计模式
## [1.代理模式](#proxy)
## [2.单例模式](#singleton)

# <a name="proxy">代理模式</a>
> 给某一个对象提供一个代理对象，并由代理对象控制对原对象的引用。
### 代理模式通俗的来讲代理模式就是我们生活中常见的中介。 
### 作用：
### （1）通过引入代理对象的方式来间接访问目标对象，防止直接访问目标对象给系统带来的不必要复杂性；
### （2）通过代理对象对原有的业务增强。
### 代理模式一般会有三个角色：抽象对象、真实对象和代理对象
- #### 抽象对象：指代理对象和真实对象对外提供的公共方法，一般为一个接口
- #### 真实对象：需要实现抽象对象接口，定义了真实对象所要实现的业务逻辑，以便供代理对象调用。真正的业务逻辑在此。
- #### 代理对象：需要实现抽象对象接口，是真实对象的代理，通过真实对象的业务逻辑方法来实现抽象方法，并可以附加自己的操作。将统一的流程控制都放到代理对象中处理！
### 代理模式分为静态代理和动态代理

## 一、静态代理
### 静态代理在使用时,需要定义接口或者父类,被代理对象与代理对象一起实现相同的接口或者是继承相同父类。一般，被代理对象和代理对象是一对一的关系。部分例码如下：  
####  1.定义接口，即创建抽象对象
```
/**
 * 代理模式中的抽象对象，作为真实对象和抽象对象的公共接口
 * */
public interface AToolsFactory {
    void sellATools(String name);
}
```
####  2.创建真实对象并实现抽象对象的接口
```
/**
 * 代理模式中的真实对象
 */
public class AFactory implements AToolsFactory {
    @Override
    public void sellATools(String name) {
        System.out.println("批量订制" + name + "产品");
    }
}
```
### 3.代理对象实现作为抽象对象的接口，并持有真实对象
```
/**
 * 代理模式中的代理对象
 * 代理对象实现作为抽象对象的接口，并持有真实对象
 */
public class ASaler implements AToolsFactory {

    private AToolsFactory factory;

    public ASaler(AToolsFactory factory) {
        this.factory = factory;
    }

    /* 前置处理 */
    private void sellBefor() {
        System.out.println("提出需求，市场调研，产品分析");
    }

    /* 后置处理 */
    private void sellAffer() {
        System.out.println("售后服务，用户满意调查问卷");
    }

    @Override
    public void sellATools(String name) {
        sellBefor();
        factory.sellATools(name);
        sellAffer();
    }
}
```
#### 4.访问调用
```
public class Client {

    public static void main(String[] args) {
        /* 静态模式 */
        AToolsFactory aFactory = new AFactory();
        ASaler aSaler = new ASaler(aFactory);
        aSaler.sellATools("AA");

    }

}

------------ result ------------
提出需求，市场调研，产品分析
批量订制AA产品
售后服务，用户满意调查问卷
```
### 上述已经是一个完整的静态代理案例，若此时业务变更，要实现一个代理对象对应多个被代理对象或再新增一对一个代理对象和被代理对象。展示部分例码，完整案例请移步至下方github链接中查看
- #### 一对多
#### 1.新增抽象对象
```
public interface BToolsFactory {
    void sellBTools(float size);
}
```
#### 2.新增真实对象
```
public class BFactory implements BToolsFactory {
    @Override
    public void sellBTools(float size) {
        System.out.println("批量订制规模薄度为" + size + "mm钢材");
    }
}
```
#### 3.原代理对象实现作为新抽象对象的接口
```
public class ASaler implements AToolsFactory, BToolsFactory {

    ·····

    @Override
    public void sellBTools(float size) {
        sellBToolsBefor();
        bToolsFactory.sellBTools(size);
        sellBToolsAffer();
    }
}
```
#### 4.访问调用
```
BToolsFactory bFactory = new BFactory();
ASaler aaSaler = new ASaler(bFactory);
aaSaler.sellBTools(0.3f);
```
- #### 一对一
#### 1.前两个步骤同上，创建新的代理对象
```
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
```
#### 2.访问调用
```
BToolsFactory bFactory = new BFactory();
BSaler bSaler = new BSaler(bFactory);
bSaler.sellBTools(0.35f);
```
### 通过以上两个案例可看出，静态代理违反了开闭原则，一对多则代理对象会出现扩展能力差的问题；一对一则会出现时静态代理对象量多、代码量大，从而导致代码复杂，可维护性差的问题。而动态代理类就可以解决创建多个静态代理的问题，灵活性强。

## 二、动态代理
### 接口InvocationHandler和类Proxy是实现动态代理所必须用到的。接口InvocationHandler是给动态代理类实现的，负责处理被代理对象的操作的；类Proxy是用来创建动态代理类实例对象的，通过得到类Proxy对象调用需要代理的方法
### 优点：解决创建多个静态代理的问题，灵活性强
### 缺点：
#### 1、动态代理通过反射间接调用目标对象方法，因此效率较静态代理低；
#### 2、应用场景受限，只能针对接口实现代理，不能针对类(Java单继承特性)

- ### 部分实现代码
#### 添加动态代理类
```
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
```
#### 调用
```
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
```

- ### 实现原理
#### **注：Android Studio内嵌JDK在原生JDK基础上有所改动，以下展示的是原生JDK源码**
##### 通过调试可以发现，代理对象的类名与其他有区别
![1](https://github.com/ly-d-ick/design-mode/blob/master/app/image/1.png)
##### 查看Proxy#newProxyInstance，以下节选源码
```
        Class<?> cl = getProxyClass0(loader, intfs);

        /*
         * Invoke its constructor with the designated invocation handler.
         */
        try {
            if (sm != null) {
                checkNewProxyPermission(Reflection.getCallerClass(), cl);
            }

            final Constructor<?> cons = cl.getConstructor(constructorParams);
            final InvocationHandler ih = h;
            if (!Modifier.isPublic(cl.getModifiers())) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        cons.setAccessible(true);
                        return null;
                    }
                });
            }
            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException|InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
```
##### 上述代码可得，动态代理类的创建通过调用Proxy#getProxyClass0，继续点进去查看

```
    private static Class<?> getProxyClass0(ClassLoader loader,
                                           Class<?>... interfaces) {
        if (interfaces.length > 65535) {
            throw new IllegalArgumentException("interface limit exceeded");
        }

        // If the proxy class defined by the given loader implementing
        // the given interfaces exists, this will simply return the cached copy;
        // otherwise, it will create the proxy class via the ProxyClassFactory
        return proxyClassCache.get(loader, interfaces);
    }
```
##### 继续查看WeakCahce#get方法实现

```
    public V get(K key, P parameter) {
        Objects.requireNonNull(parameter);

        expungeStaleEntries();

        Object cacheKey = CacheKey.valueOf(key, refQueue);

        // lazily install the 2nd level valuesMap for the particular cacheKey
        ConcurrentMap<Object, Supplier<V>> valuesMap = map.get(cacheKey);
        if (valuesMap == null) {
            ConcurrentMap<Object, Supplier<V>> oldValuesMap
                = map.putIfAbsent(cacheKey,
                                  valuesMap = new ConcurrentHashMap<>());
            if (oldValuesMap != null) {
                valuesMap = oldValuesMap;
            }
        }

        // create subKey and retrieve the possible Supplier<V> stored by that
        // subKey from valuesMap
        Object subKey = Objects.requireNonNull(subKeyFactory.apply(key, parameter));
        Supplier<V> supplier = valuesMap.get(subKey);
        Factory factory = null;

        while (true) {
            if (supplier != null) {
                // supplier might be a Factory or a CacheValue<V> instance
                V value = supplier.get();
                if (value != null) {
                    return value;
                }
            }
            // else no supplier in cache
            // or a supplier that returned null (could be a cleared CacheValue
            // or a Factory that wasn't successful in installing the CacheValue)

            // lazily construct a Factory
            if (factory == null) {
                factory = new Factory(key, parameter, subKey, valuesMap);
            }

            if (supplier == null) {
                supplier = valuesMap.putIfAbsent(subKey, factory);
                if (supplier == null) {
                    // successfully installed Factory
                    supplier = factory;
                }
                // else retry with winning supplier
            } else {
                if (valuesMap.replace(subKey, supplier, factory)) {
                    // successfully replaced
                    // cleared CacheEntry / unsuccessful Factory
                    // with our Factory
                    supplier = factory;
                } else {
                    // retry with current supplier
                    supplier = valuesMap.get(subKey);
                }
            }
        }
    }
```
##### 通过上述代码可知，WeakCahce#get先判断有没有动态代理类缓存，有直接从缓存取，没有就进行缓存。Object subKey = Objects.requireNonNull(subKeyFactory.apply(key, parameter));这一行是创建代理类的关键代码，点击BiFunction#apply，是一个泛型方法，通过查看哪些类实现了这个接口，然后回到Proxy类的ProxyClassFactory
![2](https://github.com/ly-d-ick/design-mode/blob/master/app/image/2.png)
##### 一进来就发现，代理类的类名前缀，继续往下翻
![3](https://github.com/ly-d-ick/design-mode/blob/master/app/image/3.png)
##### 序号通过CAS操作生成，Proxy#defineClass0是一个本地方法，关键代码为byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, interfaces, accessFlags)，生成一个代理类字节码数组。接下来，调用ProxyGenerator#generateProxyClass获取已经实例过的代理类的byte数组，然后通过I/O得到class文件，再反编译瞧瞧代理类的内容。

![4](https://github.com/ly-d-ick/design-mode/blob/master/app/image/4.png)
##### 类$Proxy0是Proxy的子类，而且已经实现了抽象对象的接口，继续往下翻

![5](https://github.com/ly-d-ick/design-mode/blob/master/app/image/5.png)
![6](https://github.com/ly-d-ick/design-mode/blob/master/app/image/6.png)
##### 变量h是Proxy类中的InvocationHandler对象，这也说明了为什么动态代理中实现InvocationHandler接口原因；变量m3是真实对象实现接口的方法。
----
### 以上为动态代理的全部实现。

# <a name="singleton">单例模式</a>
> 确保一个类只有一个实例，而且自行实例化并向整个系统提供这个实例。
## 特征
- #### 构造方法一般不外开放
- #### 通过一个静态方法或枚举返回单例类的对象
- #### 注意多线程的场景
- #### 注意单例类对象在反序列化时不会重新创建对象

---

## 单例类的实现
### 1.懒汉式单例
```
    // 1.懒汉式单例
    static class Singleton1 {

        // 私有静态变量
        private static Singleton1 mInstance = null;

        // 私有构造方法
        private Singleton1() {
            System.out.println("懒汉式单例");
        }

        // 暴露公有静态方法
        public static Singleton1 getInstance() {
            if (mInstance == null) {
                mInstance = new Singleton1();
            }
            return mInstance;
        }
    }
```
#### 以上是简单的懒汉式单例实现，如果单例类中有作网络请求、缓存操作或线程池策略等耗时操作，会创建多个不同的实例，以下稍作改动，模拟耗时操作。
#### main()中添加
```
        for (int i = 0; i < 100; i++) {
            final int index = i;
            new Thread(){
                @Override
                public void run() {
                    System.out.println("index = " + index + ", instance: " + Singleton1.getInstance());
                }
            }.start();
        }
```
#### getInstance()中添加，再运行结果
```
try {
     Thread.sleep(500);
} catch (InterruptedException e) {
    e.printStackTrace();
}

---------- 节选结果 ----------
index = 1, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@6b52350c
懒汉式单例
懒汉式单例
懒汉式单例
index = 7, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@4a1a7268
懒汉式单例
懒汉式单例
index = 9, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@ce407e7
懒汉式单例
懒汉式单例
懒汉式单例
index = 11, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@59ac37b1
index = 8, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@1dca18a4
index = 13, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@5795dfe4
index = 10, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@7898c39
index = 6, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@fb49a4f
index = 5, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@47c48106
index = 3, instance: com.example.designmodedemo.singleton.SingletonMode$Singleton1@6fada00f
```
#### 输出的哈希地址不相等说明对象多次创建。想要线程安全，需要加synchronized关键字。
#### 1、直接锁方法。锁方法的缺陷是同步锁粒度太大，耗性能
#### 2、双重校验DCL。同步锁粒度小很多。<u>由于JVM有指令重排特殊性，会导致双重校验失效，静态变量需要添加volatile关键字</u>
```
private volatile static Singleton1 sInstance = null;

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
            }
        }
    }
    return sInstance;
}
```


### 2.饿汉式单例(线程安全)
```
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
```

### 3.静态内部类单例(线程安全)
```
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
```

### 4.使用枚举(线程安全)
```
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
```

### 4.使用容器，如源码中类SystemServiceRegistry
```
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
```
