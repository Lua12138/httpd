import fi.iki.elonen.NanoHTTPD;
import fordream.http.AbstractRequestHandler;

import java.util.Date;
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
        String html = "<html>" +
                "<head>" +
                "<title>Test Html</title>" +
                "</head>" +
                "<body>" +
                "<h1> --- Now is " + new Date().toLocaleString() + " --- </h1>" +
                "</body>" +
                "</html>";
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, html);

    }
}
