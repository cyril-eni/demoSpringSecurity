package fr.eni.demospringsecurity.security;

import fr.eni.demospringsecurity.bo.Membre;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Lorsque je crée un service qui implémente UserDetailsService
 * Je dois dire à Spring comment il récupère un utilisateur à partir du username/password qu'il aura extrait de la requête HTTP
 * Pour ca, je dois overrider la méthode : UserDetails loadUserByUsername(String username)
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    //TODO: Utiliser un MembreRepository pour gérer les utilisateurs en base de donnée
    List<Membre> membres = new ArrayList<>();

    public MyUserDetailsService(PasswordEncoder passwordEncoder) {
        Membre m1 = new Membre("Cyril", "Mace", "membre1", passwordEncoder.encode("membre1"), false);
        Membre m2 = new Membre("Eric", "Legrand", "membre2", passwordEncoder.encode("membre2"), false);
        Membre m3 = new Membre("Patricia", "Martin", "admin", passwordEncoder.encode("admin"), true);
        membres.add(m1);
        membres.add(m2);
        membres.add(m3);
    }

    /**
     * Méthode qui explique à Spring Security comment on récupère un utilisateur à partir d'un username
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 1 - je recupère le Membre à partir de son username (ici en mémoire mais même principe en base de donnée)
        for (Membre membre : membres) {
            if (membre.getUsername().equals(username)) {
                // je retourne une instance de UtilisateurSpringSecurity à Spring (qui wrappe ce membre)
                return new UtilisateurSpringSecurity(membre);
            }
        }
        throw new UsernameNotFoundException(username);
    }
}