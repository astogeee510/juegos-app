package cl.duocuc.juegos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import cl.duocuc.juegos.application.JuegoService;
import cl.duocuc.juegos.domain.Juego;
import cl.duocuc.juegos.infrastructure.entity.JuegoEntity;
import cl.duocuc.juegos.infrastructure.repository.JuegoRepository;

class JuegoServiceTest {

    private JuegoRepository repo;
    private JuegoService service;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(JuegoRepository.class);
        service = new JuegoService(repo);
    }

    @Test
    void listar_retornaListaMapeada() {
        JuegoEntity entity = new JuegoEntity(1L, "Zelda", "Aventura");
        when(repo.findAll()).thenReturn(List.of(entity));

        List<Juego> result = service.listar();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("Zelda", result.getFirst().getTitulo());
        assertEquals("Aventura", result.getFirst().getGenero());
        verify(repo, times(1)).findAll();
    }

    @Test
    void listar_retornaListaVacia() {
        when(repo.findAll()).thenReturn(List.of());

        List<Juego> result = service.listar();

        assertTrue(result.isEmpty());
    }

    @Test
    void crear_guardaYRetornaJuego() {
        Juego input = new Juego(null, "God of War", "Acción");
        JuegoEntity savedEntity = new JuegoEntity(2L, "God of War", "Acción");
        when(repo.save(any(JuegoEntity.class))).thenReturn(savedEntity);

        Juego result = service.crear(input);

        assertEquals(2L, result.getId());
        assertEquals("God of War", result.getTitulo());
        assertEquals("Acción", result.getGenero());
        verify(repo, times(1)).save(any(JuegoEntity.class));
    }

    @Test
    void actualizar_seteaIdYGuarda() {
        Juego input = new Juego(null, "FIFA 25", "Deportes");
        JuegoEntity savedEntity = new JuegoEntity(5L, "FIFA 25", "Deportes");
        when(repo.existsById(5L)).thenReturn(true);
        when(repo.save(any(JuegoEntity.class))).thenReturn(savedEntity);

        Juego result = service.actualizar(5L, input);

        assertEquals(5L, input.getId());
        assertEquals(5L, result.getId());
        assertEquals("FIFA 25", result.getTitulo());
        verify(repo, times(1)).save(any(JuegoEntity.class));
    }

    @Test
    void actualizar_lanzaExcepcionSiNoExiste() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.actualizar(99L, new Juego(null, "X", "Y")));
        verify(repo, never()).save(any());
    }

    @Test
    void eliminar_llamaDeleteById() {
        when(repo.existsById(3L)).thenReturn(true);
        doNothing().when(repo).deleteById(3L);

        service.eliminar(3L);

        verify(repo, times(1)).deleteById(3L);
    }

    @Test
    void eliminar_lanzaExcepcionSiNoExiste() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.eliminar(99L));
        verify(repo, never()).deleteById(any());
    }
}
