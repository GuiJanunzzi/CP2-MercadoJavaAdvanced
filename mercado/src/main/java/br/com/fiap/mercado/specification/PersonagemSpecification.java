package br.com.fiap.mercado.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mercado.model.Personagem;
import br.com.fiap.mercado.model.PersonagemFilter;
import jakarta.persistence.criteria.Predicate;

public class PersonagemSpecification {
    public static Specification<Personagem> withFilter(PersonagemFilter filter){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.nome() != null){
                predicates.add(cb.equal(root.get("nome"), filter.nome()));
            }

            if(filter.classe() != null){
                predicates.add(cb.equal(root.get("classe"), filter.classe()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
