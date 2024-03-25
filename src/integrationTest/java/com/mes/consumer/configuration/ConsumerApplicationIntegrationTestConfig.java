package com.mes.consumer.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(KafkaTestConfig.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ConsumerApplicationIntegrationTestConfig {
}
