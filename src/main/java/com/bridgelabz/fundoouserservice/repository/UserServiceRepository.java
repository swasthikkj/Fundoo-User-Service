package com.bridgelabz.fundoouserservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoouserservice.model.UserServiceModel;

@Repository
public interface UserServiceRepository extends JpaRepository<UserServiceModel, Long> {

	Optional<UserServiceModel> findByEmailId(String emailId);

}
