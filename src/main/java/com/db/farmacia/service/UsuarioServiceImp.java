package com.db.farmacia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.farmacia.model.Usuario;
import com.db.farmacia.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImp implements IUsuarioService{

	@Autowired
	private IUsuarioRepository usuariorepository;

	@Override
	public Optional<Usuario> findByid(Integer id) {

		return usuariorepository.findById(id);
	}

	@Override
	public Usuario GuardarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return usuariorepository.save(usuario);
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		// TODO Auto-generated method stub
		return usuariorepository.findByEmail(email);
	}

	@Override
	public List<Usuario> finAll() {
		// TODO Auto-generated method stub
		return usuariorepository.findAll();
	}
	
	

}
