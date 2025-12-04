package fr.eni.demospringsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfiguration {

    /**
     * Spring Security met à disposition une classe PasswordEncoder
     * qui peut se charger de l'encodage des mots de passe
     * C'est obligatoire, Spring Security va toujours chercher à décoder des mots de passe
     *
     * @return
     */
    @Bean // on définit un bean pour l'utilitaire d'encryption de mot de passe
    public PasswordEncoder passwordEncoder() {
        // on utilise la config par défault avec un encodage bcrypt
        return new BCryptPasswordEncoder();
    }
}