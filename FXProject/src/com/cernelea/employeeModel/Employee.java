package com.cernelea.employeeModel;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {

    private int id;
    private String name;
    private LocalDate birthDay;
    private String job;
    private double salary;

    public Employee(int id, String name, LocalDate birthDay, String job, double salary) {
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
        this.job = job;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.birthDay);
        hash = 29 * hash + Objects.hashCode(this.job);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.salary) ^ (Double.doubleToLongBits(this.salary) >>> 32));
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee{" + "name=" + name + ", birthDay=" + birthDay + ", job=" + job + ", salary=" + salary + ", id=" + id + '}';
    }

}
