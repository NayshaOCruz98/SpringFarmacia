package com.db.farmacia.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.db.farmacia.model.Producto;
import com.db.farmacia.model.Usuario;
import com.db.farmacia.service.IProductoService;
import com.db.farmacia.service.IUsuarioService;
import com.db.farmacia.service.UploadFileService;



@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private IProductoService productoservice;
	
	@Autowired
	private IUsuarioService usuarioservice;
	
	@Autowired
	private UploadFileService upload;
	
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoservice.findAll());
		
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/GuardarProducto")
	public String GuardarProducto(Producto producto, @RequestParam("img") MultipartFile file,HttpSession session) throws IOException {
		LOGGER.info("Este es el objeto producto {}",producto);
		
		Usuario u= usuarioservice.findByid((Integer)(session.getAttribute("idusuario"))).get();
		
		producto.setUsuario(u);
		
		//imagen
		if(producto.getId()==null) {//cuando se crea un producto
		  String nombreImagen= upload.GuardarImage(file);
		  producto.setImagen(nombreImagen);
		
		}
		
		productoservice.GuardarProducto(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id,Model model) {
		Producto producto= new Producto();
		
		Optional<Producto> optionalProducto=productoservice.BuscarProducto(id);
		producto = optionalProducto.get();
		
		LOGGER.info("Producto buscado: {}", producto);
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	
	@PostMapping("/ActualizarProducto")
	public String ActualizarProducto(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		
		Producto p = new Producto();
		p=productoservice.BuscarProducto(producto.getId()).get();
		
		if(file.isEmpty()) {// cuando editamos el producto pero no cambiamos la imagen
			
		    producto.setImagen(p.getImagen());
		}else {
			
			//eliminar para cuando no sea la imagen por defecto
			if (!p.getImagen().equals("default.jpg")) {
				upload.EliminarImage(p.getNombre());
				
			}
			producto.setUsuario(p.getUsuario());
			String nombreImagen= upload.GuardarImage(file);
			  producto.setImagen(nombreImagen);
			
		}
		
		productoservice.ActualizarProducto(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String EliminarProducto(@PathVariable Integer id) {
		
		Producto p= new Producto();
		p=productoservice.BuscarProducto(id).get();
		
		//eliminar para cuando no sea la imagen por defecto
		if (!p.getImagen().equals("default.jpg")) {
			upload.EliminarImage(p.getNombre());
			
		}
		
		productoservice.EliminarProducto(id);
		return "redirect:/productos";
	}
	
	
}
