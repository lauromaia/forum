package br.com.alura.forum.controller.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.forum.modelo.StatusTopico;
import br.com.alura.forum.modelo.Topico;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
public Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
public Optional findById(Long id);


@Query("SELECT t FROM Topico t WHERE t.status = :status")
public List<Topico> findByStatusTopico(@Param("status") StatusTopico status);
	
}
