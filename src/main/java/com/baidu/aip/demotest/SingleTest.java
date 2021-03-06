package com.baidu.aip.demotest;

import com.baidu.aip.playback.LocalNetPlaybackFetcher;
import com.baidu.aip.playback.PlaybackFetcher;
import com.baidu.aip.playback.PlaybackManager;
import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.util.Properties;


public class SingleTest {

    private Properties properties;
    private String cuid;

    public SingleTest(Properties properties, String cuid) {
        this.properties = properties;
        this.cuid = cuid;
    }

    public void execute() throws Exception {
        PlaybackManager playbackManager = new PlaybackManager(properties, cuid);
        PlaybackFetcher playbackFetcher = new LocalNetPlaybackFetcher(playbackManager, cuid);

        InputHandler inputHandler = new LocalNetInputHandler();
        playbackManager.start();

        ASRResultListener resultListener = new ASRResultListener(inputHandler);
        Controller controller = new Controller(new LogBeforeUploadListener(), resultListener, properties);

        ASRPerformer performer = new ASRPerformer(playbackFetcher);

        Session session = performer.asr(controller);

        while (true) {
            if (resultListener.isCallEnd(session.getCallId())) {
                System.out.println("Server receive call END EVENT");
                break;
            }
            Thread.sleep(500); // sleep 0.5s
        }
        controller.stop();
        playbackManager.stop();
    }
}