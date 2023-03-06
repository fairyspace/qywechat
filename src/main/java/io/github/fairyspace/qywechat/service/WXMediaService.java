package io.github.fairyspace.qywechat.service;

import io.github.fairyspace.qywechat.beans.res.WXMediaRes;

import java.io.File;

public interface WXMediaService {
    WXMediaRes upload(File file);
}
