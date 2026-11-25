package com.ebac.modulo60;

import com.ebac.modulo60.dto.Usuario;
import com.ebac.modulo60.dto.Direccion;
import com.ebac.modulo60.model.UsuarioModel;
import com.ebac.modulo60.model.DireccionModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class Contexto {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("coneccionLocalMySQL");

        try (EntityManager entityManager = emf.createEntityManager();
             Scanner scanner = new Scanner(System.in)) {

            UsuarioModel usuarioModel = new UsuarioModel(entityManager);
            DireccionModel direccionModel = new DireccionModel(entityManager);

            int opcion;

            do {
                System.out.println("\n========== SISTEMA CRUD ==========");
                System.out.println("       GESTIÓN DE USUARIOS");
                System.out.println("==================================");
                System.out.println("1. Consultar usuarios");
                System.out.println("2. Registrar usuario");
                System.out.println("3. Actualizar usuario");
                System.out.println("4. Eliminar usuario");

                System.out.println("----------------------------------");
                System.out.println("     GESTIÓN DE DIRECCIONES");
                System.out.println("----------------------------------");
                System.out.println("5. Registrar dirección");
                System.out.println("6. Consultar direcciones");
                System.out.println("7. Actualizar dirección");
                System.out.println("8. Eliminar dirección");

                System.out.println("----------------------------------");
                System.out.println("0. Salir");
                System.out.println("==================================");

                Integer seleccion = leerEnteroCancelable(
                        scanner, "Selecciona una opción: ", false);

                if (seleccion == null) {
                    opcion = 0;
                    System.out.println("\nSaliendo del sistema...");
                    continue;
                }

                opcion = seleccion;

                switch (opcion) {

                    // READ: Consultar usuarios
                    case 1 -> {
                        mostrarUsuarios(usuarioModel.obtenerUsuarios());
                    }

                    // CREATE: Registrar usuario
                    case 2 -> {
                        System.out.println("\n===== REGISTRAR USUARIO =====");
                        mostrarUsuarios(usuarioModel.obtenerUsuarios());
                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        String nombre = leerTextoCancelable(scanner, "Ingresa el nombre: ");
                        if (verificarCancelacion(nombre, "Registro de usuario cancelado.")) {
                            break;
                        }

                        Integer edad = leerEnteroCancelable(scanner, "Ingresa la edad: ", true);
                        if (verificarCancelacion(edad, "Registro de usuario cancelado.")) {
                            break;
                        }

                        Usuario nuevoUsuario = new Usuario();
                        nuevoUsuario.setNombre(nombre);
                        nuevoUsuario.setEdad(edad);

                        boolean registrado = usuarioModel.guardarUsuario(nuevoUsuario);

                        if (registrado) {
                            System.out.println("\nUsuario registrado correctamente.");
                            mostrarUsuarioDetalle(nuevoUsuario);
                        } else {
                            System.out.println("No se pudo registrar el usuario.");
                        }
                    }

                    // UPDATE: Actualizar usuario
                    case 3 -> {
                        System.out.println("\n===== ACTUALIZAR USUARIO =====");
                        System.out.println("Escribe 0 en cualquier momento para cancelar.");

                        Usuario usuarioActualizar = solicitarUsuarioExistente(
                                scanner, usuarioModel, entityManager);

                        if (verificarCancelacion(usuarioActualizar,
                                "Actualización de usuario cancelada.")) {
                            break;
                        }

                        // Mostrar los datos actuales del usuario.
                        System.out.println("\n===== USUARIO ACTUAL =====");
                        mostrarUsuarioDetalle(usuarioActualizar);

                        // Selección de los campos que se actualizarán.
                        System.out.println("\nSelecciona los campos que deseas actualizar:");
                        System.out.println("1. Nombre");
                        System.out.println("2. Edad");
                        System.out.println("3. Todos los campos");
                        System.out.println("0. Cancelar operación");

                        System.out.println("\nEjemplos:");
                        System.out.println("1 = Actualizar solamente el nombre.");
                        System.out.println("2 = Actualizar solamente la edad.");
                        System.out.println("3 = Actualizar ambos campos.");
                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        String opcionesActualizarUsuario = leerTextoCancelable(
                                scanner, "\nIngresa tu selección: ");

                        if (verificarCancelacion(opcionesActualizarUsuario,
                                "Actualización de usuario cancelada.")) {
                            break;
                        }

                        boolean actualizarNombre = false;
                        boolean actualizarEdad = false;

                        switch (opcionesActualizarUsuario.trim()) {
                            case "1" -> actualizarNombre = true;
                            case "2" -> actualizarEdad = true;
                            case "3" -> {
                                actualizarNombre = true;
                                actualizarEdad = true;
                            }
                            default -> {
                                System.out.println("Selección no válida.");
                                break;
                            }
                        }

                        if (!actualizarNombre && !actualizarEdad) {
                            break;
                        }

                        // Solicitar el nuevo nombre.
                        if (actualizarNombre) {
                            String nuevoNombre = leerTextoCancelable(
                                    scanner, "Ingresa el nuevo nombre: ");

                            if (verificarCancelacion(nuevoNombre,
                                    "Actualización de usuario cancelada.")) {
                                break;
                            }

                            usuarioActualizar.setNombre(nuevoNombre);
                        }

                        // Solicitar la nueva edad.
                        if (actualizarEdad) {
                            Integer nuevaEdad = leerEnteroCancelable(
                                    scanner, "Ingresa la nueva edad: ", true);

                            if (verificarCancelacion(nuevaEdad,
                                    "Actualización de usuario cancelada.")) {
                                break;
                            }

                            usuarioActualizar.setEdad(nuevaEdad);
                        }

                        // Mostrar los datos antes de confirmar la actualización.
                        System.out.println("\n===== DATOS ACTUALIZADOS =====");
                        mostrarUsuarioDetalle(usuarioActualizar);

                        String confirmacion = leerConfirmacionCancelable(
                                scanner, "\n¿Confirmas la actualización? (S/N): ");

                        if (verificarCancelacion(confirmacion,
                                "Actualización de usuario cancelada.")
                                || confirmacion.equalsIgnoreCase("N")) {
                            System.out.println("Actualización cancelada.");
                            break;
                        }

                        // Guardar los cambios en la base de datos.
                        boolean actualizado =
                                usuarioModel.actualizarUsuario(usuarioActualizar);

                        if (actualizado) {
                            System.out.println("\nUsuario actualizado correctamente.");
                            mostrarUsuarioDetalle(usuarioActualizar);
                        } else {
                            System.out.println("No fue posible actualizar el usuario.");
                        }
                    }


                    // DELETE: Eliminar usuario
                    case 4 -> {
                        System.out.println("\n===== ELIMINAR USUARIO =====");
                        System.out.println("Escribe 0 en cualquier momento para cancelar.");

                        Usuario usuarioEliminar = solicitarUsuarioExistente(
                                scanner, usuarioModel, entityManager);

                        if (verificarCancelacion(usuarioEliminar,
                                "Eliminación de usuario cancelada.")) {
                            break;
                        }

                        // Mostrar los datos del usuario que se eliminará.

                        mostrarUsuarioDetalle(usuarioEliminar);

                        String confirmacion = leerConfirmacionCancelable(
                                scanner,
                                "¿Confirmas que deseas eliminarlo? (S/N): ");

                        if (verificarCancelacion(confirmacion,
                                "Eliminación de usuario cancelada.")
                                || confirmacion.equalsIgnoreCase("N")) {
                            System.out.println("Operación cancelada.");
                            break;
                        }

                        int resultado = usuarioModel.eliminarUsuario(
                                usuarioEliminar.getIdUsuario());

                        if (resultado >= 0) {
                            System.out.println("\nUsuario eliminado correctamente.");

                            if (resultado > 0) {
                                System.out.println(
                                        "También se eliminaron " + resultado +
                                                " dirección(es) asociada(s).");
                            }
                        } else {
                            System.out.println("No fue posible eliminar el usuario.");
                        }
                    }

                    // CREATE: Registrar dirección
                    case 5 -> {

                        System.out.println("\n===== REGISTRAR DIRECCIÓN =====");
                        System.out.println("Escribe 0 en cualquier momento para cancelar.");

                        Usuario usuarioEncontrado = solicitarUsuarioExistente(
                                scanner, usuarioModel, entityManager);

                        if (verificarCancelacion(usuarioEncontrado,
                                "Registro de dirección cancelado.")) {
                            break;
                        }

                        // Mostrar información del usuario seleccionado
                        mostrarUsuarioDetalle(usuarioEncontrado);

                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        String calle = leerTextoCancelable(scanner, "Ingresa la calle: ");

                        if (verificarCancelacion(calle, "Registro de dirección cancelado.")) {
                            break;
                        }

                        String numero = leerTextoCancelable(
                                scanner, "Ingresa el número (o S/N si no tiene): ");

                        if (verificarCancelacion(numero,
                                "Registro de dirección cancelado.")) {
                            break;
                        }

                        String estado = leerTextoCancelable(scanner, "Ingresa el estado: ");

                        if (verificarCancelacion(estado,
                                "Registro de dirección cancelado.")) {
                            break;
                        }

                        Direccion direccion = new Direccion();
                        direccion.setIdUsuario(usuarioEncontrado.getIdUsuario());
                        direccion.setCalle(calle);
                        direccion.setNumero(numero);
                        direccion.setEstado(estado);

                        boolean registrada = direccionModel.guardar(direccion);

                        if (registrada) {
                            System.out.println("\nDirección registrada correctamente.");
                            System.out.println("ID de la dirección: " + direccion.getIdDireccion());
                            System.out.println("Usuario relacionado: " + usuarioEncontrado.getNombre());
                        } else {
                            System.out.println("No fue posible registrar la dirección.");
                        }
                    }

                    // READ: Consultar direcciones
                    case 6 -> {
                        mostrarDireccionesConUsuario(direccionModel);
                    }

                    // UPDATE: Actualizar dirección
                    case 7 -> {

                        System.out.println("\n===== ACTUALIZAR DIRECCIÓN =====");
                        System.out.println("Escribe 0 en cualquier momento para cancelar.");

                        Direccion direccionActualizar = solicitarDireccionExistente(
                                scanner, direccionModel);

                        if (verificarCancelacion(direccionActualizar,
                                "Actualización de dirección cancelada.")) {
                            break;
                        }

                        // Mostrar los datos actuales de la dirección.
                        System.out.println("\n===== DIRECCIÓN SELECCIONADA =====");
                        System.out.println("Calle: " + direccionActualizar.getCalle());
                        System.out.println("Número: " + direccionActualizar.getNumero());
                        System.out.println("Estado: " + direccionActualizar.getEstado());

                        // Selección de los campos que se actualizarán.
                        System.out.println("\nSelecciona los campos que deseas actualizar:");
                        System.out.println("1. Calle");
                        System.out.println("2. Número");
                        System.out.println("3. Estado");
                        System.out.println("4. Todos los campos");
                        System.out.println("0. Cancelar operación");

                        System.out.println("\nPuedes seleccionar varias opciones separándolas por comas.");
                        System.out.println("\nEjemplos:");
                        System.out.println("1 = Actualizar solamente la calle.");
                        System.out.println("1,2 = Actualizar calle y número.");
                        System.out.println("2,3 = Actualizar número y estado.");
                        System.out.println("4 = Actualizar todos los campos.");

                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        String opcionesActualizarDireccion = leerTextoCancelable(
                                scanner, "\nIngresa tu selección: ");

                        if (verificarCancelacion(opcionesActualizarDireccion,
                                "Actualización de dirección cancelada.")) {
                            break;
                        }

                        boolean actualizarCalle = false;
                        boolean actualizarNumero = false;
                        boolean actualizarEstado = false;
                        boolean seleccionInvalida = false;

                        // Identificar los campos seleccionados.
                        String[] opcionesDireccion = opcionesActualizarDireccion.split(",");

                        // Identificar los campos seleccionados.
                        for (String opcionSeleccionadaDireccion : opcionesDireccion) {
                            switch (opcionSeleccionadaDireccion.trim()) {
                                case "1" -> actualizarCalle = true;
                                case "2" -> actualizarNumero = true;
                                case "3" -> actualizarEstado = true;
                                case "4" -> {
                                    if (opcionesDireccion.length == 1) {
                                        actualizarCalle = true;
                                        actualizarNumero = true;
                                        actualizarEstado = true;
                                    } else {
                                        seleccionInvalida = true;
                                    }
                                }
                                default -> seleccionInvalida = true;
                            }
                        }

                        // Solicitar la nueva calle.
                        if (actualizarCalle) {
                            String nuevaCalle = leerTextoCancelable(
                                    scanner, "Ingresa la nueva calle: ");

                            if (verificarCancelacion(nuevaCalle,
                                    "Actualización de dirección cancelada.")) {
                                break;
                            }

                            direccionActualizar.setCalle(nuevaCalle);
                        }

                        // Solicitar el nuevo número.
                        if (actualizarNumero) {
                            String nuevoNumero = leerTextoCancelable(
                                    scanner, "Ingresa el nuevo número: ");

                            if (verificarCancelacion(nuevoNumero,
                                    "Actualización de dirección cancelada.")) {
                                break;
                            }

                            direccionActualizar.setNumero(nuevoNumero);
                        }

                        // Solicitar el nuevo estado.
                        if (actualizarEstado) {
                            String nuevoEstado = leerTextoCancelable(
                                    scanner, "Ingresa el nuevo estado: ");

                            if (verificarCancelacion(nuevoEstado,
                                    "Actualización de dirección cancelada.")) {
                                break;
                            }

                            direccionActualizar.setEstado(nuevoEstado);
                        }

                        // Mostrar los datos antes de confirmar la actualización.
                        System.out.println("\n===== DATOS ACTUALIZADOS =====");
                        System.out.println("Calle: " + direccionActualizar.getCalle());
                        System.out.println("Número: " + direccionActualizar.getNumero());
                        System.out.println("Estado: " + direccionActualizar.getEstado());

                        String confirmacion = leerConfirmacionCancelable(
                                scanner, "\n¿Deseas continuar con el registro de la dirección? (S/N): ");

                        if (verificarCancelacion(confirmacion,
                                "Registro de dirección cancelado.")
                                || confirmacion.equalsIgnoreCase("N")) {
                            System.out.println("Registro de dirección cancelado.");
                            break;
                        }

                        // Guardar los cambios en la base de datos.
                        boolean actualizado = direccionModel.actualizar(direccionActualizar);

                        if (actualizado) {
                            System.out.println("\nDirección actualizada correctamente.");

                            System.out.println("\n===== DETALLE DE LA DIRECCIÓN =====");
                            System.out.println("Calle: " + direccionActualizar.getCalle());
                            System.out.println("Número: " + direccionActualizar.getNumero());
                            System.out.println("Estado: " + direccionActualizar.getEstado());

                        } else {
                            System.out.println("No fue posible actualizar la dirección.");
                        }
                    }

                    // DELETE: Eliminar dirección
                    case 8 -> {
                        System.out.println("\n===== ELIMINAR DIRECCIÓN =====");
                        System.out.println("Escribe 0 en cualquier momento para cancelar.");

                        Direccion direccionEliminar = solicitarDireccionExistente(
                                scanner, direccionModel);

                        if (verificarCancelacion(direccionEliminar,
                                "Eliminación de dirección cancelada.")) {
                            break;
                        }

                        // Mostrar todos los datos de la dirección seleccionada.
                        mostrarDireccionDetalle(direccionEliminar, entityManager);

                        // Confirmar la eliminación.
                        String confirmacion = leerConfirmacionCancelable(
                                scanner,
                                "\n¿Confirmas que deseas eliminar esta dirección? (S/N): ");

                        if (verificarCancelacion(confirmacion,
                                "Eliminación de dirección cancelada.")
                                || confirmacion.equalsIgnoreCase("N")) {
                            System.out.println("Eliminación de dirección cancelada.");
                            break;
                        }

                        // Eliminar únicamente la dirección seleccionada.
                        boolean eliminada = direccionModel.eliminar(
                                direccionEliminar.getIdDireccion());

                        if (eliminada) {
                            System.out.println("\nDirección eliminada correctamente.");
                            System.out.println("El usuario relacionado permanece registrado.");
                        } else {
                            System.out.println("No fue posible eliminar la dirección.");
                        }
                    }

                    case 0 -> System.out.println("\nSaliendo del sistema...");

                    default -> System.out.println("Opción no válida.");
                }

                if (opcion != 0) {
                    System.out.println("\n========== CONTINUAR ==========");
                    System.out.println("1. Regresar al menú principal");
                    System.out.println("0. Salir del sistema");
                    System.out.println("================================");

                    Integer continuar = leerEnteroCancelable(
                            scanner, "Selecciona una opción: ", false);

                    if (continuar == null || continuar == 0) {
                        opcion = 0;
                        System.out.println("\nSaliendo del sistema...");
                    }
                }

            } while (opcion != 0);

        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (emf.isOpen()) {
                emf.close();
            }
        }
    }


    /**
     * Muestra los usuarios existentes para identificar el ID antes de operar.
     */
    private static void mostrarUsuarios(List<Usuario> usuarios) {
        System.out.println("\n===== USUARIOS REGISTRADOS =====");

        if (usuarios.isEmpty()) {
            System.out.println("No existen usuarios registrados.");
            return;
        }

        for (Usuario usuario : usuarios) {
            System.out.println(
                    "ID: " + usuario.getIdUsuario() +
                            " | Nombre: " + usuario.getNombre() +
                            " | Edad: " + usuario.getEdad());
        }
    }


    /**
     * Muestra los usuarios y solicita un ID válido.
     * Repite la solicitud hasta encontrarlo o cancelar con 0.
     */
    private static Usuario solicitarUsuarioExistente(
            Scanner scanner,
            UsuarioModel usuarioModel,
            EntityManager entityManager) {

        List<Usuario> usuarios = usuarioModel.obtenerUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No existen usuarios registrados.");
            return null;
        }

        mostrarUsuarios(usuarios);

        while (true) {
            Integer id = leerEnteroCancelable(
                    scanner, "\nIngresa el ID del usuario (0 para cancelar): ", true);

            if (id == null) {
                return null;
            }

            Usuario usuario = entityManager.find(Usuario.class, id);

            if (usuario != null) {
                return usuario;
            }

            System.out.println("No existe un usuario con ese ID. Intenta nuevamente.");
        }
    }

    /**
     * Muestra las direcciones y solicita un ID válido.
     * Repite la solicitud hasta encontrarla o cancelar con 0.
     */
    private static Direccion solicitarDireccionExistente(
            Scanner scanner,
            DireccionModel direccionModel) {

        if (!mostrarDireccionesConUsuario(direccionModel)) {
            return null;
        }

        while (true) {
            Integer id = leerEnteroCancelable(
                    scanner, "\nIngresa el ID de la dirección (0 para cancelar): ", true);

            if (id == null) {
                return null;
            }

            Direccion direccion = direccionModel.obtenerPorId(id);

            if (direccion != null) {
                return direccion;
            }

            System.out.println("No existe una dirección con ese ID. Intenta nuevamente.");
        }
    }


    /**
     * Lee texto. Si el usuario escribe 0, devuelve null para cancelar.
     */
    private static String leerTextoCancelable(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String valor = scanner.nextLine().trim();
        return valor.equals("0") ? null : valor;
    }

    /**
     * Verifica si una captura fue cancelada.
     * Si recibe null, muestra el mensaje correspondiente.
     */
    private static boolean verificarCancelacion(Object valor, String mensaje) {

        if (valor == null) {
            System.out.println(mensaje);
            return true;
        }

        return false;
    }

    /**
     * Lee un entero. Si el usuario escribe 0, devuelve null cuando
     * cero se utiliza como opción de cancelación.
     */
    private static Integer leerEnteroCancelable(
            Scanner scanner, String mensaje, boolean ceroCancela) {

        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            if (ceroCancela && entrada.equals("0")) {
                return null;
            }

            try {
                int valor = Integer.parseInt(entrada);

                if (!ceroCancela && valor < 0) {
                    System.out.println("Ingresa una opción válida.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar un número válido. Escribe 0 para cancelar.");
            }
        }
    }

    /**
     * Lee S, N o 0. Devuelve null cuando se cancela con 0.
     */
    private static String leerConfirmacionCancelable(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = scanner.nextLine().trim();

            if (respuesta.equals("0")) {
                return null;
            }

            if (respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("N")) {
                return respuesta;
            }

            System.out.println("Respuesta no válida. Escribe S, N o 0 para cancelar.");
        }
    }


    private static boolean mostrarDireccionesConUsuario(
            DireccionModel direccionModel) {

        List<Object[]> resultados =
                direccionModel.obtenerDireccionesConUsuario();

        System.out.println("\n===== DIRECCIONES REGISTRADAS =====");

        if (resultados.isEmpty()) {
            System.out.println("No existen direcciones registradas.");
            return false;
        }

        for (Object[] resultado : resultados) {

            Direccion direccion = (Direccion) resultado[0];
            String nombreUsuario = (String) resultado[1];

            System.out.println(
                    "ID: " + direccion.getIdDireccion() +
                            " | ID Usuario: " + direccion.getIdUsuario() +
                            " | Usuario: " + nombreUsuario +
                            " | Calle: " + direccion.getCalle() +
                            " | Número: " + direccion.getNumero() +
                            " | Estado: " + direccion.getEstado()
            );
        }

        return true;
    }

    /**
     * Muestra todos los datos de la dirección y su usuario relacionado.
     */
    private static void mostrarDireccionDetalle(
            Direccion direccion, EntityManager entityManager) {

        Usuario usuario = entityManager.find(
                Usuario.class, direccion.getIdUsuario());

        System.out.println("\n===== DIRECCIÓN SELECCIONADA =====");
        System.out.println(
                "ID: " + direccion.getIdDireccion() +
                        " | ID Usuario: " + direccion.getIdUsuario() +
                        " | Usuario: " + (usuario != null ? usuario.getNombre() : "No encontrado") +
                        " | Calle: " + direccion.getCalle() +
                        " | Número: " + direccion.getNumero() +
                        " | Estado: " + direccion.getEstado()
        );
    }

    // Muestra los datos principales de un usuario.
    private static void mostrarUsuarioDetalle(Usuario usuario) {

        System.out.println("\n===== DETALLE DEL USUARIO =====");
        System.out.println("ID: " + usuario.getIdUsuario());
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Edad: " + usuario.getEdad());
    }
}