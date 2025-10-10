package com.example.biblioteca.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biblioteca.dto.ListaBreveLibrosDTO;
import com.example.biblioteca.model.Autor;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.AutorRepository;
import com.example.biblioteca.repository.LibroRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository; // Lo necesitamos para validar que tenga autor

    // crear libro
    public Optional<Libro> guardarLibro(Libro libro, Long autorId) {

        // buscar al autor
        Optional<Autor> autor = autorRepository.findById(autorId);

        // si autor existe
        if (autor.isPresent()) {

            // lo obtenemos
            Autor autorExistente = autor.get();

            // relacionamos con la tabla de libro
            libro.setAutor(autorExistente);

            // guardamos el libro
            Libro libroGuardado = libroRepository.save(libro);
            return Optional.of(libroGuardado);

        } else {
            // no hubo autor, optional vacio
            return Optional.empty();
        }
    }

    // listar todos los libros
    public List<Libro> getLibros() {
        return libroRepository.findAll();
    }

    // obtener libro por ID
    public Optional<Libro> getLibroById(Long id) {
        // Optional porque la busqueda puede fallar jeje, capaz ni existe :c
        return libroRepository.findById(id);
    }

    // actualizar libro por id
    public Optional<Libro> actualizarLibro(Long id, Libro libroDetalles, Long nuevoAutorId) {

        // Libro existe?
        Optional<Libro> libroOpt = libroRepository.findById(id);

        if (libroOpt.isPresent()) {
            // si si, lo obtengo
            Libro libroExistente = libroOpt.get();

            // Buscar el nuevo uutor (si el ID es diferente de null)
            Optional<Autor> autorOpt = autorRepository.findById(nuevoAutorId);

            if (autorOpt.isEmpty()) {
                // Si el nuevo autor no existe, no podemos actualizar la relación
                return Optional.empty();
            }
            // si existe sigue↓
            // Actualizar campos
            libroExistente.setTitulo(libroDetalles.getTitulo());
            libroExistente.setIsbn(libroDetalles.getIsbn());
            libroExistente.setFecha_publicacion(libroDetalles.getFecha_publicacion());
            libroExistente.setGenero(libroDetalles.getGenero());

            // Actualizar la relación con el nuevo autor
            libroExistente.setAutor(autorOpt.get());

            // Guardar y devolver el resultado
            return Optional.of(libroRepository.save(libroExistente));

        } else {
            // Libro no encontrado
            return Optional.empty();
        }
    }

    // eliminar por id
    public boolean deleteLibroById(Long id) {
        if (!libroRepository.existsById(id))
            return false;
        libroRepository.deleteById(id);
        return true;
    }

    // DERIVED QUERIES METHODS

    public List<Libro> buscarPorGenero(String genero) {
        return libroRepository.findByGenero(genero);
    }

    public List<ListaBreveLibrosDTO> buscarLibrosPorAutor(Long autorId) {
        return libroRepository.findByAutorId(autorId)
                .stream().map(ListaBreveLibrosDTO::new)
                .collect(Collectors.toList());
    }

    public List<Object[]> contarLibrosPorAutor() {
        return libroRepository.contarLibrosPorAutor();
    }
}
