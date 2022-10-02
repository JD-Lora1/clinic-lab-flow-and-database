package ui;

import model.*;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

//TODO
// Move some methods to Control
// create a String datbaseParent (folder). database.getParentFile

public class Main {

    private static final String APPSTATE_DB_PATH = "appState/dataBase-Path.txt"; //File that contains the path of DataBase file
    private static String databaseFile = " ";
    private static String os = "exe"; //Default OS: Windows
    private static AVL_Tree avlTree; //Binary tree to save patients info
    private static Gson gson; //To use Json
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        avlTree = new AVL_Tree();
        gson = new Gson();

        //Start reading the file that contains the path of database
        databaseFile = readFile(APPSTATE_DB_PATH);
        //Read Json. Deserialize, set data to Root's tree (and it children)
        createDataBase();
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
                        id = readId(id);
                    }
                    Node foundNode = avlTree.findPatient(id);
                    if (foundNode!=null){
                        System.out.println("Found:");
                        System.out.println(foundNode.getPatient().toString());
                    }
                    break;
                case "2":
                    System.out.println("Please provide the full name: ");
                    String name = sc.nextLine();
                    System.out.println("Now, write the id: ");
                    while (id == -1){
                        id = readId(id);
                    }
                    avlTree.insert(new Patient(name,id));

                    //Serialize the data locally
                    writeJsonFile();
                    break;

                case "3":
                    try {
                        File file = new File(databaseFile);
                        //List of files contained by the parent folder of databaseFile
                        ArrayList<File> myFiles = new ArrayList<>();
                        //myFiles.addAll(List.of(new File(databaseFile.replace("/DataBase.txt","")).listFiles()));
                        myFiles.addAll(List.of(new File(databaseFile).getParentFile().listFiles()));
                        if (!myFiles.contains(new File(new File(databaseFile).getParent()+"/.git")) ){
                            initializeGit();
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
                    gitPull(new File(databaseFile), "commit");
                    loadDataToRoot();

            }
        }
        sc.close();
    }

    // Used on the menu
    public static long readId(long id){
        //Guarantee id is a number
        try {
            id = Long.parseLong(sc.nextLine());
        }catch (NumberFormatException e){
            System.out.println("Please provide a valid id number: ");
        }
        return id;
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
                value = " ";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    // Check if DataBase file exist. And calls loadDataToRoot()
    public static void createDataBase(){
        File file = new File(databaseFile);
        while (!file.exists()) {
            //Guarantee folder and file existence
            try {
                System.out.println("-> Write the full path of the folder where you want to create your DataBase file");
                String xPath = sc.nextLine();
                file = new File(xPath);
                if (file.isDirectory() && file.listFiles().length==0){
                    file = new File(xPath+"/DataBase.txt");
                    file.createNewFile();
                    databaseFile = xPath+"/DataBase.txt";
                    System.out.println("DataBase.txt Created");
                    writeFiles(APPSTATE_DB_PATH, databaseFile);

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
                if (option.equalsIgnoreCase("Y")) {
                    //Git pull (get the latest remote version) or choose a specific commit
                    gitPull(file, "");
                    // Load to Root on tree
                    loadDataToRoot();
                }
            }
        }

    }

    // Read Json on the .txt file and Loads Data
    public static void loadDataToRoot(){
        String json = readFile(databaseFile);
        //Load the data on the AVL tree. BST
        Node node = gson.fromJson(json, Node.class);
        avlTree.setRoot(node);
    }

    public static void writeJsonFile(){
        File file = new File(databaseFile);
        // Create file or delete its data
        try {
            if(!file.exists())
                file.createNewFile();
            else{
                //Clear data on DataBase.txt To Overwrite
                new FileWriter(databaseFile, false).close();
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
        System.out.print(".");
        try {
            //Initialize the repo
            String commandsTotal = "powershell.exe cd "+ databaseFile.replace("/DataBase.txt","")+"; git init"+
                    "; git branch -M main"+
                    "; git remote add origin https://github.com/JD-Lora1/Clinic-DataBase-Backup.git -m main";

            process = Runtime.getRuntime().exec(commandsTotal);
            System.out.print(".");
            process.waitFor();
            System.out.print(" 100%");

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
        while ((line = buf.readLine()) != null)
            System.out.println(line);

        //Read console output
        buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = buf.readLine()) != null)
            System.out.println(line);

        buf.close();
    }

    public static void backupCommand() throws IOException {
        Date date = new Date();
        System.out.print("Backup .");
        String commandsTotal = "powershell.exe cd "+ databaseFile.replace("/DataBase.txt","") +
                "; git add DataBase.txt"+
                "; git commit -m 'Backup: "+date.toString()+"'"+
                "; git push origin main";
        System.out.print(".");
        Process process = Runtime.getRuntime().exec(commandsTotal);
        System.out.print(".");
        try {
            //If there are errors, print them
            process.waitFor();
            System.out.print(".");
        } catch (InterruptedException e) {
            e.printStackTrace();
            process.destroy();
        }
        System.out.println(". Done");
        powershellReader(process);
    }
    private static ArrayList<String> gitLog() {
        String command1 = "powershell."+os+" cd " + databaseFile.replace("/DataBase.txt","")+
                "; git log --graph --decorate --format=format:\'%C(bold blue)%h%C(reset) - %C(bold green)(%ar)%C(reset) %C(white)%s%C(reset) %C(dim white)\'";
        Process process = null;
        ArrayList<String> commits = new ArrayList<>();

        try {
            process = Runtime.getRuntime().exec(command1);
            process.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
            }
            buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int counter =0;
            while ((line = buf.readLine()) != null) {
                System.out.println(counter+++") "+line);
                //Commits reduced
                commits.add(line.substring(2,9));
            }
            buf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            process.destroy();
        }
        return commits;
    }


    public static void gitPull(File file, String option){
        //Check if git was initialized
        ArrayList<File> myFiles = new ArrayList<>(List.of(new File(databaseFile).getParentFile().listFiles()));
        if (!myFiles.contains(new File(databaseFile.replace("/DataBase.txt","")+"/.git"))){
            initializeGit();
        }

        //If file is empty, delete it to do a git pull
        if (file.length()==0){
            file.delete();
        } else {
            String opt = "";
            try {
                while (!opt.equals("1") && !opt.equals("2")){
                    System.out.println("The current changes will be deleted. " +
                            "\nPlease first do a commit. " +
                            "Or delete data of the file. Choose: " +
                            "\n1. Commit changes" +
                            "\n2. Delete current data on file");
                    opt = sc.nextLine();
                    if (opt.equals("1"))
                        backupCommand();
                    else if (opt.equals("2"))
                        file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        int exitValue = gitPullCommit("0"); //git pull basic. Charge the latest version, and brings log to local

        //If gitPull finalized whitout errors (exitvalue=0) and option is "" (User dont ask to get another commit directly)
        if (option.equals("") && (exitValue==0)) {
            System.out.println("Get the last version of DataBase.txt. Do you want to choose another one? Y/N ");
            option = "";
            while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                option = sc.nextLine();
                if (option.equalsIgnoreCase("Y"))
                    gitLogCommits();
            }

        }
        //User asks to get an specific commit
        else if (exitValue==0)
            gitLogCommits();

    }

    //Show log, then calls gitPullCommit
    public static void gitLogCommits(){
        ArrayList<String> commits = new ArrayList<>();
        int commit = -1;

        while (commit==-1 || commit>commits.size()-1){
            System.out.println("Chose the hash's index of the backup which you want to import:");
            commits = gitLog();
            commit = Integer.parseInt(sc.nextLine());
        }
        gitPullCommit(commits.get(commit));
    }

    //Restore the given commit latest/specific one
    public static int gitPullCommit(String commit) {
        int exitValue=1;
        String command = "powershell."+os+" cd "+ databaseFile.replace("/DataBase.txt","");
        //Get the latest commit and the log
        if (commit.equals("0"))
            command+="; git pull origin main";
        
        //Get a specific commit by its hash
        else {
            System.out.print("Reading Data ");
            command+= "; git checkout "+commit+" -- "+ databaseFile;
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if ((exitValue = process.waitFor())==1){
                powershellReader(process);
            }else {
                System.out.println("Data Actualized with "+(commit.equals("0")? "Last":commit)+" commit");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return exitValue;
    }

}
