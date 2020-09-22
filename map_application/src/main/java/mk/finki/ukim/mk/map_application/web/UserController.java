package mk.finki.ukim.mk.map_application.web;

import mk.finki.ukim.mk.map_application.model.Auth.Role;
import mk.finki.ukim.mk.map_application.model.Auth.User;
import mk.finki.ukim.mk.map_application.model.Exception.Auth.RoleNotFoundException;
import mk.finki.ukim.mk.map_application.model.Exception.Auth.UserNotFoundException;
import mk.finki.ukim.mk.map_application.repository.Auth.RoleRepository;
import mk.finki.ukim.mk.map_application.repository.Auth.UserRepository;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/users", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class UserController {
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public UserController(RoleRepository roleRepository, UserRepository userRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping({"/{id}/role"})
    public Role getUserRole(@PathVariable("id") Long id){
        System.out.println("User controller");
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Role role = (Role) user.getRoles().toArray()[0];
        return roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new);
    }

    @GetMapping({"/{id}"})
    public User getUserById(@PathVariable("id") Long id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
