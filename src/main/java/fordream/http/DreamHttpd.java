package fordream.http;

import fi.iki.elonen.NanoHTTPD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * Http Server.<br>
 * If no handler to handle a request, it will return the 501 HTTP Code(NOT IMPLEMENTED).<br>
 * If there are any uncatched exception, it will return the 500 HTTP Code(SERVER INTERNAL ERROR).<br>
 * The order of handler that you register is the order of response. This means that if there are two handlers work with a request, the first handler can handle the request, and the last will not be able to handle it.
 */
public class DreamHttpd extends NanoHTTPD {
    private List<RequestHandler> handlers;

    private String webRoot; // root path

    public DreamHttpd(int port, String webRoot) {
        super(port);
        this.handlers = new ArrayList<>();
        this.webRoot = webRoot;
    }

    /**
     * register new http request handler
     *
     * @param handler the handler you want register.
     */
    public void registerHandler(RequestHandler handler) {
        boolean repeat = false;
        for (RequestHandler h : this.handlers) {
            if (h.getClass().equals(handler.getClass())) {
                repeat = true;
                break;
            }
        }

        // add to list
        if (!repeat)
            this.handlers.add(handler);
    }

    /**
     * remove the registered request handler
     *
     * @param handler the handler you want to remove
     */
    public void removeHandler(RequestHandler handler) {
        for (int i = 0; i < this.handlers.size(); i++) {
            if (this.handlers.get(i).getClass().equals(handler.getClass())) {
                this.handlers.remove(i);
                break;
            }
        }

    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            Map<String, String> args = session.getParms();

            Response response = null;
            for (RequestHandler handler : this.handlers) {
                if (handler.doHandler(args, session.getUri())) {
                    response = handler.onRequest(this.webRoot, args, session);
                    break;
                }
            }

            if (response == null)
                // return no content ,if no handler.
                return newFixedLengthResponse(Status.NOT_IMPLEMENTED, MIME_PLAINTEXT, null);
            else {
                return response;
            }
        } catch (Exception e) {
            return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, e.getMessage());
        }
    }
}
