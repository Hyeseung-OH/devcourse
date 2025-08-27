package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "안녕하세요 sbb에 오신 것을 환영합니다";
    }

    @GetMapping("/hello")
    public String hello() {
        return "안녕하세요 sbb/hello에 오신 것을 환영합니다";
    }
}
