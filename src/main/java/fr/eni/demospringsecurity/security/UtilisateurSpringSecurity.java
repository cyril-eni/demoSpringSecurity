package fr.eni.demospringsecurity.security;

import fr.eni.demospringsecurity.bo.Membre;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Classe "technique" d'utilisateur
 * Ce sont es instances de cette classe que va manuipuler Spring Security
 * = un "wrapper" par dessus notre classe métier Membre (qui sera utilisée dans les associations de notre appli métier)
 * Elle implemente UserDetails pour fournir à Spring les infos dont il a besoin (permissions(authorities), username et password)
 */
@Data
@AllArgsConstructor
public class UtilisateurSpringSecurity implements UserDetails {
    private Membre membre;

    /**
     * Définit comment on recupère la liste des permissions (GrantedAuthority) de cet utilisateur
     * Equivalence entre roles et permissions :
     * Permission ROLE_XXX = Role XXX
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // je définit pour mon utilisateur si il a tel ou tel rôle via son attribut admin
        if (membre.isAdmin()) {
            return List.of(new SimpleGrantedAuthority("ROLE_admin"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_user"));
    }

    /**
     * Définit comment on recupère le password à partir d'une instance de notre classe
     */
    @Override
    public String getPassword() {
        return membre.getPassword();
    }

    /**
     * Définit comment on recupère le username à partir d'une instance de notre classe
     */
    @Override
    public String getUsername() {
        return membre.getUsername();
    }
}