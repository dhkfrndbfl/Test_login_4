package Test.Testlogin.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LogoutController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/logout")
    public String logout( HttpServletRequest request){
       HttpSession session = request.getSession(false);
       if( session != null){
           session.invalidate();
       }

       String sessionID = request.getRequestedSessionId();
       jdbcTemplate.update("DELETE FROM custom_session_table WHERE SESSION_ID = ?", sessionID);
       return "/login";
    }
}
