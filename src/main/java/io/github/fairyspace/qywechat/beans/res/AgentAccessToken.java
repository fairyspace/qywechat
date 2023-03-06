package io.github.fairyspace.qywechat.beans.res;

import lombok.Data;

@Data
public class AgentAccessToken {
    private int errcode;
    private String errmsg;
    private String access_token;
    private int expires_in;
    private long login_time;
}
