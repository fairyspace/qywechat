package io.github.fairyspace.qywechat.service.impl;

import io.github.fairyspace.qywechat.beans.common.WXAgentURL;
import io.github.fairyspace.qywechat.beans.res.WXMediaRes;
import io.github.fairyspace.qywechat.service.WXMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@Service
public class WXMediaServiceImpl implements WXMediaService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TokenManageService tokenManageService;

    @Override
    public WXMediaRes upload(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String access_token = tokenManageService.getAccessToken().getAccess_token();
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("media", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        WXMediaRes response = restTemplate.postForObject(WXAgentURL.UPLOADTEMPMEDIA, requestEntity, WXMediaRes.class,access_token);
        log.info("token==>{},上传素材{}",access_token, response);
        return response;
    }
}
