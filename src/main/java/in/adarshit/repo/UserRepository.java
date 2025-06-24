package in.adarshit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.adarshit.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);

}
