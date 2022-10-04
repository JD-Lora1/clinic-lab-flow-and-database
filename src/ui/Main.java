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

        control.start();

        //menu
        String opt = "";
        while (!opt.equals("0")){
            System.out.println("\nChoose an option:" +
                    "\n 1.Search a patient" +
                    "\n 2.Add a new patient" +
                    "\n 3.Backup to Github" +
                    "\n 4.Delete Patient (On process)"+
                    "\n 5.Restore a backup of DataBase" +
                    "\n 6.Factory RESET"+
                    "\n 0.Exit");
            opt = sc.nextLine();
            long id = -1;

            switch (opt){
                case "1":
                    System.out.println("Please provide the id: ");
                    while (id == -1){
                        id = readId(id);
                    }
                    control.findPatient(id);
                    break;
                case "2":
                    System.out.println("Please provide the full name: ");
                    String name = sc.nextLine();
                    System.out.println("Now, write the id: ");
                    while (id == -1){
                        id = readId(id);
                    }
                    control.addPatient(name,id);

                    //Serialize the data locally
                    control.writeJsonFile();
                    break;

                case "3":
                    try {
                        File file = new File(control.databaseFile);
                        //List of files contained by the parent folder of databaseFile
                        ArrayList<File> myFiles = new ArrayList<>();
                        //myFiles.addAll(List.of(new File(databaseFile.replace("/DataBase.txt","")).listFiles()));
                        myFiles.addAll(List.of(new File(control.databaseFile).getParentFile().listFiles()));
                        if (!myFiles.contains(new File(new File(control.databaseFile).getParent()+"/.git")) ){
                            control.initializeGit();
                        }

                        control.writeJsonFile();
                        control.backupCommand();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    break;
                case "5":
                    control.gitPull(new File(control.databaseFile), "commit"); //second option. Direct option
                    control.loadDataToRoot();
                    break;
                case "6":
                    control.factoryReset();
                    break;
            }
        }
        sc.close();
    }

    public static long readId(long id){
        //Guarantee id is a number
        try {
            id = Long.parseLong(sc.nextLine());
        }catch (NumberFormatException e){
            System.out.println("Please provide a valid id number: ");
        }
        return id;
    }



}
