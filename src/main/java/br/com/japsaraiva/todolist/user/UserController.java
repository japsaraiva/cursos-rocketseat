package br.com.japsaraiva.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

//Springboot funciona recursivamente, a pasta controller deve estar dentro da ultima pasta (mesma pasta da API principal) da estrutura

//Controller é um componente que se localiza entre a requisição do usuário e as demais camadas (Negócios/Banco de dados), controla o que o usuário requisita (Post/get/etc..)
//É possivel criar de 2 formas:
//@Controller (permite flexibilidade a estruturas que retornam páginas/templates)
//@RestController (ex. de uso: APIs, aplicações, usado em conceitos de arquitetura REST)

@RestController
// @RestController cria uma rota para o browser (http://localhost:8080/.... <-users)
@RequestMapping("/users")

public class UserController { 
    /*Representação de Classe
        Uma classe é um molde do qual podemos criar objetos. Definimos na classe o comportamento e os estados que terá o objeto do seu tipo. 
        A classe pode e geralmente representa coisas do mundo real, como por exemplo, um carro.
        Por exemplo, uma montadora de carros cria um novo modelo, então ele é reproduzido em série. 
        Na orientação a objetos, a classe é o modelo que o projetista do carro criou. 
        As unidades do carro produzidas na fábrica são os objetos, pois seguem as especificações do modelo. 
        As características do carro são os tipos de estado daquele objeto. 
        A cor, combustível e outras características podem variar de carro para carro, mesmo sendo do mesmo modelo. 
        Na classe esses tipos de estado são definidos pelas variáveis. O comportamento do carro são suas ações, como: acelerar, frear, trocar macha, etc. 
        O comportamento da classe é definido pelos seus métodos. 
        Então por serem considerados os principais componentes de uma classe, as variáveis e métodos são conhecidos como membros da classe.

        O nome da classe respeita a uma convenção, sempre mantendo o padrão ao criar classe, ex: UserController "Primeiro caractere do substantivo deve ser maiusculo"
    */

    /* Métodos de requisição HTTP
     * GET - Buscar uma informação
     * POST - Adicionar um dado/informação
     * PUT - Alterar um dado/info
     * DELETE - Remove um dado
     * PATCH - Alterar somente uma parte da info/dado.
     */
    //@GetMapping("/primeiroMetodo")

    @Autowired //instância todos os atributos da JpaRepository pois IUserRepository herda os atributos da JpaRepository com a anotação extends (conceito de herança)
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
         /*
         * parâmetros usermodel sera requisitado dentro do body com o @ResquestBody
         */

         /*public void create() <-método| public = refere-se ao 'modificador de acesso' | String = tipo | FirstMessage = nome do método | () = parâmetros
            Níveis de acesso são conhecidos por public, private, protected e default. 
            E os modificadores são apenas três: public, private, protected. 
            O nível de acesso default (padrão) não exige modificador. 
            Quando não se declara nenhum modificador, o nível default é implícito.

            Public: Com este modificador, o acesso é livre em qualquer lugar do programa.
            Private: Com este modificador, o acesso é permitido somente dentro da classe onde ele foi declarado. Por padrão, é a visibilidade definida para métodos e atributos em uma classe.
            Protected: Com este modificador, apenas a classe que contém o modificador e os tipos derivados dessa classe tem o acesso.
            Internal: Com este modificador, o acesso é limitado apenas ao assembly atual.
            Protected Internal: Com este modificador, o acesso é limitado ao assembly atual e aos tipos derivados da classe que contém o modificador

            Tipos comuns:
            String (texto)
            Integer (int) numeros inteiros
            Double (double) numeros 0.0000 casas
            Float (float) numeros 0.000 casas 'ele arredonda o valor'
            char (1 caractere)
            Date (data)
            void (sem retorno/vazio)
        */
     
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null) {
            //Temos que retornar um:
            //Mensagem de Erro
            //Status Code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário Existente"); //Http.Status é uma library que já traz definido através de atributos
                                                                                            //os valores para cada tipo de erro caso você não souber.
        }

        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray()); // cria o valor da senha criptografada com a biblioteca bcrypt
                                                                                                             // 12 refere-se ao valor padrão na documentação

        userModel.setPassword(passwordHashred); //seta o valor da senha com a nova senha mascarada com a criptografia, dessa forma a senha não fica exposta ao puxar os valores do BD

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
            
    }

       

}
