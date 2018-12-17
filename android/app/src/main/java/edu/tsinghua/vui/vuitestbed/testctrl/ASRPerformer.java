package edu.tsinghua.vui.vuitestbed.testctrl;

import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.ISessionController;
import com.baidu.aip.talker.facade.exception.SendException;

import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;

public class ASRPerformer {

    AudioCapturer capturer;
    PlaybackFetcher playbackFetcher;
    boolean stopped;

    public ASRPerformer(PlaybackFetcher playbackFetcher) {
        this.playbackFetcher = playbackFetcher;
        capturer = new AudioCapturer();
    }
    
    public Session asr(ISessionController controller) {
        Session.Config config = Session.createConfig(Session.Config.RoleId.AGENT, false);
        stopped = false;
        
        config.setAgentDn(123);
        Session session = null;
        try {
            session = controller.startSession(config);
            registerShutdown(controller, session); // ctrl+C exits

            startCapture(session);
            session.sendEndSpeech();
            session.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("meet exception to exit ASR");
            System.exit(5);
        }

        return session;
    }

    private void startCapture(Session session) throws Exception {
        capturer.start();
        try {
            while (!stopped) {
                try {
                    Thread.sleep(AudioCapturer.packageDurationInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                byte[] bytes = capturer.read();

                if (bytes == null) {
                    break;
                }
                session.sendFirstRoleSpeech(bytes);
                playbackFetcher.fetch(); // fetch response
            };
        } finally {
            capturer.stop();
        }

    }
    
    private static void registerShutdown(ISessionController controller, final Session session) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    session.sendEndSpeech();
                    Thread.currentThread().sleep(1000);
                } catch (SendException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public synchronized void stop() {
        stopped = true;
    }
}