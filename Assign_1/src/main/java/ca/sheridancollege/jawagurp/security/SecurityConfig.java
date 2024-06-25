package ca.sheridancollege.jawagurp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import ca.sheridancollege.jawagurp.database.DatabaseAccess;
//import ca.sheridancollege.jawagurp.beans.User;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
		return http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(mvc.pattern("/secure/**")).hasAnyAuthority("USER", "ADMIN")
				.requestMatchers(mvc.pattern("/")).permitAll()
				.requestMatchers(mvc.pattern("/signup")).permitAll()
				.requestMatchers(mvc.pattern("/addUser")).permitAll()
				.requestMatchers(mvc.pattern("/deleteBook/{isbn}")).permitAll()
				.requestMatchers(mvc.pattern("/removeBook/{isbn}")).hasAuthority("ADMIN")
				.requestMatchers(mvc.pattern("/addBook")).hasAuthority("ADMIN")
				.requestMatchers(mvc.pattern("/addBookToList")).hasAuthority("ADMIN")
				.requestMatchers(mvc.pattern("/js/**")).permitAll()
				.requestMatchers(mvc.pattern("/css/**")).permitAll()
				.requestMatchers(mvc.pattern("/images/**")).permitAll()
				.requestMatchers(mvc.pattern("/permission-denied")).permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
				.requestMatchers(mvc.pattern("/**")).denyAll()
		)
				.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
						.disable())
				.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
				.formLogin(form -> form.loginPage("/login").permitAll())
				.exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
				.logout(logout -> logout.permitAll())
				.build();

	}


	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}