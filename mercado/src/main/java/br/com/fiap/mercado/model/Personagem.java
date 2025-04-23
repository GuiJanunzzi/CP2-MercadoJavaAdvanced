package br.com.fiap.mercado.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class Personagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Campo obrigatório!")
    @Size(min = 5, max = 255, message = "Deve ter entre 5 á 255 caracteres e não pode ser nulo!")
    private String nome;

    @NotNull(message = "Não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private Classe classe;

    @Min(1)
    @Max(99)
    private int nivel;

    @PositiveOrZero(message = "O numero não pode ser negativo!")
    private int moedas;
}
