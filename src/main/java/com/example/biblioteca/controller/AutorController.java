package com.example.biblioteca.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.dto.CrearAutorDTO;
import com.example.biblioteca.dto.ListaBreveLibrosDTO;
import com.example.biblioteca.model.Autor;
import com.example.biblioteca.service.AutorService;
import com.example.biblioteca.service.LibroService;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {
        
        @Autowired
        private AutorService autorService;

        @Autowired
        private LibroService libroService;

        // crear autor
        @PostMapping
        public ResponseEntity<JSendResponse> crearAutor(@RequestBody CrearAutorDTO dtoEntrada) {
                // lo del dto lo pasa a la "entidad base"
                Autor nuevAutor = new Autor(
                        dtoEntrada.getNombre(),
                        dtoEntrada.getApellido(),
                        dtoEntrada.getNacionalidad());
                // guarda el autor
                Autor autorGuardado = autorService.guardarAutor(nuevAutor);

                return new ResponseEntity<>(
                        new JSendResponse("success", autorGuardado, null),HttpStatus.CREATED);
        }

        // obtener todos los autores
        @GetMapping
        public ResponseEntity<JSendResponse> obtenerAutores() {
                List<Autor> autores = autorService.getAutores();
                return ResponseEntity.ok(new JSendResponse("success", autores, null));
        }

        // obtener autor por Id
        @GetMapping("/{id}")
        public ResponseEntity<JSendResponse> obtenerPorId(@PathVariable Long id) {
                return autorService.getAutorById(id)
                        .map(autor -> new ResponseEntity<>(new JSendResponse("success", autor, null),HttpStatus.OK))
                        // Fallo
                        .orElseGet(() -> {
                                 // objeto error
                                Map<String, String> errorDetails = Map.of("autorId", "Autor con ID " + id + " no encontrado.");
                                        return new ResponseEntity<>(
                                        new JSendResponse("fail", errorDetails, null),HttpStatus.NOT_FOUND);
                        });
        }

        // actualizar autor por id
        @PutMapping("/{id}")
        public ResponseEntity<JSendResponse> actualizarAutor(@PathVariable Long id,
        @RequestBody CrearAutorDTO dtoEntrada) {
                // Entidad "temporal" con los detalles del DTO
                Autor autorDetalles = new Autor(
                                dtoEntrada.getNombre(),
                                dtoEntrada.getApellido(),
                                dtoEntrada.getNacionalidad());

                return autorService.actualizarAutor(id, autorDetalles)
                        .map(autorActualizado -> {
                        return new ResponseEntity<>(
                        new JSendResponse("success", autorActualizado, null),HttpStatus.OK);
                        })
                        // si falló :c
                        .orElseGet(() -> {
                                Map<String, String> errorDetails = Map.of("autorId",
                                "Autor con ID " + id + " no encontrado para actualizar.");
                                return new ResponseEntity<>(
                                        new JSendResponse("fail", errorDetails, null),HttpStatus.NOT_FOUND);
                                });
        }

        // delete
        @DeleteMapping("/{id}")
        public ResponseEntity<JSendResponse> eliminarAutor(@PathVariable Long id) {
                // devuelve bool
                if (autorService.eliminarPorId(id)) {
                        return new ResponseEntity<>(new JSendResponse("success", null, "autor eliminado con éxito"),HttpStatus.NO_CONTENT);
                }
                // si falla...
                Map<String, String> errorDetails = Map.of(
                                "autorId", "Autor con ID " + id + " no encontrado para eliminar.");
                return new ResponseEntity<>(
                                new JSendResponse("fail", errorDetails, "efecito"),
                                HttpStatus.NOT_FOUND);

        }

        // CONSULTAS ESPECIALES-DERIVED QUERIES METHODS
        @GetMapping(value = "/search", params = "nacionalidad")
        public ResponseEntity<JSendResponse> buscarAutorPorNacionalidad(@RequestParam String nacionalidad) {
                List<Autor> resultados = autorService.buscarPorNacionalidad(nacionalidad);
                if (resultados.isEmpty()) {
                        return ResponseEntity.ok(
                                new JSendResponse("success", List.of(),"No se encontraron actores con esa nacionalidad"));
                }
                return ResponseEntity.ok(
                                new JSendResponse("success", resultados, "si hubo resultados!"));
        }

        // buscar por parte de apellido ( no es necesario poner todo el apellido completo xd)

        @GetMapping(value = "/search", params = "apellido")
        public ResponseEntity<JSendResponse> buscarAutorPorApellido(@RequestParam String apellido) {
                List<Autor> resultados = autorService.buscarPorApellido(apellido);
                if (resultados.isEmpty()) {
                        return ResponseEntity.ok(
                                new JSendResponse("success", List.of(), "No se encontraron actores con ese apellido"));
                }
                return ResponseEntity.ok(
                        new JSendResponse("success", resultados, "si hubo resultados!"));
        }

        // libros de autor, se pone aqui porque el Padre es autor y libros son su subconjunto

        @GetMapping("/{id}/libros")
        public ResponseEntity<JSendResponse> obtenerLibrosPorAutorId(@PathVariable Long id) {
                // Verificar si el autor existe
                if (!autorService.getAutorById(id).isPresent()) {
                        Map<String, String> errorDetails = Map.of(
                                        "autorId", "Autor con ID " + id + " no encontrado.");
                        return new ResponseEntity<>(
                                new JSendResponse("fail", errorDetails, null),HttpStatus.NOT_FOUND);
                }

                List<ListaBreveLibrosDTO> libros = libroService.buscarLibrosPorAutor(id);
                return ResponseEntity.ok(
                                new JSendResponse("success", libros, "Lista de libros del autor " + id));
        }

}
