package com.example.tarefas.controller;
import com.example.tarefas.dto.TarefaDTO;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.repository.TarefaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/tarefas")//começo da api
@CrossOrigin(origins="*")
@Tag(name="Tarefa",description="API de gerenciamentos de tarefas")
public class TarefaController{
    @Autowired
    private TarefaRepository repository;//chamo lista
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    //1.listar todas
    @GetMapping
    @Operation(summary="Lista de todas as tarefas")
    public ResponseEntity<List<TarefaDTO>>listarTodas(){
        List<Tarefa> tarefas = repository.findAll();
        List<TarefaDTO> dtos = tarefas.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    //2.buscar por id
    @GetMapping("/{id}")
    @Operation(summary="Buscar uma tarefa por ID")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable long id){
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Tarefa não encontrada"));
        return ResponseEntity.ok(toDTO(tarefa));
    }
    //3.criar tarefa
    @PostMapping
    @Operation(summary="Criar nova tarefa")
    public ResponseEntity<TarefaDTO> criar(@RequestBody TarefaDTO dto){
        Tarefa tarefa = Tarefa.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .concluida(false)
                .build();
        Tarefa salva = repository.save(tarefa);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(toDTO(salva));
    }
    //4.atualizar
    @PutMapping("/{id}")
    @Operation(summary="Atualiza uma tarefa existente")
    public ResponseEntity<TarefaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody TarefaDTO dto){
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        if(dto.getTitulo() != null) tarefa.setTitulo(dto.getTitulo());
        if(dto.getDescricao()!=null) tarefa.setDescricao(dto.getDescricao());
        if(dto.getConcluida() != null) tarefa.setConcluida(dto.getConcluida());
        Tarefa atualizada = repository.save(tarefa);
        return ResponseEntity.ok(toDTO(atualizada));
    }
    //5.alterar status (concluir/reabrir)
    @PatchMapping("/{id}/concluir")
    @Operation(summary="Alterar status de conclusão de tarefa")
    public ResponseEntity<TarefaDTO> alternarStatus(@PathVariable Long id){
        Tarefa tarefa = repository.findById(id)//se existir id prossegue
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));//se não, função não executada
        tarefa.setConcluida(!tarefa.isConcluida());
        Tarefa atualizada = repository.save(tarefa);
        return ResponseEntity.ok(toDTO(atualizada));
    }
    //6.deletar
    @DeleteMapping("/{id}")
    @Operation(summary="Deletar tarefa")
    public ResponseEntity<Void>deletar(@PathVariable Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Tarefa não encontrada");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    //7.deletar todas as concluidas
    @DeleteMapping("/concluidas")
    @Operation(summary="deleta todas as tarefas concluidas")
    public ResponseEntity<Void>deletarConcluidas(){
        List<Tarefa> concluidas = repository.findByConcluida(true);
        repository.deleteAll(concluidas);
        return ResponseEntity.noContent().build();
    }
    //metodo auxiliar: converter para dto
    private TarefaDTO toDTO(Tarefa tarefa){
        return TarefaDTO.builder()
                .id(tarefa.getId())
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .concluida(tarefa.isConcluida())
                .criadoEm(tarefa.getCriadoEm()!=null ?
                        tarefa.getCriadoEm().format(FORMATTER):null)
                .build();
    }
}