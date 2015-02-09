package org.power.dynaload.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * 类加载器注册表.
 * @author li.zhang
 * 2014-9-12
 *
 */
public class LoaderRegistry
{
    /** instance. **/
    private static final LoaderRegistry INSTANCE = new LoaderRegistry();
    
    private Map<String, ClassLoader> loaders = new HashMap<String, ClassLoader>();
    
    /**
     * 私有构造方法
     */
    private LoaderRegistry()
    {
        
    }
    
    /**
     * 返回实例.
     * @return
     */
    public static LoaderRegistry getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 注册类加载器.
     * @param loadType 类加载器类型
     * @param loader 类加载器
     */
    public synchronized void registerLoader(String loadType, ClassLoader loader)
    {
        loaders.put(loadType, loader);
    }
    
    /**
     * 查询类加载器.
     * @param key
     * @return
     */
    public ClassLoader queryLoader(String key)
    {
        return loaders.get(key);
    }
}
