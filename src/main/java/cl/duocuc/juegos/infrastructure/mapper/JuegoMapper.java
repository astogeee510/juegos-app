package cl.duocuc.juegos.infrastructure.mapper;

import cl.duocuc.juegos.domain.Juego;
import cl.duocuc.juegos.infrastructure.entity.JuegoEntity;

public final class JuegoMapper {

    private JuegoMapper() {
        // clase de utilidad, no instanciar
    }

    public static Juego toDomain(JuegoEntity e) {
        return new Juego(e.getId(), e.getTitulo(), e.getGenero());
    }

    public static JuegoEntity toEntity(Juego j) {
        return new JuegoEntity(j.getId(), j.getTitulo(), j.getGenero());
    }
}
