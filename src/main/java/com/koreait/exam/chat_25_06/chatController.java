package com.koreait.exam.chat_25_06;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class chatController {

    ChatMessage message = new ChatMessage("홍길동", "안녕");

    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData writeMessage() {
        return new RsData<>("S-1", "메세지가 작성됨", message);
    }
}
