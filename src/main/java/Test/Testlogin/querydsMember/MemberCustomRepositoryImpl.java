package Test.Testlogin.querydsMember;

import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Member.MemberEntity;
import Test.Testlogin.Member.QMemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private JPAQueryFactory jpaQueryFactory;
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<MemberEntity> findLoginMember(MemberLoginDto memberLoginDto) {
        QMemberEntity memberEntity = QMemberEntity.memberEntity;
        MemberEntity result = jpaQueryFactory.selectFrom(memberEntity)
                .where(memberEntity.memberId.eq(memberLoginDto.getMemberId()), memberEntity.memberPwd.eq(passwordEncoder.encode(memberLoginDto.getMemberPwd())))
                .fetchOne();
        return Optional.empty();
    }
}
