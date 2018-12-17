package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;
import edu.tsinghua.vui.vuitestbed.util.ConnUtil;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

public class NetInputHandler implements InputHandler {

    private PlaybackManager playbackManager;
    private String localNetURL;
    private String cuid;

    public NetInputHandler(PlaybackManager playbackManager, String cuid) {
        this.playbackManager = playbackManager;
        this.cuid = cuid;
        this.localNetURL = NetConfig.getNetUrl() +  "/user";
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