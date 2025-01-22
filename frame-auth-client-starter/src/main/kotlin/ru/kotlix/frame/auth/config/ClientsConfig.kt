package ru.kotlix.frame.auth.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackageClasses = [ru.kotlix.frame.auth.client.AuthClient::class])
@ConditionalOnMissingBean(ru.kotlix.frame.auth.client.AuthClient::class)
class ClientsConfig
