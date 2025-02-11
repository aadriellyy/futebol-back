package com.example.demo.futebol.Estadio;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EstadioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadioService.class);

    private EstadioDao repository;

    public EstadioService(EstadioDao repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Estadio> findAll() {
        try{
            LOGGER.info("Buscando todos Estadio (service).");
            List<Estadio> list = this.repository.findAll();
            LOGGER.info("Tamanho da lista recuperada (service): {}", list.size());
            return list;
        }catch (Exception e){
            LOGGER.error("Erro ao buscar todos Estadio (service).", e);
            throw e;
        }
    }

    @Transactional
    public Optional<Estadio> findById(Integer id) {
        try{
            LOGGER.info("Buscando Estadio com ID (service): {}", id);
            Optional<Estadio> estadio = this.repository.findById(id);
                if (estadio.isPresent()) {
                    LOGGER.info("Estadio encontrado (service): {}");
                } else {
                    LOGGER.warn("Estadio  com ID {} não encontrado (service)", id);
                }
            return estadio ;
        } catch (Exception e){
            LOGGER.error("Erro ao buscar Estadio com ID (service): {}", id, e);
            throw e;
        }

    }

    @Transactional
    public void save(Estadio estadio) {
        try {
            LOGGER.info("Criando o Estadio (service): {}", estadio);
            this.repository.save(estadio);  // Salva o estadio uma vez
            LOGGER.info("Estadio criado com sucesso (service): {}", estadio);
        } catch (Exception e) {
            LOGGER.error("Erro ao criar Estadio (service): {}", e);
            throw e;  // Lança a exceção para marcar a transação como rollback-only
        }
    }

    @Transactional
    public void update(Estadio estadio) {
        try {
            LOGGER.info("Atualizando Estadio (service): {}");
            this.repository.update(estadio);
            LOGGER.info("Estadio atualizado com sucesso (service): {}");
        } catch (Exception e) {
            LOGGER.error("Erro ao atualizar Estadio (service): {}", e);
            throw e;
        }
        this.repository.update(estadio);
    }

    @Transactional
    public void delete(Estadio estadio) {
        try {
            LOGGER.info("Deletando Estadio (service): {}");
            this.repository.delete(estadio);
            LOGGER.info("Estadio deletado com sucesso (service): {}");
        } catch (Exception e) {
            LOGGER.error("Erro ao deletar Estadio (service): {}", e);
            throw e;
        }
        this.repository.delete(estadio);
    }


}
