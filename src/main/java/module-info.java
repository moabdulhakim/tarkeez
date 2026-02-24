module com.example.tarkeez {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    requires org.jsoup;
    requires flying.saucer.pdf;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.tarkeez to javafx.fxml;
    exports com.example.tarkeez;
}