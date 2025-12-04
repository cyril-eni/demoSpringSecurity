package fr.eni.demospringsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class SecurityConfiguration {
    /**
     * En créant et mettant dans le contexte un Bean de type SecurityFilterChain
     * Je peux spécifier la config de Spring Security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                // accès à /ressourcePrivee : il faut être authentifié
                .requestMatchers("/ressourcePrivee").authenticated()
                // accès à /ressourceAdmin : il faut avoir le role admin
                .requestMatchers("/ressourceAdmin").hasRole("admin")
                // équivalent : .requestMatchers("/ressourceAdmin").hasAuthority("ROLE_admin")
                // accès à toutes les autres ressources : autorisé
                // ATTENTION : les conditions sont appliquées dans l'ordre : mettre celle_ci en dernier
                .requestMatchers("/**").permitAll())
                // authentification basique : on doit ajouter un username/password dans chaque requête HTTP
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Spring Security met à disposition une classe PasswordEncoder
     * qui peut se charger de l'encodage des mots de passe
     * C'est obligatoire, Spring Security va toujours chercher à décoder des mots de passe
     * @return
     */
    @Bean // on définit un bean pour l'utilitaire d'encryption de mot de passe
    public PasswordEncoder passwordEncoder() {
        // on utilise la config par défault avec un encodage bcrypt
        return new BCryptPasswordEncoder();
    }

    /**
     * On définit un bean pour la gestion des utilisateurs en mémoire
     * On utilise une classe de SpringSecurity qui implémente UserDetailsService
     * => Spring va aller chercher dans son contexte cette classe pour gérer l'authentification
     * @return
     */
    // Plus besoin de ce @Bean car on a déjà un service personnalisé qui implémente UserDetailsService : MyUserDetailsService
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
    }
}