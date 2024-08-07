package hello.thymeleaf.basic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * • 간단한 표현:
 * ◦ 변수 표현식: ${...}
 * ◦ 선택 변수 표현식: *{...}
 * ◦ 메시지 표현식: #{...}
 * ◦ 링크 URL 표현식: @{...}
 * ◦ 조각 표현식: ~{...}
 * • 리터럴
 * ◦ 텍스트: 'one text', 'Another one!',…
 * ◦ 숫자: 0, 34, 3.0, 12.3,…
 * ◦ 불린: true, false
 * ◦ 널: null
 * ◦ 리터럴 토큰: one, sometext, main,…
 * • 문자 연산:
 * ◦ 문자 합치기: +
 * ◦ 리터럴 대체: |The name is ${name}|
 * • 산술 연산:
 * ◦ Binary operators: +, -, *, /, %
 * ◦ Minus sign (unary operator): -
 * • 불린 연산:
 * ◦ Binary operators: and, or
 * ◦ Boolean negation (unary operator): !, not
 * • 비교와 동등:
 * ◦ 비교: >, <, >=, <= (gt, lt, ge, le)
 * ◦ 동등 연산: ==, != (eq, ne)
 * • 조건 연산:
 * ◦ If-then: (if) ? (then)
 * ◦ If-then-else: (if) ? (then) : (else)
 * ◦ Default: (value) ?: (defaultvalue)
 * • 특별한 토큰:
 * ◦ No-Operation:
 */

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "<b>Hello Spring<b>");

        return "basic/text-basic";
    }

    @GetMapping("/text-unescaped")
    public String textUnescaped(Model model) {
        model.addAttribute("data", "<b>Hello Spring<b>");
        return "basic/text-unescaped";
    }

    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 10);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    @Data
    static class User {
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

    @GetMapping("/basic-objects")
    public String basicObjects(Model model, HttpServletRequest request,
                               HttpServletResponse response,
                               HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());

        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello" + data;
        }
    }
}
