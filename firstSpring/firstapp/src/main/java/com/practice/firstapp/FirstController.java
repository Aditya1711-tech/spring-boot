package com.practice.firstapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {

  @RequestMapping("/test")
  @ResponseBody
  public String first() {
    return "My name is Aditya";
  }
}
