package fordream.http;

import fi.iki.elonen.NanoHTTPD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * Http Server
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
     * @param handler
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
     * @param handler
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
            else
                return response;
        } catch (Exception e) {
            return newFixedLengthResponse(Status.INTERNAL_ERROR, MIME_PLAINTEXT, e.getMessage());
        }
    }
}
