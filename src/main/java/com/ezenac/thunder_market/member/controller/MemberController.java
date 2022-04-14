package com.ezenac.thunder_market.member.controller;

import com.ezenac.thunder_market.member.entity.MemberDTO;
import com.ezenac.thunder_market.member.entity.Token;
import com.ezenac.thunder_market.member.service.MemberService;
import com.ezenac.thunder_market.member.utils.GenerateRandomNumber;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final DefaultMessageService messageService;
    private final MemberService memberService;

    public MemberController(MemberService memberService, BCryptPasswordEncoder bCryptPasswordEncoder
            , @Value("${coolSMS.apiKey}") String apiKey, @Value("${coolSMS.apiSecretKey}") String apiSecretKey
            , @Value("${coolSMS.domain}") String domain) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain);
        this.memberService = memberService;
    }
    // 회원가입 GET
    @GetMapping(value = "/auth/register")
    public void registerGet() {
        log.info("register http method - GET");
    }
    // 회원가입 POST
    @PostMapping(value = "/auth/register")
    public String registerPost(@Valid MemberDTO memberDTO, BindingResult bindingResult) throws Exception {
        log.info("register http method - POST");

        if (bindingResult.hasErrors()) {
            return "member/auth/register";
        } else {
            memberService.signup(memberDTO);
            return "redirect:/member/auth/signin";
        }
    }
    // 전화번호로 인증번호 보내기
    @PostMapping(value = "/auth/validation")
    @ResponseBody
    public String setValidationToken(@RequestBody Map<String, Object> param) throws Exception {
        GenerateRandomNumber ranNumGenerator = new GenerateRandomNumber();

        String phoneNumber = (String) param.get("phoneNum");

        // 랜덤 번호 생성
        String ranNum = ranNumGenerator.executeGeneration();
        // 메세지 생성
        Message message = new Message();
        message.setFrom("01027294072");
        message.setTo(phoneNumber);
        message.setText("천둥마켓 본인확인 인증번호(" + ranNum + ")입력시 정상 처리 됩니다.");
        // 메세지 발송
        messageService.sendOne(new SingleMessageSendingRequest(message));
        // 토큰생성
        Token token = new Token(phoneNumber, ranNum);
        memberService.saveToken(token);

        return phoneNumber;
    }
    // 전화번호 인증하기
    @GetMapping(value = "/auth/validation/{phoneNum}/{validationNum}")
    @ResponseBody
    public int validateTokenNum(@PathVariable String phoneNum, @PathVariable String validationNum) throws Exception {

        if (phoneNum == null) {
            return 0;
        } else if (validationNum == null) {
            return 0;
        } else {
            return memberService.validateToken(phoneNum, validationNum);
        }
    }
    // 로그인
    @GetMapping(value = "/auth/signin")
    public void signinGet() {

        log.info("signinGet");
    }
    // 아이디 중복 검증
    @GetMapping(value = "/auth/validation/{id}")
    @ResponseBody
    public int memberIdValidate(@PathVariable String id) throws Exception {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(id);

        return memberService.findMemberId(memberDTO);
    }
    // 마이 페이지
    @GetMapping(value = "/my-page")
    @ResponseBody
    public String getMyPage() {

        return "myPage";
    }
}