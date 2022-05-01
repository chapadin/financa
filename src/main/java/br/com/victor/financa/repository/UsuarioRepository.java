package br.com.victor.financa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.victor.financa.model.Users;

public interface UsuarioRepository extends JpaRepository<Users, Long> {

    List<Users> findByNomeNot(String string);

    Users findByEmail(String email);

}
