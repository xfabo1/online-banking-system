package cz.muni.fi.obs.security;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
@Profile("!test-disable-security")
public class SecurityConfig {

    public static final String SECURITY_SCHEME_BEARER = "Bearer";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> openApi.getComponents()
                                 .addSecuritySchemes(SecurityConfig.SECURITY_SCHEME_BEARER,
                                                     new SecurityScheme()
                                                             .type(SecurityScheme.Type.HTTP)
                                                             .scheme("bearer")
                                                             .description("Provide an access token")
                                 );
    }
}
