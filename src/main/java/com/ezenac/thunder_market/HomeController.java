package com.ezenac.thunder_market;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {

        return "index";
    }
    @GetMapping(value = "/manager/{number}")
    @ResponseBody
    public String manager(@PathVariable int number) {

        return "manager" + number;
    }

    @GetMapping(value = "/admin/{number}")
    @ResponseBody
    public String admin(@PathVariable int number) {

        return "admin" + number;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/message")
    public String messagePage(){
        return "message";
    }



}