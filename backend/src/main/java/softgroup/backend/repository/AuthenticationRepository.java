package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Auth;

import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Auth,Integer> {

    Auth findByUserid(String userid);

    List<Auth> findByAuthidNotNull();

    @Transactional
    void deleteByUserid(String userid);
}
