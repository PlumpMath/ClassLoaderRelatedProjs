package org.power.dynaload.utils;

import java.io.File;

public final class IOUtils
{
    /**
     * 得到相对父目录的路径. 得到com/howbuy/形如这样的相对目录.
     * @param dir 目录
     * @param accordings 相对文件
     * @return
     */
    public static String getRelativePath(File file, File parent)
    {
        String relativePath = null;
        relativePath = file.getAbsolutePath().substring(parent.getAbsolutePath().length() + 1);
        relativePath = relativePath.substring(0, relativePath.lastIndexOf("\\"));
        return relativePath;
    }
}
