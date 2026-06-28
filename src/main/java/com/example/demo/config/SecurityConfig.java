package com.example.demo.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
				.loginPage("/login")
				.defaultSuccessUrl("/mypage")
				.failureHandler((request, response, exception) -> {
					// 認証失敗時は簡潔にログを出してログインページへリダイレクト
					String username = request.getParameter("username");
					System.err.println("Authentication failed for username: " + username + ", reason: " + exception.getMessage());
					response.sendRedirect(request.getContextPath() + "/login?error=true");
				})
			);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
    	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    	return logoutHandler;
    }
    
    @Bean
	public UserDetailsManager userDetailsManager() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(this.dataSource);

		// --- カスタムクエリの設定 ---
		// users テーブルが独自スキーマ（user_id, auth0_sub 等を含む）のため
		// username / password / enabled カラムを明示的に指定する
		manager.setUsersByUsernameQuery(
			"SELECT username, password, enabled FROM users WHERE username = ?"
		);
		// authorities テーブルも独自スキーマ（id カラムあり）のため明示指定
		manager.setAuthoritiesByUsernameQuery(
			"SELECT username, authority FROM authorities WHERE username = ?"
		);
		// ユーザー存在確認クエリ（登録時の重複チェック用）
		manager.setUserExistsSql(
			"SELECT username FROM users WHERE username = ?"
		);
		// ユーザー挿入クエリ
		// auth0_sub は UNIQUE 制約があるため、UUID()関数で一意な値を挿入する
		manager.setCreateUserSql(
			"INSERT INTO users (username, password, enabled, auth0_sub) VALUES (?, ?, ?, UUID())"
		);
		// 権限挿入クエリ
		manager.setCreateAuthoritySql(
			"INSERT INTO authorities (username, authority) VALUES (?, ?)"
		);

		return manager;
	}
}
