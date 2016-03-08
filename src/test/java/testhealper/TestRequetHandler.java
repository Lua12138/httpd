package testhealper;

import fi.iki.elonen.NanoHTTPD;
import fordream.http.AbstractRequestHandler;

import java.util.Map;

/**
 * Created by forDream on 2016-03-08.
 */
public class TestRequetHandler extends AbstractRequestHandler {
    @Override
    public boolean doHandler(Map<String, String> args, String uri) {
        return true;
    }

    @Override
    public NanoHTTPD.Response onRequest(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        String html = "{\"method\" : \"" + session.getMethod().name() + "\", " +
                "\"args\" : \"" + args.get("test") + "\", " +
                "\"time\":" + System.currentTimeMillis() +
                "}";
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, html);

    }
}
