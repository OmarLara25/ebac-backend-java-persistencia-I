
package com.ebac.modulo59;

import com.ebac.modulo59.MysqlConnection;
import com.ebac.modulo59.dto.Direccion;
import com.ebac.modulo59.dto.Telefono;
import com.ebac.modulo59.dto.Usuario;
import com.ebac.modulo59.model.DireccionModel;
import com.ebac.modulo59.model.TelefonoModel;
import com.ebac.modulo59.model.UsuarioModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Contexto {

    static Connection conn;

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/modulo59";
        String user = "root";
        String password = "root";

        MysqlConnection mysqlConnection = new MysqlConnection();
        conn = mysqlConnection.getConnection(url, user, password);

        try {

            // SELECT CON STATEMENT
            consultarUsuarios();

            // OPERACIONES CRUD
            operacionConUsuarios();
            operacionConTelefonos();
            operacionConDirecciones();

        } finally {
            conn.close();
        }
    }

    // ------------------------------------------------
    // CONSULTAR USUARIOS CON STATEMENT
    // ------------------------------------------------

    public static void consultarUsuarios() throws SQLException {

        System.out.println("------- CONSULTA DE USUARIOS -------");

        String sql = "SELECT * FROM usuarios";

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {

            System.out.println("id de usuario: " + resultSet.getInt("idUsuario"));
            System.out.println("nombre de usuario: " + resultSet.getString("nombre"));
            System.out.println("edad del usuario: " + resultSet.getInt("edad"));

            System.out.println("-----------------------------------");
        }
    }

    // ------------------------------------------------
    // OPERACIONES CON USUARIOS
    // ------------------------------------------------

    public static void operacionConUsuarios() throws SQLException {

        System.out.println("------- OPERACION CON USUARIOS -------");

        Usuario usuarioMaria = crearUsuario("Maria", 25);
        Usuario usuarioJulian = crearUsuario("Julian", 23);

        UsuarioModel usuarioModel = new UsuarioModel(conn);

        // GUARDAR MARIA
        Usuario maria = usuarioModel.guardar(usuarioMaria);
        int idMaria = obtenerUltimoId("usuarios", "idUsuario");

        System.out.println(maria);

        // GUARDAR JULIAN
        Usuario julian = usuarioModel.guardar(usuarioJulian);
        int idJulian = obtenerUltimoId("usuarios", "idUsuario");

        System.out.println(julian);

        System.out.println("-----------------------------------");

        // OBTENER
        Usuario usuario1EnDB = usuarioModel.obtenerPorId(idMaria);
        System.out.println(usuario1EnDB);

        Usuario usuario2EnDB = usuarioModel.obtenerPorId(idJulian);
        System.out.println(usuario2EnDB);

        System.out.println("-----------------------------------");

        // ELIMINAR
        int elementosEliminados = usuarioModel.eliminarPorId(idJulian);

        System.out.println("Usuarios eliminados: " + elementosEliminados);

        System.out.println("-----------------------------------");
    }

    // ------------------------------------------------
    // OPERACIONES CON TELEFONOS
    // ------------------------------------------------

    public static void operacionConTelefonos() throws SQLException {

        System.out.println("------- OPERACION CON TELEFONOS -------");

        Telefono telefono = crearTelefono(
                1,
                "55-11111-22222",
                "Casa"
        );

        TelefonoModel telefonoModel = new TelefonoModel(conn);

        // GUARDAR
        telefonoModel.guardar(telefono);

        int idTelefono = obtenerUltimoId("telefonos", "idTelefono");

        // OBTENER
        Telefono telefonoEnDB = telefonoModel.obtenerPorId(idTelefono);

        System.out.println(telefonoEnDB);

        System.out.println("-----------------------------------");

        // ACTUALIZAR
        telefonoEnDB.setNumero("55-33333-44444");
        telefonoEnDB.setTipo("Celular");

        Telefono telefonoActualizado = telefonoModel.actualizarPorId(telefonoEnDB);

        System.out.println(telefonoActualizado);

        System.out.println("-----------------------------------");

        // ELIMINAR
        int elementosEliminados = telefonoModel.eliminarPorId(idTelefono);

        System.out.println("Telefonos eliminados: " + elementosEliminados);

        System.out.println("-----------------------------------");
    }
    // ------------------------------------------------
    // OPERACIONES CON DIRECCIONES
    // ------------------------------------------------

    public static void operacionConDirecciones() throws SQLException {

        System.out.println("------- OPERACION CON DIRECCIONES -------");

        Direccion direccion = crearDireccion(
                1,
                "Av. Reforma",
                250,
                "CDMX"
        );

        DireccionModel direccionModel = new DireccionModel(conn);

        // GUARDAR
        Direccion direccionGuardada = direccionModel.guardar(direccion);

        int idDireccion = obtenerUltimoId("direcciones", "idDireccion");

        System.out.println(direccionGuardada);

        System.out.println("-----------------------------------");

        // OBTENER
        Direccion direccionEnDB = direccionModel.obtenerPorId(idDireccion);

        System.out.println(direccionEnDB);

        System.out.println("-----------------------------------");

        // ACTUALIZAR
        direccionEnDB.setCalle("Av. Insurgentes");
        direccionEnDB.setNumero(350);
        direccionEnDB.setEstado("San Luis Potosi");

        Direccion direccionActualizada =
                direccionModel.actualizarPorId(direccionEnDB);

        System.out.println(direccionActualizada);

        System.out.println("-----------------------------------");

        // OBTENER ACTUALIZACION
        Direccion direccionActualizadaEnDB =
                direccionModel.obtenerPorId(idDireccion);

        System.out.println(direccionActualizadaEnDB);

        System.out.println("-----------------------------------");

        // ELIMINAR
        int elementosEliminados = direccionModel.eliminarPorId(idDireccion);

        System.out.println("Direcciones eliminadas: " + elementosEliminados);

        System.out.println("-----------------------------------");
    }

    private static int obtenerUltimoId(String tabla, String columna) throws SQLException {

        String sql = "SELECT MAX(" + columna + ") FROM " + tabla;

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    // ------------------------------------------------
    // METODOS AUXILIARES PARA CREAR DTO
    // ------------------------------------------------

    private static Usuario crearUsuario(String nombre, int edad) {

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEdad(edad);

        return usuario;
    }

    private static Telefono crearTelefono(
            int idUsuario,
            String numero,
            String tipo) {

        Telefono telefono = new Telefono();

        telefono.setIdUsuario(idUsuario);
        telefono.setNumero(numero);
        telefono.setTipo(tipo);

        return telefono;
    }

    private static Direccion crearDireccion(
            int idUsuario,
            String calle,
            int numero,
            String estado) {

        Direccion direccion = new Direccion();

        direccion.setIdUsuario(idUsuario);
        direccion.setCalle(calle);
        direccion.setNumero(numero);
        direccion.setEstado(estado);

        return direccion;
    }
}
