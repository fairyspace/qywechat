package io.github.fairyspace.qywechat.beans.res;

import lombok.Data;

@Data
public class WXAgentBackRes {
    int errcode;//返回码
    String errmsg;    //对返回码的文本描述内容
    String invaliduser;//	不合法的userid，不区分大小写，统一转为小写
    String invalidparty;//不合法的partyid
    String invalidtag;    //不合法的标签id
    String unlicenseduser;    //没有基础接口许可(包含已过期)的userid
    String msgid;//消息id，用于撤回应用消息
    String response_code;    //仅消息类型为“按钮交互型”，“投票选择型”和“多项选择型”的模板卡片消息返回，应用可使用response_code调用更新模版卡片消息接口，72小时内有效，且只能使用一次

}
