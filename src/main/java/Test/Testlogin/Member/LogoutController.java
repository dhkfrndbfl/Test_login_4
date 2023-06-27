package Test.Testlogin.Member;

import Test.Testlogin.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/out")
public class LogoutController {
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LogoutController(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token){
        //Authorization 헤더에서 JWT토큰 추출
        String jwtToken = token.substring(7);

        //JWT 토큰 검증
        if(JwtTokenProvider.validateToken(jwtToken)){
            return "로그아웃 성공";
        }
        else {
            return "로그아웃 실패";
        }
    }

}
