package com.ensah.gestiondestock.service;

import com.ensah.gestiondestock.model.Utilisateur;
import com.ensah.gestiondestock.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void enregistrerUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> trouverParUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }
}
