package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;
import com.algaworks.osworks.domain.service.CadastroClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CadastroClienteService cadastroCliente;
	
	// metodo de listar todos os clientes
	@GetMapping
	public List<Cliente> listar(){
		
		// retorna todos os clientes
		return clienteRepository.findAll();
		
		// busca por nome
		//return clienteRepository.findByNome("jo");
		
		// busca por like
		//return clienteRepository.findByNomeContaining("m");
	}
	
	// retorna apenas um cliente de acordo com id
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		// se encontrou cliente http status = 200 se não 404
		if( cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	// metodo de inserir
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return cadastroCliente.salvar(cliente);
	}
	
	// metodo de atualizar
	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long clienteId,
			@RequestBody Cliente cliente){
		
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		// seta o id para atualizar
		cliente.setId(clienteId);
		cliente = cadastroCliente.salvar(cliente);
		
		return ResponseEntity.ok(cliente);
	}
	
	// metodo de excluir
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> deletar(@PathVariable Long clienteId){
		
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroCliente.excluir(clienteId);
		
		return ResponseEntity.noContent().build();
		
		
	}		
}

