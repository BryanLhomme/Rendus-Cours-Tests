package com.example;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createAccount(String email, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return "Identifiant déjà utilisé";
        }
        userRepository.save(new User(email, username, password));
        return "Compte créé avec succès";
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return "Identifiants incorrects";
        }
        return "Connexion réussie";
    }
}
