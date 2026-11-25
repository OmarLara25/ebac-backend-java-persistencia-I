package com.ebac.modulo60.mongodb;


import com.ebac.modulo60.mongodb.dto.Telefono;
import com.ebac.modulo60.mongodb.model.TelefonoModel;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

public class Contexto {

    public static void main(String[] args) {

        MongoClient mongoClient = null;

        try (Scanner sc = new Scanner(System.in)) {

            // CONEXIÓN A MONGODB
            mongoClient = MongoClients.create("mongodb://root:toor@localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("modulo60");

            // Verificar conexión
            mongoClient.getDatabase("admin").runCommand(new Document("ping", 1));

            System.out.println("Conexión a MongoDB establecida correctamente.");

            TelefonoModel telefonoModel = new TelefonoModel(database);

            int opcion;

            do {
                System.out.println("\n================================");
                System.out.println("   SISTEMA DE GESTIÓN DE TELÉFONOS");
                System.out.println("================================");
                System.out.println("1. Registrar teléfono");
                System.out.println("2. Consultar todos los teléfonos");
                System.out.println("3. Buscar teléfono por ID");
                System.out.println("4. Actualizar teléfono");
                System.out.println("5. Eliminar teléfono");
                System.out.println("0. Salir");
                System.out.println("================================");

                Integer seleccion = leerEnteroCancelable(
                        sc, "Selecciona una opción: ", false);

                if (seleccion == null) {
                    opcion = 0;
                    System.out.println("\nSaliendo del sistema...");
                    continue;
                }

                opcion = seleccion;

                switch (opcion) {

                    // CREATE: REGISTRAR TELÉFONO
                    case 1: {
                        System.out.println("\n===== REGISTRAR TELÉFONO =====");

                        // MOSTRAR LOS TELÉFONOS REGISTRADOS EN MONGODB
                        System.out.println("\n===== TELÉFONOS REGISTRADOS =====");

                        List<Telefono> telefonos = telefonoModel.obtenerTelefonos();

                        if (telefonos.isEmpty()) {
                            System.out.println("No hay teléfonos registrados.");
                        } else {
                            telefonos.forEach(System.out::println);
                        }
                        System.out.println();

                        System.out.println("Escribe 0 en cualquier momento para cancelar.");

                        System.out.println();

                        Integer idUsuario = leerEnteroCancelable(
                                sc, "Introduce el ID del usuario: ", true);

                        if (verificarCancelacion(idUsuario, "Registro de teléfono cancelado.")) {
                            break;
                        }

                        String tipo = leerTextoCancelable(
                                sc, "Introduce el tipo de teléfono: ");

                        if (verificarCancelacion(tipo, "Registro de teléfono cancelado.")) {
                            break;
                        }

                        String numero = leerTextoCancelable(
                                sc, "Introduce el número telefónico: ");

                        if (verificarCancelacion(numero, "Registro de teléfono cancelado.")) {
                            break;
                        }

                        Telefono telefono = new Telefono(idUsuario, tipo, numero);
                        boolean guardado = telefonoModel.guardar(telefono);

                        if (guardado) {
                            System.out.println("\nTeléfono guardado correctamente.");
                            System.out.println(telefono);
                        } else {
                            System.out.println("No fue posible guardar el teléfono.");
                        }

                        break;
                    }

                    // READ ALL: CONSULTAR TODOS LOS TELÉFONOS
                    case 2: {
                        System.out.println("\n===== TELÉFONOS REGISTRADOS =====");

                        List<Telefono> telefonos = telefonoModel.obtenerTelefonos();

                        if (telefonos.isEmpty()) {
                            System.out.println("No hay teléfonos registrados.");
                        } else {
                            telefonos.forEach(System.out::println);
                        }

                        break;
                    }

                    // READ BY ID: BUSCAR TELÉFONO
                    case 3: {
                        System.out.println("\n===== BUSCAR TELÉFONO POR ID =====");
                        System.out.println("Escribe 0 para cancelar.");

                        String id = leerTextoCancelable(
                                sc, "Introduce el ID del teléfono: ");

                        if (verificarCancelacion(id, "Búsqueda de teléfono cancelada.")) {
                            break;
                        }

                        Telefono telefonoEncontrado = telefonoModel.obtenerPorId(id);

                        if (telefonoEncontrado != null) {
                            System.out.println("\n===== TELÉFONO ENCONTRADO =====");
                            System.out.println(telefonoEncontrado);
                        } else {
                            System.out.println("No se encontró el teléfono.");
                        }

                        break;
                    }

                    // UPDATE: ACTUALIZAR TELÉFONO
                    case 4: {
                        System.out.println("\n===== ACTUALIZAR TELÉFONO =====");

                        List<Telefono> listaTelefonos = telefonoModel.obtenerTelefonos();

                        if (listaTelefonos.isEmpty()) {
                            System.out.println("No hay teléfonos registrados.");
                            break;
                        }

                        System.out.println("\n===== TELÉFONOS REGISTRADOS =====");
                        listaTelefonos.forEach(System.out::println);

                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        Telefono telefonoActualizar = null;

                        while (telefonoActualizar == null) {

                            String idActualizar = leerTextoCancelable(
                                    sc, "\nIntroduce el ID del teléfono que deseas actualizar: ");

                            if (verificarCancelacion(idActualizar,
                                    "Actualización de teléfono cancelada.")) {
                                break;
                            }

                            telefonoActualizar = telefonoModel.obtenerPorId(idActualizar);

                            if (telefonoActualizar == null) {
                                System.out.println("No existe un teléfono con ese ID. Intenta nuevamente.");
                            }
                        }

                        if (telefonoActualizar == null) {
                            break;
                        }

                        System.out.println("\n===== TELÉFONO SELECCIONADO =====");
                        System.out.println(telefonoActualizar);

                        System.out.println("\nSelecciona los campos que deseas actualizar:");
                        System.out.println("1. ID Usuario");
                        System.out.println("2. Tipo de teléfono");
                        System.out.println("3. Número telefónico");
                        System.out.println("4. Todos los campos");
                        System.out.println("0. Cancelar operación");

                        System.out.println("\nPuedes seleccionar varias opciones separándolas por comas.");
                        System.out.println("Ejemplos:");
                        System.out.println("1 = Actualizar solamente el ID del usuario.");
                        System.out.println("1,2 = Actualizar ID de usuario y tipo.");
                        System.out.println("2,3 = Actualizar tipo y número.");
                        System.out.println("4 = Actualizar todos los campos.");
                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        String opciones = leerTextoCancelable(
                                sc, "\nIngresa tu selección: ");

                        if (verificarCancelacion(opciones,
                                "Actualización de teléfono cancelada.")) {
                            break;
                        }

                        String[] campos = opciones.split(",");
                        Set<String> camposSeleccionados = new LinkedHashSet<>();
                        boolean seleccionValida = true;

                        // VALIDAR CAMPOS Y EVITAR OPCIONES REPETIDAS
                        for (String campo : campos) {
                            String campoLimpio = campo.trim();

                            if (!campoLimpio.equals("1")
                                    && !campoLimpio.equals("2")
                                    && !campoLimpio.equals("3")
                                    && !campoLimpio.equals("4")) {
                                seleccionValida = false;
                                break;
                            }

                            if (!camposSeleccionados.add(campoLimpio)) {
                                seleccionValida = false;
                                System.out.println("No puedes repetir una opción: " + campoLimpio);
                                break;
                            }
                        }

                        if (camposSeleccionados.contains("4")) {
                            if (camposSeleccionados.size() == 1) {
                                camposSeleccionados.clear();
                                camposSeleccionados.addAll(Arrays.asList("1", "2", "3"));
                            } else {
                                seleccionValida = false;
                            }
                        }

                        if (!seleccionValida || camposSeleccionados.isEmpty()) {
                            System.out.println("Selección incorrecta. No se realizaron cambios.");
                            break;
                        }

                        // CAPTURAR LOS NUEVOS VALORES
                        boolean cancelarActualizacion = false;

                        for (String campo : camposSeleccionados) {
                            switch (campo) {
                                case "1": {
                                    Integer nuevoId = leerEnteroCancelable(
                                            sc, "Nuevo ID de usuario: ", true);

                                    if (verificarCancelacion(nuevoId,
                                            "Actualización de teléfono cancelada.")) {
                                        cancelarActualizacion = true;
                                    } else {
                                        telefonoActualizar.setIdUsuario(nuevoId);
                                    }
                                    break;
                                }

                                case "2": {
                                    String nuevoTipo = leerTextoCancelable(
                                            sc, "Nuevo tipo de teléfono: ");

                                    if (verificarCancelacion(nuevoTipo,
                                            "Actualización de teléfono cancelada.")) {
                                        cancelarActualizacion = true;
                                    } else {
                                        telefonoActualizar.setTipo(nuevoTipo);
                                    }
                                    break;
                                }

                                case "3": {
                                    String nuevoNumero = leerTextoCancelable(
                                            sc, "Nuevo número telefónico: ");

                                    if (verificarCancelacion(nuevoNumero,
                                            "Actualización de teléfono cancelada.")) {
                                        cancelarActualizacion = true;
                                    } else {
                                        telefonoActualizar.setNumero(nuevoNumero);
                                    }
                                    break;
                                }
                            }

                            if (cancelarActualizacion) {
                                break;
                            }
                        }

                        if (cancelarActualizacion) {
                            System.out.println("No se guardaron los cambios.");
                            break;
                        }

                        System.out.println("\n===== DATOS ACTUALIZADOS =====");
                        System.out.println(telefonoActualizar);

                        String confirmacion = leerConfirmacionCancelable(
                                sc, "\n¿Confirmas la actualización? (S/N): ");

                        if (verificarCancelacion(confirmacion,
                                "Actualización de teléfono cancelada.")
                                || confirmacion.equalsIgnoreCase("N")) {
                            System.out.println("Actualización cancelada. No se guardaron los cambios.");
                            break;
                        }

                        boolean actualizado = telefonoModel.actualizar(
                                telefonoActualizar,
                                new ArrayList<>(camposSeleccionados));

                        if (actualizado) {
                            System.out.println("\nTeléfono actualizado correctamente en MongoDB.");
                            //System.out.println(telefonoActualizar);
                        } else {
                            System.out.println("No se pudo actualizar el teléfono.");
                        }

                        break;
                    }

                    // DELETE: ELIMINAR TELÉFONO
                    case 5: {
                        System.out.println("\n===== ELIMINAR TELÉFONO =====");

                        List<Telefono> telefonosEliminar = telefonoModel.obtenerTelefonos();

                        if (telefonosEliminar.isEmpty()) {
                            System.out.println("No hay teléfonos registrados.");
                            break;
                        }

                        System.out.println("\n===== TELÉFONOS REGISTRADOS =====");
                        telefonosEliminar.forEach(System.out::println);

                        System.out.println("\nEscribe 0 en cualquier momento para cancelar.");

                        Telefono telefonoEliminar = null;
                        String idEliminar = null;

                        while (telefonoEliminar == null) {

                            idEliminar = leerTextoCancelable(
                                    sc, "\nIntroduce el ID del teléfono que deseas eliminar: ");

                            if (verificarCancelacion(idEliminar,
                                    "Eliminación de teléfono cancelada.")) {
                                break;
                            }

                            telefonoEliminar = telefonoModel.obtenerPorId(idEliminar);

                            if (telefonoEliminar == null) {
                                System.out.println("No existe un teléfono con ese ID. Intenta nuevamente.");
                            }
                        }

                        if (telefonoEliminar == null) {
                            break;
                        }

                        System.out.println("\n===== TELÉFONO SELECCIONADO =====");
                        System.out.println(telefonoEliminar);

                        String confirmacion = leerConfirmacionCancelable(
                                sc, "\n¿Confirmas que deseas eliminarlo? (S/N): ");

                        if (verificarCancelacion(confirmacion,
                                "Eliminación de teléfono cancelada.")
                                || confirmacion.equalsIgnoreCase("N")) {
                            System.out.println("Eliminación cancelada.");
                            break;
                        }

                        boolean eliminado = telefonoModel.eliminar(idEliminar);

                        if (eliminado) {
                            System.out.println("\nTeléfono eliminado correctamente.");
                        } else {
                            System.out.println("No se pudo eliminar el teléfono.");
                        }

                        break;
                    }

                    case 0:
                        System.out.println("\nSaliendo del sistema...");
                        break;

                    default:
                        System.out.println("\nOpción incorrecta. Intenta nuevamente.");
                }

                if (opcion != 0) {
                    System.out.println("\n========== CONTINUAR ==========");
                    System.out.println("1. Regresar al menú principal");
                    System.out.println("0. Salir del sistema");
                    System.out.println("================================");

                    Integer continuar = leerEnteroCancelable(
                            sc, "Selecciona una opción: ", false);

                    if (continuar == null || continuar == 0) {
                        opcion = 0;
                        System.out.println("\nSaliendo del sistema...");
                    }
                }

            } while (opcion != 0);

        } catch (Exception e) {
            System.err.println("\nOcurrió un error durante la ejecución.");
            System.err.println("Motivo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
                System.out.println("Cerrando la conexión con MongoDB...");
            }
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
     * Lee un entero. Si ceroCancela es true, escribir 0 devuelve null.
     * Si ceroCancela es false, permite utilizar 0 como opción del menú.
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

                if (valor < 0) {
                    System.out.println("Ingresa un número válido.");
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

            if (respuesta.equalsIgnoreCase("S")
                    || respuesta.equalsIgnoreCase("N")) {
                return respuesta;
            }

            System.out.println("Respuesta no válida. Escribe S o N.");
        }
    }
}
