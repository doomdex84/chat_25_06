
package com.koreait.exam.chat_25_06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private List<ChatMessage> chatMessages = new ArrayList<>();


    public record writeChatMessageRequest(String authorName, String content) {
    }

    public record writeChatMessageResponse(long id, String authorName, String content) {

    }

    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<writeChatMessageResponse> writeMessage(@RequestBody writeChatMessageRequest req) {
        ChatMessage message = new ChatMessage(req.authorName, req.content);

        chatMessages.add(message);

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

    // ✅ "/messages" 경로로 GET 요청이 오면 이 메서드가 실행됨
    @GetMapping("/messages")
    @ResponseBody
    public RsData<messagesResponse> showMessages(messagesRequest req) {

        // 전체 채팅 메시지 리스트를 가져옴 (예: 메모리에 저장된 채팅 기록)
        List<ChatMessage> messages = chatMessages;

        // 요청값을 로그로 출력 (디버깅용)
        log.debug("req : {}", req);

        // 요청 파라미터에 fromId가 있을 경우 → 특정 메시지 이후의 메시지만 가져오기 위한 조건
        if (req.fromId != null) {

            // fromId와 같은 ID를 가진 메시지가 전체 리스트의 몇 번째 인덱스인지 찾음
            // fromId : 채팅방을 껏다 다시 들어갈대 어디부터 대화를 불러올것인지 정할때 사용한다.
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == req.fromId) // 메시지의 ID가 요청된 fromId와 같은지 확인
                    .findFirst()
                    .orElse(-1); // 못 찾으면 -1 반환

            // 해당 인덱스를 찾았을 경우 (즉, fromId에 해당하는 메시지가 존재할 경우)
            if (index != -1) {
                // 해당 인덱스 다음부터 끝까지의 메시지만 가져오도록 슬라이싱
                messages = messages.subList(index + 1, messages.size());
            }
        }

        // 성공 응답 반환
        return new RsData<>(
                "S-1", // 상태 코드
                "성공", // 메시지
                new messagesResponse(messages, messages.size()) // 응답 데이터: 메시지 리스트와 메시지 개수
        );
    }

}
