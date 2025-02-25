package di.tarea5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Clase que centraliza la conexión a la base de datos
public class Conexion {

    private static String URLdb = "jdbc:sqlite:db/chinook.db";
    private static Connection conexion;

    private Conexion() { }

    public static Connection getConexion() {
        return conexion;
    }

    public static boolean conectar(){
        if (conexion != null){
            return true;
        }
        try{
            conexion = DriverManager.getConnection(URLdb);
            return true;
        }catch(SQLException e){
            System.err.println("Error al conectar con la base de datos. +info: \n" + e.getMessage());
            return false;
        }
    }

    public static boolean desconectar(){
        if (conexion == null){
            return true;
        }
        try{
            conexion.close();
            conexion = null;
            return true;
        }catch(SQLException e){
            System.err.println("Error al desconectr de la base de datos. +info: \n" + e.getMessage());
            return false;
        }
    }
}
