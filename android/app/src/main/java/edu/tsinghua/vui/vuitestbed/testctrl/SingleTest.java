package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.NetPlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;

import java.util.Properties;


public class SingleTest implements Runnable {

    private NetServerNotifier notifier;
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
        notifier = new NetServerNotifier(cuid);
        notifier.notifyStart();
        PlaybackManager playbackManager = new PlaybackManager(properties, cuid);
        PlaybackFetcher playbackFetcher = new NetPlaybackFetcher(playbackManager, cuid);

        RawAudioDataHandler rawAudioDataHandler = new NetRawAudioDataHandler(playbackManager, cuid);
        playbackManager.start();

        performer = new ASRPerformer(playbackFetcher, rawAudioDataHandler);
        performer.startCapture();
        playbackManager.stop();
    }

    public synchronized void stop() {
        performer.stop();
        notifier.notifyEnd();
    }

}