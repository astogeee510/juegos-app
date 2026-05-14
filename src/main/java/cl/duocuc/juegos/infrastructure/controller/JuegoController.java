package cl.duocuc.juegos.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import cl.duocuc.juegos.application.JuegoService;
import cl.duocuc.juegos.domain.Juego;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/juegos")
@Tag(name = "Juegos", description = "Operaciones CRUD y CSV")
public class JuegoController {

    private final JuegoService service;

    public JuegoController(JuegoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar juegos")
    @GetMapping
    public List<Juego> listar() {
        return service.listar();
    }

    @Operation(summary = "Crear juego")
    @PostMapping
    public Juego crear(@RequestBody Juego j) {
        return service.crear(j);
    }

    @Operation(summary = "Actualizar juego")
    @PutMapping("/{id}")
    public Juego actualizar(@PathVariable Long id, @RequestBody Juego j) {
        return service.actualizar(id, j);
    }

    @Operation(summary = "Eliminar juego")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @Operation(summary = "Exportar juegos a CSV")
    @GetMapping("/export")
    public String exportar() {
        return service.listar().stream()
                .map(j -> j.getTitulo() + "," + j.getGenero())
                .collect(Collectors.joining("\n"));
    }

    @Operation(summary = "Importar juegos desde CSV")
    @PostMapping("/import")
    public void importar(@RequestBody String csv) {
        java.util.Arrays.stream(csv.split("\n"))
                .map(line -> line.split(","))
                .filter(parts -> parts.length >= 2)
                .map(parts -> new Juego(null, parts[0], parts[1]))
                .forEach(service::crear);
    }
}
