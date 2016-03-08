import fordream.http.SSLDreamHttpd;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Created by forDream on 2016-03-07.
 */
public class SSLDreamHttpdTest {
    private SSLDreamHttpd httpd;

    @Before
    public void startup() throws IOException {
        httpd = new SSLDreamHttpd(80, 443, "/keystore.jks", "4357211", null);
        httpd.registerHandler(new TestRequetHandler());
        httpd.start();
    }

    @After
    public void endup() {
        this.httpd.stop();
    }

    @Test
    public void httpTest() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(new URL("http://127.0.0.1"), "GET", null, null, false);
        assert response.getResponseCode() == 200;
    }

    @Test
    public void httpsTest() throws IOException {
        HttpRequester.HttpResponse response = HttpRequester.doRequest(new URL("https://127.0.0.1"), "GET", null, null, false);
        assert response.getResponseCode() == 200;
    }
}
