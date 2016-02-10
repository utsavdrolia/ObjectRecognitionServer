package org.crowdcache.server;

import org.zeromq.ZMQ;

import java.util.HashMap;

/**
 * Created by utsav on 2/9/16.
 */
public abstract class Server extends Thread
{
    ZMQ.Context context;
    private boolean stop = false;
    private ZMQ.Socket registerSocket;

    public Server(String address)
    {
        context = ZMQ.context(1);
        registerSocket = context.socket(ZMQ.REP);
        registerSocket.bind("tcp://" + address);
    }

    public void run()
    {
        while (!stop && !this.isInterrupted())
        {
            byte[] data = registerSocket.recv();
            if (data != null)
            {
                byte[] reply = this.process(data);
                if(reply!=null)
                    registerSocket.send(reply);
            }
        }
    }

    public void close()
    {
        stop = true;
        context.term();
    }

    /**
     * Process data and return reply
     * @param data
     * @return
     */
    abstract public byte[] process(byte[] data);
}
