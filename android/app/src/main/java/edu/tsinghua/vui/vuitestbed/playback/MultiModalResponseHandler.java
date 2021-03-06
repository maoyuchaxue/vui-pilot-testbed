package edu.tsinghua.vui.vuitestbed.playback;

import android.util.Log;

import edu.tsinghua.vui.vuitestbed.SingleTestActivity;

public class MultiModalResponseHandler {
    private MultiModalConfig modalConfig;
    private Boolean updated;
    private String text;
    private String imageURL;

    private SingleTestActivity activity;

    public MultiModalResponseHandler(SingleTestActivity activity, MultiModalConfig modalConfig) {
        this.activity = activity;
        this.modalConfig = modalConfig;
        this.updated = false;
        this.text = "";
        this.imageURL = "";
    }

    public void onAddText(String text) {
        if (modalConfig.hasTextFeedback) {
            Log.i("VUI", "add Text: " + text);
            synchronized (this) {
                if (this.text != text) {
                    updated = true;
                }
                this.text = text;
            }
        }
    }

    public void onAddGraph(String url) {
        if (modalConfig.hasGraphFeedback) {
            Log.i("VUI", "add Graph: " + url);
            synchronized (imageURL) {
                if (this.imageURL != url) {
                    updated = true;
                }
                imageURL = url;
            }
        }
    }

    public boolean hasVoiceFeedback() { return modalConfig.hasVoiceFeedback; }

    public void update() {
        synchronized (this) {
            if (updated) {
                activity.clear();
                activity.addText(text);
                activity.addGraph(imageURL);
            }
            updated = false;
        }
    }
}
