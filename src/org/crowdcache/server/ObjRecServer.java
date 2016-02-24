package org.crowdcache.server;

import org.crowdcache.objrec.opencv.Recognizer;
import org.crowdcache.objrec.opencv.extractors.ORB;
import org.crowdcache.objrec.opencv.matchers.BFMatcher_HAM;
import org.opencv.core.Core;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.Map;

/**
 * Created by utsav on 2/9/16.
 */
public class ObjRecServer extends Server
{
    private Recognizer recognizer;
    /**
     * Provide address at which to listen
     * @param address
     * @param dblistpath
     */
    public ObjRecServer(String address, String dblistpath) throws IOException
    {
        super(address);
        this.recognizer = new Recognizer(new ORB(), new BFMatcher_HAM(), dblistpath);
    }

    /**
     * Process incoming image, recognize, return annotation
     * @param data
     * @return
     */
    @Override
    public byte[] process(byte[] data)
    {
        System.out.println("Received");
        Long start = System.currentTimeMillis();
        String reply = recognizer.recognize(data);
        Long total = System.currentTimeMillis() - start;
        reply = reply + "," + total.toString();
        return reply.getBytes(ZMQ.CHARSET);
    }

    public static void main(String args[]) throws IOException
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        if (args.length == 1)
        {
            String DBdirpath = args[0];
            ObjRecServer server = new ObjRecServer("192.168.1.9:50505", DBdirpath);
            server.start();
        }
    }
}
