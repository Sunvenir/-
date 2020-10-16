package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import softgroup.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User findByUseridAndPassword(String id, String password);

    User findByUserid(String id);
}
