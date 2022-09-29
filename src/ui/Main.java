package ui;

import model.AVL_Tree;
import model.Control;
import model.Node;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main {

    private static final String FILEPATH = "DataBase.txt";
    private static AVL_Tree avlTree;
    private static Gson gson;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        avlTree = new AVL_Tree();
        gson = new Gson();
        //It starts reading the local database file
        readJsonFile();

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
                    avlTree.insert(new Node(control.addPacient(name,id)));
                    //Serialize the data locally
                    writeJsonFile();
                    break;

                case "3":
                    //Default OS: Windows(exe)
                    try {
                        writeJsonFile();
                        powershellCommand("exe");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case "4": isBalance();
            }
        }
        sc.close();
    }

    public static void isBalance(){
        System.out.println("left: "+avlTree.findIterations(avlTree.getRoot().getLeft(),0));
        System.out.println("right: "+avlTree.findIterations(avlTree.getRoot().getRight(),0));

    }

    public static void readUrl() {
        String html = "";
        URL url;
        try {
            url = new URL("https://github.com/JD-Lora1/clinic-lab-flow-and-database/blob/main/DataBase.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                html+=line;
            Document doc = Jsoup.parse(html);

            // Get elements by "td". Where is the Json on the second one
            Elements elementsByTd = doc.getElementsByTag("td");
            if(elementsByTd.size()>1){
                html = elementsByTd.get(1).text();
            }else {
                html = "";
            }

        } catch (MalformedURLException e) {
            System.out.println("URL no valida");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node node = gson.fromJson(html, Node.class);
        avlTree.setRoot(node);
    }

    public static void powershellCommand(String os) throws IOException {
        String command1 = "powershell."+os+" git add DataBase.txt";
        String command2 = "powershell."+os+" git commit -m 'Backup: ";
        String command3 = "powershell."+os+" git push";

        // Execute the commands
        System.out.print("Backup ");
        Process process1 = Runtime.getRuntime().exec(command1);
        Process process2 = null;
        Process process3 = null;

        String nProcess ="";
        try {
            if (process1.waitFor() == 0 ){
                System.out.print(".");
                Date date = new Date();
                process2 = Runtime.getRuntime().exec(command2+date+"'");
                if (process2.waitFor() == 0 ){
                    System.out.print(".");
                    process3 = Runtime.getRuntime().exec(command3);
                    System.out.print(".");
                    if (process3.waitFor()== 0){
                        System.out.print(" Done");
                    }
                    else if (nProcess.equals(""))
                        nProcess = "process3";
                }else if (nProcess.equals(""))
                    nProcess = "process2";

            }else
                nProcess = "process1";

            if (!nProcess.equals("")) {
                Process process = null;
                switch (nProcess){
                    case "process1":
                        process = process1;
                        System.out.print("Incomplete\n");
                        break;
                    case "process2":
                        process = process2;
                        System.out.println("Up to Date");
                        break;
                    case "process3":
                        process = process3;
                        System.out.print("Incomplete\n");
                        break;
                }
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
            System.out.println("");

        } catch (InterruptedException e) {
            process1.destroy();
            if (process2 != null)
                process2.destroy();
            if (process3 != null)
                process3.destroy();
        }
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
            System.out.println("The DataBase is empty");
            String option = "";
            while (!option.equals("Y") && !option.equals("N")) {
                System.out.println("Do you wanna import the data from a remote DataBase?: Y/N");
                option = sc.nextLine();
                if (option.equals("Y")){
                    readUrl();
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
