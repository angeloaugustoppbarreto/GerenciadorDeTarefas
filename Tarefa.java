package com.example.tarefas.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity
@Table(name="tarefas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; //valor unico
    @Column(nullable=false,length=200)
    private String titulo;
    @Column(length=1000)
    private String descricao;
    @Column(nullable=false)
    @Builder.Default
    private boolean concluida=false;
    @CreationTimestamp
    @Column(name="Criado em",updatable=false)
    private LocalDateTime criadoEm;
}//modelo para cada tarefa criada, cada tarefa criada será guardada em uma lista(repository);


