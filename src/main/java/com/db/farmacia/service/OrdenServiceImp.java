package com.db.farmacia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.farmacia.model.Orden;
import com.db.farmacia.model.Usuario;
import com.db.farmacia.repository.IOrdenRepository;

@Service
public class OrdenServiceImp implements IOrdenService{

	@Autowired
	private IOrdenRepository ordenrepository;
	
	@Override
	public Orden guardarOrden(Orden orden) {
		// TODO Auto-generated method stub
		return ordenrepository.save(orden);
	}

	@Override
	public List<Orden> BuscarOrden() {
		// TODO Auto-generated method stub
		return ordenrepository.findAll();
	}
	
	public String generarNumeroOrden() {
		
		int numero=0;
		String numeroConcatenado= "";
		
		List<Orden> ordenes= BuscarOrden();
		
		List<Integer> numeros= new ArrayList<Integer>();
		
		ordenes.stream().forEach(p -> numeros.add(Integer.parseInt(p.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero=1;
			
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero<10) {//000000000
			numeroConcatenado="000000000" + String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000" + String.valueOf(numero);	
		}else if(numero<1000) {
			numeroConcatenado="0000000" + String.valueOf(numero);
		}else if(numero<100000) {
			numeroConcatenado="000000" + String.valueOf(numero);
		}
		
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return ordenrepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> FindById(Integer id) {
		// TODO Auto-generated method stub
		return ordenrepository.findById(id);
	}

}
