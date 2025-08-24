package org.example.workingmoney.repository.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {

    default void create(String passwordHash, String nickname, String email) {
        insert(new UserEntity(passwordHash, nickname, email));
    }

    /**
     * 저장 시 save 사용 권장
     */
    void insert(UserEntity userEntity);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    void lockByEmail(String email);

    void lockByNickname(String nickname);

    void updateNickname(Long id, String nickname);

    void deleteById(Long id);
}
