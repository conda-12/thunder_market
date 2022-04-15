package com.ezenac.thunder_market.controller;

import com.ezenac.thunder_market.dto.BigGroupDTO;
import com.ezenac.thunder_market.dto.SmallGroupDTO;
import com.ezenac.thunder_market.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/bg/all")
    public ResponseEntity<List<BigGroupDTO>> getBGList() {

        return new ResponseEntity<>(groupService.getBGList(), HttpStatus.OK);
    }

    @GetMapping("/sg/{bgNum}")
    public ResponseEntity<List<SmallGroupDTO>> getSGList(@PathVariable String bgNum) {
        log.info(bgNum);
        return new ResponseEntity<>(groupService.getSGList(bgNum),HttpStatus.OK);
    }
}
