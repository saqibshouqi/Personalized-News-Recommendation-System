module com.example.personalizednewsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires com.opencsv;
    requires opennlp.tools;

    opens com.example.personalizednewsrecommendationsystem to javafx.fxml;
    exports com.example.personalizednewsrecommendationsystem;
}