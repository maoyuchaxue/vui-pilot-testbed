package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.MultiModalConfig;
import edu.tsinghua.vui.vuitestbed.playback.MultiModalResponseHandler;
import edu.tsinghua.vui.vuitestbed.playback.NetPlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;

import java.util.Properties;


public class SingleTest implements Runnable {

    private NetServerNotifier notifier;
    private MultiModalConfig modalConfig;
    private Properties properties;
    private String cuid;


    public SingleTest(Properties properties, MultiModalConfig modalConfig, String cuid) {
        this.properties = properties;
        this.modalConfig = modalConfig;
        this.cuid = cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    private ASRPerformer performer;

    public void run() {
        notifier = new NetServerNotifier(cuid);
        notifier.notifyStart();
        MultiModalResponseHandler responseHandler = new MultiModalResponseHandler(modalConfig);
        PlaybackManager playbackManager = new PlaybackManager(responseHandler, properties, cuid);
        PlaybackFetcher playbackFetcher = new NetPlaybackFetcher(playbackManager, cuid);

        RawAudioDataHandler rawAudioDataHandler = new NetRawAudioDataHandler(playbackManager, cuid);
        playbackManager.start();

        performer = new ASRPerformer(playbackFetcher, rawAudioDataHandler);
        performer.startCapture();
        playbackManager.stop();
        notifier.notifyEnd();
    }

    public synchronized void stop() {
        performer.stop();
    }

}