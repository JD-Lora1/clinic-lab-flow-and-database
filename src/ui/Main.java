package ui;

import Comparators.CompareByID;
import model.AVL_Tree;
import model.Control;
import model.Patient;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    //toDo
    // Create a submenu, with the options categorized:
    // Patients{search,add,delete}
    // Github{Backup, restore Backup}
    // Advanced options{Factory reset(clear data such as on dataBase-Path.txt, or set Windows OS by default)
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Comparator<Patient> comparator = new CompareByID();
        Control control = new Control(comparator);
        control.start(); // Initialize the file. Read the data

        //menu
        String opt = "";
        Patient tempPatient = new Patient(null,"-1", 8, true);
        while (!opt.equals("0")){
            System.out.println("\nChoose an option:" +
//                    "\n 1.Search a patient" +
//                    "\n 2.Add a new patient" +
//                    "\n 3.Delete Patient (On process)"+
//                    "\n 4.Create a backup locally and to Github" +
//                    "\n 5.Restore a backup of DataBase" +
//                    "\n 6.Factory RESET"+
//                    "\n 7.Save/Serialize" +
                    "\n 8.Undo" +
                    "\n 9.Admit patient to the laboratory" +
                    "\n 10.Discharge patient from laboratory" +
                    "\n 0.Exit\n");
            opt = sc.nextLine();
            String  id = "";
            String lab = "";

            switch (opt){
                case "1":
                    if(control.avlTree.getRoot() == null) {
                        System.out.println("There are not patients");
                    }else{
                        System.out.print("Please provide the id: ");
                        while (id.equals("")){
                            id = readId(id);
                        }
                        tempPatient = control.findPatient(id);
                        System.out.println("");
                    }
                    break;
                case "2":
                    System.out.print("Please provide the full name: ");
                    String name = sc.nextLine();
                    System.out.print("Now, write the id: ");
                    while (id.equals("")){
                        id = readId(id);
                    }

                    System.out.println("Enter the age of the patient");
                    int age = sc.nextInt();
                    boolean isPriority = false;
                    if(age>=50){
                        isPriority = true;
                    }else{
                        System.out.println("Type of atention: \n" +
                                "1.Normal \n" +
                                "2.Priority");
                        int typeAtention = sc.nextInt();

                        switch (typeAtention){
                            case 1:
                                isPriority = false;
                                break;
                            case 2:
                                isPriority = true;
                                break;
                        }
                    }

                    control.addNodeHistory(control.addPatient(name,id, age, isPriority), null,"Insert AVL-Node");
                    //Serialize the data
                    control.writeJsonFile();
                    System.out.println("");
                    break;
                case "3":
                    //Find it, then delete it
                    System.out.print("Write the id: ");
                    while (id.equals("")){
                        id = readId(id);
                    }
                    control.addNodeHistory(control.avlTree.delete(id), null,"Delete AVL-Node");

                    //Serialize the data
                    control.writeJsonFile();
                    System.out.println("");
                    break;
                case "4":
                    try {
                        File file = new File(control.databasePath);
                        //List of files contained by the parent folder of databaseFile
                        ArrayList<File> myFiles = new ArrayList<>();
                        myFiles.addAll(List.of(new File(control.databaseParent).listFiles()));
                        if (!myFiles.contains(new File(new File(control.databasePath).getParent()+"/.git")) ){
                            control.initializeGit();
                        }

                        control.writeJsonFile();
                        control.backupCommand();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("");
                    break;
                case "5":
                    control.gitPull(new File(control.databasePath), "commit"); //second option. Direct option
                    control.loadDataToRoot();
                    System.out.println("");
                    break;
                case "6":
                    control.factoryReset();
                    System.out.println("Done");
                    opt="0";
                    break;
                case "7":
                    control.writeJsonFile();
                    System.out.println("Serialized");
                    break;
                case "8":
                    //Undo
                    control.undo();
                    control.writeJsonFile();
                    break;
                case "9":

                    if(tempPatient.getId() == "-1"){
                        System.out.println("You must look for the patient first (Option 1)");
                    } else{
                        String selected = "";
                        while(!selected.equals("1") && !selected.equals("2")) {
                            System.out.println("You have selected the patient: \n" + tempPatient.showData() + "\n¿Would you like to continue?\n 1.Yes\n 2.No");
                            selected = sc.nextLine();

                            if (selected.equals("1")) {

                                if (control.avlTree.getRoot() == null) {
                                    System.out.println("There are not patients in the hospital");
                                } else if (tempPatient == null) {
                                    System.out.println("The selected patient does not exist, to change the patient select the first option in the main menu");
                                } else { //All right
                                    control.entryLab(tempPatient, lab);
                                }
                            } else if (selected.equals("2")) {
                                System.out.println("To change patient select option 1 in the main menu");
                            } else {
                                System.out.println("Invalid option");
                            }
                        }
                    }
                    break;

                case "10":
                    if(control.avlTree.getRoot() == null){
                        System.out.println("There are not patients in the hospital");
                    }else {
                        lab = "";
                        String queueType = "";
                        while (!lab.equals("1") && !lab.equals("2")) {
                            System.out.println("Select the laboratory:\n 1.Hematology laboratory\n 2.General laboratory\n");
                            lab = sc.nextLine();
                        }
                        while (!queueType.equals("1") && !queueType.equals("2")) {
                            System.out.println("Dequeue in:\n 1.Priority\n 2.Secondary\n");
                            queueType = sc.nextLine();
                        }

                        if(!control.queueEmpty(lab, queueType).equals("")){
                            System.out.println(control.queueEmpty(lab, queueType));
                        }else{
                            System.out.println("\tRemoved\n"+control.dischargeLab(lab, queueType).showData());
                        }
                    }
                    break;
            }
        }
        control.writeJsonFile();
        sc.close();
    }

    public static String readId(String id){
        //Guarantee id is a number
        Long longId;
        try {
            longId = Long.parseLong(sc.nextLine());
            id = longId.toString();
        }catch (NumberFormatException e){
            System.out.print("Please provide a valid id number: ");
        }
        return id;
    }

}
