package cl.duocuc.juegos.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duocuc.juegos.infrastructure.entity.JuegoEntity;

public interface JuegoRepository extends JpaRepository<JuegoEntity, Long> {}
