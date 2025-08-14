package org.example.workingmoney.service.auth;

import org.example.workingmoney.repository.user.UserRepository;
import org.example.workingmoney.service.auth.exception.DuplicatedEmailException;
import org.example.workingmoney.service.auth.exception.DuplicatedNicknameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Boolean checkEmailDuplicate(String email) {
        return !userRepository.existsByEmail(email);
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void join(String email, String nickname, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException();
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException();
        }

        String hashedPassword = bCryptPasswordEncoder.encode(password);
        userRepository.save(hashedPassword, nickname, email);
    }
}
