package com.github.ngnhub.rsocket_ui.domain.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@ComponentScan(basePackages = ["com.github.ngnhub.rsocket_ui.domain"])
@EnableR2dbcRepositories(basePackages = ["com.github.ngnhub.rsocket_ui.domain.repository"])
class DomainAutoConfiguration
