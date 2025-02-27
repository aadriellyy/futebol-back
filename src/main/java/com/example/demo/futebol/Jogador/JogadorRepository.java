package com.example.demo.futebol.Jogador;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;


@Repository
public class JogadorRepository implements JogadorDao{


    @PersistenceContext
    private EntityManager em;
    private static final Logger LOGGER = LoggerFactory.getLogger(JogadorRepository.class.getName());

    @Override
    public void save(Jogador jogador) {
        try{
            em.persist(jogador);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível criar jogador", e.getMessage());
        }
    }

    @Override
    public void delete(Jogador jogador) {
        try{
            em.remove(jogador);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível deletar jogador", e.getMessage());
        }
    }

    @Override
    public void update(Jogador jogador) {
        try{
            em.merge(jogador);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível atualizar jogador", e.getMessage());
        }
    }

    @Override
    public Optional<Jogador> findById(Integer id) {
        String jpql = "SELECT jogador FROM Jogador jogador where jogador.id =: id_jogador";

        TypedQuery<Jogador> query = em.createQuery(jpql, Jogador.class);
        query.setParameter("id_jogador", id);

        try{
            Jogador jogador = query.getSingleResult();
            return Optional.of(jogador);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível recuperar jogador com o id {} => {}", id, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Jogador> findByName(String nome) {
        String jpql = "SELECT jogador FROM Jogador jogador where jogador.name =: nome_jogador";

        TypedQuery<Jogador> query = em.createQuery(jpql, Jogador.class);
        query.setParameter("nome_jogador", nome);

        try{
            Jogador jogador = query.getSingleResult();
            return Optional.of(jogador);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível recuperar jogador com o nome {} => {}", nome, e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public List<Jogador> findAll() {
        String jpql = "SELECT jogador FROM Jogador jogador";

        TypedQuery<Jogador> query = em.createQuery(jpql, Jogador.class);

        try{
            return query.getResultList();
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível recuperar jogadores => {}", e.getMessage());
        }

        return Collections.emptyList();
    }

}
