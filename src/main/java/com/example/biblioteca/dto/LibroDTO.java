package com.example.biblioteca.dto;

import java.time.LocalDate;

import com.example.biblioteca.model.Libro;

public class LibroDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private LocalDate fechaPublicacion;
    private String genero;
    private String autorNombreCompleto;

    public LibroDTO(Libro libro) {
        this.id = libro.getId();
        this.titulo = libro.getTitulo();
        this.isbn = libro.getIsbn();
        this.fechaPublicacion = libro.getFecha_publicacion();
        this.genero = libro.getGenero();

        // Aquí combinamos el nombre y apellido del Autor
        this.autorNombreCompleto = libro.getAutor().getNombre() + " " + libro.getAutor().getApellido();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutorNombreCompleto() {
        return autorNombreCompleto;
    }

    public void setAutorNombreCompleto(String autorNombreCompleto) {
        this.autorNombreCompleto = autorNombreCompleto;
    }

}
