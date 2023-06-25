package Test.Testlogin.Form;                           //결과 값을 주기 위한 폼

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ResponseForm {
    private HttpStatus httpStatus;      //통신요청 상태 ok || badrequest -> 너무 상세하게 실패의 원인을 제공하는것 또한 서버의 보안적 취약점이라서 성공했을때만 OK를 보낸다.
    private Object result;              //결과값
    private String comment;             //코멘트 내가 무슨 경우에서 발생했는지 확인하기 위해서 넣음
    private String accessToken;         //토큰


}
