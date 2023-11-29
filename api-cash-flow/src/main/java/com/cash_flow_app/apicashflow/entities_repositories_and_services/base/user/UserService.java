package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.user;
import com.cash_flow_app.apicashflow.dtos.AccountDto;
import com.cash_flow_app.apicashflow.dtos.AccountDtos;
import com.cash_flow_app.apicashflow.dtos.UserDto;
import com.cash_flow_app.apicashflow.dtos.UsersDto;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.Authority;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority.AuthorityService;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security.LoginDetails;
import com.cash_flow_app.apicashflow.entities_repositories_and_services.base.security.LoginService;
import com.cash_flow_app.apicashflow.utils.PermissionName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findByUsername(principal.toString());
    }

    public void setCurrentLoggedUser(String username){
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDetails userDetails = new LoginDetails(user);
        Authentication authentication =new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserDto usersToDto(@NonNull User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .build();
    }

    public UsersDto usersToDtos(@NonNull List<User> users) {
        UsersDto usersDtos = UsersDto.builder().build();
        List<UserDto> listUserDtos = new ArrayList<>();
        for (User user : users) {
            listUserDtos.add(usersToDto(user));
        }
        usersDtos.setUserDtos(listUserDtos);
        return usersDtos;
    }

}
