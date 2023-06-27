package Test.Testlogin.querydsMember;

import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Member.MemberEntity;

import java.util.Optional;

public interface MemberCustomRepository {
    Optional<MemberEntity> findLoginMember(MemberLoginDto memberLoginDto);
}
