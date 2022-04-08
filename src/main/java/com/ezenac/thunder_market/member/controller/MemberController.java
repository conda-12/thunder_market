package com.ezenac.thunder_market.member.controller;

import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.member.domain.Token;
import com.ezenac.thunder_market.member.service.MemberService;
import com.ezenac.thunder_market.member.utils.GenerateRandomNumber;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final DefaultMessageService messageService;
    private final MemberService memberService;

    public MemberController(MemberService memberService, BCryptPasswordEncoder bCryptPasswordEncoder
            , @Value("${coolSMS.apiKey}") String apiKey, @Value("${coolSMS.apiSecretKey}") String apiSecretKey
            , @Value("${coolSMS.domain}") String domain) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain);
        this.memberService = memberService;
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.GET)
    public void registerGet() {
        logger.info("register http method - GET");
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public String registerPost(@Valid MemberDTO memberDTO, BindingResult bindingResult) throws Exception {
        logger.info("register http method - POST");

        if (bindingResult.hasErrors()) {
            return "member/auth/register";
        } else {
            memberService.signup(memberDTO);
            return "redirect:/member/auth/signin";
        }
    }

    @RequestMapping(value = "/auth/validation", method = RequestMethod.POST)
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

    @RequestMapping(value = "/auth/validation/{phoneNum}/{validationNum}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/auth/signin", method = RequestMethod.GET)
    public void signinGet() {

        logger.info("signinGet");
    }

    @RequestMapping(value = "/auth/validation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public int memberIdValidate(@PathVariable String id) throws Exception {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(id);

        return memberService.findMemberId(memberDTO);
    }

    @GetMapping(value = "/my-page")
    @ResponseBody
    public String getMyPage() {

        return "myPage";
    }
}