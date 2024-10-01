package com.example.khiata.models;

public class Costureira {

    private long id=0;
    private String nome;

    public Costureira(){}

    public Costureira(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
