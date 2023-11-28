package com.db.farmacia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.farmacia.model.DetalleOrden;
import com.db.farmacia.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImp implements IDetalleOrdenService{

	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;
	
	@Override
	public DetalleOrden GuardarDetalle(DetalleOrden detalleOrden) {
		// TODO Auto-generated method stub
		return detalleOrdenRepository.save(detalleOrden);
	}

	
	

}
