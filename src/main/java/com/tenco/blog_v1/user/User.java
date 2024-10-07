package com.tenco.blog_v1.user;

import com.tenco.blog_v1.board.Board;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)// 유니크 제약 조건 설정
    private String username;
    private String password;
    private String email;

    @CreationTimestamp // 엔티티 생성시 자동으로 현재시간입력
    private Timestamp createdAt;

    // 단방향, 양방향 매핑(mappedBy)
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //  지연로딩설정(boards를 부를때)
//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // 즉시 로딩
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards;


    @Builder
    public User(Integer id, String username, String password, String email, Timestamp createdAt, List<Board> boards){
        this.id= id;
        this.username=username;
        this.password=password;
        this.createdAt=createdAt;
        this.boards=boards;
    }



}
