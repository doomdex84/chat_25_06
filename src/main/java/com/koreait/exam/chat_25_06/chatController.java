package com.koreait.exam.chat_25_06;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class chatController
{
    @PostMapping("/writeMessage")
    @ResponseBody
    public String writeMessage(@RequestParam("message")String message){
        return "메시지 작성됨";
    }
}
