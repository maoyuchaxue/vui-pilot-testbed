package edu.tsinghua.vui.vuitestbed.playback;

public class MultiModalResponseHandler {
    private MultiModalConfig modalConfig;

    public MultiModalResponseHandler(MultiModalConfig modalConfig) {
        this.modalConfig = modalConfig;
    }

    public void onWakeup() {
        // TODO
    }

    public void onAddText(String text) {
        if (modalConfig.hasTextFeedback) {
            // TODO
        }
    }

    public void onAddGraph(String url) {
        if (modalConfig.hasGraphFeedback) {
            // TODO
        }
    }

    public boolean hasVoiceFeedback() { return modalConfig.hasVoiceFeedback; }
}
