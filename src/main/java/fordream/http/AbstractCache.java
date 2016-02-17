package fordream.http;

import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response;

/**
 * Support response from cache
 */
public abstract class AbstractCache implements RequestHandler {
    private final RequestHandler handler;

    public AbstractCache(RequestHandler handler) {
        this.handler = handler;
    }

    /**
     * invoked before call the real object. return a Response if the request can be cached, otherwise null.
     *
     * @param root    the url of web root.
     * @param args    the params of request.
     * @param session NanoHTTPD request object.
     * @return null if there is no cache, else return the cached response.
     */
    public abstract Response canCache(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session);

    /**
     * cache the response.<br>
     * it won't be invoked if the current response is from cache.
     *
     * @param response the reponse will be cached.
     * @param session  NanoHTTPD request object.
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

