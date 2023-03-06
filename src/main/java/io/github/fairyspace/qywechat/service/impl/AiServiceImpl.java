package io.github.fairyspace.qywechat.service.impl;

import io.github.fairyspace.qywechat.beans.common.AskAndAnswer;
import io.github.fairyspace.qywechat.beans.common.ImageSize;
import io.github.fairyspace.qywechat.beans.common.OpenAiURL;
import io.github.fairyspace.qywechat.beans.req.*;
import io.github.fairyspace.qywechat.beans.res.AiCompleteRes;
import io.github.fairyspace.qywechat.beans.res.AiImageRes;
import io.github.fairyspace.qywechat.beans.res.Choices;
import io.github.fairyspace.qywechat.beans.res.ImageData;
import io.github.fairyspace.qywechat.service.AgentService;
import io.github.fairyspace.qywechat.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static io.github.fairyspace.qywechat.beans.common.Global.userHistoryDialog;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Value("${opneAi.key}")
    String openAiKey;

    @Autowired
    @Lazy
    AgentService agentService;

    @Override
    public AiCompleteRes complete(AiCompleteReq aiCompleteReq) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + openAiKey);

        HttpEntity<AiCompleteReq> reqHttpEntity = new HttpEntity<>(aiCompleteReq, httpHeaders);
        AiCompleteRes aiCompleteRes = restTemplate.postForObject(OpenAiURL.COMPLETE, reqHttpEntity, AiCompleteRes.class);
        System.err.println(aiCompleteRes);
        return aiCompleteRes;
    }

    @Override
    public String dealWithAi(WXMsgData wxMsgData, String media) {


        if (media.equals("text")) {
            /*解析content 语义*/
            AiCompleteReq aiCompleteReq = new AiCompleteReq();
            String content = wxMsgData.getContent();
            aiCompleteReq.setPrompt(content);
            AiCompleteRes complete = complete(aiCompleteReq);
            List<Choices> choices = complete.getChoices();
            String answer = choices.get(0).getText();
            String finish = choices.get(0).getFinish_reason();
            /*保留AI上下文，未能完成输出*/
            if (!finish.equals("stop")) {
                wxMsgData.setStop(false);
                threadPoolExecutor.execute(() -> {
                    String s = content + answer;
                    wxMsgData.setContent(s);
                    agentService.dealByText(wxMsgData);
                });
            } else {
                wxMsgData.setStop(true);

            }

            return answer;
        }


        if (media.equals("image")) {
            AiImageReq req = new AiImageReq();
            String content = wxMsgData.getContent();
            if(content.contains("高清")){
                content = content.replace("高清", "");
                req.setSize(ImageSize.SIZE1024.size);
            }

            if(content.contains("标清")){
                content = content.replace("标清", "");
                req.setSize(ImageSize.SIZE512.size);
            }

            req.setPrompt(content);
            AiImageRes aiImageRes = imagine(req);
            List<ImageData> data = aiImageRes.getData();
            return data.get(0).getUrl();
        }


        return "";

    }

    private AiImageRes imagine(AiImageReq req) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + openAiKey);

        HttpEntity<AiImageReq> reqHttpEntity = new HttpEntity<>(req, httpHeaders);
        AiImageRes aiImageRes = restTemplate.postForObject(OpenAiURL.IMAGE, reqHttpEntity, AiImageRes.class);
        System.err.println(aiImageRes);
        return aiImageRes;
    }




}
