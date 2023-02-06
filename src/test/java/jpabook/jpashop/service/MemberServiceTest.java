package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class) // JUit을 실행할 때 스프링이랑 같이 할래
@SpringBootTest // 스프링부트를 띄운 상태로 테스트
@Transactional // 스프링의 트랜잭셔널은 기본적으로 커밋을 안하고 롤백을 함
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");
        Member member1 = new Member();
        member1.setName("lee");

        // when
        Long saveId = memberService.join(member);

        // then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); // 예외가 발생해야 한다.
        });
        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}