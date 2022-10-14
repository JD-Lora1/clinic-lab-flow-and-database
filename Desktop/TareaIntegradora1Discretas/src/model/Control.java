package model;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Control {

    public final String APPSTATE_DB_PATH = "appState/dataBase-Path.txt"; //File that contains the path of DataBase file
    public String databasePath = " ";
    public String databaseParent = " ";
    public String os = "exe"; //Default OS: Windows
    public AVL_Tree avlTree; //Binary tree to save patients info
    public Gson gson; //To use Json
    private Scanner sc = new Scanner(System.in);
    public StackUndo<Node, NodeQueue<Patient> >  undoHistory;
    public MyQueue queue;

    public Control(Comparator comparator){
        avlTree = new AVL_Tree(comparator);
        gson = new Gson();
        undoHistory = new StackUndo<>();
    }

    public void start(){
        //Start reading the file that contains the path of database, also get the value to its parent folder
        databasePath = readFile(APPSTATE_DB_PATH);
        databaseParent = new File(databasePath).getParent();
        //Check if file exists, else create it
        createDataBase();
        //Read Json. Deserialize, set data to Root's tree (and it children)
        loadDataToRoot();
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
        File file = new File(databasePath);
        if (!file.exists()){
            file = null;
        }
        while (file==null) {
            //Guarantee folder and file existence
            try {
                System.out.println("-> Write the full path of the folder where you want to create your DataBase file" +
                        "\nor where it has already been created");
                String xPath = sc.nextLine();
                file = new File(xPath);
                databaseParent = file.getAbsolutePath();
                ArrayList<File> myFiles;
                try {
                    myFiles = new ArrayList<>(List.of(file.listFiles()));
                }catch (NullPointerException e){
                    System.out.println("Invalid path. Choose another one");
                    if (file.isFile())
                        System.out.println("Expected: Directory  |  Given: File");
                    file = null;
                    databaseParent = null;
                    continue;
                }

                if (myFiles.size()<=2){
                    if (myFiles.size()==0){
                        file = new File(xPath, "DataBase.txt");
                        databasePath = file.getAbsolutePath();
                        file.createNewFile();
                        System.out.println("DataBase.txt Created");
                        writeFiles(APPSTATE_DB_PATH, databasePath);
                        break;
                    }else {
                        int index = -1; //to know in which position is the txt file

                        if (myFiles.size()==1){ //size == 1
                            //Check if just contains a .txt file
                            if (myFiles.get(0).isFile() && myFiles.get(0).getName().endsWith(".txt"))
                                index = 0;
                        }
                        else { //size==2
                            //Check if contains a .txt file
                            if (myFiles.get(0).isFile() && myFiles.get(0).getName().endsWith(".txt")){
                                index = 0;
                            }else if (myFiles.get(1).isFile() && myFiles.get(1).getName().endsWith(".txt"))
                                index = 1;

                            //Check if also contains a .git folder
                            if (index==0 && myFiles.get(1).isDirectory() && myFiles.get(1).getName().equals(".git")) {
                                index=0;
                            }else if (index == 1 && myFiles.get(0).isDirectory() && myFiles.get(0).getName().equals(".git")) {
                                index=1;
                            }else
                                index = -1;
                        }

                        if(index!=-1) {
                            file = new File(xPath, "DataBase.txt");
                            databasePath = file.getAbsolutePath();

                            if (!myFiles.get(index).getAbsolutePath().equals(databasePath)) {
                                //Move data to file, and delete oldFile.
                                Path oldFile = myFiles.get(index).toPath();
                                Files.move(oldFile, oldFile.resolveSibling(databasePath));
                                System.out.println("File renamed as DataBase.txt");
                            } else
                                System.out.println("DataBase.txt accepted");

                            writeFiles(APPSTATE_DB_PATH, databasePath);
                            break;
                        }else {
                            file = null;
                            databaseParent = null;
                            System.out.println("The folder should be empty; just with the DataBase.txt file or with that file and the .git folder");
                        }
                    }
                }
                else {
                    file = null;
                    databaseParent = null;
                    System.out.println("The folder should be empty; just with the DataBase.txt file or with that file and the .git folder");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(file.length()==0){
            System.out.println("The local DataBase seems empty");
            String option = "";
            while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                System.out.println("Do you want to import the data from a remote DataBase?: Y/N");
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
        if (new File(databasePath).exists()) {
            String json = readFile(databasePath);
            //Load the data on the AVL tree. BST
            Node node = gson.fromJson(json, Node.class);
            avlTree.setRoot(node);
        }
    }

    public void writeJsonFile(){
        File file = new File(databasePath);
        // Create file or delete its data
        try {
            if(!file.exists())
                file.createNewFile();
            else{
                //Clear data on DataBase.txt To Overwrite
                new FileWriter(databasePath, false).close();
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
            String commandsTotal = "powershell.exe cd "+ databaseParent+"; git init"+
                    "; git branch -M main"+
                    "; git remote add origin https://github.com/JD-Lora1/Clinic-DataBase-Backup.git -m main" +"";
                    //"; git add DataBase.txt";

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
        String commandsTotal = "powershell.exe cd "+ databaseParent +
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
        String command1 = "powershell."+os+" cd " + databaseParent +
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
        ArrayList<File> myFiles = new ArrayList<>(List.of(new File(databaseParent).listFiles()));
        if (!myFiles.contains(new File(databaseParent, ".git"))){
            initializeGit();
        }

        //If file is empty, delete it to do a git pull
        File tempFile = null;
        if (file.length()==0){
            //Move data to tempfile, and delete oldFile.
            try {
                tempFile = new File(databaseParent,"~DB_TempFile.txt");
                Path oldFile = file.toPath();
                Files.move(oldFile, oldFile.resolveSibling(tempFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                    else if (opt.equals("2")){
                        tempFile = new File(databaseParent+"/~DB_TempFile.txt");
                        file.renameTo(tempFile);
                        file.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        int exitValue = gitPullCommit("0"); //git pull basic. Charge the latest version, and brings log to local

        //If gitPull finalized without errors (exit value = 0) and option is "" (User doesn't ask to get another commit directly)
        if (option.equals("") && (exitValue==0)) {
            System.out.println("Get the last version of DataBase.txt. Do you want to choose another one? Y/N ");
            while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                option = sc.nextLine();
                if (option.equalsIgnoreCase("Y"))
                    gitLogCommits();
            }
        }
        //User asks to get a specific commit
        else if (exitValue==0){
            gitLogCommits();
        }

        if (tempFile != null) {
            if (file.exists())
                tempFile.delete();
            else {
                try {
                    Path oldFile = tempFile.toPath();
                    Files.move(oldFile, oldFile.resolveSibling(file.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
        int exitValue = 0;
        String command = "powershell."+os+" cd "+ databaseParent;
        //Get the latest commit and the log
        if (commit.equals("0"))
            command+="; git pull origin main";

        //Get a specific commit by its hash
        else {
            System.out.print("Reading Data ");
            command+= "; git checkout "+commit+" -- "+ databasePath;
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if((exitValue = process.waitFor())==0){
                System.out.println("\nData Actualized with "+(commit.equals("0")? "Last":commit)+" commit");
                if(new File(databasePath).length() == 0)
                    System.out.println("The data of the "+(commit.equals("0")? "last":commit)+" Backup was empty" +
                            "\nSee it on: https://github.com/JD-Lora1/Clinic-DataBase-Backup");
            }else {
                powershellReader(process);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return exitValue;
    }

    //TO NODES

    public void findPatient(String id){
        Node foundNode = avlTree.findPatient(id);
        if (foundNode!=null){
            System.out.println("Found:");
            System.out.println(foundNode.getPatient().showData());
        }else {
            System.out.println("Not Found:");
        }
    }

    public Node addPatient(String name, String id){

        return avlTree.insert(new Patient(name,id));
    }

    // Advanced options
    public void factoryReset(){
        File file = new File(APPSTATE_DB_PATH);
        try {
            new FileWriter(APPSTATE_DB_PATH, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void undo(){
        if (!undoHistory.isEmpty()) {
            NodeHistory lastChange = undoHistory.pop();
            Node avlnode = (Node) lastChange.getNodeTvalue();
            NodeQueue nodeQueue = (NodeQueue) lastChange.getNodeHvalue();
            String actionT = lastChange.getActionT();

            if (avlnode!=null){
                if (actionT.equals("Delete AVL-Node")){
                    avlTree.insert(avlnode.getPatient());
                }else if(actionT.equals("Insert AVL-Node")){
                    //TODO
                    // Make delete method works with AVL trees
                    avlTree.delete(avlnode.getPatient().getId());
                }
            }
            else if (nodeQueue!=null){
                if (actionT.equals("Delete MyQueue-Node")){
                    queue.undoDequeue(nodeQueue);
                }else if(actionT.equals("Insert MyQueue-Node")){
                    queue.undoEnqueue();
                }
            }
            System.out.println("* Undone");

            //Todo
            // Do it like enumeration
        }else {
            System.out.println("* There are no more changes registered");
        }
    }

    public void addNodeHistory(Node avlNode , NodeQueue nodeQueue, String actionT){
        undoHistory.push(avlNode, nodeQueue, actionT);
    }
}
