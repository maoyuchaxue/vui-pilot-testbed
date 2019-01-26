package edu.tsinghua.vui.vuitestbed.util;

public class NetConfig {
    private static String serverIP = "localhost";

    public static void  setServerIP(String serverIP) {
        NetConfig.serverIP = serverIP;
    }

    public static String getNetUrl() {
        return "http://" + serverIP + ":7575";
    }

    public static int getIDevPort() {
        return 9000;
    }

    public static int getODevPort() {
        return 9001;
    }

    public static String getServerIP() {
        return serverIP;
    }
}
