package com.example.biblioteca.dto;

import com.example.biblioteca.model.Libro;

public class ListaBreveLibrosDTO {
    private Long id;
    private String titulo;

    //Esto lo usaremos para el DTO de autores, para que solo se muestren los titulos de libros y no toda la info
    public ListaBreveLibrosDTO(Libro libro) {
        this.id = libro.getId();
        this.titulo = libro.getTitulo();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
