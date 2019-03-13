package edu.tsinghua.vui.screen.displayer.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.Socket;

public class ODevNetHandler implements Runnable {

    public static final int UNWAKEUP = 0;
    public static final int VISUAL_WAKEUP = 1;

    private Handler handler;
    private Socket visualSocket;

    public ODevNetHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String serverIP = NetConfig.getServerIP();
        int iDevPort = NetConfig.getIDevPort();
        int oDevPort = NetConfig.getODevPort();
        try {
            visualSocket = new Socket(serverIP, oDevPort);

            while (!visualSocket.isConnected());
            visualSocket.getOutputStream().write("screen".getBytes());
            visualSocket.getOutputStream().flush();

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

    }

}
