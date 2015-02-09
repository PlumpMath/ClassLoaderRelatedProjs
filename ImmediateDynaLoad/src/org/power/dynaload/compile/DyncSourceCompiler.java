package org.power.dynaload.compile;

import java.io.File;

import org.power.dynaload.startup.Config;
import org.power.dynaload.startup.Startup;

import com.sun.tools.javac.Main;


/**
 * ��̬���������.
 * @author li.zhang
 * 2014-9-12
 *
 */
public final class DyncSourceCompiler
{
    /** conf **/
    private Config conf = Startup.getInstance().getConfig();
    
    /**
     * ����srcFilePathĿ¼�µĴ���.
     * @param dyncodeSourcePath �����Ŀ¼�µ�javaԴ��
     * @param dyncodeCompilePath ���������ŵ�·��.
     */
    public void compile(String srcFilePath, String compilePath)
    {
        File srcFileDir = new File(srcFilePath);
        if (!srcFileDir.isDirectory())
        {
            return;
        }
        
        transval(srcFileDir);
    }
    
    /**
     * ����һ��Դ�ļ�.
     * @param sourceFile Ҫ�����Դ�ļ�
     */
    public void compile(File sourceFile)
    {
        if (!sourceFile.exists())
        {
            return;
        }
        
        doCompile(sourceFile);
    }

    private void transval(File srcFileDir)
    {
        File[] children = srcFileDir.listFiles();
        if (null == children)
        {
            return;
        }
        
        for (int i = 0; i < children.length; i++)
        {
            File child = children[i];
            if (child.isDirectory())
            {
                transval(child);
            }
            else 
            {
                if (child.isFile() && child.getName().endsWith(".java"))
                {
                    doCompile(child);
                }
            }
        }
    }

    private void doCompile(File child)
    {
        String dyncodeCompilePath = conf.getDyncodeCompilePath();
        
        
        File output = new File(dyncodeCompilePath);
        if (!output.exists())
        {
            output.mkdirs();
        }
        
        Main.compile(new String[]{"-d", output.getAbsolutePath(), child.getAbsolutePath()});
    }
}
