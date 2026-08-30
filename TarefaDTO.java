package com.example.tarefas.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDTO{
    private Long id;private
    String titulo;
    private String descricao;
    private Boolean concluida;
    private String criadoEm;
}