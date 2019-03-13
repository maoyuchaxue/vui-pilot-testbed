package edu.tsinghua.vui.screen.displayer.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.OutputStream;
import java.net.Socket;

public class IDevNetHandler implements Runnable  {

    private Socket screenSocket;

    @Override
    public void run() {
        String serverIP = NetConfig.getServerIP();
        int iDevPort = NetConfig.getIDevPort();
        int oDevPort = NetConfig.getODevPort();
        try {
            screenSocket = new Socket(serverIP, iDevPort);

            while (!screenSocket.isConnected());
            screenSocket.getOutputStream().write("screen".getBytes());
            screenSocket.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            while (true);
        }


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
