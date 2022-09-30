package ui;

import model.*;

//Gson Lib. To use Json
import com.google.gson.Gson;
//Jsoup Lib. To read GitHub html page
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static java.lang.Math.abs;

//TODO
// Move some methods to Control

public class Main {

    private static final String FILEPATH = "DataBase.txt";
    private static final String STATEAPP_PATH = "appState/currentState.txt";
    private static final String KEYWORD_BACKUP = "Backup:";
    private static AVL_Tree avlTree;
    private static Gson gson;
    private static Scanner sc = new Scanner(System.in);
    private static String os = "exe"; //Default OS, Windows

    public static void main(String[] args) {
        avlTree = new AVL_Tree();
        gson = new Gson();
        //It starts reading the local database file
        readJsonFile();
        readAppState();
        Control control = new Control();

        //menu
        String opt = "";
        while (!opt.equals("0")){
            System.out.println("Choose an option:" +
                    "\n 1.Search a patient" +
                    "\n 2.Add a new patient" +
                    "\n 3.Backup to Github" +
                    "\n 4.Is the tree balanced?" +
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
                        File fileAppState = new File(STATEAPP_PATH);
                        if (fileAppState.length()==0) {
                            overwriteAppState(fileAppState);
                            firstCommit();
                        }
                        writeJsonFile();
                        backupCommand();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4": isBalance();
                break;
            }
        }
        sc.close();
    }

    public static void overwriteAppState(File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("git initialized");
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readAppState(){
        File file = new File(STATEAPP_PATH);
        String initial ="";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            initial = br.readLine();
            fis.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void firstCommit(){
        System.out.println("FirstCommit");
        Process process = null;
        try {
            //Initialize the repo
            process = Runtime.getRuntime().exec("powershell."+os+" git init");
            process.waitFor();
            powershellReader(process);
            process = Runtime.getRuntime().exec("powershell."+os+" git add .");
            process.waitFor();
            powershellReader(process);
            System.out.println("Hola");
            process = Runtime.getRuntime().exec("powershell."+os+" git remote add origin https://github.com/JD-Lora1/clinic-lab-flow-and-database.git");
            process.waitFor();
            powershellReader(process);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            process.destroy();
        }
    }

    public static void powershellReader(Process process) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }

        buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }
        buf.close();
    }

    public static void isBalance(){
        if (avlTree.getBalance(avlTree.getRoot())==0){
            System.out.println("It´s balanced");
        }else if (abs(avlTree.getBalance(avlTree.getRoot()))==1){
            System.out.println("It's almost balanced");
        }else {
            System.out.println(">2");
        }
    }

    public static void backupCommand() throws IOException {
        String command1 = "powershell."+os+" git add DataBase.txt";
        String command2 = "powershell."+os+" git commit -m 'Backup: ";
        String command3 = "powershell."+os+" git push";

        // Execute the commands
        System.out.print("Backup ");
        Process process = Runtime.getRuntime().exec(command1);

        int exitWhit = 0;
        try {
            exitWhit = process.waitFor();
            if (exitWhit==1)
                throw new InterruptedException();
            System.out.print(".");
            Date date = new Date();
            process = Runtime.getRuntime().exec(command2+date+"'");
            exitWhit = process.waitFor();
            if (exitWhit==1)
                throw new InterruptedException();
            System.out.print(".");
            process = Runtime.getRuntime().exec(command3);
            System.out.print(".");
            exitWhit = process.waitFor();
            if (exitWhit==1)
                throw new InterruptedException();
            System.out.println(" Done");
            System.out.println("See it on https://github.com/JD-Lora1/clinic-lab-flow-and-database/blob/main/DataBase.txt");

        } catch (InterruptedException e) {
            powershellReader(process);
            process.destroy();
        }
    }
    private static ArrayList<String> gitLog() {
        String command1 = "powershell."+os+" git log --graph --decorate --format=format:\'%C(bold blue)%h%C(reset) - %C(bold green)(%ar)%C(reset) %C(white)%s%C(reset) %C(dim white)\'";
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

            while ((line = buf.readLine()) != null) {
                if (line.contains(KEYWORD_BACKUP)){
                    System.out.println(line);
                    commits.add(line.substring(2,9));
                }
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
        File file = new File(FILEPATH);
        try {
            if(!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(file.length()==0){
            System.out.println("The local DataBase is empty");
            String option = "";
            while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                System.out.println("Do you wanna import the data from a remote DataBase?: Y/N");
                option = sc.nextLine();
                if (option.equalsIgnoreCase("Y")){
                    String commit = "";
                    ArrayList<String> commits = new ArrayList<>();
                    while (commit.equals("") || !commits.contains(commit)){
                        System.out.println("Copy and paste the hash of the backup which you want to import");
                        commits = gitLog();
                        commit = sc.nextLine();
                    }
                    readUrl(commit);
                }
            }
        }else {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String json = "";
                String line;

                while ((line = br.readLine()) != null) {
                    json += line;
                }
                fis.close();
                br.close();

                Node node = gson.fromJson(json, Node.class);
                avlTree.setRoot(node);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeJsonFile();
    }

    public static void readUrl(String commit) {
        String html = "";
        URL url;
        System.out.print("Reading Data ");
        try {
            url = new URL("https://github.com/JD-Lora1/clinic-lab-flow-and-database/commit/"+commit);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            System.out.print(".");
            while ((line = reader.readLine()) != null)
                html+=line;
            Document doc = Jsoup.parse(html);

            System.out.print(".");

            // Get elements by "td". Where is the Json on the second one
            Elements elementsByTd = doc.getElementsByTag("td");
            ArrayList<String> elementsText = new ArrayList<>();
            for (int i=0; i<elementsByTd.size();i++){
                if (elementsByTd.get(i).hasText())
                    elementsText.add(elementsByTd.get(i).text());
            }

            if (elementsText.get(0).contains("+0,0"))
                html = "";
            else
                html = elementsText.get(elementsText.size()-1);

            System.out.println(". Done");

        } catch (MalformedURLException e) {
            System.out.println("URL no valida");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node node = gson.fromJson(html, Node.class);
        avlTree.setRoot(node);
    }

    public static void writeJsonFile(){
        File file = new File(FILEPATH);
        // Create file or delete its data
        try {
            if(!file.exists())
                file.createNewFile();
            else{
                //Delete data on DataBase.txt
                new FileWriter(FILEPATH, false).close();
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
