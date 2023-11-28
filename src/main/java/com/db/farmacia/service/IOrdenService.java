package com.db.farmacia.service;

import java.util.List;
import java.util.Optional;

import com.db.farmacia.model.Orden;
import com.db.farmacia.model.Usuario;

public interface IOrdenService {
    
   List<Orden> BuscarOrden();
   Optional<Orden> FindById(Integer id);
   Orden guardarOrden(Orden orden);
   String generarNumeroOrden();
   List<Orden> findByUsuario(Usuario usuario);
   
   
}
