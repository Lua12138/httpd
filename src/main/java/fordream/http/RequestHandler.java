package fordream.http;

import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.IHTTPSession;
import static fi.iki.elonen.NanoHTTPD.Response;

public interface RequestHandler {
    /**
     * @param args the params of the request
     * @param uri  the uri of the request
     * @return return true, if this handler handle the request.
     */
    boolean doHandler(Map<String, String> args, String uri);

    /**
     * handle the request
     *
     * @param root    the url of the HTTP server's root
     * @param args    the params of the request
     * @param session information of the session
     * @return the response to return to client.
     */
    Response onRequest(String root, Map<String, String> args, IHTTPSession session);
}
