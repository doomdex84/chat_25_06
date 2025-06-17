package com.koreait.exam.chat_25_06;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SseEmitters sseEmitters;

    private List<ChatMessage> chatMessages = new ArrayList<>();


    public record writeChatMessageRequest(String authorName, String content) {
    }

    public record writeChatMessageResponse(long id, String authorName, String content) {

    }

    @GetMapping("/room")
    public String showRoom() {
        return "chat/room";
    }


    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<writeChatMessageResponse> writeMessage(@RequestBody writeChatMessageRequest req) {
        ChatMessage message = new ChatMessage(req.authorName, req.content);

        chatMessages.add(message);

        sseEmitters.noti("chat__messageAdded");

        return new RsData<>(
                "S-1",
                "메세지가 작성됨",
                new writeChatMessageResponse(message.getId(), message.getAuthorName(), message.getContent())
        );
    }

    public record messagesRequest(Long fromId) {

    }

    public record messagesResponse(List<ChatMessage> chatMessages, long count) {

    }

    @GetMapping("/messages")
    @ResponseBody
    public RsData<messagesResponse> showMessages(messagesRequest req) {

        List<ChatMessage> messages = chatMessages;

        log.debug("req : {}", req);

        // 번호가 같이 입력되는 경우
        if (req.fromId != null) {

            // 해당 번호의 채팅 메세지가 전체 리스트의 몇번째 인덱스인지?
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == req.fromId)
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                // 만약에 index가 -1 이 아니라면? 0번부터 index번 까지 제거한 리스트를 만든다.
                messages = messages.subList(index + 1, messages.size());
            }
        }


        return new RsData<>(
                "S-1",
                "성공",
                new messagesResponse(messages, messages.size())
        );
    }
}