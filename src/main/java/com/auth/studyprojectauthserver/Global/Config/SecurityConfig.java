package com.auth.studyprojectauthserver.Global.Config;

import com.auth.studyprojectauthserver.Global.Filter.JwtAuthenticationFilter;
import com.auth.studyprojectauthserver.Global.Jwt.JwtFailureHandler;
import com.auth.studyprojectauthserver.Global.Jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    /**
     * 인증 서버에서 사용할 기본적인 권한을 보함하는 Configuration입니다.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
        http.formLogin().disable();
        http.logout().disable();
        http.httpBasic().disable();
        http.csrf().disable();
        http.addFilter(jwtAuthenticationFilter());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    /**
     * Password를 암호화하는 함수입니다.
     * @return bCryptPasswordEncoder();
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT 인증을 위해 UsernamePasswordAuthenticationFilter를 custom한 Filter의 설정을 위한 기능입니다.
     * @return
     * @throws Exception
     */
    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authenticationManager(null),
                jwtTokenProvider,
                redisTemplate
        );

        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        // return configuration.getAuthenticationManager();
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new JwtFailureHandler();
    }
}
