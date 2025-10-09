package com.example.biblioteca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.model.Autor;
import com.example.biblioteca.service.AutorService;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    //create
    @PostMapping
    public ResponseEntity<Autor> createAutor(@RequestBody Autor autor) {
        Autor nuevoAutor = autorService.saveAutor(autor);
        return new ResponseEntity<>(nuevoAutor, HttpStatus.CREATED);
    }

    //read
    @GetMapping
    public List<Autor> getAllAutores() {
        return autorService.getAllAutores();
    }

    //read by id
    @GetMapping("/{id}")
    public ResponseEntity<Autor> getAutorById(@PathVariable Long id) {
        Optional<Autor> autor = autorService.getAutorById(id);
        return autor.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<Autor> updateAutor(@PathVariable Long id, @RequestBody Autor autorDetalles) {
        Optional<Autor> autorExistente = autorService.getAutorById(id);
        if (autorExistente.isPresent()) {
            Autor autor = autorExistente.get();
            autor.setNombre(autorDetalles.getNombre());
            autor.setNacionalidad(autorDetalles.getNacionalidad());
            autor.setFecha_nacimiento(autorDetalles.getFecha_nacimiento());
            return ResponseEntity.ok(autorService.saveAutor(autor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        autorService.deleteAutor(id);
        return ResponseEntity.noContent().build();
    }
}
