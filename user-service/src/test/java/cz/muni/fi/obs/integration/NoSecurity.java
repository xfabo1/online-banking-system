package cz.muni.fi.obs.integration;

import cz.muni.fi.obs.security.Security;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class NoSecurity {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                           .requestMatchers(new AntPathRequestMatcher("/**"));
    }

    @ConditionalOnMissingBean(Security.class)
    @Bean
    public TestSecurity testSecurity() {
        return new TestSecurity();
    }
}