package project.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Role;
import project.bookstore.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(RoleName name);
}
