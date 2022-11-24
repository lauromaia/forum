package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@Cacheable(value = "listaTopicos")
	public Page<TopicoDto> topicos(@RequestParam(required = false) String nomeCurso, 
		@PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable paginacao){
		if(nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);	
		}
		else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
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
	@CacheEvict(value = "listaTopicos", allEntries = true)
	public ResponseEntity<DetalhesTopicoDto> detalhes(@PathVariable Long id){
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()){
			
			return ResponseEntity.ok(new DetalhesTopicoDto((Topico) topico.get()));
			
		}else {
			return ResponseEntity.notFound().build();
			
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
