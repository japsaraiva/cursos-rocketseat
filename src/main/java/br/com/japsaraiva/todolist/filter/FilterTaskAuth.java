//O código em questão irá delimitar o acesso ao banco por meio de uma autenticação, após verificada é permitida o uso dos métodos de acesso (GET/POST/etc...).

package br.com.japsaraiva.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.japsaraiva.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Servlet é uma classe Java, que consegue gerar páginas dinâmicas para a camada de apresentação de aplicações web. 
//O principal objetivo é receber chamadas HTTP, sendo processada e devolvida uma resposta para o cliente.
//Os servlets trabalham juntamente com a tecnologia Java Server Pages (JSP).
// é a base da maioria dos frameworks

@Component //Anotação para que o Spring Boot gerencie a classe abaixo
public class FilterTaskAuth extends OncePerRequestFilter {


    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();

                if(servletPath.startsWith("/tasks/")) { //Faz a verificação de rota / startsWith 'começa com o prefixo /tasks/' para poder ler os ids das tasks criadas subsequentemente

                    //Pegar a autenticação (usuario e senha)
                    var authorization = request.getHeader("Authorization");

                    var authEncoded = authorization.substring("Basic".length()).trim(); //remove a palavra basic calculando o tamanho da palavra, o .trim faz o corte removendo os espaços em branco.
                    
                    byte[] authDecode = Base64.getDecoder().decode(authEncoded); //pega o valor da autenticação que vem codificada em basic e transforma em um array de bytes.
                    
                    var authString = new String(authDecode); //pega o array e transforma em String.


                    String[] credentials = authString.split(":");
                    // usa do regex para pegar o conteudo no caso: 'usuario:senha' e separar os 2 valores definindo o ':' como o meio e jogando os 2 valores em um array de string.
                    // assim como os valores são jogados na ordem do array a posição 0 guarda o usuario e 1 a senha.
                    String username = credentials[0];
                    String password = credentials[1];

                    //Validar usuário
                    var user = this.userRepository.findByUsername(username); //valida se o usuario existe no BD para depois validar a senha
                    if(user == null){
                        response.sendError(401);
                    } else {
                        //Validar senha
                        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()); // compara a senha digitada com a registrada no DB
                        if(passwordVerify.verified)  { //verificaçao booleana true ou false
                            request.setAttribute("idUser", user.getId()); //passa o valor do Iduser registrado no BD para a requisição de autenticação,
                            filterChain.doFilter(request, response);      //evitando que o método de POST passe o valor do idUser e registre a tarefa.
                        } else {
                            response.sendError(401);
                        }
                         
                    //Segue viagem
                    }
                }else{
                    filterChain.doFilter(request, response);
                }
    }

}
