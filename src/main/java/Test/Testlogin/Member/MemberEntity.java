package Test.Testlogin.Member;

import Test.Testlogin.Dto.MemberSignUpDto;
import Test.Testlogin.Member.Role.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "member_mange")
public class MemberEntity implements UserDetails {
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

   /* @Column(name = "member_sex")
    String memberSex;*/             //성별은 잠시 보류

    @Column(name = "role")
    String memberRole;

    @Column(name = "create_at")
    LocalDateTime memberCreateAt;

    @Column(name = "token")
    String memberToken;

    public void signUpDtoEntity(MemberSignUpDto dto){
        this.setMemberId(dto.getMemberId());
        this.setMemberPwd(dto.getMemberPwd());
        this.setMemberName(dto.getMemberName());
        this.setMemberAge(dto.getMemberAge());
        this.setMemberRole(Role.User.getValue());
        this.setMemberCreateAt(LocalDateTime.now());
        //this.setMemberSex(dto.getMemberSex());            성별은 잠시 보류
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : memberRole.split(";")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

   @Override
    public String getPassword() { return this.memberPwd;}

    @Override
    public String getUsername(){ return this.memberName;}

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
