package fordream.http;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

/**
 * Support both HTTP and HTTPS
 */
public class SSLDreamHttpd {
    private DreamHttpd httpd;
    private DreamHttpd httpsd;

    /**
     * init
     *
     * @param httpPort                      the port of http-server
     * @param httpsPort                     the port of https-server
     * @param keyAndTrustStoreClasspathPath KeyStore resource
     * @param passphrase                    passphrase
     * @param webRoot                       the path of web root
     * @throws IOException
     */
    public SSLDreamHttpd(int httpPort, int httpsPort, String keyAndTrustStoreClasspathPath, String passphrase, String webRoot) throws IOException {
        this.httpd = new DreamHttpd(httpPort, webRoot);
        this.httpsd = new DreamHttpd(httpsPort, webRoot);
        this.httpsd.makeSecure(NanoHTTPD.makeSSLSocketFactory(keyAndTrustStoreClasspathPath, passphrase.toCharArray()), null);
    }

    /**
     * register new http request handler both HTTP and HTTPS
     *
     * @param handler the handler you want register.
     */
    public void registerHandler(RequestHandler handler) {
        this.httpd.registerHandler(handler);
        this.httpsd.registerHandler(handler);
    }

    /**
     * remove the registered request handler both HTTP and HTTPS
     *
     * @param handler the handler you want to remove
     */
    public void removeHandler(RequestHandler handler) {
        this.httpd.removeHandler(handler);
        this.httpd.removeHandler(handler);
    }

    /**
     * start the servers
     *
     * @throws IOException if the socket is in use.
     */
    public void start() throws IOException {
        this.httpd.start();
        this.httpsd.start();
    }

    /**
     * stop the servers
     */
    public void stop() {
        this.httpd.stop();
        this.httpsd.stop();
    }

    /**
     * @return the http server
     */
    public DreamHttpd getHttpServer() {
        return this.httpd;
    }

    /**
     * @return the https server
     */
    public DreamHttpd getHttpsServer() {
        return this.httpsd;
    }
}
