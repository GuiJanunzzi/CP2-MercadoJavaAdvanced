package br.com.fiap.mercado.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.mercado.model.Personagem;
import br.com.fiap.mercado.model.PersonagemFilter;
import br.com.fiap.mercado.repository.PersonagemRepository;
import br.com.fiap.mercado.specification.PersonagemSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/personagem")
@CrossOrigin
public class PersonagemController {
    
   private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private PersonagemRepository repository;

    //----- Documentação Swagger -----
    @Operation(
        summary = "Listar todos os Personagens",
        description = "Retorna uma lista com todos os personagens cadastrados no sistema",
        responses = {
                @ApiResponse(responseCode = "200", description = "Lista de personagens retornado com sucesso")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping
    @Cacheable("personagem")
    public Page<Personagem> index(PersonagemFilter filter, @PageableDefault(size = 5, sort = "valorTotal", direction = Direction.DESC) Pageable pageable){

        var specification = PersonagemSpecification.withFilter(filter);
        return repository.findAll(specification, pageable);
    }

    @CacheEvict(value = "personagem", allEntries = true)
    //----- Documentação Swagger -----
    @Operation(
        summary = "Cadastrar Personagem",
        description = "Coleta os dados para adicionar um personagem no sistema",
        responses = {
                @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        }
    )
    //----- Documentação Swagger -----
    @PostMapping
    public ResponseEntity<Personagem> create(@RequestBody @Valid Personagem personagem){
        log.info("Cadastrando Personagem");
        repository.save(personagem);
        return ResponseEntity.status(201).body(personagem);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Buscar Personagem por ID",
        description = "Retorna os dados de um personagem com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Personagem encontrado"),
                @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping({"/{id}"})
    public Personagem get(@PathVariable Long id){
        log.info("Buscando personagem ID: " + id);
        return getPersonagem(id);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Atualizar Personagem",
        description = "Atualiza os dados de um personagem existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Personagem atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @CacheEvict(value = "personagem", allEntries = true)
    @PutMapping({"/{id}"})
    public Personagem update(@PathVariable Long id, @RequestBody @Valid Personagem personagem){
        log.info("Atualizando personagem " + personagem.toString());

        getPersonagem(id);
        personagem.setId(id);
        repository.save(personagem);

        return personagem;
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Deletar Personagem",
        description = "Remove um Personagem existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "204", description = "Personagem removido com sucesso"),
                @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @CacheEvict(value = "personagem", allEntries = true)
    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Apagando Personagem ID: " + id);
        repository.delete(getPersonagem(id));
    }

    private Personagem getPersonagem(Long id) {
        return repository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }
}
