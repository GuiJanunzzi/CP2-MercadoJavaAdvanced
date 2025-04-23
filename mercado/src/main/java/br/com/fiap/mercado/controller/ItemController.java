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

import br.com.fiap.mercado.model.Item;
import br.com.fiap.mercado.model.ItemFilter;
import br.com.fiap.mercado.repository.ItemRepository;
import br.com.fiap.mercado.specification.ItemSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {
    
   private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ItemRepository repository;

    //----- Documentação Swagger -----
    @Operation(
        summary = "Listar todas os itens",
        description = "Retorna uma lista com todos os itens cadastrados no sistema",
        responses = {
                @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping
    @Cacheable("item")
    public Page<Item> index(ItemFilter filter, @PageableDefault(size = 5, sort = "valorTotal", direction = Direction.DESC) Pageable pageable){

        var specification = ItemSpecification.withFilter(filter);
        return repository.findAll(specification, pageable);
    }

    @CacheEvict(value = "item", allEntries = true)
    //----- Documentação Swagger -----
    @Operation(
        summary = "Cadastrar Item",
        description = "Coleta os dados para adicionar um item no sistema",
        responses = {
                @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        }
    )
    //----- Documentação Swagger -----
    @PostMapping
    public ResponseEntity<Item> create(@RequestBody @Valid Item item){
        log.info("Cadastrando Item");
        repository.save(item);
        return ResponseEntity.status(201).body(item);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Buscar Item por ID",
        description = "Retorna os dados de um item com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Item encontrado"),
                @ApiResponse(responseCode = "404", description = "Item não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @GetMapping({"/{id}"})
    public Item get(@PathVariable Long id){
        log.info("Buscando item ID: " + id);
        return getItem(id);
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Atualizar Item",
        description = "Atualiza os dados de um item existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                @ApiResponse(responseCode = "404", description = "Item não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @CacheEvict(value = "item", allEntries = true)
    @PutMapping({"/{id}"})
    public Item update(@PathVariable Long id, @RequestBody @Valid Item item){
        log.info("Atualizando item " + item.toString());

        getItem(id);
        item.setId(id);
        repository.save(item);

        return item;
    }

    //----- Documentação Swagger -----
    @Operation(
        summary = "Deletar Item",
        description = "Remove um Item existente com base no ID fornecido",
        responses = {
                @ApiResponse(responseCode = "204", description = "Item removido com sucesso"),
                @ApiResponse(responseCode = "404", description = "Item não encontrado")
        }
    )
    //----- Documentação Swagger -----
    @CacheEvict(value = "item", allEntries = true)
    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Apagando Item ID: " + id);
        repository.delete(getItem(id));
    }

    private Item getItem(Long id) {
        return repository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }
}
