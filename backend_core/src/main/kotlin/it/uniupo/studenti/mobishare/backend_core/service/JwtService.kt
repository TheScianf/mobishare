package it.uniupo.studenti.mobishare.backend_core.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    private val secretKey = "secretKeyToPutInYAML"  //TODO: put in YAML
    private val expiration: Long = 30 * 24 * 3600000L  //30D

    /**
     * Generate a JWT token with the given user's username
     *
     * The token is signed with the application's secret key and will expire after 30 days
     *
     * @param userDetails the user to generate the token for
     * @return the generated JWT token
     */
    fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    /**
     * Validates a JWT token. The token is valid if it has not expired and it was signed with the same secret key used to generate it.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Returns the username associated with the given JWT token
     *
     * @param token the JWT token to get the username from
     * @return the username associated with the token
     */
    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        return claims.subject
    }
}