package io.github.fairyspace.qywechat.service.impl;

import io.github.fairyspace.qywechat.beans.common.WXURL;
import io.github.fairyspace.qywechat.beans.req.*;
import io.github.fairyspace.qywechat.beans.res.AiCompleteRes;
import io.github.fairyspace.qywechat.beans.res.WXBotBackRes;
import io.github.fairyspace.qywechat.beans.res.WXMsgRes;
import io.github.fairyspace.qywechat.service.AiService;
import io.github.fairyspace.qywechat.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class BotServiceImpl implements BotService {

    @Autowired
    RestTemplate restTemplate;


    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    AiService aiService;

    @Value("${wechat.botKey}")
    String key;

    @Override
    public void dealWith(WXMsgData wxMsgData) {
        WXBotTextReq wxBotTextReq = new WXBotTextReq();
        WXBotText text = new WXBotText();
        text.setContent(wxMsgData.getContent());
        List<String> mentionedList = text.getMentionedList();
        mentionedList.add(wxMsgData.getToUserName());
        wxBotTextReq.setText(text);
        sendTxt(wxBotTextReq);
    }

    @Override
    public void sendTxt(WXBotTextReq wxBotTextReq) {
        log.info("wxBotTextReq:{}", wxBotTextReq);

        /*把信息传递给openAi处理*/
        AiCompleteReq aiCompleteReq = new AiCompleteReq();
        aiCompleteReq.setPrompt(wxBotTextReq.getText().getContent());

        threadPoolExecutor.execute(() -> {
            AiCompleteRes complete = aiService.complete(aiCompleteReq);
            String answer = complete.getChoices().get(0).getText();
            wxBotTextReq.getText().setContent(answer);
            WXBotBackRes s = restTemplate.postForObject(WXURL.SENDMESSAGE, wxBotTextReq, WXBotBackRes.class, key);

        });

    }

    @Override
    public WXBotBackRes sendMarkDown(WXBotMarkDownReq wxBotMarkDownReq) {
        return null;
    }
}
