package com.baidu.aip.demotest;

import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.ISessionController;
import com.baidu.aip.talker.facade.exception.SendException;

public class ASRPerformer {

    AudioCapturer capturer;

    public ASRPerformer() {
        capturer = new AudioCapturer();
    }
    
    public Session asr(ISessionController controller) {
        Session.Config config = Session.createConfig(Session.Config.RoleId.AGENT, false);
        
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
            while (true) {
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
            };
        } finally {
            capturer.stop();
        }

    }
    
    private static void registerShutdown(ISessionController controller, Session session) {
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
}