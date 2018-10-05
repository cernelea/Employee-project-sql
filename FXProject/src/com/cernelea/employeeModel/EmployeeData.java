package com.cernelea.employeeModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;

public class EmployeeData {

    public static final String DATABASE_NAME = "employee.db";

    public static final String CONNECTION_SOURCE = "jdbc:sqlite:" + DATABASE_NAME;

    public static final String TABLE_EMPLOYEES = "employees";

    public static final String COLUMN_EMPLOYEE_ID = "_id";
    public static final String COLUMN_EMPLOYEE_NAME = "name";
    public static final String COLUMN_EMPLOYEE_SALARY = "salary";
    public static final String COLUMN_EMPLOYEE_JOB = "job";
    public static final String COLUMN_EMPLOYEE_BITRHDAY = "birthday";

    public static final String TABLE_JOBS = "jobs";
    public static final String COLUMN_JOBS_ID = "_id";
    public static final String COLUMN_JOBS_TITLE = "title";

    public static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEES + "(" + COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL, " + COLUMN_EMPLOYEE_SALARY + " DOUBLE(2), "
            + COLUMN_EMPLOYEE_JOB + " TEXT NOT NULL, " + COLUMN_EMPLOYEE_BITRHDAY + " DATE " + ")";

    public static final String CREATE_JOBS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_JOBS + "(" + COLUMN_JOBS_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_JOBS_TITLE + " TEXT NOT NULL" + ")";

    public static final String INSERT_EMPLOYEE = "INSERT INTO " + TABLE_EMPLOYEES
            + "(" + COLUMN_EMPLOYEE_ID + ", " + COLUMN_EMPLOYEE_NAME + ", " + COLUMN_EMPLOYEE_SALARY + ", " + COLUMN_EMPLOYEE_JOB
            + ", " + COLUMN_EMPLOYEE_BITRHDAY + ") " + "VALUES(?,?,?,?,?)";

    public static final String INSERT_JOBS = "INSERT INTO " + TABLE_JOBS
            + "(" + COLUMN_JOBS_TITLE + ") " + "VALUES(?)";

    public static final String DELETE_JOBS = "DELETE FROM " + TABLE_JOBS + " WHERE "
            + COLUMN_JOBS_TITLE + " = ?";

    public static final String DELETE_EMPLOYEE_BY_ID = "DELETE FROM " + TABLE_EMPLOYEES
            + " WHERE " + COLUMN_EMPLOYEE_ID + " = ?";

    public static final String UPDATE_EMPLOYEE = "UPDATE " + TABLE_EMPLOYEES + " SET "
            + COLUMN_EMPLOYEE_NAME + " = " + "?, " + COLUMN_EMPLOYEE_SALARY + "=?, "
            + COLUMN_EMPLOYEE_JOB + "=?, " + COLUMN_EMPLOYEE_BITRHDAY + "=? "
            + " WHERE "
            + COLUMN_EMPLOYEE_ID + " = ? ";

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    private Connection conn;
    private PreparedStatement insertEmployee;
    private PreparedStatement insertJob;
    private PreparedStatement deleteJob;
    private PreparedStatement deleteEmployee;
    private PreparedStatement updateFullEmployee;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static EmployeeData instance = new EmployeeData();

    public boolean open() {
        try {

            conn = DriverManager.getConnection(CONNECTION_SOURCE);
            Statement statement = conn.createStatement();
            statement.execute(CREATE_EMPLOYEE_TABLE);
            statement.execute(CREATE_JOBS_TABLE);
            insertEmployee = conn.prepareStatement(INSERT_EMPLOYEE);
            insertJob = conn.prepareStatement(INSERT_JOBS);
            deleteJob = conn.prepareStatement(DELETE_JOBS);
            deleteEmployee = conn.prepareStatement(DELETE_EMPLOYEE_BY_ID);
            updateFullEmployee = conn.prepareStatement(UPDATE_EMPLOYEE);
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean close() {

        try {
            if (insertEmployee != null) {

                insertEmployee.close();
            }
            if (insertJob != null) {

                insertJob.close();
            }
            if (deleteJob != null) {

                deleteJob.close();
            }

            if (deleteEmployee != null) {

                deleteEmployee.close();
            }

            if (updateFullEmployee != null) {
                updateFullEmployee.close();

            }
            if (conn != null) {

                conn.close();
            }

            return true;

        } catch (SQLException ex) {

            ex.printStackTrace();
            return false;

        }

    }

    public static EmployeeData getInstance() {
        return instance;
    }

    public boolean addEmployee(Employee employee) {
        try {
            int id = employee.getId();
            String name = employee.getName();
            String job = employee.getJob();
            Double salary = employee.getSalary();
            String birthDay = LocalDate.parse(employee.getBirthDay().toString(), formatter).toString();

            conn.setAutoCommit(false);
            java.sql.Date date = Date.valueOf(birthDay);
            insertEmployee.setInt(1, id);
            insertEmployee.setString(2, name);
            insertEmployee.setDouble(3, salary);
            insertEmployee.setString(4, job);
            insertEmployee.setString(5, date.toString());

            int changedRows = insertEmployee.executeUpdate();
            if (changedRows == 1) {

                conn.commit();
                return true;
            } else {

                throw new SQLException("The song insert failed");
            }

        } catch (SQLException ex) {

            ex.printStackTrace();

            try {
                conn.rollback();

            } catch (SQLException ex2) {

                ex2.printStackTrace();

            }catch(NullPointerException ex1){
            
            }

        } finally {
            try {
                conn.setAutoCommit(true);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public void removeEmployee(Employee employee) {

        int id = employee.getId();

        try {
            conn.setAutoCommit(false);
            deleteEmployee.setInt(1, id);
            deleteEmployee.executeUpdate();

            conn.commit();

        } catch (SQLException ex) {

            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex2) {

                ex2.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    public boolean updateEmployee(Employee employee) {

        try {
//           
            String name = employee.getName();
            String job = employee.getJob();
            double salary = employee.getSalary();
            String birthday = employee.getBirthDay().toString();
            Date date = Date.valueOf(birthday);
            updateFullEmployee.setString(1, name);
            updateFullEmployee.setDouble(2, salary);
            updateFullEmployee.setString(3, job);
            updateFullEmployee.setString(4, date.toString());
            updateFullEmployee.setInt(5, employee.getId());

            updateFullEmployee.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;

        }
    }

    public ObservableList<Employee> employeeList(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_EMPLOYEES);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_EMPLOYEE_NAME);
            sb.append(" COLLATE NOCASE ");

            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");

            } else {

                sb.append(" ASC ");
            }
        }

        try (Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery(sb.toString())) {
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();
            while (results.next()) {
                int id = results.getInt(COLUMN_EMPLOYEE_ID);
                String name = results.getString(COLUMN_EMPLOYEE_NAME);
                String job = results.getString(COLUMN_EMPLOYEE_JOB);
                Double salary = results.getDouble(COLUMN_EMPLOYEE_SALARY);
                LocalDate birthday = LocalDate.parse(results.getString(COLUMN_EMPLOYEE_BITRHDAY));

                Employee employee = new Employee(id, name, birthday, job, salary);
                employeeList.add(employee);

            }
            return employeeList;

        } catch (SQLException ex) {
            return null;

        }

    }

}
