package Test.Testlogin.Form;                           //결과 값을 주기 위한 폼

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ResponseForm {
    private HttpStatus httpStatus;      //통신요청 상태 ok badrequest
    private Object result;              //결과값
    private String comment;             //코멘트 내가 무슨 경우에서 발생했는지 확인하기 위해서 넣음
    private String accessToken;         //토큰
}
