package com.sample;

import com.comPort.Control;
import com.controllers.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        String fxmlFile = "/fxml/main_NEW.fxml";
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
        stage.getIcons().add(new Image("images/iconfinder_blood-test-laboratory-lab-virus_5986147.png"));
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
                    control.sendOnExit();
                    control.closeConnection();
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
