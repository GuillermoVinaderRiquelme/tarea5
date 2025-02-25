package di.tarea5;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class HelloController {

    public Button btnClientes;

    @FXML
    public void onBtnClientes(ActionEvent actionEvent) {
            generarInforme();
    }

    public void onBtnArtistas(ActionEvent actionEvent) {
        Scene nuevaEscena = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("artistas.fxml"));
        try{
            nuevaEscena = new Scene (loader.load());
            ArtistasController controladorArtistas = loader.getController();
            Stage stage = new Stage();
            stage.setScene(nuevaEscena);
            stage.showAndWait();
        }catch (IOException e){
            System.err.println("Error al abrir la nueva ventana. +info:\n" + e.getMessage());
        }
    }

    public void onBtnSalir(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void generarInforme(){
        try {
            //A partir del fichero .jrxml se genera el objeto JasperReport compilado
            String jasperFilePath = "informes/clientes.jrxml";
            InputStream inputStream = HelloApplication.class.getResourceAsStream(jasperFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            //Conecto a la base de datos
            Conexion.conectar();
            //Se instancia un objeto JasperPrint con el JasperReport compilado y la conexión a la base de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), Conexion.getConexion());
            // Mostramos el informe
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            System.err.println("Error al generar el informe. +info:\n" + e.getMessage());
        }finally {
            Conexion.desconectar();
        }
    }

}
