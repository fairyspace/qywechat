package io.github.fairyspace.qywechat.beans.res;

import lombok.Data;

import java.util.List;

@Data
public class AiImageRes {

    private long created;

    private List<ImageData> data;

}
