package fr.eni.demospringsecurity.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    // J'injecte une instance de mon filtre JWT qui va être executé avant chaque verification Spring Security (pour veriier le token JWT et mettre à jur le contexte Spring)
    private JwtFilter jwtFilter;

    /**
     * En créant et mettant dans le contexte un Bean de type SecurityFilterChain
     * Je peux spécifier la config de Spring Security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1 - suppression de la vérification csrf pour les API stateless (pas de sessions)
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((authorize) -> authorize
                // 2 autorisation des requêtes OPTIONS sur toutes les routes
                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                // accès à /ressourcePrivee : il faut être authentifié
                .requestMatchers("/ressourcePrivee").authenticated()
                // accès à /ressourceAdmin : il faut avoir le role admin
                .requestMatchers("/ressourceAdmin").hasRole("admin")
                // équivalent : .requestMatchers("/ressourceAdmin").hasAuthority("ROLE_admin")
                // accès à toutes les autres ressources : autorisé
                // ATTENTION : les conditions sont appliquées dans l'ordre : mettre celle_ci en dernier
                .requestMatchers("/**").permitAll());
                // authentification basique : on doit ajouter un username/password dans chaque requête HTTP
                // plus besoin car on utilise JWT : .httpBasic(Customizer.withDefaults());
                // 3- ajout du Filtre de vérification tu token JWT
                //http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                // utilisation de oauth2 et d'un serveur externe pour l'authentification
                http.oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
        );
        return http.build();
    }



    /**
     * On définit un bean pour la gestion des utilisateurs en mémoire
     * On utilise une classe de SpringSecurity qui implémente UserDetailsService
     * => Spring va aller chercher dans son contexte cette classe pour gérer l'authentification
     * @return
     */
    /* Plus besoin de ce @Bean car on a déjà un service personnalisé qui implémente UserDetailsService : MyUserDetailsService
    public InMemoryUserDetailsManager userDetailsService() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        // utilisateur membre, mdp membre123, roles : user
        userDetailsList.add( User.withUsername("membre")
        // j'encode le mot de passe avec le PasswordEncoder
        .password(passwordEncoder().encode("membre123"))
        .roles("user").build());

        // utilisateur admin, mdp admin123, roles : admin, user
        userDetailsList.add( User.withUsername("admin").
        password(passwordEncoder().encode("admin123"))
        .roles("admin", "user").build());
        return new InMemoryUserDetailsManager(userDetailsList);
    }*/
}