package com.db.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.farmacia.model.Orden;
import com.db.farmacia.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {
	
	List<Orden> findByUsuario(Usuario usuario);

}
