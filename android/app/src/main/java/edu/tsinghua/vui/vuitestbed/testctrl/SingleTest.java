package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.LocalNetPlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;
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

        InputHandler inputHandler = new LocalNetInputHandler(playbackManager, cuid);
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