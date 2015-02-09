package com.power.dynaload.test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class LoaderTest
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        ClassLoader parent = LoaderTest.class.getClassLoader();
        URL[] urls = new URL[1];
        urls[0] = new File("dyncode-compile").toURI().toURL();
        URLClassLoader loader = new URLClassLoader(urls, parent);
        
        loader.loadClass("com.power.service.WeatherForcastService");
    }

}
