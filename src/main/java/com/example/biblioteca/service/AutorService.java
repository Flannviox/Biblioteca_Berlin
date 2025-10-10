package com.example.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.biblioteca.model.Autor;
import com.example.biblioteca.repository.AutorRepository;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    // crear
    public Autor guardarAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    // obtener todos
    public List<Autor> getAutores() {
        return autorRepository.findAll();
    }

    // buscar por id
    public Optional<Autor> getAutorById(Long id) {
        return autorRepository.findById(id);
    }

    // actualizar actuor por id
    public Optional<Autor> actualizarAutor(Long id, Autor autorDetalles) {
        // autor existe?
        return autorRepository.findById(id)
                // como devuelve un optional, si esta presente map se ejecuta sino devuelve
                // opt.empty
                .map(autorExistente -> {
                    autorExistente.setNombre(autorDetalles.getNombre());
                    autorExistente.setApellido(autorDetalles.getApellido());
                    autorExistente.setNacionalidad(autorDetalles.getNacionalidad());
                    return autorRepository.save(autorExistente);
                });
    }

    // eliminar por id
    public boolean eliminarPorId(Long id) {
        if (autorRepository.existsById(id)) {
            autorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //DERIVED QUERIES METHODS

    public List<Autor>buscarPorNacionalidad(String nacionalidad){
        return autorRepository.findByNacionalidad(nacionalidad);
    }

}
