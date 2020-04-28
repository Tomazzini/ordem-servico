package com.algaworks.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.osworks.api.model.Comentario;
import com.algaworks.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.osworks.domain.exception.NegocioException;
import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.model.FinalizarOrdemServico;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.model.StatusOrdemServico;
import com.algaworks.osworks.domain.repository.ClienteRepository;
import com.algaworks.osworks.domain.repository.ComentarioRepository;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
		
	public OrdemServico criar(OrdemServico ordemServico) {
		
		// carrega os dados de cliente
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("cliente não encontrado"));
			
		// seta o cliente
		ordemServico.setCliente(cliente);
		// status inicial
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	// metodo de inserir comentario
	public Comentario adicioanarComentario(Long ordemServicoId, String descricao) {
		OrdemServico ordemServico = buscar(ordemServicoId); 
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		
		return comentarioRepository.save(comentario);
		
	}
	
	// finalizar uma ordem de serviço
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
			
		if (naoPodeSerFinalizada()){
			throw new NegocioException("ordem de serviço não pode ser finalizada");
		}
		
		ordemServico.setStatus(StatusOrdemServico.FINALIZADA);
		ordemServico.setDataFinalizacao(OffsetDateTime.now());
				
		ordemServicoRepository.save(ordemServico);
	}
	
	// cancelar uma ordem de serviço
	public void cancelar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);

		if (naoPodeSerCancelada()){
			throw new NegocioException("ordem de serviço não pode ser cancelada");
		}
		
		ordemServico.setStatus(StatusOrdemServico.CANCELADA);
		ordemServico.setDataFinalizacao(OffsetDateTime.now());
				
		ordemServicoRepository.save(ordemServico);
			
	}
	
	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("ordem de serviço não encontrada"));
	}
		
	public boolean podeSerFinalizada() {
		OrdemServico ordemServico = new OrdemServico();
		return StatusOrdemServico.ABERTA.equals(ordemServico.getStatus());
	}
	
	public boolean naoPodeSerFinalizada() {
			return !podeSerFinalizada();
	}
	
	public boolean podeSerCancelada() {
		OrdemServico ordemServico = new OrdemServico();
		return StatusOrdemServico.FINALIZADA.equals(ordemServico.getStatus());
	}
	
	public boolean naoPodeSerCancelada() {
		return !podeSerCancelada();
	}
	
}
