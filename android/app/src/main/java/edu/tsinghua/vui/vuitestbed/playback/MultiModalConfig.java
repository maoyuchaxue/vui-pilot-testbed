package edu.tsinghua.vui.vuitestbed.playback;

public class MultiModalConfig {
    public boolean hasVoiceFeedback;
    public boolean hasTextFeedback;
    public boolean hasGraphFeedback;

    public MultiModalConfig(boolean hasVoiceFeedback, boolean hasTextFeedback, boolean hasGraphFeedback) {
        this.hasVoiceFeedback = hasVoiceFeedback;
        this.hasTextFeedback = hasTextFeedback;
        this.hasGraphFeedback = hasGraphFeedback;
    }
}
