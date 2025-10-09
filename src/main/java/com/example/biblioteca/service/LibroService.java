package com.example.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.LibroRepository;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    // Constructor
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }


    //crear o actualizar
    public Libro saveLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    //listar
    public List<Libro> getAllLibros() {
        return libroRepository.findAll();
    }

    public Optional<Libro> getLibroById(Long id) {
        return libroRepository.findById(id);
    }
    public void deleteLibro(Long id) {
        libroRepository.deleteById(id);
    }
}
