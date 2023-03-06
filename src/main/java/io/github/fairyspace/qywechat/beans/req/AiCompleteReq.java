package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

@Data
public class AiCompleteReq {
    private String model="text-davinci-003";
    private String prompt;
    private int temperature=0;
    private int max_tokens=300;
    private double top_p=1.0;
    private double frequency_penalty=0.5;
    private double presence_penalty=0.0;
}
