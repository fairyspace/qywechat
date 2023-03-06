package io.github.fairyspace.qywechat.service;

import io.github.fairyspace.qywechat.beans.req.WXBotMarkDownReq;
import io.github.fairyspace.qywechat.beans.req.WXBotTextReq;
import io.github.fairyspace.qywechat.beans.req.WXMsgData;
import io.github.fairyspace.qywechat.beans.res.WXBotBackRes;

public interface BotService {
  void dealWith(WXMsgData wxMsgData);


  void sendTxt(WXBotTextReq wxBotTextReq);
  WXBotBackRes sendMarkDown(WXBotMarkDownReq wxBotMarkDownReq);
}
