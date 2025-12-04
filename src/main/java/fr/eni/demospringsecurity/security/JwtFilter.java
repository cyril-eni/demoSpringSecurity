package fr.eni.demospringsecurity.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private MyUserDetailsService gestionPersoUtilisateurs;

    /**
     * Méthode appelée avant la verification de Spring Security pour vérifier le JWT et metre à jour le Contexte de sécurité
     **/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
            ServletException, IOException {
        try {
            // 1 - je recupère le token JWT depuis la requête HTTP
            String jwt = jwtUtils.parseJwt(request);
            // 2 - si token valide (encodé avec le bon secret), je continue
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 3 - je recupère le username à partir du token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                // 4 - je recupère l'utilisateur Spring Security correspondant au username
                UserDetails userDetails = gestionPersoUtilisateurs.loadUserByUsername(username);
                // 5 - je délègue l'authentification de cet utilisateur à Spring
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 6 - je mets à jour le contexte SpringSecurity avec l'état d'authentification de l'utilisateur
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}