package com.gklx.travel.controller;

import io.github.imfangs.dify.client.DifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @Autowired
    private DifyClient difyClient;


    @GetMapping
    public String index() {

        return "Hello World";
    }

}
