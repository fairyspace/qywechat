package io.github.fairyspace.qywechat.controller;

import io.github.fairyspace.qywechat.beans.common.AskAndAnswer;
import io.github.fairyspace.qywechat.beans.common.MediaType;
import io.github.fairyspace.qywechat.beans.req.WXMsgData;
import io.github.fairyspace.qywechat.beans.req.WXMsgReq;
import io.github.fairyspace.qywechat.exceptions.AesException;
import io.github.fairyspace.qywechat.service.AgentService;
import io.github.fairyspace.qywechat.service.BotService;
import io.github.fairyspace.qywechat.utils.WXBizJsonMsgCrypt;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

import static io.github.fairyspace.qywechat.beans.common.Global.userHistoryDialog;


@RestController
@RequestMapping("/sign")
@Slf4j
public class MessageCallBackController {

    @Autowired
    WXBizJsonMsgCrypt wxcpt;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    BotService botService;
    @Autowired
    AgentService agentService;

    @SneakyThrows
    @PostMapping(value = "/receive")
    public Object receiveMessage(String msg_signature, String timestamp, String nonce, @RequestBody WXMsgReq wxMsgReq) throws AesException, IOException {
        String encrypt = wxMsgReq.getEncrypt();
        String result = wxcpt.DecryptMsg2(msg_signature, timestamp, nonce, encrypt);
        JAXBContext jaxbContext = JAXBContext.newInstance(WXMsgData.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(result);
        WXMsgData wxMsgData = (WXMsgData) unmarshaller.unmarshal(reader);

        String question = wxMsgData.getContent();

        threadPoolExecutor.execute(() -> {
           // botService.dealWith(wxMsgData);
            agentService.dealWith(wxMsgData);

        });
        agentService.replay(wxMsgData);
        System.err.println(wxMsgData);

        if (wxMsgData.getStop()) {
            saveUserDialog(question,wxMsgData);
        }

        return "success";
    }


    private static void saveUserDialog(String question,WXMsgData wxMsgData) {

        String userName = wxMsgData.getToUserName();
        LinkedList<AskAndAnswer> askAndAnswers = userHistoryDialog.get(userName);
        if (askAndAnswers == null) {
            askAndAnswers = new LinkedList<>();
        }
        AskAndAnswer e = new AskAndAnswer(question, wxMsgData.getContent(),wxMsgData.getMsgId(),wxMsgData.getMediaId(),wxMsgData.getPicUrl());

        askAndAnswers.add(e);
        userHistoryDialog.put(userName, askAndAnswers);
    }


}
