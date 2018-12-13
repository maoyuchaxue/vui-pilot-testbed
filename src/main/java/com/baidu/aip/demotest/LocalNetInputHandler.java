package com.baidu.aip.demotest;

import com.baidu.aip.playback.PlaybackManager;
import com.baidu.util.ConnUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

public class LocalNetInputHandler implements InputHandler {

    private PlaybackManager playbackManager;
    private String localNetURL = "http://localhost:7575/user";
    private String cuid;

    public LocalNetInputHandler(PlaybackManager playbackManager, String cuid) {
        this.playbackManager = playbackManager;
        this.cuid = cuid;
    }

    public void onInputReceived(String input) {

        String params = "cuid=" + cuid;
        if (input != null) {
            params += "text=" + ConnUtil.urlEncode(ConnUtil.urlEncode(input));
        }

        String paramedURL = localNetURL + "?" + params;

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(paramedURL).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot access net server");
            return ;
        }

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(2000);

        try {
            byte[] response = ConnUtil.getResponseBytes(conn);
            if (response.length > 0) {
                playbackManager.addMessage(new String(response));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}