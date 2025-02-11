package com.example.demo.futebol.Estadio;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

@Repository
public class EstadioRepository implements EstadioDao {

    @PersistenceContext
    private EntityManager em;
    private static final Logger LOGGER = LoggerFactory.getLogger(EstadioRepository.class.getName());

    @Override
    public void save(Estadio estadio) {
        try {
            em.persist(estadio);
        } catch (PersistenceException e) {
            LOGGER.error("Erro de persistência: não foi possível criar estádio", e);
            throw e;  // Relança a exceção para que o Spring possa tratá-la corretamente
        }
    }


    @Override
    public void delete(Estadio estadio) {
        try{
            em.remove(estadio);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível deletar estádio", e.getMessage());
        }
    }

    @Override
    public void update(Estadio estadio) {
        try{
            em.merge(estadio);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível atualizar estádio", e.getMessage());
        }
    }

    @Override
    public Optional<Estadio> findById(Integer id) {
        String jpql = "SELECT estadio FROM Estadio estadio WHERE estadio.id = :id_estadio";

        TypedQuery<Estadio> query = em.createQuery(jpql, Estadio.class);
        query.setParameter("id_estadio", id);

        try{
            Estadio estadio = query.getSingleResult();
            return Optional.of(estadio);
        }catch(Exception e){
            LOGGER.error("Erro de persistência: não foi possível recuperar o estádio com id {} => {}", id, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Estadio> findByName(String nome) {
        String jpql = "SELECT estadio FROM Estadio estadio WHERE estadio.nome = :nome_estadio";

        TypedQuery<Estadio> query = em.createQuery(jpql, Estadio.class);
        query.setParameter("nome_estadio", nome);

        try{
            Estadio estadio = query.getSingleResult();
            return Optional.of(estadio);
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível recuperar o estádio com o nome {} => {}", nome, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Estadio> findAll() {
        String jpql = "SELECT estadio FROM Estadio estadio";
        TypedQuery<Estadio> query = em.createQuery(jpql, Estadio.class);

        try{
            return query.getResultList();
        }catch(PersistenceException e){
            LOGGER.error("Erro de persistência: não foi possível recuperar todos os estádios => {}", e.getMessage());
        }

        return Collections.emptyList();
    }

}
