package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public long findProfileMinimumId() {
        final Optional<Profile> firstProfile = profileRepository.findFirstByOrderByIdAsc();

        if (firstProfile.isPresent()) {
            return firstProfile.get().getId();
        }

        return -1L;
    }

    public Range findProfileIdRange() {
        final Optional<Profile> firstProfile = profileRepository.findFirstByOrderByIdAsc();
        final Optional<Profile> lastProfile = profileRepository.findFirstByOrderByIdDesc();

        if (firstProfile.isPresent() && lastProfile.isPresent()) {
            return new Range(firstProfile.get().getId(), lastProfile.get().getId());
        }

        return new Range(-1L, -1L);
    }

    public Optional<Profile> findById(final long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> findWithOAuthById(final long id) {
        return profileRepository.findLeftJoinFetchOAuthById(id);
    }

    public Optional<Profile> findWithTilById(final long id) {
        return profileRepository.findLeftJoinFetchTilById(id);
    }

    public void insertProfiles(final List<Profile> profiles) {
        profileRepository.saveAll(profiles);
    }
}