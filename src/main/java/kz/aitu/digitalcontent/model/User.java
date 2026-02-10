package kz.aitu.digitalcontent.model;

public class User extends BaseEntity {
    private String email;

    public User() {
        super();
    }

    public User(int id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    @Override
    public String getEntityType() {
        return "USER";
    }

    @Override
    public String displayInfo() {
        return String.format("%s | Email: %s", getBasicInfo(), email);
    }

    @Override
    public String describe() {
        return String.format("User account for %s", getName());
    }

    public void validate() {
        if (getName() == null || getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}