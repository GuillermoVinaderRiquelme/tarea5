package di.tarea5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ArtistasController {

    @FXML
    private ListView<Artista> listadoArtistas;
    private String query = "SELECT ArtistId, Name FROM artists";

    //Aquí se guardará la lista de artistas
    private ObservableList<Artista> artistas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //Se llena la observableList de los datos de la base de datos
        cargarArtistas();
        //Se asigna la observableList al ListView de la vista
        listadoArtistas.setItems(artistas);
        listadoArtistas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Artista artista = listadoArtistas.getSelectionModel().getSelectedItem();
                    generarInforme(artista);
                }
        );
    }

    public void cargarArtistas(){
        try{
            Conexion.conectar();
            Statement statement = Conexion.getConexion().createStatement();
            ResultSet rs = statement.executeQuery(query);
            //Se llena la lista de artistas
            while(rs.next()){
                int id = rs.getInt("ArtistId");
                String name = rs.getString("Name");
                artistas.add(new Artista(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar la base de datos. +info:\n" + e.getMessage());
        }finally{
            Conexion.desconectar();
        }
    }

    //REcibo por parámetro el artista seleccionado
    public void generarInforme(Artista artista) {
        if (artista == null) { //Si por lo que sea no se recibe bien el artista, no se intenta generar un informe vacío
            System.out.println("No has seleccionado ningún artista");
            return;
        }
        try {
            //A partir del fichero .jrxml se genera el objeto JasperReport compilado
            String jasperFilePath = "informes/artista.jrxml";
            InputStream inputStream = HelloApplication.class.getResourceAsStream(jasperFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            //Acceder a la base de datos
            Conexion.conectar();
            //El informe solicita un id para filtrar por el artista
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("artista", artista.getId());  //Se extrae el id del artista seleccionado

            //Se envía tanto el informe compilado, la conexión a la base de datos y el id del artista seleccionado (parámetro)
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, Conexion.getConexion());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            System.err.println("Error al generar el informe. +info:\n" + e.getMessage());
        }finally {
            Conexion.desconectar();
        }
    }



}
