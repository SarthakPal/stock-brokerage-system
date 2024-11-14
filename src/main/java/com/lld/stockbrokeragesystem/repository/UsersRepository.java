package com.lld.stockbrokeragesystem.repository;

import com.lld.stockbrokeragesystem.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}

