package io.github.fairyspace.qywechat.beans.res;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WXMsgRes {
    @XmlElement(name = "Encrypt")
    private String encrypt;
    @XmlElement(name = "MsgSignature")
    private String msgSignature;
    @XmlElement(name = "TimeStamp")
    private String timeStamp;
    @XmlElement(name = "Nonce")
    private String nonce;

}
