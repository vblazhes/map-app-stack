package mk.finki.ukim.mk.map_application.repository.Auth;

import mk.finki.ukim.mk.map_application.model.Auth.Role;
import mk.finki.ukim.mk.map_application.model.Auth.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
    Optional<Role> findById(Long id);
}