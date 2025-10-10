package com.example.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.biblioteca.model.Libro;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // encontrar libros por genero
    List<Libro> findByGenero(String genero);

    // Encuentra libros que contengan una parte del título O que coincidan con un ISBN exacto
    List<Libro> findByTituloContainingOrIsbn(String parteDelTitulo, String isbn);

    //Libros por autor id
    List<Libro>findByAutorId(Long autorId);
}
