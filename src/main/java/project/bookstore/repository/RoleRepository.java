package project.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.enums.RoleName;
import project.bookstore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(RoleName name);
}
