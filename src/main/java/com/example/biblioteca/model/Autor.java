package com.example.biblioteca.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;

@Entity
@Table(name = "autor")
public class Autor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_autor;

    @Column(nullable = false)
    private String nombre;
    private String nacionalidad;
    private LocalDate fecha_nacimiento;

    // la relacion de un autor puede tener muchos libros
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    
    private List<Libro> libros = new ArrayList<>();

        //constructor solo
    public Autor() {
    }


    public Autor(String nombre, String nacionalidad, LocalDate fecha_nacimiento) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.fecha_nacimiento = fecha_nacimiento;
    }






    public Long getId_autor() { return id_autor; }
    public void setId_autor(Long id_autor) { this.id_autor = id_autor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public LocalDate getFecha_nacimiento() { return fecha_nacimiento; }
    public void setFecha_nacimiento(LocalDate fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }


}
