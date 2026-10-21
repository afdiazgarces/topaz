package com.topaz.support.infrastructure.config;

import com.topaz.support.domain.factory.SupportOrderFactory;
import com.topaz.support.domain.policy.InProgressTransitionRule;
import com.topaz.support.domain.policy.OpenTransitionRule;
import com.topaz.support.domain.policy.StatusTransitionPolicy;
import java.time.Clock;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Ensambla las clases de dominio (que no dependen de Spring). */
@Configuration
public class DomainConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    SupportOrderFactory supportOrderFactory(Clock clock) {
        return new SupportOrderFactory(clock);
    }

    @Bean
    StatusTransitionPolicy statusTransitionPolicy() {
        return new StatusTransitionPolicy(List.of(new OpenTransitionRule(), new InProgressTransitionRule()));
    }
}
