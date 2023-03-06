package io.github.fairyspace.qywechat.service.impl;

import io.github.fairyspace.qywechat.beans.common.*;
import io.github.fairyspace.qywechat.beans.req.*;
import io.github.fairyspace.qywechat.beans.res.AgentAccessToken;
import io.github.fairyspace.qywechat.beans.res.WXAgentBackRes;
import io.github.fairyspace.qywechat.beans.res.WXMediaRes;
import io.github.fairyspace.qywechat.service.AgentService;
import io.github.fairyspace.qywechat.service.AiService;
import io.github.fairyspace.qywechat.service.WXMediaService;
import io.github.fairyspace.qywechat.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    @Lazy
    AiService aiService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;


    @Autowired
    TokenManageService tokenManageService;

    @Autowired
    WXMediaService wxMediaService;


    @Override
    public void doSendTxt(WXAgentTextReq wxAgentTextReq) {
        log.info("wxAgentTextReq:{}", wxAgentTextReq);
        AgentAccessToken accessToken = tokenManageService.getAccessToken();
        WXAgentBackRes wxAgentBackRes = restTemplate.postForObject(WXAgentURL.SendAgentMessage, wxAgentTextReq, WXAgentBackRes.class, accessToken.getAccess_token());
        log.info("wxAgentBackRes:{}", wxAgentBackRes);
    }

    @Override
    public void doSendImage(WXAgentMediaReq wxAgentMediaReq) {
        log.info("wxAgentMediaReq:{}", wxAgentMediaReq);
        AgentAccessToken accessToken = tokenManageService.getAccessToken();
        WXAgentBackRes wxAgentBackRes = restTemplate.postForObject(WXAgentURL.SendAgentMessage, wxAgentMediaReq, WXAgentBackRes.class, accessToken.getAccess_token());
        log.info("wxAgentBackRes:{}", wxAgentBackRes);
    }

    @Override
    public void dealWith(WXMsgData wxMsgData) {


        /*用户输入不同的消息处理，选择不同的逻辑*/
        if (wxMsgData.getMsgType().equals(MediaType.Text.name)) {
            String content = wxMsgData.getContent();
            /*判断是否承接上次会话*/
            if (content.startsWith("相似")) {
                /*查找上一次的会话是什么*/
                LinkedList<AskAndAnswer> askAndAnswers = Global.userHistoryDialog.get(wxMsgData.getFromUserName());
                AskAndAnswer last = askAndAnswers.getLast();
                return;
            }




            /*开始回答消息*/
            /*把信息传递给openAi处理,AI选择模型处理,默认text模型*/

            String prefixKey = "想象";
            if (content.startsWith(prefixKey)) {
                int i = content.indexOf(prefixKey);
                String prompt = content.substring(i + prefixKey.length());
                log.info("绘画指令是{}", prompt);
                wxMsgData.setContent(prompt);
                threadPoolExecutor.execute(() -> {
                    dealByImage(wxMsgData);
                });
                return;
            } else {
                dealByText(wxMsgData);
            }
        }



        if (wxMsgData.getMsgType().equals(MediaType.Image.name)) {
            /**/
            Map<String, PictureInfo> images = Global.images;
            PictureInfo value = new PictureInfo(wxMsgData.getPicUrl(), wxMsgData.getMediaId());
            images.put(wxMsgData.getFromUserName(), value);
            log.info("用户{},信息{}", wxMsgData.getFromUserName(), value);
        }


    }

    public void dealByText(WXMsgData wxMsgData) {
        String answer = aiService.dealWithAi(wxMsgData, "text");
        WXAgentTextReq wxAgentTextReq = new WXAgentTextReq();
        wxAgentTextReq.setTouser(wxMsgData.getFromUserName());
        wxAgentTextReq.setAgentid(wxMsgData.getAgentID());

        if (!wxMsgData.getStop()) {
            answer = answer + "\n==我还没说完，等我喘口气先哈==";
        }
        wxAgentTextReq.setText(new WXAgentText(answer.trim()));
        doSendTxt(wxAgentTextReq);
    }

    @Override
    public void dealByImage(WXMsgData wxMsgData) {
        String answer = aiService.dealWithAi(wxMsgData, "image");
        //  String answer = "https://oaidalleapiprodscus.blob.core.windows.net/private/org-RcZSXRMwLhdCmGNYGMnuaFyq/user-aTl9H9J0NQstzudYVx1fhnag/img-kH8M2jROsYVy2O0IWIQRN3UO.png?st=2023-02-24T05%3A55%3A54Z&se=2023-02-24T07%3A55%3A54Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-02-24T00%3A12%3A10Z&ske=2023-02-25T00%3A12%3A10Z&sks=b&skv=2021-08-06&sig=KfnB6cYAA3ep9vYFWpUQEmJMosVH8Pd8FQbhlW3vjeA%3D";
        /*上传素材*/
        URL url = null;
        try {
            url = new URL(answer);
            String fileName = FileUtil.createFileName(null, ".jpg");
            File file = new File("/temp/" + fileName);
            FileUtils.copyURLToFile(url, file);
            WXMediaRes upload = wxMediaService.upload(file);

            String media_id = upload.getMedia_id();
            WXAgentMediaReq wxAgentMediaReq = new WXAgentMediaReq();
            wxAgentMediaReq.setAgentid(wxMsgData.getAgentID());
            wxAgentMediaReq.setTouser(wxMsgData.getFromUserName());
            wxAgentMediaReq.setImage(new WXAgentImage(media_id));
            doSendImage(wxAgentMediaReq);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void replay(WXMsgData wxMsgData) {
        WXAgentTextReq wxAgentTextReq = new WXAgentTextReq();
        wxAgentTextReq.setTouser(wxMsgData.getFromUserName());
        wxAgentTextReq.setAgentid(wxMsgData.getAgentID());
        if (wxMsgData.getMsgType().equals(MediaType.Image.name)) {
            wxAgentTextReq.setText(new WXAgentText("==你想对这张图片做什么？找相似or编辑"));
        }
        if (wxMsgData.getMsgType().equals(MediaType.Text.name)) {
            wxAgentTextReq.setText(new WXAgentText("==让我思考一会，再回复你=="));
        }

        doSendTxt(wxAgentTextReq);
    }


}
