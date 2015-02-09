package org.power.dynaload.proxy;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.power.dynaload.startup.Config;
import org.power.dynaload.startup.Startup;

/**
 * 动态服务类的代理.
 * @author li.zhang
 * 2014-9-15
 *
 */
public class DyncServProxy implements InvocationHandler
{
    /** 类加载. **/
    private ClassLoader loader;
    
    /** 类名. **/
    private final String className;
    
    private Class<?> backendClzz;
    
    private Object backendObj;
    
    /** 标识代理对应的类的源文件是否有变动. **/
    private boolean changed;
    
    /**
     * 构造方法.
     */
    public DyncServProxy(ClassLoader loader, String className)
    {
        this.loader = loader;
        this.className = className;
        
        try
        {
            backendClzz = loader.loadClass(className);
            backendObj = backendClzz.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        if (changed)
        {
            Config conf = Startup.getInstance().getConfig();
            URL[] urls = new URL[1];
            urls[0] = new File(conf.getDyncodeCompilePath()).toURI().toURL();
            loader = new URLClassLoader(urls, getClass().getClassLoader());
            backendClzz = loader.loadClass(className);
            backendObj = backendClzz.newInstance();
            
            changed = false;
        }
        
        return method.invoke(backendObj, args);
    }

    public void notifyChanged()
    {
        this.changed = true;
    }
}
