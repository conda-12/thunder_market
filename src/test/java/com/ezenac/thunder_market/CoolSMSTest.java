package com.ezenac.thunder_market;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.Test;

public class CoolSMSTest {

    private final DefaultMessageService messageService;

    public CoolSMSTest() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSPQQN9QFPX4OSZ", "SNIORBCZZQHHUJGKO5BJ7PWVP6STKA3J", "https://api.coolsms.co.kr");
    }

    @Test
    public void sendSms() {
        Message message = new Message();
        message.setFrom("01027294072");
        message.setTo("0312434072");
        message.setText("천둥마켓 테스트 중입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

    }
}
