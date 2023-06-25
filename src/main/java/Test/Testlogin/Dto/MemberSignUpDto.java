package Test.Testlogin.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

public class MemberSignUpDto {
    public String memberId;
    public String memberPwd;
    public String memberName;
    public int memberAge;
    //public String memberSex;    성별은 잠시 보류
}
