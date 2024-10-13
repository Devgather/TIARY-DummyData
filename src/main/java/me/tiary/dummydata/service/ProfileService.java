package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final TransactionTemplate transactionTemplate;

    private final ProfileRepository profileRepository;

    public long findProfileMinimumId() {
        final Optional<Profile> firstProfile = profileRepository.findFirstByOrderByIdAsc();

        if (firstProfile.isPresent()) {
            return firstProfile.get().getId();
        }

        return -1L;
    }

    public Optional<Profile> findById(final long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> findWithOAuthById(final long id) {
        return profileRepository.findLeftJoinFetchOAuthById(id);
    }

    public void insertProfiles(final List<Profile> profiles) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                profileRepository.saveAllAndFlush(profiles);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}