package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

@Data
public class WXBotTextReq {
    String msgtype = "text";
    WXBotText text;
}
