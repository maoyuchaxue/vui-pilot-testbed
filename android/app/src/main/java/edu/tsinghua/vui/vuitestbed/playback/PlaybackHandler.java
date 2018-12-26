package edu.tsinghua.vui.vuitestbed.playback;

public interface PlaybackHandler {
    public void register(String text, String cuid);
    public void play(int mills);
    public boolean ended();
}