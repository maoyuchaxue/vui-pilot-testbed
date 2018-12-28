package edu.tsinghua.vui.vuitestbed.testctrl;

import android.os.Handler;

import edu.tsinghua.vui.vuitestbed.SingleTestActivity;
import edu.tsinghua.vui.vuitestbed.playback.MultiModalConfig;
import edu.tsinghua.vui.vuitestbed.playback.MultiModalResponseHandler;
import edu.tsinghua.vui.vuitestbed.playback.NetPlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;
import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;
import edu.tsinghua.vui.vuitestbed.util.MessageQueue;

import java.util.Properties;


public class SingleTest implements Runnable {

    private NetServerNotifier notifier;
    private MultiModalResponseHandler responseHandler;
    private Handler messageToUIHandler;
    private Properties properties;
    private String cuid;
    private MessageQueue messageQueue;

    public SingleTest(MultiModalResponseHandler responseHandler, Handler messageToUIHandler, Properties properties, String cuid) {
        this.responseHandler = responseHandler;
        this.messageToUIHandler = messageToUIHandler;
        this.properties = properties;
        this.cuid = cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    private ASRPerformer performer;

    public void run() {
        messageQueue = new MessageQueue();
        notifier = new NetServerNotifier(cuid);
        notifier.notifyStart();
        PlaybackManager playbackManager = new PlaybackManager(responseHandler, messageToUIHandler, properties, cuid);
        PlaybackFetcher playbackFetcher = new NetPlaybackFetcher(playbackManager, cuid);

        RawAudioDataHandler rawAudioDataHandler = new NetRawAudioDataHandler(playbackManager, messageQueue, cuid);
        playbackManager.start();

        performer = new ASRPerformer(playbackFetcher, rawAudioDataHandler);
        performer.startCapture();
        playbackManager.stop();
        notifier.notifyEnd();
    }

    public synchronized void stop() {
        performer.stop();
    }

    public void addMessage(String msg){
        messageQueue.addMessage(msg);
    }
}