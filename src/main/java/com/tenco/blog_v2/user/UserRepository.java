package com.tenco.blog_v2.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository // IoC
public class UserRepository {

    private final EntityManager em;

    /**
     * 사용자 저장 메서드 (JPA API 사용)
     * @param user
     * @return 저장된 사용자 엔티티
     */
    @Transactional
    public User save(User user) {
        // JPQL 은 INSERT 구문을 직접 지원하지 않습니다.
        em.persist(user); // 영속화
        return  user;
    }




    /**
     * 사용자 이름과 비밀번호로 사용자 조회
     * @param username
     * @param password
     * @return 조회된 User 엔티티, null
     */
    public User findByUsernameAndPassword(String username, String password) {
        TypedQuery<User> jpql =
                em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        jpql.setParameter("username", username);
        jpql.setParameter("password", password);
        return jpql.getSingleResult();
    }


    // 사용자 정보 수 정 기능 만들기
    @Transactional
    public User updateByIdJpql(int id, String password, String email) {
        // 1.
        Query jpql1 = em.createQuery("UPDATE User u SET u.password= :password, u.email= :email WHERE u.id = :id");
        jpql1.setParameter("id", id);
        jpql1.setParameter("password", password);
        jpql1.setParameter("email", email);
        jpql1.executeUpdate();

        // 2.
        Query jpql2 = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        jpql2.setParameter("id", id);

        return (User) jpql2.getSingleResult();
    }
    @Transactional // -- 더티 체킹 사용
    public User updateById(int id, String password, String email){
        User userEntity=findById(id);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        return  userEntity;
    }

    public User findById(int id){
        return em.find(User.class,id);
    }
}
