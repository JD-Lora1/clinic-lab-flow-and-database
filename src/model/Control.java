package model;

public class Control {

    public Control(){

    }

    public Patient addPacient(String name, int id){
        Patient x = new Patient(name,id);
        return x;
    }
}
