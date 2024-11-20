package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.OAuth;
import me.tiary.dummydata.repository.OAuthRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthAccessor {
    private final OAuthRepository oAuthRepository;

    public void insertOAuths(final List<OAuth> oAuths) {
        oAuthRepository.saveAll(oAuths);
    }
}