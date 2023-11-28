package com.db.farmacia.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.farmacia.model.Orden;
import com.db.farmacia.model.Producto;
import com.db.farmacia.service.IOrdenService;
import com.db.farmacia.service.IProductoService;
import com.db.farmacia.service.IUsuarioService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    
	@Autowired
	private IProductoService productoservice;
	
	@Autowired
	private IUsuarioService usuarioservice;
	
	@Autowired
	private IOrdenService ordenservice;
	
	
	private final Logger log=LoggerFactory.getLogger(AdministradorController.class);
	
	@GetMapping("")
	public String Home(Model model) {
		
		List<Producto> productos= productoservice.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		
		model.addAttribute("usuarios", usuarioservice.finAll());
		return "administrador/usuarios";
	}
	
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordenservice.BuscarOrden());
		return "administrador/ordenes";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model,@PathVariable Integer id) {
		log.info("id de la orden:  {}", id);
		
	    Orden orden= ordenservice.FindById(id).get();
	    
	    model.addAttribute("detalle", orden.getDetalle());
	    
		return "administrador/detalleOrden";
		
	}
	
}
