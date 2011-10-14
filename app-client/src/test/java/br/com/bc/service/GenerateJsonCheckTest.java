package br.com.bc.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import br.com.bc.service.ConsultarCep;

public class GenerateJsonCheckTest {

	
	@Test public void geraJSON() throws IOException {
		               
		ConsultarCep servicoConsultaCepJson = new ConsultarCep();
		String servicoConsultaCep = servicoConsultaCepJson.geraJson();
		System.out.println("ServicoConsultaCep:");
		System.out.println(servicoConsultaCep);
		System.out.println("");
		assertEquals("{\"response\":\"\",\"classes\":[{\"source\":\"\",\"name\":\"br.com.bc.service.ServicoConsultaCep\",\"data\":[]}],\"request\":\"\",\"name\":\"ServicoConsultaCep\"}", servicoConsultaCep);
	
	}
}
