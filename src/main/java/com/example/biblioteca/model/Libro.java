package com.example.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity

@NamedQueries({
        // simple: buscar por título exacto
        @NamedQuery(name = "Libro.findByTitulo", query = "SELECT l FROM Libro l WHERE l.titulo = :titulo"),
        // compleja: agrupa por autor y cuenta sus libros
        @NamedQuery(name = "Libro.countByAutor", query = "SELECT a.nombre, COUNT(l) FROM Libro l JOIN l.autor a GROUP BY a.nombre")
})
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long id;

    private String titulo;
    private String isbn;
    private LocalDate fecha_publicacion;
    private String genero;

    // muchos libros pertencen a un autor
    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    @JsonBackReference
    private Autor autor;

    // constructores
    public Libro() {
    }

    public Libro(String titulo, String isbn, LocalDate fecha_publicacion, String genero, Autor autor) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.fecha_publicacion = fecha_publicacion;
        this.genero = genero;
        this.autor = autor;
    }

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

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
