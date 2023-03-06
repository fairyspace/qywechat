package io.github.fairyspace.qywechat.service.impl;

import io.github.fairyspace.qywechat.beans.common.WXAgentURL;
import io.github.fairyspace.qywechat.beans.res.AgentAccessToken;
import io.github.fairyspace.qywechat.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TokenManageService {

    @Autowired
    RestTemplate restTemplate;

    AgentAccessToken accessToken;
    @Value("${wechat.CorpID}")
    String corpID;

    @Value("${wechat.Secret}")
    String Secret;

    @Value("${wechat.refreshToken}")
    Long time;


    public AgentAccessToken getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() - accessToken.getLogin_time() > time) {
            accessToken = doGetAccessToken();
        }
        return accessToken;
    }

    public AgentAccessToken doGetAccessToken() {
        AgentAccessToken accessToken = restTemplate.getForObject(WXAgentURL.GETACCESSTOKEN, AgentAccessToken.class, corpID, Secret);
        accessToken.setLogin_time(System.currentTimeMillis());
        log.info("获取到的token是：{}",accessToken);
        return accessToken;
    }


}
