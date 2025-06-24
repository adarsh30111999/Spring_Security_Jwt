package in.adarshit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.adarshit.filter.AppFilter;
import in.adarshit.service.UserService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private AppFilter appFilter;

	@Bean
	public PasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean // It is used to load customer record for authentication
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(pwdEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();

	}

	@Bean
	public SecurityFilterChain securityFilter(HttpSecurity security) throws Exception {

		security.authorizeHttpRequests(
				(req) -> req.requestMatchers("/register", "/login").permitAll().anyRequest().authenticated()

		).sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		security.addFilterBefore(appFilter, UsernamePasswordAuthenticationFilter.class);
		return security.csrf().disable().build();

	}

}
