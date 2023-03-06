package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

@Data
public class WXAgentImage {
    String media_id;

    public WXAgentImage(String media_id) {
        this.media_id = media_id;
    }
}
