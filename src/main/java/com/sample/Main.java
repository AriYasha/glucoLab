package com.sample;

import com.comPort.ComPortConnection;
import com.comPort.Control;
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
        String fxmlFile = "/fxml/main.fxml";
        String css = "/styles/labStyle.css";
        String icons = "/images/";

        int a = 569;
        byte b = (byte) a;
        byte c = (byte) (a >> 8);
        System.out.println(b);
        System.out.println(c);


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("GlucoLab");
        stage.resizableProperty().setValue(Boolean.FALSE);
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        Controller controller = loader.getController();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Control control = controller.getControl();
                if(control != null) {
                    control.sendTest();
                }
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
