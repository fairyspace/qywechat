package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

@Data
public class WXAgentTextReq {
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype="text";
    private long agentid;
    private WXAgentText text;
    private int safe=0;
    private int enable_id_trans=0;
    private int enable_duplicate_check=1;
    private int duplicate_check_interval=1800;
}
