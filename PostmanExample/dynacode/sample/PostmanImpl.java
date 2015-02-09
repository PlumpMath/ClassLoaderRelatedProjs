package sample;

import java.io.*;

public class PostmanImpl implements Postman
{

    private PrintStream output;

    public PostmanImpl()
    {
        output = System.out;
    }

    public void deliverMessage(String msg)
    {
        output.println("[Postman] " + msg);
        output.flush();
    }
}
