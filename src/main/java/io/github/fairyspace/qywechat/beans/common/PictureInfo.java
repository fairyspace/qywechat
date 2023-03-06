package io.github.fairyspace.qywechat.beans.common;

import lombok.Data;

@Data
public class PictureInfo {
    String picUrl;
    String mediaId;

    public PictureInfo(String picUrl, String mediaId) {
        this.picUrl = picUrl;
        this.mediaId = mediaId;
    }

    public PictureInfo() {
    }
}
