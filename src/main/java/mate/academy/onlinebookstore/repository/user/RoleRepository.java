package mate.academy.onlinebookstore.repository.user;

import mate.academy.onlinebookstore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByName(Role.RoleName roleName);
}
