package sample;

import comPort.ComPortConnection;
import exception.ComPortException;
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
        String css = "/styles/labStyle.css";
        String icons = "/images/";

        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(root);
        stage.setTitle("GlucoLab");
        stage.resizableProperty().setValue(Boolean.FALSE);
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

//        try {
//            ComPortConnection comPortConnection = ComPortConnection.getInstance(ComPortConnection.getPortName());
//            comPortConnection.openPort();
//        } catch (ComPortException e){
//            e.printStackTrace();
//        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
