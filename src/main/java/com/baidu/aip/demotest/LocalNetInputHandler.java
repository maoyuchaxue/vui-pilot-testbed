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

    private String localNetURL = "http://localhost:7575/user";
    private String cuid;

    public LocalNetInputHandler() {}

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public void onInputReceived(String input, boolean completed) {
        try {
            input = new String(input.getBytes("utf-8"), "gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String params = "cuid=" + cuid + "&completed=" + new Boolean(completed).toString();
        if (input != null) {
            params += "&text=" + ConnUtil.urlEncodeGBK(ConnUtil.urlEncodeGBK(input));
        }

        String paramedURL = localNetURL + "?" + params;

        System.out.println(paramedURL);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(paramedURL).openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setConnectTimeout(2000);
            conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot access net server");
            return ;
        }
    }
}