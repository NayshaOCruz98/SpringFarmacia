package com.db.farmacia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.farmacia.model.Producto;
import com.db.farmacia.repository.IProductoRepository;

@Service
public class ProductoServiceImp implements IProductoService{

	@Autowired
	private IProductoRepository productorepository;
	
	@Override
	public Producto GuardarProducto(Producto producto) {
	
		return productorepository.save(producto);
	}

	@Override
	public Optional<Producto> BuscarProducto(Integer id) {
		// TODO Auto-generated method stub
		return productorepository.findById(id);
		
	}

	@Override
	public void ActualizarProducto(Producto producto) {
		// TODO Auto-generated method stub
		productorepository.save(producto);
	}

	@Override
	public void EliminarProducto(Integer id) {
		// TODO Auto-generated method stub
		productorepository.deleteById(id);
	}

	@Override
	public List<Producto> findAll() {
		// TODO Auto-generated method stub
		return productorepository.findAll();
	}

}
