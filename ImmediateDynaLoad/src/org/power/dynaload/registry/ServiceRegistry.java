package org.power.dynaload.registry;

import java.util.HashMap;
import java.util.Map;

import org.power.dynaload.IDynamicService;

/**
 * 服务注册表
 * @author li.zhang
 * 2014-9-12
 *
 */
public final class ServiceRegistry
{
    /** instance. **/
    private static final ServiceRegistry INSTANCE = new ServiceRegistry();
    
    /** key为com.power.servcie.WeatherForcastService, value为服务名WeatherForcast等. **/
    private Map<String, String> dyncServiceNames = new HashMap<String, String>();
    
    /** 服务, key为服务名，value为对应的服务实例. **/
    private Map<String, IDynamicService> services = new HashMap<String, IDynamicService>();
    
    /**
     * 构造方法
     */
    private ServiceRegistry()
    {
        
    }
    
    /**
     * 获取实例.
     * @return
     */
    public static ServiceRegistry getInstance()
    {
        return INSTANCE;
    }

    /**
     * 添加动态服务.
     * @param servClassName
     */
    public synchronized void addDyncService(String servClassName, String serviceName)
    {
        dyncServiceNames.put(servClassName, serviceName);
    }
    
    /**
     * 删除动态服务
     * @param servClassName
     */
    public synchronized void removeDyncService(String servClassName)
    {
        dyncServiceNames.remove(servClassName);
    }
    
    /**
     * 注册服务
     * @param service 服务
     */
    public void registerService(IDynamicService service)
    {
        if (null == service)
        {
            return;
        }
        
        Map<String, IDynamicService> maps = new HashMap<String, IDynamicService>();
        maps.putAll(services);
        maps.put(service.getName(), service);
        
        services = maps;
    }
    
    public synchronized void unregisterService(String serviceName)
    {
        services.remove(serviceName);
    }
    
    /**
     * 根据服务名查询出对应的服务.
     * @param serviceName 服务名
     * @return
     */
    public IDynamicService queryService(String serviceName)
    {
        return services.get(serviceName);
    }

    /**
     * 返回只读的集合.
     * @return
     */
    public Map<String, String> getDyncServiceNames()
    {
        return dyncServiceNames;
    }
}
