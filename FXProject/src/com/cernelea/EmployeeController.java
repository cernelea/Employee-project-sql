package com.cernelea;

import com.cernelea.employeeModel.Employee;
import com.cernelea.employeeModel.EmployeeData;
import com.cernelea.employeeModel.Jobs;
import java.sql.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EmployeeController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField salaryField;

    @FXML
    private DatePicker datePicker;

    @FXML

    private ComboBox comboBox;

    @FXML

    private Button okButton;

    @FXML

    private Button cancelButton;

    @FXML
    private Label exceptionLabel;

    @FXML
    private TextField idField;

    @FXML
    Button okButton2;

    @FXML
    private Label exceptionLabel2;

    static boolean isOkPressed;

    public void initialize() {
        for (Jobs job : Jobs.values()) {

            comboBox.getItems().add(job);
            comboBox.getSelectionModel().selectFirst();

        }
        datePicker.setValue(LocalDate.now());
        okButton.setDisable(true);
        okButton2.setVisible(false);
        isOkPressed = false;
        exceptionLabel2.setVisible(false);
    }

    public void setEmployeeFields(Employee employee) {
        nameField.setText(employee.getName());

        salaryField.setText(String.valueOf(employee.getSalary()));
        comboBox.getSelectionModel().select(employee.getJob());
        datePicker.setValue(employee.getBirthDay());
        idField.setText(String.valueOf(employee.getId()));
        idField.setDisable(true);
        okButton.setVisible(false);
        okButton2.setVisible(true);
        okButton2.setDisable(true);
        exceptionLabel2.setVisible(true);

    }

    @FXML
    public Employee createEmployee() {
        try {

            String name = this.nameField.getText();

            int id = Integer.parseInt(this.idField.getText());
            double salary = Double.parseDouble(this.salaryField.getText());
            LocalDate birthDay = this.datePicker.getValue();
            LocalDate.parse(birthDay.toString());
            String jobText = this.comboBox.getSelectionModel().getSelectedItem().toString();
            Employee employee = new Employee(id, name, birthDay, jobText, salary);
            if (EmployeeData.getInstance().employeeList(1).contains(employee)) {

                System.out.println(EmployeeData.getInstance().employeeList(1).contains(employee));
                exceptionLabel.setText("Can not add a existing ID");
                return null;
            } else {

                exceptionLabel.setText("");
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
                isOkPressed = true;
                return employee;

            }
        } catch (NumberFormatException ex) {

            exceptionLabel.setText(" Please enter a valid number");

        }
        return null;

    }

    @FXML
    public Employee updateEmployee(Employee employee) {

        employee.setName(nameField.getText());
        employee.setSalary(Double.parseDouble(salaryField.getText()));
        employee.setBirthDay(datePicker.getValue());
        employee.setJob(comboBox.getSelectionModel().getSelectedItem().toString());

        return employee;

    }

    public void checkLabels() {

        String name = this.nameField.getText();

        String salary = this.salaryField.getText();
        String id = this.idField.getText();

        Boolean isNameFiledEmpty = name.isEmpty() || name.trim().isEmpty();

        Boolean isSalaryFieldEmpty = salary.isEmpty() || salary.trim().isEmpty();

        Boolean isIdFieldEmpty = id.isEmpty() || id.trim().isEmpty();

        if (isNameFiledEmpty || isSalaryFieldEmpty || isIdFieldEmpty) {

            okButton.setDisable(true);
            okButton2.setDisable(true);
        } else {

            okButton.setDisable(false);
            okButton2.setDisable(false);

        }
        try {
            Integer.parseInt(id);
            Double.parseDouble(salary);
            exceptionLabel2.setText("");
        } catch (NumberFormatException ex) {

            exceptionLabel2.setText(" Please enter a valid number");

            okButton2.setDisable(true);

        }

    }

    @FXML
    public void setCancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        isOkPressed = false;
        stage.close();

    }

    @FXML
    public void setokButton2() {

        Stage stage = (Stage) okButton2.getScene().getWindow();
        isOkPressed = true;

        stage.close();

    }

}
