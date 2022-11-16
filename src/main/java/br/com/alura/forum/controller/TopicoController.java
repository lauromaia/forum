package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizaTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.controller.repository.CursoRepository;
import br.com.alura.forum.controller.repository.TopicoRepository;
import br.com.alura.forum.modelo.StatusTopico;
import br.com.alura.forum.modelo.Topico;


@RestController
@RequestMapping("/topicos")
public class TopicoController {

	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDto> topicos(String nomeCurso){
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);	
		}
		else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
	}
	
	@RequestMapping("/topicos/status")
	public List<Topico> topicosStatus(StatusTopico status){
		if(status == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return topicos;
		}
		else{
			List<Topico> topicos = topicoRepository.findByStatusTopico(status);
			return topicos;
		}
	}
	
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = UriComponentsBuilder.fromPath("topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")	
	public ResponseEntity<DetalhesTopicoDto> detalhes(@PathVariable Long id){
		Topico topico = topicoRepository.getReferenceById(id);
		if(topico != null){
			
			return ResponseEntity.notFound().build();
			
		}else {
			
			return ResponseEntity.ok(new DetalhesTopicoDto((Topico) topico));
		}
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm form) {
		Topico topico = form.atualiza(id, topicoRepository);
		return ResponseEntity.ok(new TopicoDto(topico));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletar(@PathVariable Long id){
		Topico topico = topicoRepository.getReferenceById(id);
		topicoRepository.delete(topico);
		return ResponseEntity.ok().build();
	}
}
