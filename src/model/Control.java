package model;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Control {

    //TODO
    // create a String datbaseParent (folder). database.getParentFile
    public final String APPSTATE_DB_PATH = "appState/dataBase-Path.txt"; //File that contains the path of DataBase file
    public String databaseFile = " ";
    public String os = "exe"; //Default OS: Windows
    public AVL_Tree avlTree; //Binary tree to save patients info
    public Gson gson; //To use Json
    private Scanner sc = new Scanner(System.in);

    public Control(){
        avlTree = new AVL_Tree();
        gson = new Gson();
    }

    public void start(){
        //Start reading the file that contains the path of database
        databaseFile = readFile(APPSTATE_DB_PATH);
        //Read Json. Deserialize, set data to Root's tree (and it children)
        createDataBase();
    }


    public static String readFile(String filePath) {
        StringBuilder value = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;

            while ((line = br.readLine()) != null) {
                value.append(line);
            }
            fis.close();
            br.close();

            if (value==null)
                value = new StringBuilder(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value.toString();
    }

    // Check if DataBase file exist. And calls loadDataToRoot()
    public void createDataBase(){
        File file = new File(databaseFile);
        while (!file.exists()) {
            //Guarantee folder and file existence
            try {
                //The path works with normal slash (/) or reverted slash (\), but seems not always with the last one
                System.out.println("-> Write the full path of the folder where you want to create your DataBase file" +
                        "\nor where it has already been created");
                String xPath = sc.nextLine();
                file = new File(xPath);
                ArrayList<File> myFiles;
                try {
                    myFiles = new ArrayList<>(List.of(file.listFiles()));
                }catch (NullPointerException e){
                    System.out.println("Invalid path. Choose another one");
                    if (file.isFile())
                        System.out.println("Expected: Directory  |  Given: File");
                    file = new File("");
                    continue;
                }

                if (myFiles.size()==0){
                    file = new File(xPath+"/DataBase.txt");
                    file.createNewFile();
                    databaseFile = xPath+"/DataBase.txt";
                    System.out.println("DataBase.txt Created");
                    writeFiles(APPSTATE_DB_PATH, databaseFile);
                    break;
                }else if(myFiles.size()==1 && myFiles.get(0).isFile()){
                    databaseFile = xPath+"/DataBase.txt";
                    if (!myFiles.get(0).equals(new File(databaseFile))){
                        myFiles.get(0).renameTo(new File(databaseFile));
                        System.out.println("File renamed as DataBase.txt");
                    }else {
                        System.out.println("Same");
                    }
                    writeFiles(APPSTATE_DB_PATH, databaseFile);
                    break;
                }else {
                    System.out.println("The folder should be empty or just with the DataBase.txt file");
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
    public void loadDataToRoot(){
        String json = readFile(databaseFile);
        //Load the data on the AVL tree. BST
        Node node = gson.fromJson(json, Node.class);
        avlTree.setRoot(node);
    }

    public void writeJsonFile(){
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

    public void writeFiles(String filePath, String sWrite){
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

    public int initializeGit(){
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

    public void powershellReader(Process process) throws IOException {
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

    public void backupCommand() throws IOException {
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
    private ArrayList<String> gitLog() {
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


    public void gitPull(File file, String option){
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
    public void gitLogCommits(){
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
    public int gitPullCommit(String commit) {
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
                System.out.println("\nData Actualized with "+(commit.equals("0")? "Last":commit)+" commit\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return exitValue;
    }

    //TO NODES

    public void findPatient(long id){
        Node foundNode = avlTree.findPatient(id);
        if (foundNode!=null){
            System.out.println("Found:");
            System.out.println(foundNode.getPatient().toString());
        }else {
            System.out.println("Not Found:");
        }
    }

    public void addPatient(String name, long id){
        avlTree.insert(new Patient(name,id));
        System.out.println("Patient added");
    }
}
