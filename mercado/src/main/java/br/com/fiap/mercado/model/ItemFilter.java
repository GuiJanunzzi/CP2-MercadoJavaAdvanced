package br.com.fiap.mercado.model;

public record ItemFilter(
    String nome,
    Tipo tipo,
    Double valorMin,
    Double valorMax,
    Raridade raridade){

}
