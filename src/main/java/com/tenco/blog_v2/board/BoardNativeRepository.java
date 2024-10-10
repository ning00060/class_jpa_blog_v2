package com.tenco.blog_v2.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository // IoC
public class BoardNativeRepository {
    // DI 처리
    private final EntityManager em;

    @Transactional
    public void save(String title, String content) {
        Query query = em.createNativeQuery(
                "INSERT INTO board_tb(title, content, created_at) VALUES (?, ?, NOW())"
        );
        query.setParameter(1, title);
        query.setParameter(2, content);
        query.executeUpdate();
    }

    public Board findById(int id) {
        Query query = em.createNativeQuery("SELECT * FROM board_tb WHERE id = ?", Board.class);
        query.setParameter(1, id);
        return (Board) query.getSingleResult();
    }

    public List<Board> findAll() {
        TypedQuery<Board> query = em.createQuery("SELECT b FROM Board b ORDER BY b.id DESC", Board.class);
        return query.getResultList();
    }

    @Transactional
    public void updateById(int id, String title, String content) {
        Query query = em.createNativeQuery(
                "UPDATE board_tb SET title = ?, content = ? WHERE id = ?"
        );
        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, id);
        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("DELETE FROM board_tb WHERE id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }
}