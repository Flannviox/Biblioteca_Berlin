package com.example.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.biblioteca.model.Autor;
import com.example.biblioteca.repository.AutorRepository;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    //constructor
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    //crear o actualizar
    public Autor saveAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    //listar
    public List<Autor> getAllAutores() {
        return autorRepository.findAll();
    }

    //buscar por id
    public Optional<Autor> getAutorById(Long id) {
        return autorRepository.findById(id);
    }

    //eliminar por id
    public void deleteAutor(Long id) {
        autorRepository.deleteById(id);
    }
}
