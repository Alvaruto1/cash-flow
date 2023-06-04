package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    public User save(@NonNull User user){
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public void delete(@NonNull User user){
        userRepository.delete(user);
    }
    public Optional<User> findByUsername(@NonNull String username){
        return userRepository.findByUsername(username);
    }

}
