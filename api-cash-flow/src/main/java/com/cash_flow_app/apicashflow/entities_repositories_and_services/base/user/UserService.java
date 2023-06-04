package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user;
import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.AuthorityService;
import com.cash_flow_app.apicashflow.utils.PermissionName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    public User save(@NonNull User user){
        return userRepository.save(user);
    }
    public User signup(@NonNull UserDto userDto) {
        ArrayList<Authority> authorities = new ArrayList<>();
        for (HashMap<String, Object> authorityHashmap: userDto.getAuthorities()){
            ArrayList<String> actions = (ArrayList<String>) authorityHashmap.get("actions");
            String endpoint = (String)authorityHashmap.get("endpoint");
            if (actions.contains("all")){
                authorities.addAll(authorityService.getAllPermissionsToEndpoint(endpoint));
                continue;
            }
            for (String action : actions){
                Optional<Authority> authority = authorityService.findByPermissionAndEndpoint(PermissionName.valueOf(action), endpoint);
                if (authority.isPresent()){
                    authorities.add(authority.get());
                }
            }
        }
        if (authorities.isEmpty()){
            return null;
        }
        else{
            return save(new User(userDto.getUsername(), new BCryptPasswordEncoder().encode(userDto.getPassword()), authorities));
        }
    }
    public void delete(@NonNull User user){
        userRepository.delete(user);
    }
    public Optional<User> findByUsername(@NonNull String username){
        return userRepository.findByUsername(username);
    }

}
