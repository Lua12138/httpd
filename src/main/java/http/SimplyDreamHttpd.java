package http;

import http.handler.StaticResourcesHandler;

/**
 * Created by forDream on 2016-01-11.
 */
public class SimplyDreamHttpd extends DreamHttpd {
    public SimplyDreamHttpd(int port, String webRoot) {
        super(port, webRoot);
        this.registerHandler(new StaticResourcesHandler());
    }

    public static void main(String[] args) throws Exception {
        int port = -1;
        String root = null;
        if (args.length > 0)
            port = Integer.valueOf(args[0]);
        else
            port = 2046;

        if (args.length > 1)
            root = args[1];
        else
            root = System.getProperty("user.dir");

        DreamHttpd httpd = new SimplyDreamHttpd(port, root);
        httpd.start();

        System.out.println(String.format("Start Http Server with %d and %s", port, root));

        System.out.println("Press Ctrl + C to exit.");

        while (true) System.in.read();
    }
}
