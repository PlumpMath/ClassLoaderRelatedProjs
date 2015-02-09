package org.power.dynaload.startup;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;

import org.power.dynaload.IDynamicService;
import org.power.dynaload.compile.DyncSourceCompiler;
import org.power.dynaload.monitor.FileChangeMonitor;
import org.power.dynaload.proxy.DyncServProxy;
import org.power.dynaload.registry.FileRegistry;
import org.power.dynaload.registry.LoaderRegistry;
import org.power.dynaload.registry.ServiceRegistry;

/**
 * 启动类.
 * @author li.zhang
 * 2014-9-12
 *
 */
public class Startup
{
    /** instance. **/
    private static final Startup INSTANCE = new Startup();
    
    /** 配置信息. **/
    private Config config;
    
    /**
     * 私有构造方法
     */
    private Startup()
    {
        
    }
    
    /**
     * 得到实例.
     * @return
     */
    public static Startup getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 系统启动.
     * @throws IOException 
     */
    public void start() throws Exception
    {
        //1.加载启动配置.
        initConfig();
        
        //2.编译动态代码.
        recompileDyncCode();
        
        //3.创建代码动态加载的树图.
        loadDyncServcies();
    }

    /**
     * 初始化配置.
     */
    private void initConfig()
    {
        Config conf = new Config();
        conf.setDyncodePath("dyncode");//默认,dyncode目录下的代码是支持动态加载的.
        conf.setDyncodeCompilePath("dyncode-compile");//动态编译的代码放在这个目录下.
        this.config = conf;
    }
    
    /**
     * 重新编译dyncode目录下的java源代码.
     */
    private void recompileDyncCode()
    {
        Config config = getConfig();
        DyncSourceCompiler compiler = new DyncSourceCompiler();
        compiler.compile(config.getDyncodePath(), config.getDyncodeCompilePath());
    }

    /**
     * 加载动态服务.
     * @throws IOException 
     */
    private void loadDyncServcies() throws Exception
    {
        String dyncodePath = this.config.getDyncodePath();
        File dyncodeDir = new File(dyncodePath);
        if (!dyncodeDir.exists() || !dyncodeDir.isDirectory())
        {
            return;
        }
        
        //1.遍历dyncode目录，将各个动态服务类名进行注册.
        transvalDirectory(dyncodeDir, new File(dyncodePath));
        
        //2.启动源文件监听器
        Thread monitor = new Thread(new FileChangeMonitor(new File(config.getDyncodePath())));
        monitor.start();
        
        //3.初始化动态服务类的类加载器.
        initDyncServLoader();
        
        //4.加载动态服务.
        loadDyncService();
    }

    /**
     * 初始化动态服务类的类加载器.
     * 
     * 注意,如果有新的动态服务类加入的时候,我们的动态服务类类加载器应该将新的服务类的url添加进来。
     * 
     * 同理，卸载的时候，一起删除.
     * @throws Exception 
     */
    private void initDyncServLoader() throws Exception
    {
        LoaderRegistry loaderRegistry = LoaderRegistry.getInstance();
        
        ClassLoader parent = getClass().getClassLoader();
        URL[] urls = new URL[1];
        urls[0] = new File(getConfig().getDyncodeCompilePath()).toURI().toURL();
       
        
        URLClassLoader loader = new URLClassLoader(urls, parent);
        loaderRegistry.registerLoader("DyncServLoader", loader);
    }

    /**
     * 遍历目录.
     * @param dir dir
     * @param according 参照目录.
     * @throws IOException 
     */
    private void transvalDirectory(File dir, File according) throws IOException
    {
        File[] children = dir.listFiles();
        if (null == children || 0 == children.length)
        {
            return;
        }
        
        for (int i = 0; i < children.length; i++)
        {
            File child = children[i];
            if (child.isDirectory())
            {
                transvalDirectory(child, according);
            }
            else
            {
                if (child.isFile() && child.getName().endsWith(".java"))
                {
                    doTransvalOperation(child, according);
                }
            }
        }
    }

    private void doTransvalOperation(File child, File according)
    {
        String relativePath = relativePath(child, according);
        relativePath = relativePath.replaceAll("\\\\", ".");
        String servClassName = relativePath.substring(0, relativePath.lastIndexOf(".java"));
        
        ServiceRegistry registry = ServiceRegistry.getInstance();
        FileRegistry fileRegistry = FileRegistry.getInstance();
        registry.addDyncService(servClassName, null);
        fileRegistry.registrySourceFile(servClassName, child.lastModified());
    }
    
    /**
     * 加载动态服务.
     * @throws Exception 
     */
    private void loadDyncService() throws Exception
    {
        LoaderRegistry registry = LoaderRegistry.getInstance();
        ServiceRegistry servRegistry = ServiceRegistry.getInstance();
        ClassLoader loader = registry.queryLoader("DyncServLoader");
        
        Map<String, String> servClzzNames = servRegistry.getDyncServiceNames();
        if (null == servClzzNames)
        {
            return;
        }
        
        int i = 0;
        Iterator<String> iterator = servClzzNames.keySet().iterator();
        while (iterator.hasNext())
        {
            String servClzzName = iterator.next();
            
            DyncServProxy proxyHandle = new DyncServProxy(loader, servClzzName);
            IDynamicService proxy = (IDynamicService)Proxy.newProxyInstance(loader, 
                    new Class<?>[]{IDynamicService.class}, 
                    proxyHandle);
            
            servClzzNames.put(servClzzName, proxy.getName());
            
            servRegistry.registerService(proxy);
            
            i++;
        }
        
       
    }

    /**
     * 
     * @param file 文件.
     * @param parentDir 参照文件,可能是上一级或者上上一级的父目录
     * @return
     */
    private String relativePath(File file, File parentDir)
    {
        String relativePath = null;
        relativePath = file.getAbsolutePath().substring(parentDir.getAbsolutePath().length() + 1);
        return relativePath;
    }

    public Config getConfig()
    {
        return config;
    }
}
