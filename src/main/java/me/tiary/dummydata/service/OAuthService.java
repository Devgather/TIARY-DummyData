package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.OAuth;
import me.tiary.dummydata.repository.OAuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final TransactionTemplate transactionTemplate;

    private final OAuthRepository oAuthRepository;

    public void insertOAuths(final List<OAuth> oAuths) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                oAuthRepository.saveAllAndFlush(oAuths);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}