package io.github.fairyspace.qywechat.beans.common;

import lombok.Data;

@Data
public class AskAndAnswer {
    String ask;
    String answer;

    String msgId;

    String mediaId;
    String picUrl;

    public AskAndAnswer(String ask, String answer,String mesId,String mediaId,String picUrl ) {
        this.ask = ask;
        this.answer = answer;
        this.msgId = mesId;
        this.mediaId = mediaId;
        this.picUrl = picUrl;
    }
}
