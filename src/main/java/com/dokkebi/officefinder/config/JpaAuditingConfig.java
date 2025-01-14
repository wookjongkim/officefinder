package com.dokkebi.officefinder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Profile({"test", "release", "local"})
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
