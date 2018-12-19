package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;

import java.io.FileInputStream;
import java.util.Properties;


public class MainTestor {

    public static void main(String[] args) throws Exception {
        Properties properties = getProperties();
        SingleTest test = new SingleTest(properties, null, "default");
        test.run();
    }
    
    private static Properties getProperties() throws Exception {
        String fullFilename = System.getProperty("user.dir") + "../conf/sdk.properties";
        Properties properties = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(fullFilename);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }

}
