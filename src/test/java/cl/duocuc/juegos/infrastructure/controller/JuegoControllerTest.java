package cl.duocuc.juegos.infrastructure.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duocuc.juegos.application.JuegoService;
import cl.duocuc.juegos.domain.Juego;

@WebMvcTest(JuegoController.class)
@WithMockUser
class JuegoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private JuegoService service;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void listar_retorna200ConLista() throws Exception {
        when(service.listar())
                .thenReturn(
                        List.of(new Juego(1L, "Zelda", "Aventura"), new Juego(2L, "FIFA 25", "Deportes")));

        mockMvc.perform(get("/juegos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Zelda"))
                .andExpect(jsonPath("$[1].titulo").value("FIFA 25"));
    }

    @Test
    void crear_retorna200ConJuegoCreado() throws Exception {
        Juego input = new Juego(null, "Minecraft", "Sandbox");
        Juego created = new Juego(3L, "Minecraft", "Sandbox");
        when(service.crear(any(Juego.class))).thenReturn(created);

        mockMvc.perform(
                        post("/juegos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.titulo").value("Minecraft"));
    }

    @Test
    void actualizar_retorna200ConJuegoActualizado() throws Exception {
        Juego input = new Juego(null, "Among Us", "Social");
        Juego updated = new Juego(4L, "Among Us", "Social");
        when(service.actualizar(eq(4L), any(Juego.class))).thenReturn(updated);

        mockMvc.perform(
                        put("/juegos/4")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.titulo").value("Among Us"));
    }

    @Test
    void eliminar_retorna200() throws Exception {
        doNothing().when(service).eliminar(5L);

        mockMvc.perform(delete("/juegos/5").with(csrf())).andExpect(status().isOk());

        verify(service, times(1)).eliminar(5L);
    }

    @Test
    void exportar_retornaCSV() throws Exception {
        when(service.listar())
                .thenReturn(
                        List.of(new Juego(1L, "Zelda", "Aventura"), new Juego(2L, "FIFA 25", "Deportes")));

        mockMvc.perform(get("/juegos/export"))
                .andExpect(status().isOk())
                .andExpect(content().string("Zelda,Aventura\nFIFA 25,Deportes"));
    }

    @Test
    void exportar_listaVaciaRetornaCadenaVacia() throws Exception {
        when(service.listar()).thenReturn(List.of());

        mockMvc.perform(get("/juegos/export"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void importar_procesaCSVCorrectamente() throws Exception {
        String csv = "Zelda,Aventura\nFIFA 25,Deportes";
        when(service.crear(any(Juego.class))).thenReturn(new Juego(1L, "Zelda", "Aventura"));

        mockMvc.perform(
                        post("/juegos/import")
                                .with(csrf())
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(csv))
                .andExpect(status().isOk());

        verify(service, times(2)).crear(any(Juego.class));
    }

    @Test
    void importar_ignoraLineasMalformadas() throws Exception {
        String csv = "SoloTitulo\nZelda,Aventura";
        when(service.crear(any(Juego.class))).thenReturn(new Juego(1L, "Zelda", "Aventura"));

        mockMvc.perform(
                        post("/juegos/import")
                                .with(csrf())
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(csv))
                .andExpect(status().isOk());

        verify(service, times(1)).crear(any(Juego.class));
    }
}
