package org.power.dynaload.monitor;

import java.io.File;
import java.io.IOException;

import org.power.dynaload.SourceFile;
import org.power.dynaload.registry.FileRegistry;
import org.power.dynaload.startup.Config;
import org.power.dynaload.startup.Startup;
import org.power.dynaload.utils.IOUtils;

/**
 * 文件修改监听器.
 * @author li.zhang
 * 2014-9-15
 *
 */
public class FileChangeMonitor implements Runnable
{
    private Config conf = Startup.getInstance().getConfig();
    
    /** 监听目录. **/
    private final File monitorDir;
    
    /**
     * 构造方法
     * @param dir 表示监听目录
     */
    public FileChangeMonitor(File dir)
    {
        monitorDir = dir;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                monitor();
            }
            catch (IOException e)
            {
                continue;
            }
        }
    }

    private void monitor() throws IOException
    {
        //遍历源文件
        transvalDirectory(monitorDir);
    }
    
    private void transvalDirectory(File dir) throws IOException
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
                transvalDirectory(child);
            }
            else
            {
                if (child.isFile() && child.getName().endsWith(".java"))
                {
                    doBusiness(child);
                }
            }
        }
    }

    private void doBusiness(File file)
    {
        String relativePath = IOUtils.getRelativePath(file, new File(conf.getDyncodePath()));
        relativePath = relativePath + "\\" + file.getName().substring(0, file.getName().lastIndexOf("."));
        String fullClassName = relativePath.replaceAll("\\\\", ".");
        
        FileRegistry registry = FileRegistry.getInstance();
        long preLastModifiedDt = registry.querySourceFile(fullClassName);
        
        //此时文件有变动
        if (preLastModifiedDt < file.lastModified())
        {
            new SourceFile(file).notifyChanged();
            registry.registrySourceFile(fullClassName, file.lastModified());
        }
        
    }
}
