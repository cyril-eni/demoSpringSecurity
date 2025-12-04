package fr.eni.demospringsecurity.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ressource accessible à tout le monde (même personne non connectée)
 */
@RestController
@RequestMapping("/ressourcePublique")
public class RessourcePublique {
    @GetMapping
    public String getRessource() {
        return "RessourcePublique";
    }
}