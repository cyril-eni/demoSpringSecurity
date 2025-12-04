package fr.eni.demospringsecurity.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * 1 : génération du token JWT à l'authentification
     */
    public String generateJwtToken(Authentication authentication) {
        UtilisateurSpringSecurity userPrincipal = (UtilisateurSpringSecurity) authentication.getPrincipal();
        /**
         * je crée un token JWT qui contient  le username de mon utilisateur courant connecté (avec l'Authentification basique)
         *  il est encodé avec l'algo HMAC256 et mon secret (défini dans application.properties)
          */
        return JWT.create().withClaim("username", userPrincipal.getUsername()).sign(Algorithm.HMAC256(jwtSecret));
    }

    /**
     * 2 : récupération du token JWT depuis la requête HTTP
     */
    public static String parseJwt(HttpServletRequest request) {
        //La convention est de placer le token JWT dans le Header "Authorization" de ma requête HTTP
        String headerAuth = request.getHeader("Authorization");
        // ET de la placer après le mot-clé "Bearer"
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    /**
     * 3 : vérification token OK (encodé avec bon secret)
     */
    public boolean validateJwtToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("error : " + e.getStackTrace());
        }
        return false;
    }

    /**
     * 4 : récupération du username depuis un token JWT
     */
    public String getUserNameFromJwtToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(token).getClaim("username").asString();
    }
}