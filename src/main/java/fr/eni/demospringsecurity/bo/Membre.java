package fr.eni.demospringsecurity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membre {
    private String prenom;
    private String nom;
    private String username;
    private String password;
    private boolean admin;
}