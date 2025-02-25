module di.tarea5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jasperreports;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires org.slf4j;

    opens di.tarea5;
    exports di.tarea5;
}