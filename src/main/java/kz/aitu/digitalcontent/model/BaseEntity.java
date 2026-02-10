package kz.aitu.digitalcontent.model;

public abstract class BaseEntity {
    private int id;
    private String name;

    public BaseEntity() {
    }

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract String displayInfo();
    public abstract String describe();
    public abstract String getEntityType();

    public String getBasicInfo() {
        return String.format("[%s] ID: %d, Name: %s", getEntityType(), id, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return getBasicInfo();
    }
}