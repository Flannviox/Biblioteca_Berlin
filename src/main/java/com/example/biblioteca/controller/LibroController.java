package com.example.biblioteca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.service.LibroService;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    //create
    @PostMapping
    public ResponseEntity<Libro> createLibro(@RequestBody Libro libro) {
        Libro nuevoLibro = libroService.saveLibro(libro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    //read
    @GetMapping
    public List<Libro> getAllLibros() {
        return libroService.getAllLibros();
    }

    //read by id
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibroById(@PathVariable Long id) {
        Optional<Libro> libro = libroService.getLibroById(id);
        return libro.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable Long id, @RequestBody Libro libroDetalles) {
        Optional<Libro> libroExistente = libroService.getLibroById(id);
        if (libroExistente.isPresent()) {
            Libro libro = libroExistente.get();
            libro.setTitulo(libroDetalles.getTitulo());
            libro.setFecha_publicacion(libroDetalles.getFecha_publicacion());
            libro.setGenero(libroDetalles.getGenero());
            return ResponseEntity.ok(libroService.saveLibro(libro));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }
}
