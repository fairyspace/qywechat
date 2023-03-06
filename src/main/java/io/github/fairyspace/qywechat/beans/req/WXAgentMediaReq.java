package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

@Data
public class WXAgentMediaReq {
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype="image";
    private long agentid;
    private WXAgentImage image;
    private int safe=0;
    private int enable_duplicate_check=1;
    private int duplicate_check_interval=1800;
}
