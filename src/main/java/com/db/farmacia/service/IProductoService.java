package com.db.farmacia.service;

import java.util.List;
import java.util.Optional;

import com.db.farmacia.model.Producto;

public interface IProductoService {

	public Producto GuardarProducto(Producto producto);
	public Optional<Producto> BuscarProducto(Integer id);
	public void ActualizarProducto(Producto producto);
	public void EliminarProducto(Integer id);
	public List<Producto> findAll();
}
