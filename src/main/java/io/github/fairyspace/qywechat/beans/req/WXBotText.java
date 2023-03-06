package io.github.fairyspace.qywechat.beans.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WXBotText {
    String content;

    @JsonProperty("mentioned_list")
    List<String> mentionedList = new ArrayList<>();
    @JsonProperty("mentioned_mobile_list")
    List<String> mentionedMobileList = new ArrayList<>();
}
