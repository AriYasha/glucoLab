package sample;

import comPort.ComPortConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        String fxmlFile = "/fxml/sample.fxml";
        String css = "/styles/delfidiaStyle.css";
        String icons = "/images/";

        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(root);
        stage.setTitle("GlucoLab");
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        System.out.println("hello");
        String[] ports = ComPortConnection.getPortNames();

        ComPortConnection comPortConnection = ComPortConnection.getInstance(ports[1]);
        comPortConnection.openPort();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
