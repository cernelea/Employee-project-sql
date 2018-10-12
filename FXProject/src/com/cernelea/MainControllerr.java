package com.cernelea;

import com.cernelea.employeeModel.Employee;
import com.cernelea.employeeModel.EmployeeData;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.sqlite.SQLiteException;

public class MainControllerr {

    @FXML

    private BorderPane mainPanel;

    @FXML
    private TableView<Employee> employeeTable;

    private static ObservableList<Employee> employees = EmployeeData.getInstance().employeeList(1);

    public void initialize() {

        employeeTable.setItems(employees);

    }

    @FXML

    public void initiliazeAddDialog() throws SQLiteException {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add a new employee");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("employeeDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Couldn't load the dialog");

            return;

        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        dialog.showAndWait();

        EmployeeController employeeController = fxmlLoader.getController();

        if (EmployeeController.isOkPressed) {
            Employee employee = employeeController.createEmployee();

            EmployeeData.getInstance().addEmployee(employee);

            employeeTable.getItems().add(employee);

        }
    }

    @FXML

    public void deleteAction() {
        Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No employee selected");
            alert.setContentText("Please select the contact you want to delete");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete employee");
            alert.setContentText("Are you sure you want to delete contact: " + employee.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                EmployeeData.getInstance().removeEmployee(employee);
                employeeTable.getItems().remove(employee);

            }

        }

    }

    @FXML

    public void exitAction() {
        Platform.exit();

    }

    @FXML

    public void deleteKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            deleteAction();

        }

    }

    public void listEmployee() {

    }

    @FXML

    public void initiliazeEditDialog() {

        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No employee selected");
            alert.setContentText("Please select the contact you want to edit");
            alert.showAndWait();

        } else {
            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            dialog.initOwner(mainPanel.getScene().getWindow());
            dialog.setTitle("Edit Contact");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("employeeDialog.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());

            } catch (IOException ex) {

                System.out.println("Couldn't find the dialog");
                return;

            }

            EmployeeController employeeController = fxmlLoader.getController();
            employeeController.setEmployeeFields(selectedEmployee);

            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(event -> window.hide());

            dialog.showAndWait();

            if (EmployeeController.isOkPressed) {
                Employee updatedEmployee = employeeController.updateEmployee(selectedEmployee);
                EmployeeData.getInstance().updateEmployee(updatedEmployee);
                employeeTable.refresh();
            }
        }

    }

    @FXML
    public void sortByNameAsc() {

        employeeTable.getItems().removeAll();
        employeeTable.setItems(EmployeeData.getInstance().employeeList(2));

    }

    @FXML
    public void sortByNameDesc() {

        employeeTable.getItems().removeAll();
        employeeTable.setItems(EmployeeData.getInstance().employeeList(3));

    }

}
