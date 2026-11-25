
package com.ebac.modulo60.model;

import com.ebac.modulo60.dto.Direccion;
import com.ebac.modulo60.dto.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;


import java.util.List;

public class DireccionModel {

    private final EntityManager entityManager;

    public DireccionModel(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // CREATE: Guardar dirección
    public boolean guardar(Direccion direccion) {

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.persist(direccion);
            entityManager.flush();

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error al guardar dirección:");
            e.printStackTrace();

            return false;
        }
    }

    // READ: Obtener dirección por ID
    public Direccion obtenerPorId(int id) {

        return entityManager.find(Direccion.class, id);
    }

    // READ: Obtener todas las direcciones
    public List<Direccion> obtenerDirecciones() {

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Direccion> cq =
                cb.createQuery(Direccion.class);

        Root<Direccion> root =
                cq.from(Direccion.class);

        cq.select(root);

        TypedQuery<Direccion> query =
                entityManager.createQuery(cq);

        return query.getResultList();
    }

    // READ: Obtener direcciones junto con el nombre del usuario (INNER JOIN)
    public List<Object[]> obtenerDireccionesConUsuario() {

        String jpql = """
            SELECT d, u.nombre
            FROM Direccion d
            INNER JOIN Usuario u
                ON d.idUsuario = u.idUsuario
            """;

        return entityManager.createQuery(jpql, Object[].class)
                .getResultList();
    }

    // UPDATE: Actualizar dirección
    public boolean actualizar(Direccion direccion) {

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            Direccion existente = entityManager.find(
                    Direccion.class,
                    direccion.getIdDireccion()
            );

            if (existente == null) {
                transaction.rollback();
                return false;
            }

            entityManager.merge(direccion);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error al actualizar dirección:");
            e.printStackTrace();

            return false;
        }
    }

    // DELETE: Eliminar dirección por ID
    public boolean eliminar(int id) {

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            Direccion direccion =
                    entityManager.find(Direccion.class, id);

            if (direccion == null) {
                transaction.rollback();
                return false;
            }

            entityManager.remove(direccion);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error al eliminar dirección:");
            e.printStackTrace();

            return false;
        }
    }
}
