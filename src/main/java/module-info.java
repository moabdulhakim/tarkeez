module com.example.tarkeez {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.tarkeez to javafx.fxml;
    exports com.example.tarkeez;
}