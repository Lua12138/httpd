package fordream.http;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response;

/**
 * Created by forDream on 2016-01-13.
 */
public abstract class AbstractCache implements RequestHandler {
    private final RequestHandler handler;

    public AbstractCache(RequestHandler handler) {
        this.handler = handler;
    }

    /**
     * invoked before call the real object. return a Response if the request can be cached, otherwise null.
     *
     * @param root
     * @param args
     * @param session
     * @return
     */
    public abstract Response canCache(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session);

    /**
     * cache the response.
     *
     * @param response
     * @param session
     */
    public abstract void doCache(Response response, NanoHTTPD.IHTTPSession session);

    @Override
    public boolean doHandler(Map<String, String> args, String uri) {
        return this.handler.doHandler(args, uri);
    }

    @Override
    public Response onRequest(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        Response response = this.canCache(root, args, session);

        if (response != null)
            return response;

        response = this.handler.onRequest(root, args, session);

        this.doCache(response, session);

        return response;
    }
}

