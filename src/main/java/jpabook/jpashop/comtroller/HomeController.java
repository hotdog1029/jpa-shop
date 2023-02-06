package jpabook.jpashop.comtroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // 롬복에서는 이렇게 가장 편하게 로그를 남길 수 있다.
public class HomeController {

    @RequestMapping("/")
    public String home() {
        log.info("home controller"); // 로그 찍기
        return "home";
    }
}
