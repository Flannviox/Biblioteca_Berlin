package com.example.biblioteca.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.dto.CrearLibroDTO;
import com.example.biblioteca.dto.LibroDTO;
import com.example.biblioteca.exception.JSendResponse;
import com.example.biblioteca.model.Libro;
import com.example.biblioteca.service.LibroService;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

        @Autowired
        private LibroService libroService;

        // Crear libro
        @PostMapping
        public ResponseEntity<JSendResponse> crearLibro(@RequestBody CrearLibroDTO dtoEntrada) {
                Libro libroNuevo = new Libro(
                                dtoEntrada.getTitulo(),
                                dtoEntrada.getIsbn(),
                                dtoEntrada.getFecha_publicacion(),
                                dtoEntrada.getGenero(),
                                null // El autor se establece en el servicio
                );

                return libroService.guardarLibro(libroNuevo, dtoEntrada.getAutorId())
                                .map(libroGuardado -> {
                                        // Éxito: Autor encontrado y Libro guardado.
                                        // Convertimos el resultado a LibroDTO (datos libro + nombre autor) y
                                        // formateamos la respuesta JSend.
                                        LibroDTO dtoSalida = new LibroDTO(libroGuardado);

                                        return new ResponseEntity<>(
                                                        new JSendResponse("success", dtoSalida, "yuppiii"),
                                                        HttpStatus.CREATED // HTTP 201
                                        );
                                })
                                .orElseGet(() -> {
                                        // Fallo: Optional vacío, el autor no existe.
                                        // Creamos el objeto de error para el campo 'data'
                                        java.util.Map<String, String> errorDetails = java.util.Map.of(
                                                        "autorId",
                                                        "Autor con ID " + dtoEntrada.getAutorId() + " no encontrado.");

                                        return new ResponseEntity<>(
                                                        new JSendResponse("fail", errorDetails, " autor fantasma!"),
                                                        HttpStatus.NOT_FOUND // HTTP 404
                                        );
                                });
        }

        // obtener todos los libros
        @GetMapping
        // el ResponseEntity no da solo datos (List<Libro>)sino cuerpo xd: respuesta +
        // estado http, headers,etc.
        public ResponseEntity<JSendResponse> obtenerTodos() {
                List<Libro> libros = libroService.getLibros();

                // Mapear la entidad a salida LibroDTO
                List<LibroDTO> librosDTO = libros.stream()
                                .map(LibroDTO::new)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(new JSendResponse("success", librosDTO, "todo oki"));
        }

        // obtener libro por ID
        @GetMapping("/{id}")
        public ResponseEntity<JSendResponse> obtenerPorId(@PathVariable Long id) {
                return libroService.getLibroById(id)
                                .map(LibroDTO::new)
                                .map(dto -> new ResponseEntity<>(
                                                new JSendResponse("success", dto, null), HttpStatus.OK))
                                .orElseGet(() -> { // orElseGet para lógica compleja
                                        // objeto fail
                                        java.util.Map<String, String> errorDetails = java.util.Map.of(
                                                        "libroId", "Libro con ID " + id + " no encontrado.");

                                        return new ResponseEntity<>(
                                                        new JSendResponse("fail", errorDetails, null),
                                                        HttpStatus.NOT_FOUND);
                                });
        }

        // actualizar libro por ID
        @PutMapping("/{id}")
        public ResponseEntity<JSendResponse> actualizarLibro(@PathVariable Long id,
                        @RequestBody CrearLibroDTO dtoEntrada) {

                // Creamos la Entidad "temporal" con detalles de actualización
                Libro libroDetalles = new Libro(
                                dtoEntrada.getTitulo(),
                                dtoEntrada.getIsbn(),
                                dtoEntrada.getFecha_publicacion(),
                                dtoEntrada.getGenero(),
                                null);

                return libroService.actualizarLibro(id, libroDetalles, dtoEntrada.getAutorId())
                                .map(libroActualizado -> {
                                        // Libro encontrado y actualizado, lo transformo a libroDTO
                                        LibroDTO dtoSalida = new LibroDTO(libroActualizado);
                                        return new ResponseEntity<>(
                                                        new JSendResponse("success", dtoSalida, null),
                                                        HttpStatus.OK);
                                })
                                .orElseGet(() -> {
                                        // Fallo: El libro o nuevo autor no fueron encontrados
                                        java.util.Map<String, String> errorDetails;
                                        // indicamos un fallo general del recurso/aplica para libro-autor
                                        errorDetails = java.util.Map.of("recurso",
                                                        "Libro con ID " + id + " o Autor con ID "
                                                                        + dtoEntrada.getAutorId()
                                                                        + " no encontrados para actualizar.");

                                        return new ResponseEntity<>(
                                                        new JSendResponse("fail", errorDetails, null),
                                                        HttpStatus.NOT_FOUND);
                                });
        }

        // eliminar libro por id
        @DeleteMapping("/{id}")
        public ResponseEntity<JSendResponse> eliminarLibro(@PathVariable Long id) {
                if (libroService.deleteLibroById(id)) {

                        return new ResponseEntity<>(new JSendResponse("success", null, null), HttpStatus.NO_CONTENT);
                }
                // Fallo
                java.util.Map<String, String> errorDetails = java.util.Map.of(
                                "libroId", "Libro con ID " + id + " no encontrado para eliminar.");
                return new ResponseEntity<>(
                                new JSendResponse("fail", errorDetails, null),
                                HttpStatus.NOT_FOUND);
        }

        // CONSULTAS ESPECIALES-DERIVED QUERIES METHODS

        // @GetMapping("/search") si fuera una sola consulta personalizada
        // (que ya se sabe el atributo a buscar),pero como no es, hay que especificar
        // la ruta para evitar ambiguedad

        @GetMapping(value = "/search", params = "genero")

        public ResponseEntity<JSendResponse> buscarLibroPorGenero(@RequestParam String genero) {
                List<Libro> resultados = libroService.buscarPorGenero(genero);
                if (resultados.isEmpty()) {
                        return ResponseEntity.ok(
                                        new JSendResponse("success", List.of(),
                                                        "No se encontraron libros que coincidan con el género."));
                }
                // mapear a salida LIbroDTO
                List<LibroDTO> librosDTO = resultados.stream()
                                .map(LibroDTO::new)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                new JSendResponse("success", librosDTO, "si hubo resultado :)"));

        }

        // contar libros por autor
        @GetMapping("/count/autores")
        public ResponseEntity<JSendResponse> contarLibrosPorAutor() {
                var resultados = libroService.contarLibrosPorAutor();
                return ResponseEntity
                                .ok(new JSendResponse("success", resultados, "Conteo de libros agrupados por autor"));
        }
}
