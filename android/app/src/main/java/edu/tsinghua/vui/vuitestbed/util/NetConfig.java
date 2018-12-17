package edu.tsinghua.vui.vuitestbed.util;

public class NetConfig {
    private static String NetUrl = "http://localhost:7575";

    public static void  setNetUrl(String netUrl) {
        NetUrl = netUrl;
    }

    public static String getNetUrl() {
        return NetUrl;
    }
}
