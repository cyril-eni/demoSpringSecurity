package fr.eni.demospringsecurity.api;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Ressource accessible uniquements aux utilisateurs connectés avec un rôle admmin
 */
@RestController
@RequestMapping("/ressourceAdmin")
public class RessourceAdmin {

    //@RolesAllowed("admin")
    @GetMapping
    public String getRessource() {
        return "RessourceAdmin";
    }
}