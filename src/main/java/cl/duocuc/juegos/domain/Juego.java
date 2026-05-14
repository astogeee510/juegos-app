package cl.duocuc.juegos.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Juego {
    private Long id;
    private String titulo;
    private String genero;
}
