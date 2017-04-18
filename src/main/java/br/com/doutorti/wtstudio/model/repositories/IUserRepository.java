package br.com.doutorti.wtstudio.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doutorti.wtstudio.model.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

	public UserEntity findByUsernameAndPasswordAndActive(String username,
			String password, Boolean active);

}
