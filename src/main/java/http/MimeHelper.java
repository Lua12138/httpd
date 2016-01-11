package http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by forDream on 2015-12-26.
 */
public class MimeHelper {
    private static MimeHelper instance;
    private static Object locker;
    public Map<String, String> map;

    static {
        locker = new Object();
    }

    public static MimeHelper instance() {
        if (instance == null) // double check
            synchronized (locker) {
                if (instance == null)
                    instance = new MimeHelper();
            }

        return instance;
    }

    private MimeHelper() {
        //String dir = AppProperty.userDir() + File.separator;
        ObjectMapper objectMapper = new ObjectMapper();
        //File config = new File(dir + "mime.json");
        String url = MimeHelper.class.getClassLoader().getResource("mime.json").getFile();
        File config = new File(url);

        this.map = this.loadExtraMime(config);

        if (this.map == null) {
            throw new Error("init mime helper fail.");
        }
    }

    public String getMime(String file) {
        int dotPos = file.lastIndexOf(".");
        String ext = file.substring(dotPos + 1).toLowerCase();
        String mime = map.get(ext);
        if (mime == null)
            mime = "application/octet-stream";
        return mime;
    }

    public String getMime(File file) {
        return getMime(file.getAbsolutePath());
    }

    private Map<String, String> loadExtraMime(File config) {
        if (config.exists()) {
            try {
                Map<String, String> map = new ObjectMapper().readValue(config, HashMap.class);
                return map;
            } catch (IOException e) {
                System.err.println(String.format("IOException -> %s", e.getMessage()));
            }
        } else {
            System.err.println(String.format("%s not exists.", config.getAbsolutePath()));
        }
        return null;
    }

    /**
     * load extra configuration, override inner config.
     *
     * @param configUrl config file url
     */
    public void loadExtraMime(String configUrl) {
        File config = new File(configUrl);
        Map<String, String> map = this.loadExtraMime(config);
        if (map != null) {
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                String value = map.get(key);
                this.map.put(key, value);
            }
        }
    }
}
