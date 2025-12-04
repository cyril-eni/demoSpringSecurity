package fr.eni.demospringsecurity.api;

import fr.eni.demospringsecurity.bo.Membre;
import fr.eni.demospringsecurity.security.UtilisateurSpringSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    /**
     * On peut injecter un bean de type Authentication dans nos controller/services etc...
     * Cela va nous permettre de récupérer l'utilisateur connecté courant
     */
    @GetMapping("/keycloak")
    public String getConnectedUserkeyCloak(Authentication authentication) {
        return "utilisateur keycloak : " + authentication.getName();
    }

    /**
     * On peut injecter un bean de type Authentication dans nos controller/services etc...
     * Cela va nous permettre de récupérer l'utilisateur connecté courant
     */
    @GetMapping("/jwt")
    public String getConnectedUser(Authentication authentication) {
        UtilisateurSpringSecurity utilisateurConnecte = (UtilisateurSpringSecurity) authentication.getPrincipal();
        Membre membreConnecte = utilisateurConnecte.getMembre();
        return "membre lié à l'utilisateur connecté : " + membreConnecte;
    }
}