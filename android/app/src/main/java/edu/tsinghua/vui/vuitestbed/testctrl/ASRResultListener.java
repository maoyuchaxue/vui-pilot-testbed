package edu.tsinghua.vui.vuitestbed.testctrl;
import com.baidu.aip.talker.facade.common.INameConstant;
import com.baidu.aip.talker.facade.download.IAfterDownloadListener;
import com.baidu.aip.talker.facade.exception.LevelException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ASRResultListener implements IAfterDownloadListener {
    
    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private InputHandler inputHandler;

    private ConcurrentHashMap<String, Long> endCallIds;

    public ASRResultListener(InputHandler inputHandler) {
        objectMapper = new ObjectMapper();
        objectReader = objectMapper.reader();
        endCallIds = new ConcurrentHashMap<String, Long>();
        this.inputHandler = inputHandler;
    }

    @Override
    public void onReceive(String json) {
        String result = null;
        boolean completed = true;
        try {
            JsonNode node = objectReader.readTree(json);
            if (node.has("name") && INameConstant.SPEECH_END_ACK.equals(node.get("name").asText())) {
                int status = node.get("status").asInt();
                String callId = node.get("callId").asText();
                if (status == 0) {
                    endCallIds.put(callId, System.currentTimeMillis());
                    System.out.println("call ended, callId: " + callId);
                }
            } else if (node.has("content")) {
                JsonNode contentNode = node.get("content");
                if (contentNode.has("category")) {
                    String category = contentNode.get("category").asText();
                    if (category.equals("TXT")) {
                        result = parseTxt(contentNode);
                        
                        if (contentNode.has("extJson")) {
                            JsonNode nodeExt = contentNode.get("extJson");
                            if (nodeExt.has("completed")) {
                                if (nodeExt.get("completed").asInt() == 1) {
                                    completed = false;
                                } else if (nodeExt.get("completed").asInt() == 3) {
                                    completed = true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result != null) {
            inputHandler.onInputReceived(result, completed);
        }
    }

    public boolean isCallEnd(String callId) {
        return endCallIds.containsKey(callId);
    }

    // {"content": {"category": "INTENT", "content": "\u4fe1\u606f\u786e\u8ba4", "callId": "s1-ASR-34804-1-1530090052495",
    // "logId": "19D75D7A-0E28-4A40-B6F6-E9E24BB27CAC_22", "roleCategory": "AGENT", "triggerTime": 1530090069734,
    // "extJson": {"slot": {}, "say": "\u5b8c\u6210\u4efb\u52a1\uff1aMSG_CONFIRM", "cmd": 0, "intent": "\u4fe1\u606f\u786e\u8ba4"},
    // "triggerTxt": "\u6270\u5230\u60a8\u5462\u8fd9\u8fb9\u662f\u4fe1\u7528\u5361"}, "name": "knowledge_content",
    // "callId": "s1-ASR-34804-1-1530090052495", "userId": 15137876, "logid": "a2815ab2-79e8-11e8-b871-6c92bf139ec6",
    // "appId": 10811527}
    private String parseTxt(JsonNode node) throws IOException {
        if (node.has("roleCategory") && node.has("content")) {
            String text = node.get("roleCategory").asText()
                + " asr result: " + node.get("content").asText();
            System.out.println(text);
            return node.get("content").asText();
        }
        return null;
    }

    @Override
    public void onReceiveError(String json) {
        System.err.println("PrintAfterDownloadListener : RESULT receive success with error:" + json);
        System.exit(4);
    }

    @Override
    public void onRecieveLocalException(Exception exception, int level) {
        System.err.println("LogAfterDownloadListener Exception , level :" + level);
        if (level == LevelException.ERROR) {
            System.err.println("this is fatal error:");
        }
        exception.printStackTrace();
    }
}