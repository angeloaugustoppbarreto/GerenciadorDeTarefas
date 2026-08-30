package com.example.tarefas.repository;
import com.example.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa,Long>{
    List<Tarefa> findByConcluida(boolean concluida);
    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);
}