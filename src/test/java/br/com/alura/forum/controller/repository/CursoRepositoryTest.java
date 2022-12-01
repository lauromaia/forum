package br.com.alura.forum.controller.repository;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.modelo.Curso;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class CursoRepositoryTest {
	
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@Autowired
	private EntityManager em;
	
	@Test
	public void deveriaCarregarCursoAoBuscarPeloSeuNome() {
		String nomeCurso = "HTML 5";
		Curso html5 = new Curso();
		html5.setNome(nomeCurso);
		html5.setCategoria("Programação");
		em.persist(html5);
		
		
		Curso curso = cursoRepository.findByNome(nomeCurso);
		Assertions.assertNotNull(curso);
		Assertions.assertEquals(nomeCurso, curso.getNome());
	}
	
	@Test
	public void deveriaRetornarNulo() {
		String nomeCurso = "Não Existe";
		Curso curso = cursoRepository.findByNome(nomeCurso);
		Assertions.assertNull(curso);
	}

}
