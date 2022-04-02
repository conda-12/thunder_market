package com.ezenac.thunder_market.member.controller;

import com.ezenac.thunder_market.member.domain.Token;
import com.ezenac.thunder_market.member.utils.GenerateRandomNumber;
import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.member.service.MemberService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
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
    private final MemberService service;

    public MemberController(MemberService service) {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSPQQN9QFPX4OSZ", "SNIORBCZZQHHUJGKO5BJ7PWVP6STKA3J", "https://api.coolsms.co.kr");;
        this.service = service;
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.GET)
    public void registerGet(Member member, Model model) {
        logger.info("registerGet");
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public String registerPost(@Valid Member member, BindingResult bindingResult) throws Exception {
        logger.info(member.toString());

        if (bindingResult.hasErrors()) {
            return "member/auth/register";
        } else {
            service.signup(member);
            return "redirect:/member/auth/signin";
        }
    }

    @RequestMapping(value = "/auth/validation", method = RequestMethod.POST)
    @ResponseBody
    public String setValidationToken(@RequestBody Map<String, Object> phoneNum) throws Exception {
        GenerateRandomNumber ranNumGenerator = new GenerateRandomNumber();

        String phoneNumber = (String) phoneNum.get("phoneNum");

        System.out.println("post 값= "+ phoneNumber);

        String ranNum = ranNumGenerator.excuteGeneration();
        Message message = new Message();
        message.setFrom("01027294072");
        message.setTo(phoneNumber);
        message.setText("천둥마켓 본인확인 인증번호(" + ranNum + ")입력시 정상 처리 됩니다.");

        messageService.sendOne(new SingleMessageSendingRequest(message));

        Token token = new Token(phoneNumber, ranNum);
        service.saveToken(token);

        return phoneNumber;
    }

    @RequestMapping(value = "/auth/validation", method = RequestMethod.GET)
    @ResponseBody
    public int validateTokenNum(@RequestParam Map<String, Object> param) throws Exception {

        String phoneNum = (String) param.get("phoneNum");
        String validationNum = (String) param.get("validationNum");

        if (phoneNum == null) {
            return 0;
        } else if (validationNum == null) {
            return 0;
        } else {
            int num = service.validateToken(phoneNum, validationNum);
            System.out.println("토큰검색 결과=" + num);
            return num;
        }
    }

    @RequestMapping(value = "/auth/signin", method = RequestMethod.GET)
    public void signinGet() {

        logger.info("signinGet");
    }


    @RequestMapping(value = "/auth/signin", method = RequestMethod.POST)
    public String signinPost(Member member, HttpSession session) throws Exception {

        Member result = service.signin(member, session);

        if (result == null) {
            return "member/auth/signin";
        } else {
            result.setPassword(null);
            System.out.println(result);
            session.setAttribute("member", result);
            return "redirect:/board/boardlist";
        }
    }

    @RequestMapping(value = "/auth/signout")
    public String signout(HttpSession session) throws Exception {

        service.signout(session);

        return "redirect:/board/boardlist";
    }

    @RequestMapping(value = "/auth/member-id-validation", method = RequestMethod.GET)
    @ResponseBody
    public int memberIdValidate(@RequestParam Map<String, Object> param) throws Exception {

        Member member = new Member();
        member.setMemberId((String) param.get("memberId"));

        return service.findMemberId(member);
    }
}