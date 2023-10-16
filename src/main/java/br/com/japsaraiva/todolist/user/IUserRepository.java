package br.com.japsaraiva.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepository extends JpaRepository<UserModel, UUID>{ //interface: contrato dentro da API, apenas representação de métodos | <> representa que a classe recebe um generator ("atributos dinâmicos")
    UserModel findByUsername(String username); // método de busca de username existente no BD, retorna um tipo UserModel
}
