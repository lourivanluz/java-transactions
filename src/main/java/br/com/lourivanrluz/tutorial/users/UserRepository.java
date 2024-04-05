package br.com.lourivanrluz.tutorial.users;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ListCrudRepository<Users, UUID> {

    UserDetails findByEmail(String email);
}
