package testcase;

import fordream.http.MimeHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by forDream on 2016-01-11.
 */
public class MimeTest {
    private MimeHelper helper;
    public final static String[][] TEST_CASE = new String[][]{
            {"abc.html", "text/html"},
            {"abc.htm", "text/html"},
            {"abc.unknow", "application/octet-stream"},
            {"a.b.c/d/e/f/.f.g.doc", "application/msword"}
    };

    public final static String[][] EXTRA_TEST_CASE = new String[][]{
            {"aaa.xyz", "myXZY/Type"},
            {"bbb.orz", "orz/XXX"}
    };

    @Before
    public void init() {
        this.helper = MimeHelper.instance();
    }

    @Test
    public void baseTest() {
        for (String[] strings : TEST_CASE) {
            Assert.assertEquals(strings[1], this.helper.getMime(new File(strings[0])));
        }
    }

    @Test
    public void extraTest() {
        this.helper.loadExtraMime("src/test/resources/test_mime.json");
        for (String[] strings : EXTRA_TEST_CASE) {
            Assert.assertEquals(strings[1], this.helper.getMime(new File(strings[0])));
        }
    }
}
