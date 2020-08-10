package ksbysample.webapp.lending.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Spring Boot 2.3 から追加された Graceful shutdown の機能をテストするための Controller
 */
@Controller
public class GracefulShutdownTestController {

    /**
     * アクセス後 5分 sleep してからレスポンスを返す
     *
     * @return "OK"の文字列
     * @throws InterruptedException
     */
    @GetMapping("/gracefulShutdownTest")
    @ResponseBody
    public String index() throws InterruptedException {
        Thread.sleep(60_000 * 5);
        return "OK";
    }

}
