package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.biblioteca.model.Libro;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Derived Queries:
    // encontrar libros por genero
    List<Libro> findByGenero(String genero);

    // Libros de un autor
    List<Libro> findByAutorId(Long autorId);

    // Named Query

    @Query(name = "Libro.countByAutor")
    List<Object[]> contarLibrosPorAutor();

}
