package testcase;

import fordream.http.DreamHttpd;
import fordream.http.handler.StaticResourcesHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testhealper.HttpRequester;
import testhealper.TestRequetHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by forDream on 2016-03-08.
 */
public class DreamHttpdTest {
    private DreamHttpd httpd;
    private int randomPort;
    private String host;

    public Map<String, String> requestHeaders;

    @Before
    public void init() throws IOException {
        this.randomPort = new Random().nextInt(2048) + 1024;
        this.host = "http://127.0.0.1:" + this.randomPort + "/";
        final String root = "src/test/resources";
        this.httpd = new DreamHttpd(randomPort, root);
        this.httpd.registerHandler(new StaticResourcesHandler());
        this.httpd.start();

        this.requestHeaders = new HashMap<>();
        this.requestHeaders.put("test", "Test-Tag -> " + randomPort);
    }

    @After
    public void destroy() {
        this.httpd.stop();
    }

    protected URL buildUrl(String resources) throws MalformedURLException {
        return new URL(this.host + resources);
    }

    protected boolean checkFile(InputStream is, String file) throws IOException {

        InputStream isFile = new FileInputStream(file);
        if (is.available() != isFile.available()) {
            is.close();
            isFile.close();
            return false;
        }

        int b;
        while (-1 != (b = is.read())) {
            if (b != isFile.read()) {
                is.close();
                isFile.close();
                return false;
            }
        }

        is.close();
        isFile.close();
        return true;
    }

    @Test
    public void response404Test() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("no-exists.resource"), "GET", null, null, false);
        assert response.getResponseCode() == 404;
    }

    @Test
    public void response200Test() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("test_mime.json"), "GET", null, null, false);
        assert response.getResponseCode() == 200;
    }

    @Test
    public void response405Test() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("test_mime.json"), "HEAD", null, null, false);
        assert response.getResponseCode() == 405;

    }

    @Test
    public void downloadOnGetTest() throws IOException {
        InputStream inputStream = HttpRequester.doGet(this.buildUrl("test_mime.json"), null, null);
        assert this.checkFile(inputStream, "src/test/resources/test_mime.json");
    }

    @Test
    public void requestOnGetTest() throws IOException {
        this.httpd.removeHandler(new StaticResourcesHandler());
        this.httpd.registerHandler(new TestRequetHandler());

        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("keystore.jks"), "GET", this.requestHeaders, null, false);
        assert response.getResponseCode() == 200;

        StringBuilder builder = new StringBuilder();
        int b;
        InputStream is = response.getResponse();
        while (-1 != (b = is.read()))
            builder.append((char) b);

        assert builder.indexOf(this.requestHeaders.get("test")) > 0;
        assert "application/json".equals(response.getResponseHeaders().get("Content-Type").get(0));
    }
}
