package com.db.farmacia.service;

import java.util.List;
import java.util.Optional;

import com.db.farmacia.model.Usuario;

public interface IUsuarioService {
	
	List<Usuario> finAll();
	Optional<Usuario> findByid(Integer id);
	Usuario GuardarUsuario(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
}
