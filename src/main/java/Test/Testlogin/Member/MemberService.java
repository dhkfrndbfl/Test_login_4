package Test.Testlogin.Member;

import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Dto.MemberSignUpDto;
import Test.Testlogin.EmailValidation.EmailValidation;
import Test.Testlogin.Form.ResponseForm;
import Test.Testlogin.jwt.JwtTokenProvider;
import com.sun.crypto.provider.GCM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor            //final이 붙은 필드의 생성자를 자동으로 생성해주는 어노테이션
@Service                            //service를 지정해주는 어노테이션
@Transactional                      //데이터 추가, 갱신, 삭제 등으로 이루어진 작업을 처리하던 중 오류가 발생했을 때 모든 작업들을 원상태로 되돌릴 수 있다.
@Slf4j                              //로그 라이브러리를 사용하기 위해 사용
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     * @param memberSignUpDto
     * @return
     */
    public String signUp(MemberSignUpDto memberSignUpDto){
        //이메일 형식 검사
        if (EmailValidation.isValid(memberSignUpDto.getMemberId())) {
            //이메일 형식 일경우

            Optional<MemberEntity> member = memberRepository.findByMemberId(memberSignUpDto.getMemberId());
            //DB에 입력받은 이메일 정보가 존재하는지 확인

            if (member.isPresent()){    //일치하는 정보가 존재한다면 중복회원
                return "중복된 회원입니다.";
            }else {
                //일치하는 정보가 없다면 신규 회원 가입
                MemberEntity memberEntity = new MemberEntity();     //새로운 회원 객체 만듬
                memberEntity.signUpDtoEntity(memberSignUpDto);    //dto 객체를 entity 객체로 변환
                memberRepository.save(memberEntity);                //entity 정보를 DB에 저장한다.
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
        //ResponseForm을 통해서 Response에 대한 형식을 정형화 해서 진행
        ResponseForm responseForm = new ResponseForm();
        //이메일 형식 검사
        if (!EmailValidation.isValid(dto.getMemberId())){
            //올바른 형식이 아니라면
            responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
            responseForm.setResult("");
            responseForm.setComment("잘못된 요청입니다.");
        }else { //이메일 형식

            Optional<MemberEntity> member = memberRepository.findByMemberId(dto.getMemberId());
            // DB 조회 -> select * from member_manage where member_id = 'dto.getMemberId()';

            //회원 아이디 찾기
            if (member.isEmpty()){              //조회한 데이터가 비어있다면 -> 일치하는 회원 정보가 없다.
                responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseForm.setResult("");
                responseForm.setComment("일치하는 회원정보가 없습니다.");
            }else {
                //회원 정보가 존재 하는경우
                //요청이 들어온 비밀번호가 DB의 비밀번호와 일치하는지 확인해야함
                if (passwordCheck(dto.getMemberPwd(),member.get().memberPwd)) {
                    //비밀번호가 일치할경우
                    String token = jwtTokenProvider.createToken(member.get().memberPwd);       //토큰 생성    토큰을 사용해서 회원인증을 한다   토큰의 들어갈 사용자 정보는 미정이다.(데이터베이스에 생성되는 시리얼 넘버로 구상 중.)
                    member.get().setMemberToken(token);                                         //토큰저장     토큰을 DB에 저장해서 관리한다
                    responseForm.setHttpStatus(HttpStatus.OK);                                  //httpstatus를 ok로 설정하여 폼에 담는다
                    responseForm.setResult(member.get().getMemberName());                       //회원의 이름을 반환한다
                    responseForm.setComment("로그인을 환영합니다.");
                    responseForm.setAccessToken(token);                                         //회원의 인증을 담당할 토큰을 생성해서 응답한다. 프론트에 인증이 된 회원인지, 어떤 사용자인지 알려주기 위해
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
                    .memberToken(memberEntityValue.getMemberToken())
                    .build();

            log.info("member : {}", member);
            return member;
        }
        return null;
    }*/

    public boolean passwordCheck(String dtoPwd,String memberPwd) {              // //DB에 있는 패스워드랑 입력한 패스워드가 맞는지 확인
        return memberPwd.equals(dtoPwd);
    }

}
    


