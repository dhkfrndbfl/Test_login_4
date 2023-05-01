package Test.Testlogin.Member;

import Test.Testlogin.EmailValidation.EmailValidation;
import Test.Testlogin.Form.ResponseForm;
import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Dto.MemberSignUpDto;
import Test.Testlogin.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     *
     * @param memberSignUpDto
     * @return
     */
    public String signUp(MemberSignUpDto memberSignUpDto){

        if (EmailValidation.isValid(memberSignUpDto.getMemberId())) {   //이메일 형식 검사
            Optional<MemberEntity> member = memberRepository.findByMemberId(memberSignUpDto.getMemberId());
            if (member.isPresent()){    //일치하는 정보가 존재하는지 확인
                return "중복된 회원입니다.";
            }else {
                log.info(memberSignUpDto.getMemberPwd());   //로그
                //String encodePwd = passwordEncoder.encode(memberSignUpDto.getMemberPwd());  //비밀번호 암호화
                //memberSignUpDto.setMemberPwd(encodePwd);       //암호화한 비밀번호 주입
                MemberEntity memberEntity = new MemberEntity();     //회원 객체 만듬
                memberEntity.signUpDtoEntity(memberSignUpDto);    //dto 객체를 entity객체로 변환
                memberRepository.save(memberEntity);                //entity 저장
                return "회원 가입 성공";
            }
        } else {
            return "이메일 형식의 아이디를 입력해 주세요";
        }
    }
    /**
     * 로그인
     * @param dto
     * @return
     */
    public ResponseForm login(MemberLoginDto dto){
        ResponseForm responseForm = new ResponseForm();
        //이메일 형식 검사
        if (!EmailValidation.isValid(dto.getMemberId())){
            responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
            responseForm.setResult("");
            responseForm.setComment("잘못된 요청입니다.");
        }else { //이메일 형식

            Optional<MemberEntity> member = memberRepository.findByMemberId(dto.getMemberId()); // DB 조회 select * from member_manage where member_id = 'dto.getMemberId()';

            //회원 아이디 찾기
            if (member.isEmpty()){
                responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseForm.setResult("");
                responseForm.setComment("일치하는 회원정보가 없습니다.");
            }else {
                //회원 정보가 일치하는 정보 찾기 - queryDsl
                // -> 전체 로직을 생각해서 추후 성능개선 필요
                // -> 불필요한 쿼리 삭제
//                Optional<MemberEntity> member = memberCustomRepository.findLoginMember(dto);
                if (passwordCheck(dto.getMemberPwd(),member.get().memberPwd)) {
                    responseForm.setHttpStatus(HttpStatus.OK);
                    responseForm.setResult(member.get());
                    responseForm.setComment("로그인을 환영합니다.");
                    responseForm.setAccessToken(jwtTokenProvider.createToken(member.get().memberId));
                } else {
                    responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseForm.setResult("");
                    responseForm.setComment("비밀번호가 일치하지 않습니다.");
                }
            }
        }
        return responseForm;
    }

/*    @Override       //security 때문
    public MemberEntity loadUserByUsername(String username){
        //MemberEntity 정보 조회
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberId(username);


        if(memberEntity.isPresent()) {      //해당하는 값이 있다면
            MemberEntity memberEntityValue = memberEntity.get();

            MemberEntity member = MemberEntity.builder()
                    .memberSn(memberEntityValue.getMemberSn())
                    .memberId(memberEntityValue.getMemberId())
                    .memberPwd(memberEntityValue.getMemberPwd())
                    .memberName(memberEntityValue.getMemberName())
                    .memberAge(memberEntityValue.getMemberAge())
                    .memberAge(memberEntityValue.getMemberAge())
                    .memberToken(memberEntityValue.getMemberToken())
                    .build();

            log.info("member : {}", member);
            return member;
        }
        return null;
    }*/

    public boolean passwordCheck(String dtoPwd,String memberPwd) {
        return memberPwd.equals(dtoPwd);
    }

}
