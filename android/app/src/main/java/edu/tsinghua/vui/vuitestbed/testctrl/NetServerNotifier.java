package edu.tsinghua.vui.vuitestbed.testctrl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tsinghua.vui.vuitestbed.util.ConnUtil;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

public class NetServerNotifier {
    private String cuid;
    private String localNetURL;

    public NetServerNotifier(String cuid) {
        this.cuid = cuid;
        this.localNetURL = NetConfig.getNetUrl() + "/control";
    }

    public void notifyStart() {
        String params = "cuid=" + cuid + "&start=1";
        String paramedURL = localNetURL + "?" + params;
        sendRequest(paramedURL);
    }

    public void notifyEnd() {
        String params = "cuid=" + cuid + "&end=1";
        String paramedURL = localNetURL + "?" + params;
        sendRequest(paramedURL);
    }

    private void sendRequest(String paramedURL) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(paramedURL).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot access net server");
            return ;
        }

        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setConnectTimeout(2000);
        try {
            conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
