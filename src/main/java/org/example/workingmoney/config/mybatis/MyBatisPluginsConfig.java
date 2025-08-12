package org.example.workingmoney.config.mybatis;

import org.apache.ibatis.plugin.Interceptor;
import org.example.workingmoney.repository.common.AuditInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class MyBatisPluginsConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public Interceptor auditInterceptor(Clock clock) {
        return new AuditInterceptor(clock);
    }
}
