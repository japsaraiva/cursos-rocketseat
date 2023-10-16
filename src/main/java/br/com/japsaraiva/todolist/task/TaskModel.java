package br.com.japsaraiva.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;


import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * Definindo a tarefa:
 * ID
 * Usuario (user_id)
 * Descricao
 * Titulo
 * Data de Inicio
 * Data de Término
 * Prioridade
 */

 @Data
 @Entity(name =  "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50) // Limitar o tamanho do campo
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser; //para associar a tarefa criada ao usuário

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    public void setTitle(String title) throws Exception{ //throws exception, passa a exceção para a camada acima no caso quem chamar o setTitle
        if(title.length() > 50){
            throw new Exception("O campo title deve conter no máximo 50 caracteres"); //exceção
        }
        this.title = title;
    }
}
