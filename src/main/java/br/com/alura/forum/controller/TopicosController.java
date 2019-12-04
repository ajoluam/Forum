package br.com.alura.forum.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	public List<TopicoDto> lista(){
		List<Topico> listTopics= topicoRepository.findAll();		
		return TopicoDto.converter(listTopics);
	}
	
	@GetMapping("/codigo/{id}")
	public List<TopicoDto> listaPorId(@PathVariable Long id){
		Optional<Topico> topico= topicoRepository.findById(id);
		List<Topico> listTopics= new ArrayList<>();
		listTopics.add(topico.get());
		return TopicoDto.converter(listTopics);
	}
	
	@GetMapping("/nomeCurso/{nomeCurso}")
	public List<TopicoDto> listaPorNomeCurso(@PathVariable String nomeCurso){
		List<Topico> listTopics= topicoRepository.findByCursoNome(nomeCurso);
		return TopicoDto.converter(listTopics);
	}
	
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/codigo/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	
	
	
}
