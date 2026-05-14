package cl.duocuc.juegos.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.duocuc.juegos.domain.Juego;
import cl.duocuc.juegos.infrastructure.mapper.JuegoMapper;
import cl.duocuc.juegos.infrastructure.repository.JuegoRepository;

@Service
public class JuegoService {

    private final JuegoRepository repo;

    public JuegoService(JuegoRepository repo) {
        this.repo = repo;
    }

    public List<Juego> listar() {
        return repo.findAll().stream().map(JuegoMapper::toDomain).toList();
    }

    public Juego crear(Juego j) {
        return JuegoMapper.toDomain(repo.save(JuegoMapper.toEntity(j)));
    }

    public Juego actualizar(Long id, Juego j) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Juego no encontrado");
        }
        j.setId(id);
        return JuegoMapper.toDomain(repo.save(JuegoMapper.toEntity(j)));
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Juego no encontrado");
        }
        repo.deleteById(id);
    }
}
