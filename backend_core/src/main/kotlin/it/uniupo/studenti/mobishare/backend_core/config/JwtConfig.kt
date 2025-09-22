package it.uniupo.studenti.mobishare.backend_core.config

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import it.uniupo.studenti.mobishare.backend_core.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //If jwt not provided
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader.substring(7)

        try {
            if (jwtService.validateToken(jwtToken)) {
                val username = jwtService.getUsernameFromToken(jwtToken)
                val userDetails = userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (_: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("JWT Token Expired")
            return
        } catch (_: SignatureException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Invalid JWT Signature")
            return
        } catch (_: Exception) {
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            response.writer.write("Error processing JWT Token")
            return
        }

        filterChain.doFilter(request, response)
    }
}