package br.com.japsaraiva.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;


//o modulo spring data jpa é uma camada de acesso da aplicação ao banco de dados que utiliza o conceito orm (mapa relacional de objeto)
// para que o BD consiga entender os dados enviados, "traduz o java para a linguagem SQL".
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data //Anotação da ~ library lombok = @Data adiciona os metodos getters e setters à todas as variáveis(nota-se que a anotação deve ser declarado acima da classe)
      //as dependencias devem ser instaladas no arquivo pom.xml  
      //@Getter, @Setter caso queira apenas 1 dos métodos(declarado acima da variavel como no ex abaixo:)
@Entity(name = "tb_users")
      //~ jpa module 
      //Uma entidade é uma representação de um conjunto de informações sobre determinado conceito do sistema. 
      //Toda entidade possui ATRIBUTOS, que são as informações que referenciam a entidade.
      //Ex. de Entidades: Usuário, Livro, Empréstimo
      //Para identificar se aquele conceito pode ser uma entidade você deve apenas se perguntar: "Eu desejo armazenar quais informações sobre este conceito ?
      //" Se houverem informações a serem armazenadas, você tem uma ENTIDADE. 
      //Exemplificando: Eu desejo armazenar os seguintes dados do livro: Título, Autor, Editora, Ano, Edição e Volume. Temos então a entidade Livro.
public class UserModel {

    @Id // Anotação para reconhecer como um id no banco de dados ~ jpa module 
    @GeneratedValue(generator = "UUID") // para gerar os valores do UUID ~ jpa module 
    private UUID id;
    
    //@Column(name = "usuario") para mapear no banco de dados caso não houvesse os atributos definidos abaixo
    @Column(unique = true) // define valores únicos evitando cadastro duplo
    //@Getter
    //@Setter
    private String username;
    //@Getter
    private String name;
    //@Setter
    private String password;

    @CreationTimestamp // ao criar os valores no BD já atribui data/horario do momento criado
    private LocalDateTime createdAt; //por padrão sem configurar em application.properties, o BD entende que em 'createdAt' o maiusculo em 'At' representa um espaço
                                     //dessa forma ao criar a tabela no BD irá aparecer como CREATED_AT com o '_' representando um espaçamento.
    

    /*
     * Métodos getters (puxa o valor) e setters (seta o valor) servem para alterar os atributos acima
     */

    /*public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }*/

    
    
}
