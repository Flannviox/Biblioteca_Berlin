package com.example.biblioteca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.dto.CrearAutorDTO;
import com.example.biblioteca.exception.JSendResponse;
import com.example.biblioteca.service.AutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

        @Autowired
        private AutorService autorService;

        // Crear autor
        @PostMapping
        public ResponseEntity<JSendResponse> crearAutor(@Valid @RequestBody CrearAutorDTO dtoEntrada) {
                return autorService.crearAutor(dtoEntrada);
        }

        // Obtener todos
        @GetMapping
        public ResponseEntity<JSendResponse> obtenerAutores() {
                return autorService.obtenerAutores();
        }

        // Obtener por id
        @GetMapping("/{id}")
        public ResponseEntity<JSendResponse> obtenerPorId(@PathVariable Long id) {
                return autorService.obtenerPorId(id);
        }

        // Actualizar
        @PutMapping("/{id}")
        public ResponseEntity<JSendResponse> actualizarAutor(@PathVariable Long id, @Valid
                        @RequestBody CrearAutorDTO dtoEntrada) {
                return autorService.actualizarAutor(id, dtoEntrada);
        }

        // Eliminar
        @DeleteMapping("/{id}")
        public ResponseEntity<JSendResponse> eliminarAutor(@PathVariable Long id) {
                return autorService.eliminarAutor(id);
        }

        // Buscar por nacionalidad
        @GetMapping(value = "/search", params = "nacionalidad")
        public ResponseEntity<JSendResponse> buscarAutorPorNacionalidad(@RequestParam String nacionalidad) {
                return autorService.buscarPorNacionalidad(nacionalidad);
        }

        // Libros del autor
        @GetMapping("/{id}/libros")
        public ResponseEntity<JSendResponse> obtenerLibrosPorAutorId(@PathVariable Long id) {
                return autorService.obtenerLibrosPorAutorId(id);
        }
}
