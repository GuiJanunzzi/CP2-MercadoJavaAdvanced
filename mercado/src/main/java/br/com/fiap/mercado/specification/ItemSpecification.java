package br.com.fiap.mercado.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mercado.model.Item;
import br.com.fiap.mercado.model.ItemFilter;
import jakarta.persistence.criteria.Predicate;

public class ItemSpecification {
    public static Specification<Item> withFilter(ItemFilter filter){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.nome() != null){
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            if(filter.tipo() != null){
                predicates.add(cb.equal(root.get("tipo"), filter.tipo()));
            }

            if(filter.raridade() != null){
                predicates.add(cb.equal(root.get("raridade"), filter.raridade()));
            }

            if(filter.valorMin() != null && filter.valorMax() != null){
                predicates.add(cb.between(root.get("preco"), filter.valorMin(), filter.valorMax()));
            }else if(filter.valorMin() != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("preco"), filter.valorMin()));
            }else if(filter.valorMax() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("preco"), filter.valorMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
