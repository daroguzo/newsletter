package com.newsletter.skuniv.user;

import com.newsletter.skuniv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
