package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.OAuth;
import me.tiary.dummydata.repository.OAuthRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthRepository oAuthRepository;

    public void insertOAuths(final List<OAuth> oAuths) {
        oAuthRepository.saveAll(oAuths);
    }
}