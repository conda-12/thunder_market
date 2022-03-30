package com.ezenac.thunder_market.goods.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @GetMapping("/{gno}")
    public String goods(@PathVariable("gno") Long gno){

        return "/goods/goods";
    }
}
