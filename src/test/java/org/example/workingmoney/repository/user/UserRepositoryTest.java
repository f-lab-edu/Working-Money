package org.example.workingmoney.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void user_저장시_id_자동_생성_테스트() {

        // given
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");

        // when
        userRepository.save(userEntity);

        // then
        assertThat(userEntity.getId()).isNotNull();
        UserEntity foundUserEntity = userRepository.findById(userEntity.getId()).get();
        assertEquals(userEntity.getId(), foundUserEntity.getId());
    }

    @Test
    void user_저장_테스트() {

        // given
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");

        // when
        userRepository.save(userEntity);

        // then
        UserEntity findUserEntity = userRepository.findById(userEntity.getId()).get();
        assertThat(findUserEntity.getId()).isEqualTo(userEntity.getId());
        assertThat(findUserEntity.getPasswordHash()).isEqualTo(userEntity.getPasswordHash());
        assertThat(findUserEntity.getNickname()).isEqualTo(userEntity.getNickname());
        assertThat(findUserEntity.getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void id_기반_user_찾기_테스트() {

        // given
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");
        userRepository.save(userEntity);

        // when
        UserEntity findUserEntity = userRepository.findById(userEntity.getId()).get();

        // then
        assertThat(findUserEntity).isEqualTo(userEntity);
    }

    @Test
    void email_기반_user_찾기_테스트() {

        // given
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity("test", "tester", email);
        userRepository.save(userEntity);

        // when
        UserEntity findUserEntity = userRepository.findByEmail(email).get();

        // then
        assertThat(findUserEntity).isEqualTo(userEntity);
    }

    @Test
    void nickname_기반_user_찾기_테스트() {

        // given
        String nickname = "tester";
        UserEntity userEntity = new UserEntity("test", nickname, "test@example.com");
        userRepository.save(userEntity);

        // when
        UserEntity findUserEntity = userRepository.findByNickname(nickname).get();

        // then
        assertThat(findUserEntity).isEqualTo(userEntity);
    }

    @Test
    void email_존재여부_테스트() {
        // given
        String email = "exist@example.com";
        UserEntity userEntity = new UserEntity("test", "tester", email);
        userRepository.save(userEntity);

        // when & then
        assertTrue(userRepository.existsByEmail(email));
        assertFalse(userRepository.existsByEmail("not-exist@example.com"));
    }

    @Test
    void nickname_존재여부_테스트() {
        // given
        String nickname = "tester-exists";
        UserEntity userEntity = new UserEntity("test", nickname, "tester-exists@example.com");
        userRepository.save(userEntity);

        // when & then
        assertTrue(userRepository.existsByNickname(nickname));
        assertFalse(userRepository.existsByNickname("tester-not-exists"));
    }

    @Test
    void user_nickname_업데이트_테스트() {

        // given
        String newNickname = "tester2";
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");
        userRepository.save(userEntity);

        // when
        userRepository.updateNickname(userEntity.getId(), newNickname);

        // then
        UserEntity foundUser = userRepository.findById(userEntity.getId()).get();
        assertThat(foundUser.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void user_저장시_createdAt_updatedAt_일치_테스트() {

        // given
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");

        // when
        userRepository.save(userEntity);

        // then
        UserEntity findUserEntity = userRepository.findById(userEntity.getId()).get();
        assertThat(findUserEntity.getUpdatedAt()).isEqualTo(findUserEntity.getCreatedAt());
    }

    @Test
    void user_업데이트시_updatedAt_업데이트_테스트() {

        // given
        String newNickname = "tester2";
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");
        userRepository.save(userEntity);
        LocalDateTime beforeUpdatedAt = userRepository.findById(userEntity.getId()).get().getUpdatedAt();

        // when
        userRepository.updateNickname(userEntity.getId(), newNickname);

        // then
        UserEntity foundUser = userRepository.findById(userEntity.getId()).get();

        assertThat(foundUser.getCreatedAt()).isNotEqualTo(foundUser.getUpdatedAt());
        assertThat(foundUser.getUpdatedAt()).isAfter(beforeUpdatedAt);
        assertThat(foundUser.getUpdatedAt()).isAfter(foundUser.getCreatedAt());
    }

    @Test
    void id값_기반_user_삭제_테스트() {

        // given
        UserEntity userEntity = new UserEntity("test", "tester", "test@example.com");
        userRepository.save(userEntity);

        // when
        userRepository.deleteById(userEntity.getId());

        // then
        assertTrue(userRepository.findById(userEntity.getId()).isEmpty());
    }
}
