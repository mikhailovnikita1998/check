package org.example.config;

import org.example.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // Доступ к страницам администратора и модератора
                .antMatchers("/admin/**").hasRole("ADMIN")  // Доступ только для администраторов
                .antMatchers("/moderator/**").hasRole("MODERATOR")  // Доступ только для модераторов

                // Доступ к API для обновления профиля (доступен для всех аутентифицированных пользователей)
                .antMatchers("/profile/update").authenticated()  // Аутентифицированные пользователи могут обновлять профиль

                // Доступ к восстановлению пароля (открыт для всех)
                .antMatchers("/auth/forgot-password", "/auth/reset-password").permitAll()  // Восстановление пароля доступно без авторизации

                // Ограничение доступа к ресурсам пользователей
                .antMatchers("/profile/**").hasRole("USER")  // Доступ для обычных пользователей

                // Любой другой запрос требует аутентификации
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
