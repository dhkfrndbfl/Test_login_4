package Test.Testlogin.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class LoginSuccessDto {
    private String accessToken;
    private String memberId;
    private LocalDateTime memberCreateAt;
}
