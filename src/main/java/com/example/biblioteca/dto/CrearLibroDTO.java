package com.example.biblioteca.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CrearLibroDTO {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no debe exceder los 100 caracteres")
    private String titulo;

    @NotBlank(message = "El ISBN no puede estar vacío")
    private String isbn;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate fecha_publicacion;

    @NotBlank(message = "El género no puede estar vacío")
    private String genero;

    @NotNull(message = "Debe especificar el ID del autor")
    private Long autorId;

    public CrearLibroDTO() {
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

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

}
