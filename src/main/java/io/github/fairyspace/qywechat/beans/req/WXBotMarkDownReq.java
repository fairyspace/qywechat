package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WXBotMarkDownReq {
    String msgtype = "markdown";
    WXBotMarkDownReq markdown;

    @Data
    class WXBotMarkDown {
        String content;
    }
}
