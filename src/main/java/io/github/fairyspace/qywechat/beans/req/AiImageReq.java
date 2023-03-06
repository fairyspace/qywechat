package io.github.fairyspace.qywechat.beans.req;

import io.github.fairyspace.qywechat.beans.common.ImageSize;
import lombok.Data;

@Data
public class AiImageReq {
    String prompt;
    Integer n = 1;
    String size= ImageSize.SIZE256.size;
}
