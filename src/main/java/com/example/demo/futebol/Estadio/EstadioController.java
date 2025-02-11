package com.example.demo.futebol.Estadio;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.futebol.Localizacao.Localizacao;
import com.example.demo.futebol.Localizacao.LocalizacaoService;

@RestController
@RequestMapping("/Estadio")
public class EstadioController {

    private EstadioService service;
    private LocalizacaoService localizacaoService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EstadioController.class);

    public EstadioController(EstadioService service, LocalizacaoService localizacaoService) {
        this.service = service;
        this.localizacaoService = localizacaoService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
        public ResponseEntity<?> save(@RequestBody EstadioRequest request){
        Integer idLocalizacao = request.getIdLocalizacao();
        LOGGER.info("Iniciando criação de um novo Estadio para Localizacao com ID: {}", idLocalizacao);
        if (idLocalizacao == null){
            return ResponseEntity.badRequest().body("idLocalizacao está inválido");
        }

        Optional<Localizacao> possivelLocalizacao = this.localizacaoService.findById(idLocalizacao);
        if(possivelLocalizacao.isPresent()){
            Localizacao localizacao = possivelLocalizacao.get();
            Estadio estadio = request.transform(localizacao);
            service.save(estadio);
            LOGGER.info("Estadio criado para Localizacao com sucesso!");
            return ResponseEntity.ok().header("Custom-Header", "foo").body(estadio);
        }
        else{
            LOGGER.info("localizacao não encontrado com ID: {}", idLocalizacao);
            return ResponseEntity.notFound().header("not-found-id", String.valueOf(idLocalizacao)).build();
        }

    }

    @GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findAll() {
        LOGGER.info("Buscando lista de Estadio");
        
        List<Estadio> list = service.findAll();
        LOGGER.info("Estadio encontrados: {}", list.size());
        return ResponseEntity.ok().header("Custom-Header", "foo").body(list);
    }

    @GetMapping(value="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        LOGGER.info("Buscando Estadio por id: {}", id);

        if(id == null){
            return ResponseEntity.badRequest().body("id inválido");
        }

        Optional<Estadio> estadio = service.findById(id);
        if (estadio.isPresent()) {
            LOGGER.info("Estadio encontrado com sucesso: {}");
            return ResponseEntity.ok().header("Custom-header", "foo").body(estadio.get());
        }else{
            LOGGER.info("Estadio não encontrado pelo id: {}", id);
            return ResponseEntity.notFound().header("not-found-id", String.valueOf(id)).build();
        }
    }
           
    @PutMapping(value="/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody EstadioRequest request) {
        Integer idLocalizacao = request.getIdLocalizacao();
        LOGGER.info("Iniciando atualização de Estadio pelo id: {}",id);

        Optional<Localizacao> possivelLocalizacao = this.localizacaoService.findById(idLocalizacao);
        Optional<Estadio> estadio = service.findById(id);
        if(estadio.isPresent()){
            Estadio estadioFinal = request.transform(possivelLocalizacao.get());
            service.update(estadioFinal);
            LOGGER.info("Estadio atualizado(a) com sucesso: {}");
            return ResponseEntity.ok().header("Custom-Header", "foo").body(estadioFinal);
        } else{
            LOGGER.info("Estadio não encontrado(a) pelo id: {}", request.getId());
            return ResponseEntity.notFound().header("not-found-id", String.valueOf(request.getId())).build();
        }
    }


    @DeleteMapping(value="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        LOGGER.info("Iniciando deleção de Estadio pelo id: {}", id);
        Optional<Estadio> possivelEstadio = service.findById(id);
        Estadio estadio = possivelEstadio.get();
        service.delete(estadio);
        LOGGER.info("Estadio deletado(a) com sucesso: {}");
        return ResponseEntity.ok().header("Custom-Header", "foo").body(estadio);
    }

}

