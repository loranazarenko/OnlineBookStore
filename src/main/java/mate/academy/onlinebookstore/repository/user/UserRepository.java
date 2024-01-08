package mate.academy.onlinebookstore.repository.user;

import java.util.Optional;
import mate.academy.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
