package br.com.doutorti.willsalon.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doutorti.willsalon.model.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

	public UserEntity findByUsernameAndPasswordAndActive(String username,
			String password, Boolean active);

}
