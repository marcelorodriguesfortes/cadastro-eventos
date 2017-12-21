package com.eventosapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventosapp.models.Evento;

/*A partir desta classe é possível utilizar métodos já prontos, como 
 * salvar, deletar, inserir, entre outros.
 * */

/*Ao importar a classe CrudRepository, é necessário passar a classe 
 * anotada como sendo uma entidade
 * */

public interface EventosRepository extends CrudRepository<Evento, String>{ 
	Evento findByCodigo(long codigo);
}
