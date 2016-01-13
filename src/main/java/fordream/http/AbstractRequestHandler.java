package fordream.http;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.*;
import static fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * Provide the basic support, and classification according to the request method.
 */
public abstract class AbstractRequestHandler implements RequestHandler {
    protected Response onGet(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onPut(String root, Map<String, String> args, Map<String, String> files, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onPost(String root, Map<String, String> args, Map<String, String> files, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onDelete(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onHead(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onOptions(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onTrace(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onConnect(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    protected Response onPatch(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        return defaultResponse();
    }

    /**
     * override the method to change the default response of unemployment method.<br/>
     * by default, returns 405.
     *
     * @return the default response
     */
    protected Response defaultResponse() {
        return newFixedLengthResponse(Status.METHOD_NOT_ALLOWED, MIME_PLAINTEXT, null);
    }

    private void updatePostData(Map<String, String> args, String postdata) {
        if (postdata != null) {
            String[] datas = postdata.split("&");
            for (String data : datas) {
                int pos = data.indexOf('=');
                String key = data.substring(0, pos);
                String value = data.substring(pos + 1);
                args.put(key, value);
            }
        }
    }

    /**
     * dispatch the request by method.<br/>
     * Override this method is not recommended
     *
     * @param root    the url of the HTTP server's root
     * @param args    the params of the request
     * @param session information of the session
     * @return
     */
    @Override
    public Response onRequest(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Method method = session.getMethod();
        try {
            switch (method) {
                case GET:
                    return this.onGet(root, args, session);
                case PUT:
                case POST:
                    Map<String, String> files = new HashMap<>();
                    session.parseBody(files);
                    String postData = files.get("postData");
                    updatePostData(args, postData);
                    return method == Method.PUT ?
                            this.onPut(root, args, files, session) :
                            this.onPost(root, args, files, session);
                case DELETE:
                    return this.onDelete(root, args, session);
                case HEAD:
                    return this.onHead(root, args, session);
                case OPTIONS:
                    return this.onOptions(root, args, session);
                case TRACE:
                    return this.onTrace(root, args, session);
                case CONNECT:
                    return this.onConnect(root, args, session);
                case PATCH:
                    return this.onPatch(root, args, session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
