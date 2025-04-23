package br.com.fiap.mercado.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Campo obrigatório!")
    @Size(min = 5, max = 255, message = "Deve ter entre 5 á 255 caracteres e não pode ser nulo!")
    private String nome;
    
    @NotNull(message = "Não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotNull(message = "Não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private Raridade raridade;

    private double preco;

    @ManyToOne
    @JoinColumn(name = "dono_id")
    private Personagem dono;
}
