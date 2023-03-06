package io.github.fairyspace.qywechat.beans.res;

import lombok.Data;

import java.util.List;

@Data
public class AiCompleteRes {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choices> choices;
    private Usage usage;
}
