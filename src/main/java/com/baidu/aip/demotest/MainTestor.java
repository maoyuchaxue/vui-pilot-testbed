package com.baidu.aip.demotest;

import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.io.FileInputStream;
import java.util.Properties;


public class MainTestor {

    public static void main(String[] args) throws Exception {
        InputHandler inputHandler = new LocalNetInputHandler();
        ASRResultListener resultListener = new ASRResultListener(inputHandler);

        Controller controller = new Controller(new LogBeforeUploadListener(), resultListener);
        ASRPerformer performer = new ASRPerformer();

        Session session = performer.asr(controller);

        // 判断服务端是否接受到结束事件，超时5s

        while (true) {
            if (resultListener.isCallEnd(session.getCallId())) {
                System.out.println("Server receive call END EVENT");
                break;
            }
            Thread.sleep(500); // sleep 0.5s
        }

        controller.stop();
    }

    /**
     * 默认读取conf/sdk.properties, 您也可以用下面的构造方法，传入Properties类
     * <p>
     * Controller controller =
     * new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
     *
     * @return
     * @throws Exception
     */
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
