package model;

public class Patient {
    private String name;
    private String id;
    private int age;
    private boolean isPriority;

    public Patient(String name, String id,int age,boolean isPriority) {
        this.name = name;
        this.id = id;
        this.age=age;
        this.isPriority=isPriority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    public String showData(){
        return "ID: "+id+", Name: "+name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", age=" + age +
                ", isPriority=" + isPriority +
                '}';
    }
}

