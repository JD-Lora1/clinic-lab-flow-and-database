package ui;

import model.Control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //ToDo
    // Create a submenu, with the options categorized:
    // Patients{search,add,delete}
    // Github{Backup, restore Backup}
    // Advanced options{Fatory reset(clear data such as on dataBase-Path.txt, or set Windows OS by default)
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Control control = new Control();
        control.start(); // Initialize the file. Read the data

        //menu
        String opt = "";
        while (!opt.equals("0")){
            System.out.println("Choose an option:" +
                    "\n 1.Search a patient" +
                    "\n 2.Add a new patient" +
                    "\n 3.Delete Patient (On process)"+
                    "\n 4.Create a backup locally and to Github" +
                    "\n 5.Restore a backup of DataBase" +
                    "\n 6.Factory RESET"+
                    "\n 0.Exit");
            opt = sc.nextLine();
            String  id = "";

            switch (opt){
                case "1":
                    System.out.print("Please provide the id: ");
                    while (id.equals("")){
                        id = readId(id);
                    }
                    control.findPatient(id);
                    System.out.println("");
                    break;
                case "2":
                    System.out.print("Please provide the full name: ");
                    String name = sc.nextLine();
                    System.out.print("Now, write the id: ");
                    while (id.equals("")){
                        id = readId(id);
                    }
                    control.addPatient(name,id);

                    //Serialize the data locally
                    control.writeJsonFile();
                    System.out.println("");
                    break;
                case "3":
                    //Find it, then delete it
                    //Serialize the data locally
                    control.writeJsonFile();
                    System.out.println("");
                    break;
                case "4":
                    try {
                        File file = new File(control.databasePath);
                        //List of files contained by the parent folder of databaseFile
                        ArrayList<File> myFiles = new ArrayList<>();
                        //myFiles.addAll(List.of(new File(databaseFile.replace("/DataBase.txt","")).listFiles()));
                        myFiles.addAll(List.of(new File(control.databasePath).getParentFile().listFiles()));
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
            }
        }
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
