package model;

public class Employee extends User {

    public Employee(String name, String phone, String username, String password) {
        super(name, phone, username, password);
    }

    @Override
    public String getRole() {
        return "موظف";
    }
}
