package it.uniupo.studenti.mobishare.backend_core.config;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Autowired
    private lateinit var jwtFilter: JwtFilter  //Custom JWT filter

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
//                    .requestMatchers("*/**").hasAnyAuthority("ROLE_CUSTOMER")
                    .requestMatchers(*openedResources).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun authenticationManager(userDetailsService: UserDetailsService): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(userDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder)
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    fun daoAuthenticationProvider(userDetailsService: UserDetailsService): DaoAuthenticationProvider {
        val dao = DaoAuthenticationProvider()
        dao.setUserDetailsService(userDetailsService)
        dao.setPasswordEncoder(passwordEncoder)
        return dao
    }

    private val openedResources: Array<String>
        // some opened endpoints without authentication
        get() = arrayOf(
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/api-docs",
            "/api-docs/**",
            "/auth/register",
            "/auth/login",
            "auth/manager-login"
        )
}
