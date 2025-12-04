package fr.eni.demospringsecurity.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ressource accessible uniquements aux utilisateurs connectés
 */
@RestController
@RequestMapping("/ressourcePrivee")
public class RessourcePrivee {
    @GetMapping
    public String getRessource() {
        return "RessourcePrivee";
    }
}