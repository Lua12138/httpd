import fordream.http.MimeHelper;
import org.junit.Test;

/**
 * Created by forDream on 2016-01-11.
 */
public class MimeTest {
    @Test
    public void initTest() {
        MimeHelper helper = MimeHelper.instance();
        System.out.println(helper.getMime("abc.html"));
    }
}
