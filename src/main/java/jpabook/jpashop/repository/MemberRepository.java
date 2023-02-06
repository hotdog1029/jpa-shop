package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository // 컴포넌스 스캔으로 인해 자동으로 스프링 빈으로 등록
@RequiredArgsConstructor // final 이 붙거나 @nall이 붙은 필드의 생성자를 자동 생성해주는 롬북 어노테이션
public class MemberRepository {

//    @PersistenceContext // 해당 어노테이션으로 스피링이 엔티티 매니저를 주입해줌
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        Member result = em.find(Member.class, id);
        return result;
    }

    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }

    public List<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result;
    }


}
