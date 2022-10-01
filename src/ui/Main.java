package ui;

import model.*;

//Gson Lib. To use Json
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

//TODO
// Move some methods to Control

public class Main {

    private static String databaseFilePath = "";
    private static final String APPSTATE_DB_PATH = "appState/dataBase-Path.txt";
    private static AVL_Tree avlTree;
    private static Gson gson;
    private static Scanner sc = new Scanner(System.in);
    private static String os = "exe"; //Default OS, Windows

    public static void main(String[] args) {
        avlTree = new AVL_Tree();
        gson = new Gson();
        //Start reading the local database file
        databaseFilePath = readFile(APPSTATE_DB_PATH);
        readJsonFile();
        Control control = new Control();

        //menu
        String opt = "";
        while (!opt.equals("0")){
            System.out.println("Choose an option:" +
                    "\n 1.Search a patient" +
                    "\n 2.Add a new patient" +
                    "\n 3.Backup to Github" +
                    "\n 4.Delete Patient (On process)"+
                    "\n 5.Restore a backup of DataBase"+
                    "\n 0.Exit");
            opt = sc.nextLine();
            long id = -1;

            switch (opt){
                case "1":
                    System.out.println("Please provide the id: ");
                    while (id == -1){
                        try {
                            id = Long.parseLong(sc.nextLine());
                        }catch (NumberFormatException e){
                            System.out.println("Please provide a valid id number: ");
                        }
                    }
                    Node foundNode = avlTree.findPatient(id);
                    if (foundNode!=null){
                        System.out.println("Found:");
                        System.out.println(foundNode.getPatient().toString());
                    }
                    id = -1;
                    break;
                case "2":
                    System.out.println("Please provide the name: ");
                    String name = sc.nextLine();
                    System.out.println("Now, write the id: ");
                    while (id == -1){
                        try {
                            id = Long.parseLong(sc.nextLine());
                        }catch (NumberFormatException e){
                            System.out.println("Please provide a valid id number: ");
                        }
                    }
                    avlTree.insert(new Patient(name,id));
                    //Serialize the data locally
                    writeJsonFile();
                    break;

                case "3":
                    //Default OS: Windows(exe)
                    try {
                        File file = new File(databaseFilePath);
                        ArrayList<File> myFiles = new ArrayList<>();
                        myFiles.addAll(List.of(new File(databaseFilePath.replace("/DataBase.txt","")).listFiles()));
                        if (!myFiles.contains(new File(databaseFilePath.replace("/DataBase.txt","")+"/.git"))){
                            file.delete();
                            if (initializeGit()!=0)
                                file.createNewFile();
                        }
                        writeJsonFile();
                        backupCommand();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    break;
                case "5":
                    gitGetCommit(new File(databaseFilePath));
                    loadDataToRoot();

            }
        }
        sc.close();
    }

    public static void loadDataToRoot(){
        String json = readFile(databaseFilePath);
        //Load the data on the AVL tree. BST
        Node node = gson.fromJson(json, Node.class);
        avlTree.setRoot(node);

        writeJsonFile();
    }

    public static String readFile(String filePath) {
        String value = "";
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;

            while ((line = br.readLine()) != null) {
                value += line;
            }
            fis.close();
            br.close();

            if (value==null)
                value = "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void writeFiles(String filePath, String sWrite){
        File file = new File(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(sWrite);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int initializeGit(){
        System.out.print("Initializing .");
        Process process = null;
        int exitValue = 0;

        try {
            //Initialize the repo
            String commandsTotal = "powershell.exe cd "+databaseFilePath.replace("/DataBase.txt","")+"; git init"+
                    "; git branch -M main"+
                    "; git remote add origin https://github.com/JD-Lora1/Clinic-DataBase-Backup.git -m main";

            process = Runtime.getRuntime().exec(commandsTotal);

            if ((exitValue = process.waitFor())==1){
                powershellReader(process);
                process.destroy();
            }
            else {
                System.out.print(" 100%");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            process.destroy();
        }
        System.out.println("");
        return exitValue;
    }

    public static void powershellReader(Process process) throws IOException {
        //Read errors
        BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }
        //Read console output
        buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }
        buf.close();
    }

    public static void backupCommand() throws IOException {
        Date date = new Date();
        System.out.print("Backup .");
        String commandsTotal = "powershell.exe cd "+databaseFilePath.replace("/DataBase.txt","") +
                "; git add DataBase.txt"+
                "; git commit -m 'Backup: "+date.toString()+"'"+
                "; git push origin main";
        Process process = Runtime.getRuntime().exec(commandsTotal);
        try {
            if (process.waitFor()==1)
                powershellReader(process);
            System.out.print(".");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(". Done");
        System.out.println("See it on https://github.com/JD-Lora1/clinic-lab-flow-and-database/blob/main/DataBase.txt");
    }
    private static ArrayList<String> gitLog() {
        String command1 = "powershell."+os+" cd " +databaseFilePath.replace("/DataBase.txt","")+
                "; git log --graph --decorate --format=format:\'%C(bold blue)%h%C(reset) - %C(bold green)(%ar)%C(reset) %C(white)%s%C(reset) %C(dim white)\'";
        Process process1 = null;
        ArrayList<String> commits = new ArrayList<>();

        try {
            process1 = Runtime.getRuntime().exec(command1);
            process1.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(process1.getErrorStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
            }
            buf = new BufferedReader(new InputStreamReader(process1.getInputStream()));
            int counter =0;
            while ((line = buf.readLine()) != null) {
                System.out.println(counter+++") "+line);
                commits.add(line.substring(2,9));
            }
            buf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            process1.destroy();
        }
        return commits;
    }

    public static void readJsonFile(){
        File file = new File(databaseFilePath);
        while (!file.exists()) {
            //Guarantee folder and file existence
            try {
                System.out.println("-> Write the full path of the folder where you want to create your DataBase file");
                String xPath = sc.nextLine();
                file = new File(xPath);
                if (file.isDirectory() && file.listFiles().length==0){
                    file = new File(xPath+"/DataBase.txt");
                    file.createNewFile();
                    databaseFilePath = xPath+"/DataBase.txt";
                    System.out.println("DataBase.txt Created");
                    writeFiles(APPSTATE_DB_PATH, databaseFilePath);

                }else {
                    System.out.println("Invalid path directory/folder");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(file.length()==0){
            System.out.println("The local DataBase seems empty");
            String option = "";
            while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                System.out.println("Do you wanna import the data from a remote DataBase?: Y/N");
                option = sc.nextLine();
                //Git pull (get the latest remote version) or choose a specific commit
                gitGetCommit(file);
            }
        }
        // Load to Root on tree
        loadDataToRoot();
    }

    public static void gitGetCommit(File file){
        int commit = -1;
        ArrayList<String> commits = new ArrayList<>();

        //Check if git was initialized
        ArrayList<File> myFiles = new ArrayList<>(List.of(new File(databaseFilePath.replace("/DataBase.txt", "")).listFiles()));
        try {
            if (!myFiles.contains(new File(databaseFilePath.replace("/DataBase.txt","")+"/.git"))){
                file.delete();
                if (initializeGit()!=0)
                    file.createNewFile();
            }
            readUrl("0"); //git pull basic, to log
            System.out.println("Get the last version of DataBase.txt. Do you want to choose another one? Y/N ");
            String option = "";

            while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                option = sc.nextLine();
                if (option.equalsIgnoreCase("Y")){
                    while (commit==-1 || commit>commits.size()-1){
                        System.out.println("Chose the hash's index of the backup which you want to import:");
                        commits = gitLog();
                        commit = Integer.parseInt(sc.nextLine());
                    }
                    readUrl(commits.get(commit));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readUrl(String commit) {
        //TODO
        // if 0. Just pull
        String command = "powershell."+os+" cd "+databaseFilePath.replace("/DataBase.txt","");
        if (commit.equals("0")){
            command+="; git pull origin main";
        }else {
            System.out.print("Reading Data ");
            command+= "; git checkout "+commit+" -- "+databaseFilePath;

        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor()==1){
                powershellReader(process);
            }else {
                System.out.println("Data Actualized with "+(commit.equals("0")? "Last":commit)+" commit");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void writeJsonFile(){
        File file = new File(databaseFilePath);
        // Create file or delete its data
        try {
            if(!file.exists())
                file.createNewFile();
            else{
                //Cleaer data on DataBase.txt
                new FileWriter(databaseFilePath, false).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write
        try {
            if (avlTree.getRoot()!=null){
                String json = gson.toJson(avlTree.getRoot());
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(json.getBytes(StandardCharsets.UTF_8));
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
