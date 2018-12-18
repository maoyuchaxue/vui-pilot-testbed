package com.baidu.aip.demotest;

import com.baidu.aip.talker.controller.Session;
import java.io.FileInputStream;
import java.util.Properties;
import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;


public class ASRService {

    private Properties properties;
    private Session session;

    public ASRService() {
        try {
            properties = getProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(String cuid) {
        InputHandler inputHandler = new LocalNetInputHandler(cuid);
        ASRResultListener resultListener = new ASRResultListener(inputHandler);
        Controller controller = null;
        try {
            controller = new Controller(new LogBeforeUploadListener(), resultListener, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Session.Config config = Session.createConfig(Session.Config.RoleId.AGENT, false);
        config.setAgentDn(123);
        try {
            session = controller.startSession(config);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("meet exception to exit ASR");
            System.exit(5);
        }
    }

    public void stop() {
        try {
            session.sendEndSpeech();
            session.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("End speech error");
        }
    }

    public void sendBytes(byte[] data) {
        try {
            session.sendFirstRoleSpeech(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("speech error");
        }
    }

    private static Properties getProperties() throws Exception {
        String fullFilename = System.getProperty("user.dir") + "/../conf/sdk.properties";
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