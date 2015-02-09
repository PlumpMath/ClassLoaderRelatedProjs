package org.power.dynaload;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.power.dynaload.compile.DyncSourceCompiler;
import org.power.dynaload.proxy.DyncServProxy;
import org.power.dynaload.registry.ServiceRegistry;
import org.power.dynaload.utils.IOUtils;

/**
 * 代表一个源文件.
 * @author li.zhang
 * 2014-9-15
 */
public class SourceFile
{
    /** 源文件. **/
    private final File sourceFile;
    
    /** 相对dyncode文件夹的路径. **/
    private final String relativePath;
    
    /** 源文件对应的类的全限定名. **/
    private String fullClassName;
    
    /**
     * 构造方法
     * @param file
     */
    public SourceFile(File file)
    {
        sourceFile = file;
        relativePath = IOUtils.getRelativePath(file, new File("dyncode"));
        
        fullClassName = relativePath + "\\" + file.getName().substring(0, file.getName().lastIndexOf(".java"));
        fullClassName = fullClassName.replaceAll("\\\\", ".");
    }

    public File getSourceFile()
    {
        return sourceFile;
    }

    public String getRelativePath()
    {
        return relativePath;
    }
    
    /**
     * 当当前源文件发生变动时，触发这个方法.
     */
    public void notifyChanged()
    {
        System.out.println("Source File " + sourceFile.getName() + "changed....");
        DyncSourceCompiler compiler = new DyncSourceCompiler();
        compiler.compile(sourceFile);
        
        //设置对应代理的changed标志位为true.
        ServiceRegistry registry = ServiceRegistry.getInstance();
        Map<String, String> dyncServiceNames = registry.getDyncServiceNames();
        if (null == dyncServiceNames)
        {
            return;
        }
        
        String serviceName = dyncServiceNames.get(fullClassName);
        IDynamicService dyncServProxy = registry.queryService(serviceName);
        if (null == dyncServProxy)
        {
            return;
        }
        
        DyncServProxy proxy = (DyncServProxy)Proxy.getInvocationHandler(dyncServProxy);
        proxy.notifyChanged();
        
    }
}
