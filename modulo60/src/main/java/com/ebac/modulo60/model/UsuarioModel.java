package com.ebac.modulo60.model;

import com.ebac.modulo60.dto.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class UsuarioModel {

    private EntityManager entityManager;

    // Constructor
    public UsuarioModel(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // READ: Consultar usuarios registrados
    public List<Usuario> obtenerUsuarios() {
        return entityManager
                .createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
    }

    // CREATE: Registrar usuario evitando duplicados
    public boolean guardarUsuario(Usuario usuario) {

        // Validar si el usuario ya existe
        Long existe = entityManager.createQuery(
                        "SELECT COUNT(u) FROM Usuario u " +
                                "WHERE LOWER(TRIM(u.nombre)) = LOWER(TRIM(:nombre)) " +
                                "AND u.edad = :edad",
                        Long.class
                )
                .setParameter("nombre", usuario.getNombre())
                .setParameter("edad", usuario.getEdad())
                .getSingleResult();

        if (existe > 0) {
            System.out.println("El usuario ya existe. No se realizó el registro.");
            return false;
        }

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.persist(usuario);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }


    public boolean actualizarUsuario(Usuario usuario) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.merge(usuario);

            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public int eliminarUsuario(int idUsuario) {

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Buscar al usuario por su ID
            Usuario usuario = entityManager.find(Usuario.class, idUsuario);

            if (usuario == null) {
                transaction.rollback();
                return -1;
            }

            // Eliminar todas las direcciones asociadas al usuario
            int direccionesEliminadas = entityManager.createQuery(
                            "DELETE FROM Direccion d WHERE d.idUsuario = :idUsuario"
                    )
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();

            // Eliminar al usuario
            entityManager.remove(usuario);

            // Confirmar ambas operaciones
            transaction.commit();

            return direccionesEliminadas;

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return -1;
        }
    }
}
