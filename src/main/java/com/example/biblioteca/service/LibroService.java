package com.example.biblioteca.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.example.biblioteca.dto.CrearLibroDTO;
import com.example.biblioteca.dto.LibroDTO;
import com.example.biblioteca.dto.ListaBreveLibrosDTO;
import com.example.biblioteca.exception.JSendResponse;
import com.example.biblioteca.model.Autor;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.AutorRepository;
import com.example.biblioteca.repository.LibroRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    // Crear libro
    public ResponseEntity<JSendResponse> guardarLibro(CrearLibroDTO dtoEntrada) {
        Optional<Autor> autorOpt = autorRepository.findById(dtoEntrada.getAutorId());
        if (autorOpt.isEmpty()) {
            Map<String, String> error = Map.of("autorId",
                    "Autor con ID " + dtoEntrada.getAutorId() + " no encontrado.");
            return new ResponseEntity<>(new JSendResponse("fail", error, "Autor fantasma ..."), HttpStatus.NOT_FOUND);
        }

        boolean isbnDuplicado = libroRepository.findAll()
                .stream()
                .anyMatch(l -> l.getIsbn().equalsIgnoreCase(dtoEntrada.getIsbn()));

        if (isbnDuplicado) {
            Map<String, String> error = Map.of("isbn", "Ya existe un libro con ese ISBN.");
            return new ResponseEntity<>(new JSendResponse("fail", error, "Ya existe"), HttpStatus.CONFLICT);
        }

        Libro libro = new Libro(
                dtoEntrada.getTitulo(),
                dtoEntrada.getIsbn(),
                dtoEntrada.getFecha_publicacion(),
                dtoEntrada.getGenero(),
                autorOpt.get());

        Libro guardado = libroRepository.save(libro);
        return new ResponseEntity<>(new JSendResponse("success", new LibroDTO(guardado), "Libro creado correctamente"),
                HttpStatus.CREATED);
    }

    // Listar todos
    public ResponseEntity<JSendResponse> obtenerTodos() {
        List<Libro> libros = libroRepository.findAll();
        List<LibroDTO> librosDTO = libros.stream().map(LibroDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(new JSendResponse("success", librosDTO, "todo oki"));
    }

    // Buscar por ID
    public ResponseEntity<JSendResponse> obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .map(libro -> new ResponseEntity<>(
                        new JSendResponse("success", new LibroDTO(libro), null), HttpStatus.OK))
                .orElseGet(() -> {
                    Map<String, String> error = Map.of("libroId", "Libro con ID " + id + " no encontrado.");
                    return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
                });
    }

    // Actualizar libro
    public ResponseEntity<JSendResponse> actualizarLibro(Long id, CrearLibroDTO dtoEntrada) {
        Optional<Libro> libroOpt = libroRepository.findById(id);
        Optional<Autor> autorOpt = autorRepository.findById(dtoEntrada.getAutorId());

        if (libroOpt.isEmpty() || autorOpt.isEmpty()) {
            Map<String, String> error = Map.of("recurso",
                    "Libro con ID " + id + " o Autor con ID " + dtoEntrada.getAutorId()
                            + " no encontrados para actualizar.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
        }

        Libro libroExistente = libroOpt.get();
        libroExistente.setTitulo(dtoEntrada.getTitulo());
        libroExistente.setIsbn(dtoEntrada.getIsbn());
        libroExistente.setFecha_publicacion(dtoEntrada.getFecha_publicacion());
        libroExistente.setGenero(dtoEntrada.getGenero());
        libroExistente.setAutor(autorOpt.get());

        Libro actualizado = libroRepository.save(libroExistente);
        return ResponseEntity.ok(new JSendResponse("success", new LibroDTO(actualizado), null));
    }

    // Eliminar libro
    public ResponseEntity<JSendResponse> eliminarLibro(Long id) {
        if (!libroRepository.existsById(id)) {
            Map<String, String> error = Map.of("libroId", "Libro con ID " + id + " no encontrado para eliminar.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
        }
        libroRepository.deleteById(id);
        return new ResponseEntity<>(new JSendResponse("success", null, null), HttpStatus.NO_CONTENT);
    }

    // Buscar por género
    public ResponseEntity<JSendResponse> buscarPorGenero(String genero) {
        List<Libro> resultados = libroRepository.findByGenero(genero);
        if (resultados.isEmpty()) {
            return ResponseEntity.ok(new JSendResponse("success", List.of(),
                    "No se encontraron libros que coincidan con el género."));
        }

        List<LibroDTO> librosDTO = resultados.stream().map(LibroDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(new JSendResponse("success", librosDTO, "si hubo resultado :)"));
    }

    // Contar libros agrupados por autor
    public ResponseEntity<JSendResponse> contarLibrosPorAutor() {
        var resultados = libroRepository.contarLibrosPorAutor();
        return ResponseEntity.ok(new JSendResponse("success", resultados, "Conteo de libros agrupados por autor"));
    }

    // Buscar libros por autor (para el controlador de autor)
    public List<ListaBreveLibrosDTO> buscarLibrosPorAutor(Long autorId) {
        return libroRepository.findByAutorId(autorId)
                .stream().map(ListaBreveLibrosDTO::new)
                .collect(Collectors.toList());
    }
}
