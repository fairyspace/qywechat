package io.github.fairyspace.qywechat.controller;

import io.github.fairyspace.qywechat.exceptions.AesException;
import io.github.fairyspace.qywechat.utils.WXBizJsonMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
public class MessageSignController {
    @Autowired
    WXBizJsonMsgCrypt wxcpt;

    @GetMapping("/receive")
    public String receiveProtocal(String msg_signature, String timestamp, String nonce, String echostr) throws AesException {
        String sEchoStr; //需要返回的明文
        sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,
                nonce, echostr);
        System.out.println("verifyurl echostr: " + sEchoStr);
        return sEchoStr;
    }
}
