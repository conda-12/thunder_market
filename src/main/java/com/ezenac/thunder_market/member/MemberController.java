package com.ezenac.thunder_market.member;

import com.ezenac.thunder_market.member.dto.MemberDTO;
import com.ezenac.thunder_market.member.dto.MyProductDTO;
import com.ezenac.thunder_market.member.entity.Token;
import com.ezenac.thunder_market.member.service.MemberService;
import com.ezenac.thunder_market.product.entity.ProductState;
import com.ezenac.thunder_market.product.service.ProductService;
import com.ezenac.thunder_market.utils.GenerateRandomNumber;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final DefaultMessageService messageService;
    private final MemberService memberService;
    private final ProductService productService;
    public MemberController(MemberService memberService
            , @Value("${coolSMS.apiKey}") String apiKey, @Value("${coolSMS.apiSecretKey}") String apiSecretKey
            , @Value("${coolSMS.domain}") String domain, ProductService productService) {
        this.productService = productService;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain);
        this.memberService = memberService;
    }

    // ???????????? GET
    @GetMapping(value = "/auth/register")
    public void registerGet() {
        log.info("register http method - GET");
    }

    // ???????????? POST
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

    // ??????????????? ???????????? ?????????
    @PostMapping(value = "/auth/validation")
    @ResponseBody
    public String setValidationToken(@RequestBody Map<String, Object> param) throws Exception {
        GenerateRandomNumber ranNumGenerator = new GenerateRandomNumber();

        String phoneNumber = (String) param.get("phoneNum");

        // ?????? ?????? ??????
        String ranNum = ranNumGenerator.executeGeneration();
        // ????????? ??????
        Message message = new Message();
        message.setFrom("01027294072");
        message.setTo(phoneNumber);
        message.setText("???????????? ???????????? ????????????(" + ranNum + ")????????? ?????? ?????? ?????????.");
        // ????????? ??????
        messageService.sendOne(new SingleMessageSendingRequest(message));
        // ????????????
        Token token = new Token(phoneNumber, ranNum);
        memberService.saveToken(token);

        return phoneNumber;
    }

    // ???????????? ????????????
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

    // ?????????
    @GetMapping(value = "/auth/signin")
    public void signinGet(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");

        request.getSession().setAttribute("prevPage", referrer);

        log.info("signinGet");
    }

    // ????????? ?????? ??????
    @GetMapping(value = "/auth/validation/{memberId}")
    @ResponseBody
    public int memberIdValidate(@PathVariable String memberId) throws Exception {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberId);

        return memberService.findMemberId(memberDTO);
    }

    @GetMapping("/products")
    public String getMyProductList(Model model, Pageable pageable) throws Exception {

        Authentication member = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> paging = memberService.getMyProductList(member.getName(), pageable);

        List<MyProductDTO> products = (List<MyProductDTO>) paging.get("products");
        int totalPages = (int) paging.get("totalPages");

        model.addAttribute("products", products);
        model.addAttribute("pageSize", totalPages);

        return "/member/my-page/products";
    }

    @ResponseBody
    @PostMapping("/products/")
    public ResponseEntity<Long> changeProductState(@RequestBody Map<String, String> changes) throws Exception {

        Authentication member = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority i : member.getAuthorities()) {
            if (i.toString().equals("ROLE_ANONYMOUS")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        String state = changes.get("state");
        Long productId = Long.valueOf(changes.get("productID"));

        System.out.println("?????? ===> " + state);
        System.out.println("?????? ????????? ===> " + productId);

        switch (state) {
            case "?????? ???":
                if (memberService.changeMyProductState(ProductState.SELLING, productId, member.getName())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                break;
            case "?????? ???":
                if (memberService.changeMyProductState(ProductState.RESERVED, productId, member.getName())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                break;
            case "????????????":
                if (memberService.changeMyProductState(ProductState.SOLD_OUT, productId, member.getName())) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                break;
            case "??????":
                if (Objects.equals(productService.read(productId).getMemberId(), member.getName())) {
                    productService.remove(productId);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(productId, HttpStatus.OK);

    }

    // ?????? ?????????
    @GetMapping(value = "/my-page")
    @ResponseBody
    public String getMyPage() {

        return "myPage";
    }
}