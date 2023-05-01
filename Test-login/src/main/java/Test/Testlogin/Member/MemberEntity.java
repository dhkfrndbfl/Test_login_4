package Test.Testlogin.Member;

import Test.Testlogin.Dto.MemberSignUpDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "member_mange")
public class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_sn")
    Long memberSn;

    @Column(name = "member_id")
    String memberId;

    @Column(name = "member_pwd")
    String memberPwd;

    @Column(name = "member_name")
    String memberName;

    @Column(name = "member_age")
    int memberAge;

    @Column(name = "member_sex")
    String memberSex;

    @Column(name = "token")
    String memberToken;

    public void signUpDtoEntity(MemberSignUpDto dto){
        this.setMemberId(dto.getMemberId());
        this.setMemberPwd(dto.getMemberPwd());
        this.setMemberName(dto.getMemberName());
        this.setMemberAge(dto.getMemberAge());
        this.setMemberSex(dto.getMemberSex());
    }



    /*@Override
    public String getPassword() { return this.memberPwd;}

    @Override
    public String getUsername(){ return this.memberName;}
*/
}
