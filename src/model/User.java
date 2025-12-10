package model;

public class User extends Person {

    private String username;
    private String password;

    public User(String name, String phone, String username, String password) {
        super(name, phone);
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String getRole() {
        return "مستخدم";
    }
}
