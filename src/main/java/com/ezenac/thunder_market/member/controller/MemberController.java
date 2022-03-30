package com.ezenac.thunder_market.member.controller;

import com.ezenac.thunder_market.goods.domain.Goods;
import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.GET)
    public void registerGet(Member member, Model model) {
        logger.info("registerGet");
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public String registerPost(@Valid Member member, BindingResult bindingResult, Model model) throws Exception {
        logger.info(member.toString());

        if (bindingResult.hasErrors()) {
            return "member/auth/register";
        } else {
            service.signup(member);
            model.addAttribute("result", "success");

            return "/member/auth/success";
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

    @RequestMapping(value = "/myInfo/myGoodsList")
    public String myBoardList(Model model, HttpSession session) throws Exception {

        Member member = (Member) session.getAttribute("member");

        List<Goods> memberBoardList = service.getMemberGoodsList(member);

        model.addAttribute("memberBoardList", memberBoardList);

        return "/member/myInfo/myBoardList";
    }

    @RequestMapping(value = "/auth/memberIdValidation", method = RequestMethod.GET)
    @ResponseBody
    public int memberIdValidate(@RequestParam("memberId") String memberId) throws Exception {

        Member member = new Member();
        member.setMemberId(memberId);

        return service.findMemberId(member);
    }
}