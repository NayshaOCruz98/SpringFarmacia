package com.db.farmacia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.db.farmacia.model.DetalleOrden;
import com.db.farmacia.model.Orden;
import com.db.farmacia.model.Producto;
import com.db.farmacia.model.Usuario;
import com.db.farmacia.service.IDetalleOrdenService;
import com.db.farmacia.service.IOrdenService;
import com.db.farmacia.service.IProductoService;
import com.db.farmacia.service.IUsuarioService;





@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IProductoService productoservice;
	
	@Autowired
	private IUsuarioService usuarioservice;
	
	@Autowired
	private IOrdenService ordenservice;
	
	@Autowired
	private IDetalleOrdenService detalleordenservice;
	
	//para almacenar los detalles de la orden
	List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();
	
	//datos de la orden
	Orden orden= new Orden();
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		log.info("Session del usuario: {}", session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoservice.findAll());
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/home";
	}
	@GetMapping("/productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("id producto enviado como parametro {}",id);
		
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoservice.BuscarProducto(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";	
		
	}
	
	@PostMapping("/AgregarCarrito")
	public String AgregarCarrito(@RequestParam Integer id, @RequestParam Integer cantidad,
			Model model) {
		DetalleOrden detalleorden= new DetalleOrden();
		Producto producto= new Producto();
		double sumaTotal= 0;
		
		Optional<Producto> optionalproducto= productoservice.BuscarProducto(id);
		log.info("Producto añadido : {}",optionalproducto.get());
		log.info("cantidad: {}", cantidad);
		
		producto=optionalproducto.get();
		detalleorden.setCantidad(cantidad);
		detalleorden.setPrecio(producto.getPrecio());
		detalleorden.setNombre(producto.getNombre());
		detalleorden.setTotal(producto.getPrecio()*cantidad);
		detalleorden.setProducto(producto);
		
		//validar para que no se suba 2 veces un mismo producto
		Integer idProducto = producto.getId();
		Boolean ingresado = detalles.stream().anyMatch(p-> p.getProducto().getId()==idProducto);
		
		if(!ingresado) {
			detalles.add(detalleorden);
		}
		sumaTotal= detalles.stream().mapToDouble(dt-> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//Quitar un producto del carrito
	@GetMapping("/delete/carrito/{id}")
	public String EliminarProCarrito(@PathVariable Integer id,Model model) {
		
		List<DetalleOrden> ordenesnueva = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleorden:detalles) {
			if ( detalleorden.getProducto().getId()!=id) {
				ordenesnueva.add(detalleorden);
				
			}
			
		}
		
		//poner la nueva lista con los productos restantes
		detalles= ordenesnueva;
		
		double sumaTotal= 0;
		sumaTotal= detalles.stream().mapToDouble(dt-> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCarrito")
	public String getCarrito(Model model, HttpSession session) {
		
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("session", session.getAttribute("idusuario"));
		
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order (Model model, HttpSession session) {
		
		Usuario usuario = usuarioservice.findByid((Integer)(session.getAttribute("idusuario"))).get();
		
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		
		return "/usuario/resumenorden";
	}
	
	@GetMapping("/GuardarOrden")
	public String GuardarOrden(HttpSession session){
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenservice.generarNumeroOrden());
		
		//usuario que va ser referencia esa orden
		Usuario usuario = usuarioservice.findByid((Integer)(session.getAttribute("idusuario"))).get();
		
		orden.setUsuario(usuario);
		ordenservice.guardarOrden(orden);
		
		//guardar detalles
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleordenservice.GuardarDetalle(dt);
			
		}
		
		//limpiar lista y orden
		orden = new Orden();
		
		detalles.clear();
		
		
		return "redirect:/";
	}
	
	@PostMapping("/BuscarProducto")
	public String BuscarProducto(@RequestParam String nombre, Model model ) {
		
		log.info("nombre del producto: {}",nombre);
		
		List<Producto> productos= productoservice.findAll().stream().filter(p->p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		
		return "usuario/home";
	}
	
}
