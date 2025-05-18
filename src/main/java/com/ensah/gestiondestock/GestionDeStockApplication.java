package com.ensah.gestiondestock;

import com.ensah.gestiondestock.model.Utilisateur;
import com.ensah.gestiondestock.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GestionDeStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDeStockApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UtilisateurRepository utilisateurRepository, PasswordEncoder encoder) {
		return args -> {
			if (utilisateurRepository.findByUsername("kawtar").isEmpty()) {
				Utilisateur user1 = new Utilisateur();
				user1.setUsername("kawtar");
				user1.setPassword(encoder.encode("kawtar123"));
				user1.setNomComplet("Admin Kawtar");
				user1.setRole("ADMIN");
				utilisateurRepository.save(user1);
			}

			if (utilisateurRepository.findByUsername("oumaima").isEmpty()) {
				Utilisateur user2 = new Utilisateur();
				user2.setUsername("oumaima");
				user2.setPassword(encoder.encode("oumaima123"));
				user2.setNomComplet("Admin Oumaima");
				user2.setRole("ADMIN");
				utilisateurRepository.save(user2);
			}
		};
	}
}
