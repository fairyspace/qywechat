package io.github.fairyspace.qywechat.beans.res;

import lombok.Data;

@Data
public class WXMediaRes {
    int errcode;
    String errmsg;
    String type;
    String media_id;
    String created_at;
}
