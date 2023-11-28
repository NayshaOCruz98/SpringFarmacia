package com.db.farmacia.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.farmacia.model.Orden;
import com.db.farmacia.model.Usuario;
import com.db.farmacia.service.IOrdenService;
import com.db.farmacia.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger= LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioservice;
	
	@Autowired
	private IOrdenService ordenservice;
	
	BCryptPasswordEncoder passEncode=new BCryptPasswordEncoder();
	
	// /usuario/registro
	@GetMapping("/registro")
	public String crear() {
		return "usuario/registro";
	}
	
	@PostMapping("/GuardarUsuario")
	public String GuardarUsuario(Usuario usuario) {
		
		logger.info("Usuario registro : {}",usuario);
		usuario.setTipo("USER");
		usuario.setPassword(passEncode.encode(usuario.getPassword()));
		usuarioservice.GuardarUsuario(usuario);
		
		
		return "redirect:/";
		
	}
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@GetMapping("/ingresar")
	public String Ingresar(Usuario usuario,HttpSession session) {
		
		logger.info("accesos : {}",usuario);
		
		Optional<Usuario> user=usuarioservice.findByid((Integer)(session.getAttribute("idusuario")));

		//logger.info("usuario de db: {}",user.get());
		
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}
			else {
				return "redirect:/";
			}
			
		}else {
			logger.info("usuario no exite ");
		}
		return "redirect:/";
		
	}
	@GetMapping("/compras")
	public String compras(Model model,HttpSession session) {
		
		model.addAttribute("session", session.getAttribute("idusuario"));
		Usuario usuario = usuarioservice.findByid((Integer)(session.getAttribute("idusuario"))).get();
		List<Orden> ordenes= ordenservice.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id,HttpSession session,Model model) {
		logger.info("id de la orden :  {}",id);
		Optional<Orden> orden= ordenservice.FindById(id);
		
		model.addAttribute("detalles", orden.get().getDetalle());
		
		//session
		model.addAttribute("session", session.getAttribute("idusuario"));
		
		return "usuario/detallecompra";
	}
	@GetMapping("/cerrarSesion")
	public String cerrarSesion(HttpSession session) {
		
		session.removeAttribute("idusuario");
		
		return "redirect:/";
	}
}
