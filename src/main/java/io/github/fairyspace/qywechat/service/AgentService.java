package io.github.fairyspace.qywechat.service;

import io.github.fairyspace.qywechat.beans.req.WXAgentMediaReq;
import io.github.fairyspace.qywechat.beans.req.WXAgentTextReq;
import io.github.fairyspace.qywechat.beans.req.WXMsgData;

public interface AgentService {


    void doSendTxt(WXAgentTextReq wxAgentTextReq);
    void doSendImage(WXAgentMediaReq wxAgentMediaReq);

    void dealWith(WXMsgData wxMsgData);
    void replay(WXMsgData wxMsgData);

     void dealByText(WXMsgData wxMsgData);
     void dealByImage(WXMsgData wxMsgData);
}
