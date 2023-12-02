package com.example.demo.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private DataSource dataSource;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
                ).formLogin(form -> form
                        .defaultSuccessUrl("/mypage")
                        .loginPage("/login")
                );
        return http.build();
    }

    //	makeUserメソッド内のハッシュ化と競合するっぽい
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
    	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    	return logoutHandler;
    }
    
    @Bean
	public UserDetailsManager userDetailsManager() { 
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);
//		users.createUser(makeUser("ユーザーID","パスワード","ロール"));
		return users;
	}
    
	private UserDetails makeUser(String user,String pass,String role) {  //登録用メソッド
		return User.withUsername(user)
				.password(
						PasswordEncoderFactories
						.createDelegatingPasswordEncoder()
						.encode(pass))
				.roles(role)
				.build();
	}
}
