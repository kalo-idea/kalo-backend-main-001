package kalo.main.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @GetMapping("/notice")
    public void notice(Message message) throws Exception {
        System.out.println("notice : " + message);
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
    }


    @MessageMapping("/hello")
    public void message(Message message) {
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
    }
}
