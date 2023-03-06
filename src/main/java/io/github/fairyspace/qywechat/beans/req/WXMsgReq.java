package io.github.fairyspace.qywechat.beans.req;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WXMsgReq {
    @XmlElement(name = "ToUserName")
    private String toUserName;
    @XmlElement(name = "AgentId")
    private String agentId;
    @XmlElement(name = "Encrypt")
    private String encrypt;
}
