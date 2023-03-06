package io.github.fairyspace.qywechat.beans.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public interface Global {
    /*先用*/
    Map<String, LinkedList<AskAndAnswer>> userHistoryDialog = new HashMap<>();

    Map<String, PictureInfo> images = new HashMap<>();
}
