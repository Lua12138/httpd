package testcase;

import fordream.http.DreamHttpd;
import fordream.http.handler.StaticResourcesHandler;
import org.junit.After;
import org.junit.Assert;
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

        int b;
        while (-1 != (b = is.read())) {
            if (b != isFile.read()) {
                is.close();
                isFile.close();
                System.err.println("Content Error");
                return false;
            }
        }

        is.close();

        boolean bool = isFile.read() == -1;
        isFile.close();
        if (!bool) System.err.println("Length Error");
        return bool;
    }

    @Test
    public void response404Test() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("no-exists.resource"), "GET", null, null, false);
        Assert.assertEquals(404, response.getResponseCode());
    }

    @Test
    public void response200Test() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("test_mime.json"), "GET", null, null, false);
        Assert.assertEquals(200, response.getResponseCode());
    }

    @Test
    public void response405Test() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(this.buildUrl("test_mime.json"), "HEAD", null, null, false);
        Assert.assertEquals(405, response.getResponseCode());
    }

    @Test
    public void downloadOnGetTest() throws IOException {
        InputStream inputStream = HttpRequester.doGet(this.buildUrl("test_mime.json"), null, null);
        Assert.assertTrue(this.checkFile(inputStream, "src/test/resources/test_mime.json"));
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

        Assert.assertTrue(builder.indexOf(this.requestHeaders.get("test")) > 0);
        Assert.assertEquals("application/json", response.getResponseHeaders().get("Content-Type").get(0));
    }
}
