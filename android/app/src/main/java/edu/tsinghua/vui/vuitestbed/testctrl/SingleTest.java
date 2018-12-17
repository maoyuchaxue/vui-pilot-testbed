package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.NetPlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;
import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.util.Properties;


public class SingleTest implements Runnable {

    private Properties properties;
    private String cuid;

    public SingleTest(Properties properties, String cuid) {
        this.properties = properties;
        this.cuid = cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    private ASRPerformer performer;

    public void run() {
        PlaybackManager playbackManager = new PlaybackManager(properties, cuid);
        PlaybackFetcher playbackFetcher = new NetPlaybackFetcher(playbackManager, cuid);

        InputHandler inputHandler = new NetInputHandler(playbackManager, cuid);
        playbackManager.start();

        ASRResultListener resultListener = new ASRResultListener(inputHandler);
        Controller controller = null;
        try {
            controller = new Controller(new LogBeforeUploadListener(), resultListener, properties);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        performer = new ASRPerformer(playbackFetcher);

        Session session = performer.asr(controller);

        while (true) {
            if (resultListener.isCallEnd(session.getCallId())) {
                System.out.println("Server receive call END EVENT");
                break;
            }
            try {
                Thread.sleep(500); // sleep 0.5s
            } catch (Exception e) {}
        }
        controller.stop();
        playbackManager.stop();
    }

    public synchronized void stop() {
        performer.stop();
    }

}