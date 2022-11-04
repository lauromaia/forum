package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.repository.TopicoRepository;
import br.com.alura.forum.modelo.Topico;


@RestController
public class TopicoController {

	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@RequestMapping("/topicos")
	public List<TopicoDto> topicos(){
		
		List<Topico> topicos = topicoRepository.findAll();
		return TopicoDto.converter(topicos);
	}
	
}
