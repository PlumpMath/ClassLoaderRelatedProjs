package com.power.service;
import org.power.dynaload.IDynamicService;
import org.power.dynaload.ServiceNames;

/**
 * ����Ԥ������.
 * @author li.zhang
 * 2014-9-12
 *
 */
public class WeatherForcastService implements IDynamicService
{

    @Override
    public Object execute(Object... args)
    {
        System.out.println("The weather today is:������" + args + "zhongguo");
        return null;
    }

    @Override
    public String getName()
    {
        return ServiceNames.WEATHER_FORCAST_SERVICE;
    }

}
