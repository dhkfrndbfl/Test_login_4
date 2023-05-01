package Test.Testlogin.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberLoginDto {
    private String memberId;
    private String memberPwd;
}
