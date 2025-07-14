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
import com.Bank.App.model.Transferencia;
import com.Bank.App.repository.TransferenciasRepositorio;

@RestController
@RequestMapping("/bankapp/v5")
@CrossOrigin(origins = "http://localhost:4200")
public class TransferenciaControlador {

	@Autowired
	private TransferenciasRepositorio repositorio;
	
	@GetMapping("/transferencias")
	public List<Transferencia> mostrarTodasLasTransferencias() {
		return repositorio.findAll();
	}
	
	@PostMapping("/transferencias")
	public Transferencia guardarTransferencias(@RequestBody Transferencia transferencia) {
		transferencia.setFecha(LocalDateTime.now());
		return repositorio.save(transferencia);
	}

	@GetMapping("/transferencias/{id}")
	public ResponseEntity<Transferencia> obtenerTransferenciaPorId(@PathVariable int id) {
		Transferencia transferencia = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la transferencia con ID: " + id));
		return ResponseEntity.ok(transferencia);
	}

	@PutMapping("/transferencias/{id}")
	public ResponseEntity<Transferencia> actualizarTransferencia(@PathVariable int id, @RequestBody Transferencia detallesTransferencia) {
		Transferencia transferencia = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la transferencia con ID: " + id));

               // La ID de la transferencia debe mantenerse igual que la indicada en la URL
               // para evitar inconsistencias si el cuerpo de la petición contiene un valor diferente
               transferencia.setId(id);
		transferencia.setId_ordenante(detallesTransferencia.getId_ordenante());
		transferencia.setId_beneficiario(detallesTransferencia.getId_beneficiario());
		transferencia.setImporte(detallesTransferencia.getImporte());
		transferencia.setConcepto(detallesTransferencia.getConcepto());
		transferencia.setFecha(LocalDateTime.now());

		Transferencia transferenciaActualizada = repositorio.save(transferencia);
		return ResponseEntity.ok(transferenciaActualizada);
	}

	@DeleteMapping("/transferencias/{id}")
	public ResponseEntity<Map<String, Boolean>> eliminarTransferencia(@PathVariable int id) {
		Transferencia transferencia = repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la transferencia con ID: " + id));

		repositorio.delete(transferencia);
		Map<String, Boolean> respuesta = new HashMap<>();
		respuesta.put("eliminar", Boolean.TRUE);
		return ResponseEntity.ok(respuesta);
	}

}
