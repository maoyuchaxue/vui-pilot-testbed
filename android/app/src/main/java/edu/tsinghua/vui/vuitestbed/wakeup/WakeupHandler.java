package edu.tsinghua.vui.vuitestbed.wakeup;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.tsinghua.vui.vuitestbed.util.MessageQueue;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

import java.io.OutputStream;
import java.net.Socket;

public class WakeupHandler implements Runnable {
    public static final int UNWAKEUP = 0;
    public static final int VISUAL_WAKEUP = 1;
    public static final int VOICE_WAKEUP = 2;
    public static final int VIBRATE_WAKEUP = 3;

    private Handler handler;
    private MessageQueue msgQueue;
    private Socket visualSocket;
    private Socket voiceSocket;
    private Socket vibrateSocket;
    private Socket screenSocket;

    public WakeupHandler(Handler handler, MessageQueue msgQueue) {
        this.handler = handler;
        this.msgQueue = msgQueue;
    }

    public void run() {
        String serverIP = NetConfig.getServerIP();
        int iDevPort = NetConfig.getIDevPort();
        int oDevPort = NetConfig.getODevPort();

        try {
            visualSocket = new Socket(serverIP, oDevPort);
            voiceSocket = new Socket(serverIP, oDevPort);
            vibrateSocket = new Socket(serverIP, oDevPort);
            screenSocket = new Socket(serverIP, iDevPort);

            while (!visualSocket.isConnected());
            visualSocket.getOutputStream().write("screen".getBytes());
            visualSocket.getOutputStream().flush();

            while (!voiceSocket.isConnected());
            voiceSocket.getOutputStream().write("voice".getBytes());
            voiceSocket.getOutputStream().flush();

            while (!vibrateSocket.isConnected());
            vibrateSocket.getOutputStream().write("vibrate".getBytes());
            vibrateSocket.getOutputStream().flush();

            while (!screenSocket.isConnected());
            screenSocket.getOutputStream().write("screen".getBytes());
            screenSocket.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            while (true);
        }

        new Thread() {
            @Override
            public void run() {
                byte buf[] = new byte[1];
                while (true) {
                    int read = 0;
                    try {
                        read = visualSocket.getInputStream().read(buf, 0, 1);
                    } catch (Exception e) {
                        Log.e("VUI", e.getMessage());
                    }
                    if (read == 1) {
                        Message msg = handler.obtainMessage();
                        if (buf[0] == '1') {
                            msg.what = VISUAL_WAKEUP;
                        } else if (buf[0] == '0') {
                            msg.what = UNWAKEUP;
                        }
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                byte buf[] = new byte[1];
                while (true) {
                    int read = 0;
                    try {
                        read = voiceSocket.getInputStream().read(buf, 0, 1);
                    } catch (Exception e) {
                        Log.e("VUI", e.getMessage());
                    }
                    if (read == 1) {
                        Message msg = handler.obtainMessage();
                        if (buf[0] == '1') {
                            msg.what = VOICE_WAKEUP;
                            msg.obj = msgQueue;
                        }
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                byte buf[] = new byte[1];
                while (true) {
                    int read = 0;
                    try {
                        read = vibrateSocket.getInputStream().read(buf, 0, 1);
                    } catch (Exception e) {
                        Log.e("VUI", e.getMessage());
                    }
                    if (read == 1) {
                        Message msg = handler.obtainMessage();
                        if (buf[0] == '1') {
                            msg.what = VIBRATE_WAKEUP;
                        }
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();
    }

    public void screenWakeup() {
        new Thread() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = screenSocket.getOutputStream();
                    outputStream.write((int)'1');
                    outputStream.flush();
                } catch (Exception e) {
                    Log.e("VUI", e.getMessage());
                }
            }
        }.start();
    }
}
