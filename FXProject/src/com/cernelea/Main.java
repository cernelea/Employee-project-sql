package com.cernelea;

import com.cernelea.employeeModel.EmployeeData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        mainController controller = loader.getController();
        controller.listEmployee();
        primaryStage.setTitle("Employee List");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        EmployeeData.getInstance().open();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        EmployeeData.getInstance().close();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
