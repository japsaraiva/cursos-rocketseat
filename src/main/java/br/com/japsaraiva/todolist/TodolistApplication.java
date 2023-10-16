package br.com.japsaraiva.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args); //roda a aplicação (o que está na classe) no tomcat (container web) na porta 8080
	}

}
