package org.power.dynaload.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件注册表
 * @author li.zhang
 * 2014-9-15
 *
 */
public final class FileRegistry
{
    /** instance **/
    private static final FileRegistry INSTANCE = new FileRegistry();
    
    /** 文件信息. key为形如com.power.dynaload.Service全限定类名, value为文件最后修改时间. **/
    private Map<String, Long> fileLastModifiedDt = new HashMap<String, Long>();
    
    /**
     * 私有构造方法.
     */
    private FileRegistry()
    {
        
    }
    
    public static FileRegistry getInstance()
    {
        return INSTANCE;
    }

    /**
     * 注册文件.
     * @param servClassName 注册文件对应的全限定类名.
     * @param date date
     */
    public synchronized void registrySourceFile(String servClassName, long time)
    {
        fileLastModifiedDt.put(servClassName, time);
    }
    
    /**
     * 查询源文件之前保持的最后更新时间.
     * @param servClassName
     * @return
     */
    public long querySourceFile(String servClassName)
    {
        return fileLastModifiedDt.get(servClassName);
    }
}   
