package com.Bank.App.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Bank.App.exceptions.ResourceNotFoundException;
import com.Bank.App.model.Mensaje;
import com.Bank.App.repository.MensajesRepositorio;

@RestController
@RequestMapping("/bankapp/v3")
@CrossOrigin(origins = "http://localhost:4200")
public class MensajeControlador {

	@Autowired
	private MensajesRepositorio repositorio;

	@GetMapping("/mensajes")
	public List<Mensaje> mostrarTodosLosMensajes() {
		return repositorio.findAll();
	}

	@PostMapping("/mensajes")
	public Mensaje guardarMensajes(@RequestBody Mensaje mensaje) {
		mensaje.setFecha(LocalDateTime.now());
		return repositorio.save(mensaje);
	}

	@GetMapping("/mensajes/{id}")
	public ResponseEntity<Mensaje> obtenerMensajePorId(@PathVariable int id) {
		Mensaje mensaje = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el mensaje con ID: " + id));

		return ResponseEntity.ok(mensaje);
	}

	@PutMapping("/mensajes/{id}")
	public ResponseEntity<Mensaje> actualizarMensaje(@PathVariable int id, @RequestBody Mensaje detallesMensaje) {
		Mensaje mensaje = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el mensaje con ID: " + id));

               // La ID del mensaje debe coincidir con la indicada en la URL
               // para evitar que se modifique accidentalmente mediante el cuerpo
               mensaje.setId(id);
		mensaje.setId_origen(detallesMensaje.getId_origen());
		mensaje.setId_destino(detallesMensaje.getId_destino());
		mensaje.setTexto(detallesMensaje.getTexto());
		mensaje.setFecha(LocalDateTime.now());

		Mensaje mensajeActualizado = repositorio.save(mensaje);
		return ResponseEntity.ok(mensajeActualizado);
	}

	@DeleteMapping("/mensajes/{id}")
	public ResponseEntity<Map<String, Boolean>> eliminarMensaje(@PathVariable int id) {
		Mensaje mensaje = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el mensaje con ID: " + id));

		repositorio.delete(mensaje);
		Map<String, Boolean> respuesta = new HashMap<>();
		respuesta.put("eliminar", Boolean.TRUE);
		return ResponseEntity.ok(respuesta);
	}
}
