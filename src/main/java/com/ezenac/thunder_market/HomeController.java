package com.ezenac.thunder_market;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {

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

}