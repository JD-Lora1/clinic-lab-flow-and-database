package ui;

import Comparators.CompareByID;
import model.AVL_Tree;
import model.Control;
import model.Patient;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class Main {

    //toDo
    // Create a submenu, with the options categorized:
    // Patients{search,add,delete}
    // Github{Backup, restore Backup}
    // Advanced options{Factory reset(clear data such as on dataBase-Path.txt, or set Windows OS by default)

    private static Scanner sc;
    private Control control;

    private String id;
    private Patient tempPatient;

    public Main() {
        Comparator<Patient> comparator = new CompareByID();
        control = new Control(comparator);
        sc = new Scanner(System.in);
        control.start(); // Initialize the file. Read the data
        id = "";
    }

    public static void main(String[] args) {
        ui.Main main = new ui.Main();
        String select = "";
        do{
            System.out.print("\t* Press enter to continue");
            sc.nextLine();
            select = main.menu();
            main.select(select, "");
        }while (!select.equals("0"));
    }

    public String menu(){
        System.out.println("\n::::::::::::::::::::::::::::::::::\n\tMAIN MENU\n::::::::::::::::::::::::::::::::::\n(1) Data base \n(2) Hematology unit\n(3) General purpose unit\n(4) Undo\n(0) Exit\n::::::::::::::::::::::::::::::::::");
        return sc.nextLine();
    }

    public void select(String number, String exe){
        switch(number){
            case "1":
                do{
                    exe = dataBaseMenu();
                    optionsDataBase(exe);
                }while(!exe.equals("0"));
                break;

            case "2":
                do{
                    exe = menuHematologyUnit();
                    optionsHematologyUnit(exe);
                }while(!exe.equals("0"));
                break;
            case "3":
                do{
                    exe = menuGeneralUnit();
                    optionsGeneralUnit(exe);
                }while(!exe.equals("0"));
                break;
            case "4":
                //Undo
                control.undo();
                control.writeJsonFile();
                break;
            case"0":
                control.writeJsonFile();
                sc.close();
                System.out.println("Bye!");
                break;
            default:
                System.out.println("\tTypo\nEnter a valid value");
                break;
        }
        System.out.print("\t* Press enter to continue");
        sc.nextLine();
    }

    public String menuHematologyUnit(){
        System.out.println("\n::::::::::::::::::::::::::::::::::\n\tHEMATOLOGY UNIT\n::::::::::::::::::::::::::::::::::\n(1) Admit a patient\n(2) Discharge a patient\n(3) Monitor the status\n(0) Back\n::::::::::::::::::::::::::::::::::");
        String x = sc.nextLine();
        return x;
    }

    public void optionsHematologyUnit(String exe){
        switch (exe){
            case"1":
                patient2Queue("1");
                break;
            case"2":
                dischargeAnyQueue("1");
                break;
            case"3":
                System.out.println("\nPRIORITY\n::::::::::::::::::::::::::::::::::\n");
                control.priorityQueueHematology.printQueue();
                System.out.println("\nSECONDARY\n::::::::::::::::::::::::::::::::::\n");
                control.secondaryQueueHematology.printQueue();
                break;
            case"0":
                System.out.println("Bye!");
                break;
            default:
                System.out.println("\tTypo\nEnter a valid value");
        }
        System.out.print("\t* Press enter to continue");
        sc.nextLine();
    }

    public void optionsGeneralUnit(String exe){
        switch (exe){
            case"1":
                patient2Queue("2");
                break;
            case"2":
                dischargeAnyQueue("2");
                break;
            case"3":
                System.out.println("PRIORITY\n::::::::::::::::::::::::::::::::::\n");
                control.priorityQueueGeneral.printQueue();
                System.out.println("SECONDARY\n::::::::::::::::::::::::::::::::::\n");
                control.secondaryQueueGeneral.printQueue();
                break;
            case"0":
                System.out.println("Bye!");
                break;
            default:
                System.out.println("\tTypo\nEnter a valid value");
        }
    }

    public String menuGeneralUnit(){
        System.out.print("\t* Press enter to continue");
        sc.nextLine();
        System.out.println("\n::::::::::::::::::::::::::::::::::\n\tGENERAL UNIT\n::::::::::::::::::::::::::::::::::\n(1) Admit a patient\n(2) Discharge a patient\n(3) Monitor the status\n(0) Back\n::::::::::::::::::::::::::::::::::");
        return sc.nextLine();
    }

    public String dataBaseMenu(){
        System.out.println("\n::::::::::::::::::::::::::::::::::\n\tDATA BASE MENU\n::::::::::::::::::::::::::::::::::\n(1) Search patient\n(2) Add patient\n(3) Discharge a patient\n(4) Monitor the status\n(5) Create a backup locally and to Github\n(6) Restore a backup of DataBase\n(7) Factory RESET PATH\n(8) Save\n(0) Back\n::::::::::::::::::::::::::::::::::");
        return sc.nextLine();
    }

    public void optionsDataBase(String exe){

        switch (exe){
            case "1": // Search a patient on the database (AVL tree)
                searchPatient(id);
                break;

            case"2": // Add a patient to the database (AVL tree)
                System.out.print("Please provide the full name: ");
                String name = sc.nextLine();
                System.out.print("Now, write the id: ");

                do{
                    id = readId(id);
                }while (id.equals(""));

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
            case "3": // Delete a patient from the database (AVL tree)
                //Find it, then delete it
                System.out.print("Write the id: ");
                do{
                    id = readId(id);
                }while (id.equals(""));
                control.addNodeHistory(control.avlTree.delete(id), null,"Delete AVL-Node");

                //Serialize the data
                control.writeJsonFile();
                System.out.println("");
                break;
            case "4": //Print the DataBase
                control.avlTree.inorder();
                break;

            case "5": // Create a backup locally and to Github
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

            case "6": // Restore a backup from remote DataBase
                control.gitPull(new File(control.databasePath), "commit"); //second option. Direct option
                control.loadDataToRoot();
                System.out.println("");
                break;

            case "7": // Factory RESET. (Delete data like paths, but don't the DataBase itself)
                control.factoryReset();
                System.out.println("Done");
                exe="0";
                break;

            case "8": // Save (Serialize)
                control.writeJsonFile();
                System.out.println("Saved");
                break;

            case"0":
                System.out.println("Bye!");
                break;

            default:
                System.out.println("\tTypo\nEnter a valid value");
        }
        System.out.print("\t* Press enter to continue");
        sc.nextLine();
    }

    public void dischargeAnyQueue(String lab){
        if(control.avlTree.getRoot() == null){
            System.out.println("There are not patients in the hospital");
        }else {
            //System.out.println("Select the laboratory:\n 1.Hematology laboratory\n 2.General laboratory\n");
            //lab = sc.nextLine();
            System.out.println("\tRemoved\n"+control.dischargeLab(lab));
        }
    }

    public void patient2Queue(String lab){

        if (control.avlTree.getRoot() == null) {
            System.out.println("There are not patients in the hospital");
        } else {
            updateTempPatient();
            if(tempPatient != null && !tempPatient.isInQueue()){
                control.entryLab(tempPatient, lab);
            }else if (tempPatient.isInQueue()){
                System.out.println("The patient is already in a queue");
            }
        }
    }

    public Patient searchPatient(String id){
        if(control.avlTree.getRoot() == null) {
            System.out.println("There are no patients");
            return null;
        }else{
            System.out.print("Please provide the id: ");
            do{
                id = readId(id);
            }while (id.equals(""));
            System.out.println("");
            return control.findPatient(id);
        }
    }

    public String readId(String id){
        //Guarantee id is a number
        Long longId;
        try {
            longId = Long.parseLong(sc.nextLine());
            if(longId<0){
                System.out.println("Please enter a valid id");
            } else{
                id = longId.toString();
            }
        }catch (NumberFormatException e){
            System.out.print("Please provide a valid id number: ");
        }
        return id;
    }

    public void updateTempPatient(){
        tempPatient = searchPatient(id);
    }
}
