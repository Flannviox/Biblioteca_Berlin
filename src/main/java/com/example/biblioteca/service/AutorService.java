package com.example.biblioteca.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.biblioteca.dto.CrearAutorDTO;
import com.example.biblioteca.dto.ListaBreveLibrosDTO;
import com.example.biblioteca.exception.JSendResponse;
import com.example.biblioteca.model.Autor;
import com.example.biblioteca.repository.AutorRepository;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroService libroService;

    // Crear
    public ResponseEntity<JSendResponse> crearAutor(CrearAutorDTO dto) {
        boolean existe = autorRepository
                .findAll()
                .stream()
                .anyMatch(a -> a.getNombre().equalsIgnoreCase(dto.getNombre())
                        && a.getApellido().equalsIgnoreCase(dto.getApellido()));

        if (existe) {
            Map<String, String> error = Map.of("autorDuplicado", "Ya existe un autor con ese nombre y apellido.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.CONFLICT);
        }

        Autor nuevoAutor = new Autor(dto.getNombre(), dto.getApellido(), dto.getNacionalidad());
        Autor guardado = autorRepository.save(nuevoAutor);
        return new ResponseEntity<>(new JSendResponse("success", guardado, "Autor creado correctamente"),
                HttpStatus.CREATED);
    }

    // Obtener todos
    public ResponseEntity<JSendResponse> obtenerAutores() {
        List<Autor> autores = autorRepository.findAll();
        return ResponseEntity.ok(new JSendResponse("success", autores, null));
    }

    // Obtener por id
    public ResponseEntity<JSendResponse> obtenerPorId(Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        if (autor.isPresent()) {
            return ResponseEntity.ok(new JSendResponse("success", autor.get(), null));
        } else {
            Map<String, String> error = Map.of("autorId", "Autor con ID " + id + " no encontrado.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar
    public ResponseEntity<JSendResponse> actualizarAutor(Long id, CrearAutorDTO dto) {
        return autorRepository.findById(id)
                .map(autorExistente -> {
                    autorExistente.setNombre(dto.getNombre());
                    autorExistente.setApellido(dto.getApellido());
                    autorExistente.setNacionalidad(dto.getNacionalidad());
                    Autor actualizado = autorRepository.save(autorExistente);
                    return ResponseEntity
                            .ok(new JSendResponse("success", actualizado, "Autor actualizado correctamente"));
                })
                .orElseGet(() -> {
                    Map<String, String> error = Map.of("autorId",
                            "Autor con ID " + id + " no encontrado para actualizar.");
                    return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
                });
    }

    // Eliminar
    public ResponseEntity<JSendResponse> eliminarAutor(Long id) {
        Optional<Autor> autorOpt = autorRepository.findById(id);
        if (autorOpt.isEmpty()) {
            Map<String, String> error = Map.of("autorId", "Autor con ID " + id + " no encontrado para eliminar.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
        }

        if (!libroService.buscarLibrosPorAutor(id).isEmpty()) {
            Map<String, String> error = Map.of("relacion", "No se puede eliminar el autor, tiene libros asociados.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.CONFLICT);
        }

        autorRepository.deleteById(id);
        return new ResponseEntity<>(new JSendResponse("success", null, "Autor eliminado con éxito"),
                HttpStatus.NO_CONTENT);
    }

    // Buscar por nacionalidad
    public ResponseEntity<JSendResponse> buscarPorNacionalidad(String nacionalidad) {
        List<Autor> resultados = autorRepository.findByNacionalidad(nacionalidad);
        if (resultados.isEmpty()) {
            return ResponseEntity
                    .ok(new JSendResponse("success", List.of(), "No se encontraron autores con esa nacionalidad"));
        }
        return ResponseEntity.ok(new JSendResponse("success", resultados, "Autores encontrados"));
    }

    // Libros del autor
    public ResponseEntity<JSendResponse> obtenerLibrosPorAutorId(Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        if (autor.isEmpty()) {
            Map<String, String> error = Map.of("autorId", "Autor con ID " + id + " no encontrado.");
            return new ResponseEntity<>(new JSendResponse("fail", error, null), HttpStatus.NOT_FOUND);
        }

        List<ListaBreveLibrosDTO> libros = libroService.buscarLibrosPorAutor(id);
        return ResponseEntity.ok(new JSendResponse("success", libros, "Lista de libros del autor " + id));
    }
}
