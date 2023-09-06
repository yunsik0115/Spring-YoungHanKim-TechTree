package jpabasic.jpamain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("hello");
        // createEntityManagerFactor -> parsing xml "hello"


        EntityManager em = emf.createEntityManager();
        /* Be aware that Sharing Entity Manager is not permitted
        * multiple thread use might cause unexpected result */

        EntityTransaction tx = em.getTransaction();
        /* except simple data retrieving, all data modification
        * should be happened inside Transaction
        * RDB -> Data Transaction Should be pre-initiated */
        tx.begin();

        try{
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("memberA");
//            em.persist(member);

//            Member findMember = em.find(Member.class, 1L);
//
//            // em.remove(member); Delete
//            findMember.setName("memberAModified");
//            /* em.persist? -> Not Required
//            * To be Designed to use alike Java Collection Framework
//            * JPA Checks when transaction committed
//            * If there's a change or changes then it sends update query */
//
//
//            // JPQL - Query Which Not Targeting "Table" But "Object"
//            List<Member> allMembers = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member);

             


        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
            // em이 결국 내부적으로 DB 연결 물고 동작
            // 사용이 끝나면 꼭 닫아줘야 함.
        }

        emf.close();
    }
}
