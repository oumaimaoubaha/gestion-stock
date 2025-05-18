package com.ensah.gestiondestock.config;

import com.ensah.gestiondestock.model.Utilisateur;
import com.ensah.gestiondestock.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        return new User(
                utilisateur.getUsername(),
                utilisateur.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole()))
        );
    }
}
