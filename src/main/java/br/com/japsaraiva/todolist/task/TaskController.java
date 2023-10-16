package br.com.japsaraiva.todolist.task;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.japsaraiva.todolist.utils.Utils;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser"); //recupera o iduser repassado e joga na variavel idUser
        taskModel.setIdUser((UUID) idUser); //seta o UUID em taskmodel com o valor repassado para a variavel idUser

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt()) ||  currentDate.isAfter(taskModel.getEndAt())) { //*Regra de negócio* | proibir de escolher uma data que já passou 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início/término deve ser maior que a data atual");
        } 
        
        if(currentDate.isAfter(taskModel.getStartAt()) ||  currentDate.isAfter(taskModel.getEndAt())) { //*Regra de negócio* | proibir de escolher uma data que já passou 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início/término deve ser maior que a data atual");
        }   

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);        

    }
    
    @GetMapping("/") //p/ retornar tudo que for do usuario
    public List<TaskModel> list(HttpServletRequest request) { //listar as tarefas cadastradas ao usuário informado (GET authbasic no APIDOG)
            var idUser = request.getAttribute("idUser");
            var tasks = this.taskRepository.findByIdUser((UUID) idUser);
            return tasks;
    }

    //http://localhost:8080/tasks/9feb1c41-403a-4ce3-85e3-6190e2f44f02 -> /tasks/... o que vem depois de tasks é chamada de caminho(PATH) variavel, 
    //seria o nome do caminho para a task criada anteriormente sendo o nome o ID da task
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

        var task = this.taskRepository.findById(id).orElse(null); //orElse para retornar até nulo caso houver

        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST) //Verifica se o id da task informada existe
                .body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!task.getIdUser().equals(idUser)){ //verifica se o id do usuario que foi autenticado é o mesmo atribuido a tarefa 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST) //(até então qualquer login que existisse cadastro no BD poderia alterar qualquer tarefa)   
                .body("O Usuário autenticado não tem permissão para alterar essa tarefa");

        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
