package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

@Data
public class WXAgentText {
    private String content;

    public WXAgentText(String content) {
        this.content = content;
    }
}
