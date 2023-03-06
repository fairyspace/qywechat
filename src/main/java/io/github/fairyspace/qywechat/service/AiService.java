package io.github.fairyspace.qywechat.service;

import io.github.fairyspace.qywechat.beans.req.AiCompleteReq;
import io.github.fairyspace.qywechat.beans.req.WXMsgData;
import io.github.fairyspace.qywechat.beans.res.AiCompleteRes;

public interface AiService {

     AiCompleteRes complete(AiCompleteReq aiCompleteReq);


     String dealWithAi(WXMsgData content,String media);
}
