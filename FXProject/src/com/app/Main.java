package com.app;

import com.cernelea.employeeModel.EmployeeData;
import com.cernelea.MainControllerr;
import com.cernelea.MainControllerr;
import com.cernelea.MainControllerr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/com/cernelea/mainWindow.fxml"));
        Parent root = loader.load();
        MainControllerr controller = loader.getController();
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
