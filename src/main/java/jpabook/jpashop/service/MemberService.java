package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기에는 성능 최적화를 위해서 true로
@RequiredArgsConstructor // final이 있는 필드만 생성자를 만들어줌
public class MemberService {

    private final MemberRepository memberRepository; // final를 넣는 것을 추천함

//    @Autowired // 스프링이 스프링빈에 등록되어 있는 memberRepository를 주입해줌, 주로 생성자 인젝션을 사용함
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional // jpa에서의 모든 데이터 변경이나 로직은 트랜잭션 안에서 실행되어야 한다, 쓰기에는 readOnly = true를 하면 안됨
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (findMembers.size() > 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
