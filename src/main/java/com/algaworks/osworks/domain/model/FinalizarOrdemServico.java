package com.algaworks.osworks.domain.model;

import java.time.OffsetDateTime;

import com.algaworks.osworks.domain.exception.NegocioException;

public class FinalizarOrdemServico {

	static OrdemServico ordemServico = new OrdemServico();
	
	public static boolean podeSerFinalizada() {
		return StatusOrdemServico.ABERTA.equals(ordemServico.getStatus());
	}
	
	public boolean naoPodeSerFinalizada() {
			return !podeSerFinalizada();
	}
	
	public static void finalizar() {
		if(podeSerFinalizada()) {
			throw new NegocioException("ordem de serviço não pode ser finalizada");
		}
		
		ordemServico.setStatus(StatusOrdemServico.FINALIZADA);
		ordemServico.setDataFinalizacao(OffsetDateTime.now());
		
	}
}
