package com.baidu.aip.demotest;

import com.baidu.aip.playback.PlaybackManager;
import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.io.FileInputStream;
import java.util.Properties;


public class MainTestor {

    public static void main(String[] args) throws Exception {
        Properties properties = getProperties();
        SingleTest test = new SingleTest(properties, "default");
        test.execute();
    }
    
    private static Properties getProperties() throws Exception {
        String fullFilename = System.getProperty("user.dir") + "/conf/sdk.properties";
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
