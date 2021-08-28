package guru.springframework.spring5mongorecipeapp.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableCaching
@EnableWebFluxSecurity
public class WebSecurityConfig {

    // SOURCE:
    // https://docs.spring.io/spring-security/site/docs/5.1.7.RELEASE/reference/html/webflux-redirect-https.html
    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        http.redirectToHttps()
            .httpsRedirectWhen(e -> e.getRequest().getHeaders().containsKey("X-Forwarded-Proto"))
            .and()
            .formLogin()
            .disable()
            .logout()
            .disable();

        return http.build();
    }

}
