package com.example.biblioteca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.dto.CrearLibroDTO;
import com.example.biblioteca.exception.JSendResponse;
import com.example.biblioteca.service.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

        @Autowired
        private LibroService libroService;

        @PostMapping
        public ResponseEntity<JSendResponse> crearLibro(@Valid @RequestBody CrearLibroDTO dtoEntrada) {
                return libroService.guardarLibro(dtoEntrada);
        }

        @GetMapping
        public ResponseEntity<JSendResponse> obtenerTodos() {
                return libroService.obtenerTodos();
        }

        @GetMapping("/{id}")
        public ResponseEntity<JSendResponse> obtenerPorId(@PathVariable Long id) {
                return libroService.obtenerPorId(id);
        }

        @PutMapping("/{id}")
        public ResponseEntity<JSendResponse> actualizarLibro(@PathVariable Long id,
                        @Valid @RequestBody CrearLibroDTO dtoEntrada) {
                return libroService.actualizarLibro(id, dtoEntrada);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<JSendResponse> eliminarLibro(@PathVariable Long id) {
                return libroService.eliminarLibro(id);
        }

        @GetMapping(value = "/search", params = "genero")
        public ResponseEntity<JSendResponse> buscarLibroPorGenero(@RequestParam String genero) {
                return libroService.buscarPorGenero(genero);
        }

        @GetMapping("/count/autores")
        public ResponseEntity<JSendResponse> contarLibrosPorAutor() {
                return libroService.contarLibrosPorAutor();
        }
}
