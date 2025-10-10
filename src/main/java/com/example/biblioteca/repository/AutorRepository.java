package com.example.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.biblioteca.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // encontrar todos los autores de una nacionalidad
    List<Autor> findByNacionalidad(String nacionalidad);
}
