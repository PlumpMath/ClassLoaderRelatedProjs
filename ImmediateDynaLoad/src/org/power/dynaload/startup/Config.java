package org.power.dynaload.startup;

public final class Config
{
    /** 支持动态加载的代码的路径. **/
    private String dyncodePath;
    
    /** 支持动态加载的代码的遍历路径. **/
    private String dyncodeCompilePath;

    public String getDyncodePath()
    {
        return dyncodePath;
    }

    public void setDyncodePath(String dyncodePath)
    {
        this.dyncodePath = dyncodePath;
    }

    public String getDyncodeCompilePath()
    {
        return dyncodeCompilePath;
    }

    public void setDyncodeCompilePath(String dyncodeCompilePath)
    {
        this.dyncodeCompilePath = dyncodeCompilePath;
    }
}
