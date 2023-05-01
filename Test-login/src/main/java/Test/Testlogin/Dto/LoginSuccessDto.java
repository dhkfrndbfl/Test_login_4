package Test.Testlogin.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class LoginSuccessDto {
    private String accessToken;
    private String memberId;
}
