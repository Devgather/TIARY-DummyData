package me.tiary.dummydata.config;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.*;
import me.tiary.dummydata.runner.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class RunnerConfig {
    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.profile", name = "enabled")
    @Order(1)
    public ProfileDummyRunner profileDummyRunner(@Value("${runner.dummy.profile.rows}") final long rows,
                                                 @Value("${runner.dummy.profile.batch-size}") final long batchSize,
                                                 final ProfileGenerator profileGenerator) {
        return new ProfileDummyRunner(rows, batchSize, profileGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.account", name = "enabled")
    @Order(2)
    public AccountDummyRunner accountDummyRunner(@Value("${runner.dummy.account.rows}") final long rows,
                                                 @Value("${runner.dummy.account.batch-size}") final long batchSize,
                                                 final AccountGenerator accountGenerator) {
        return new AccountDummyRunner(rows, batchSize, accountGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.verification", name = "enabled")
    @Order(3)
    public VerificationDummyRunner verificationDummyRunner(@Value("${runner.dummy.verification.rows}") final long rows,
                                                           @Value("${runner.dummy.verification.batch-size}") final long batchSize,
                                                           final VerificationGenerator verificationGenerator) {
        return new VerificationDummyRunner(rows, batchSize, verificationGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.oauth", name = "enabled")
    @Order(4)
    public OAuthDummyRunner oAuthDummyRunner(@Value("${runner.dummy.oauth.rows-range-per-profile}") final Range rowsRangePerProfile,
                                             @Value("${runner.dummy.oauth.batch-size}") final long batchSize,
                                             final OAuthGenerator oAuthGenerator) {
        return new OAuthDummyRunner(rowsRangePerProfile, batchSize, oAuthGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.til", name = "enabled")
    @Order(5)
    public TilDummyRunner tilDummyRunner(@Value("${runner.dummy.til.rows-range-per-profile}") final Range rowsRangePerProfile,
                                         @Value("${runner.dummy.til.batch-size}") final long batchSize,
                                         final TilGenerator tilGenerator) {
        return new TilDummyRunner(rowsRangePerProfile, batchSize, tilGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.tag", name = "enabled")
    @Order(6)
    public TagDummyRunner tagDummyRunner(@Value("${runner.dummy.tag.rows}") final long rows,
                                         @Value("${runner.dummy.tag.batch-size}") final long batchSize,
                                         final TagGenerator tagGenerator) {
        return new TagDummyRunner(rows, batchSize, tagGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.til-tag", name = "enabled")
    @Order(7)
    public TilTagDummyRunner tilTagDummyRunner(@Value("${runner.dummy.til-tag.rows-range-per-til}") final Range rowsRangePerTil,
                                               @Value("${runner.dummy.til-tag.batch-size}") final long batchSize,
                                               final TilTagGenerator tilTagGenerator) {
        return new TilTagDummyRunner(rowsRangePerTil, batchSize, tilTagGenerator);
    }

    @Bean
    @ConditionalOnProperty(prefix = "runner.dummy.comment", name = "enabled")
    @Order(8)
    public CommentDummyRunner commentDummyRunner(@Value("${runner.dummy.comment.rows-range-per-til}") final Range rowsRangePerTil,
                                                 @Value("${runner.dummy.comment.batch-size}") final long batchSize,
                                                 final CommentGenerator commentGenerator) {
        return new CommentDummyRunner(rowsRangePerTil, batchSize, commentGenerator);
    }
}