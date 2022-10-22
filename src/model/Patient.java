package model;

public class Patient {
    private String name;
    private String id;
    private int age;
    private boolean isPriority;
    private boolean isInQueue;

    public Patient(String name, String id,int age,boolean isPriority) {
        this.name = name;
        this.id = id;
        this.age=age;
        this.isPriority=isPriority;
        isInQueue = false;
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

    public boolean isInQueue() {
        return isInQueue;
    }

    public void setInQueue(boolean inQueue) {
        isInQueue = inQueue;
    }

    public String showData(){
        return "ID: "+id+", Name: "+name;
    }

    public String toPrint() {
        return  "ID:" + id +
                ", Name:" + name  +
                ", Age:" + age +
                ", Priority atention:" + isPriority;
    }
}

