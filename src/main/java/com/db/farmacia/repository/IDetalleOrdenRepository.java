package com.db.farmacia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.farmacia.model.DetalleOrden;


@Repository
public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer>{

	
}
