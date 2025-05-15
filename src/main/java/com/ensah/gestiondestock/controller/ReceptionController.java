package com.ensah.gestiondestock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/receptions")
public class ReceptionController {

    @GetMapping
    public String listReceptions() {
        return "reception/list";
    }
}
