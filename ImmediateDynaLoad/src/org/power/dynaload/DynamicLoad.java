package org.power.dynaload;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.power.dynaload.registry.ServiceRegistry;
import org.power.dynaload.startup.Startup;

/**
 * 即时编译java源代码并且即时生效。做一个类似于jsp页面修改后即时生效的java源代码
 * 
 * 类似的程序。java源代码一旦修改，
 * @author li.zhang
 * 2014-9-12
 *
 */
public class DynamicLoad
{
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        //1.系统启动.
        Startup startup = Startup.getInstance();
        startup.start();
        
        
        //2.接受参数调用对应的服务.
        
        ServiceRegistry registry = ServiceRegistry.getInstance();
        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        
        while (true)
        {
            System.out.print("Enter a message: ");
            String line = sysin.readLine();
            
            List<String> params = parseLine(line);
            if (null == params || params.isEmpty())
            {
                continue;
            }

            String serviceName = params.get(0);
            IDynamicService service = registry.queryService(serviceName);
            if (null == service)
            {
                System.out.println("Servcie[" + serviceName + "] not registered.");
                continue;
            }
            
            Object[] arguments = null;
            if (1 < params.size())
            {
                arguments = params.subList(1, params.size()).toArray();
            }
           
            service.execute(arguments);
        }
    }

    private static List<String> parseLine(String line)
    {
        List<String> list = null;
        if (null == line || line.isEmpty())
        {
            return list;
        }
        
        list = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();
            list.add(token);
        }
        
        return list;
    }
}
