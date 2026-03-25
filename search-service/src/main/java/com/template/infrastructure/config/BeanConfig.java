
package com.template.infrastructure.config;

import com.template.application.usecase.SearchUseCaseImpl;
import com.template.domain.ports.in.SearchUseCase;
import com.template.domain.ports.out.KafkaPort;
import com.template.domain.ports.out.SearchRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public SearchUseCase searchUseCase(SearchRepositoryPort repo, KafkaPort kafka) {
        return new SearchUseCaseImpl(repo, kafka);
    }
}
