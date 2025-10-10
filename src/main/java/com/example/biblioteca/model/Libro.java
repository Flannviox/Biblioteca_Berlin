package com.example.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity

/*
 * esto lo puso flavio:)
 * 
 * @NamedQuery(
 * name = "Libro.findByGenero",
 * query = "SELECT l FROM Libro l WHERE l.genero = :genero"
 * )
 */
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
