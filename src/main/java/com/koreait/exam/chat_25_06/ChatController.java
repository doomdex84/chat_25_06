
package com.koreait.exam.chat_25_06;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@@ -14,21 +12,14 @@ public class ChatController {

    private List<ChatMessage> chatMessages = new ArrayList<>();

    public record writeChatMessageResponse(long id, String authorName, String content) {


    }

//    @AllArgsConstructor
//    @Getter
//    public static class writeChatMessageRequest {
//        private final String authorName;
//        private final String content;
//    }

    public record writeChatMessageRequest(String authorName, String content) {
    }


    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<writeChatMessageResponse> writeMessage(@RequestBody writeChatMessageRequest req) {
        @@ -43,14 +34,18 @@ public RsData<writeChatMessageResponse> writeMessage(@RequestBody writeChatMessa
        );
    }





    @GetMapping("/messages")
    @ResponseBody
    public RsData<List<ChatMessage>> showMessages() {

        return new RsData<>(
                "S-1",
                "성공",
                chatMessages
        );
    }
}