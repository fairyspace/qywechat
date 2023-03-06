package io.github.fairyspace.qywechat.beans.common;

public interface WXAgentURL {
    String SendAgentMessage="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={access_token}";
    String GETACCESSTOKEN="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";

    String UPLOADTEMPMEDIA="https://qyapi.weixin.qq.com/cgi-bin/media/upload?type=image&access_token={access_token}&debug=1";

}
