package cz.muni.fi.obs;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableFeignClients
@SpringBootApplication
public class UserManagement {

    public static final String SECURITY_SCHEME_BEARER = "Bearer";
    public static void main(String[] args) {
        SpringApplication.run(UserManagement.class, args);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()));
        return http.build();
    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        String baseUri = "/v1/users";
//        http
//                .authorizeHttpRequests(x -> x
//                        .requestMatchers(HttpMethod.POST, baseUri + "/**").hasAuthority("SCOPE_test_write")
//                        .requestMatchers(HttpMethod.PUT, baseUri + "/**").hasAuthority("SCOPE_test_write")
//                        .requestMatchers(HttpMethod.GET, baseUri + "nationalities").permitAll()
//                        .requestMatchers(HttpMethod.GET, baseUri + "/**").hasAuthority("SCOPE_test_read")
//                        .anyRequest().permitAll()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()))
//        ;
//        return http.build();
//    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> {
            openApi.getComponents()
                   .addSecuritySchemes(SECURITY_SCHEME_BEARER,
                                       new SecurityScheme()
                                               .type(SecurityScheme.Type.HTTP)
                                               .scheme("bearer")
                                               .description("Provide an access token")
                   )
//                   .addSecuritySchemes("MUNI OAuth2",
//                                       new SecurityScheme()
//                                               .type(SecurityScheme.Type.OAUTH2)
//                                               .description("Get access token with OAuth2 Authorization Code Grant")
//                                               .flows(new OAuthFlows()
//                                                              .authorizationCode(new OAuthFlow()
//                                                                                         .authorizationUrl("https://oidc.muni.cz/oidc/authorize")
//                                                                                         .tokenUrl("https://oidc.muni.cz/oidc/token")
//                                                                                         .scopes(new Scopes()
//                                                                                                         .addString
//                                                                                                         ("test_read", "reading events")
//                                                                                                         .addString
//                                                                                                         ("test_write", "creating events")
//                                                                                                         .addString
//                                                                                                         ("test_1",
//                                                                                                         "deleting
//                                                                                                         events")
//                                                                                         )
//                                                              )
//                                               )
//                   )
            ;
        };
    }
}
