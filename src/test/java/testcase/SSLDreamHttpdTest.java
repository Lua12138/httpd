package testcase;

import fordream.http.SSLDreamHttpd;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testhealper.HttpRequester;
import testhealper.TestRequetHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Created by forDream on 2016-03-07.
 */
public class SSLDreamHttpdTest {
    private SSLDreamHttpd httpd;
    private int httpPort;
    private int httpsPort;

    @Before
    public void startup() throws IOException {
        File trustStore = new File("src/test/resources/keystore.jks");
        System.setProperty("javax.net.ssl.trustStore", trustStore.getAbsolutePath());
        Random random = new Random();
        this.httpPort = random.nextInt(1024) + 2048;
        this.httpsPort = random.nextInt(1024) + 4096;
        httpd = new SSLDreamHttpd(this.httpPort, this.httpsPort, "/" + trustStore.getName(), "password", null);
        httpd.registerHandler(new TestRequetHandler());
        httpd.start();
    }

    @After
    public void destroy() {
        this.httpd.stop();
    }

    @Test
    public void httpTest() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(new URL("http://127.0.0.1:" + this.httpPort), "GET", null, null, false);
        Assert.assertEquals(200, response.getResponseCode());
    }

    @Test
    public void httpsTest() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(new URL("https://127.0.0.1:" + this.httpsPort), "GET", null, null, false);
        Assert.assertEquals(200, response.getResponseCode());
    }
}
