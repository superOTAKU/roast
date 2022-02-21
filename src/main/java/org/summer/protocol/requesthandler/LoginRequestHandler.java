package org.summer.protocol.requesthandler;

import cn.hutool.json.JSONObject;
import org.summer.domain.ChatManager;
import org.summer.domain.User;
import org.summer.protocol.ErrorCode;
import org.summer.protocol.RemoteObject;
import org.summer.protocol.data.ErrorData;
import org.summer.server.netty.Request;
import org.summer.server.netty.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class LoginRequestHandler implements RequestHandler {

    @Override
    public void handle(Request request) {
        //设置用户的昵称
        ChatManager chatManager = request.getChatManager();
        RemoteObject remoteObject = request.getRemoteObject();
        JSONObject data = remoteObject.getJSONData();
        String nickName = data.getStr("nickName");
        User user = chatManager.createUser(nickName);
        if (user == null) {
            ErrorData errorData = new ErrorData();
            errorData.setErrorCode(ErrorCode.NICK_NAME_DUPLICATED);
            request.replyError(errorData);
            return;
        }
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("userId", user.getId());
        request.replyOk(retMap);
    }

}
