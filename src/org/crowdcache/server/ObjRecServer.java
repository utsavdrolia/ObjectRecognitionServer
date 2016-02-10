package org.crowdcache.server;

import org.crowdcache.objrec.opencv.KeypointDescList;
import org.crowdcache.objrec.opencv.Recognizer;
import org.crowdcache.objrec.opencv.extractors.ORB;
import org.crowdcache.objrec.opencv.matchers.BFMatcher_HAM;
import org.zeromq.ZMQ;

import java.util.Map;

/**
 * Created by utsav on 2/9/16.
 */
public class ObjRecServer extends Server
{
    Recognizer recognizer;
    /**
     * Provide address at which to listen
     * @param address
     * @param dblistpath
     */
    public ObjRecServer(String address, Map<String, KeypointDescList> dblistpath)
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
        String reply = recognizer.recognize(data);
        return reply.getBytes(ZMQ.CHARSET);
    }
}
