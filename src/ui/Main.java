package ui;

import model.AVL_Tree;
import model.Control;
import model.Node;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.ErrorManager;

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
                    "\n 0.Exit");
            opt = sc.nextLine();
            int id = -1;

            switch (opt){
                case "1":
                    System.out.println("Please provide the id: ");
                    while (id == -1){
                        try {
                            id = Integer.parseInt(sc.nextLine());
                        }catch (NumberFormatException e){
                            System.out.println("Please provide a valid id number: ");
                        }
                    }
                    Node foundNode = avlTree.findPatient(id);
                    if (foundNode!=null){
                        System.out.println("Found:");
                        System.out.println(foundNode.getPacient().toString());
                    }
                    id = -1;
                    break;
                case "2":
                    System.out.println("Please provide the name: ");
                    String name = sc.nextLine();
                    System.out.println("Now, write the id: ");
                    while (id == -1){
                        try {
                            id = Integer.parseInt(sc.nextLine());
                        }catch (NumberFormatException e){
                            System.out.println("Please provide a valid id number: ");
                        }
                    }
                    avlTree.insert(new Node(control.addPacient(name,id)));
                    break;

                case "3":
                    //Default OS: Windows(exe)
                    try {
                        powershellCommand("exe");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        sc.close();

        //Serialize the data locally
        writeJsonFile();
    }

    public static void readUrl() {
        String html = null;
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
                html = null;
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
        String command0 = "powershell."+os+" git checkout DataBase";
        String command1 = "powershell."+os+" git add DataBase.txt";

        String command2 = "powershell."+os+" git commit -m 'Backup:";
        String command3 = "powershell."+os+" git push";

        // Execute the commands
        System.out.print("Backup ");
        Process process0 = Runtime.getRuntime().exec(command0);
        Process process1 = null;
        Process process2 = null;
        Process process3 = null;

        String nProcess ="";

        try {
            if (process0.waitFor() == 0) {
                System.out.print(".");
                process1 = Runtime.getRuntime().exec(command1);
                if (process1.waitFor() == 0 ){
                    System.out.print(".");
                    Date date = new Date();
                    process2 = Runtime.getRuntime().exec(command2+date+"'");
                    if (process2.waitFor() == 0 ){
                        System.out.print(".");
                        process3 = Runtime.getRuntime().exec(command3);
                        if (process3.waitFor()== 0)
                            System.out.print(" Done");
                        else if (nProcess.equals(""))
                            nProcess = "process3";
                    }else if (nProcess.equals(""))
                        nProcess = "process2";

                }else if (nProcess.equals(""))
                    nProcess = "process1";

            }else
                nProcess = "process0";

            if (!nProcess.equals("")) {
                System.out.print("Incomplete\n");
                Process process = null;
                switch (nProcess){
                    case "process0": process = process0; break;
                    case "process1": process = process1; break;
                    case "process2": process = process2; break;
                    case "process3": process = process3; break;
                }
                BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = "";
                while ((line = buf.readLine()) != null) {
                    System.out.println(line);
                }
            }
            System.out.println("");

        } catch (InterruptedException e) {
            process0.destroy();
            assert process1 != null;
            process1.destroy();
            assert process2 != null;
            process2.destroy();
            assert process3 != null;
            process3.destroy();
        }

        // Getting the results
        //process1.getOutputStream().close();
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
            System.out.println("There is no data");
            String option = "";
            while (!option.equals("Y") || !option.equals("N")) {
                System.out.println("Do you wanna import the data from a remote DataBase?: Y/N");
                option = sc.nextLine();
                if (option.equals("Y")){
                    readUrl();
                    break;
                }
                else if (option.equals("N")) {
                    break;
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
            else
                new FileWriter(FILEPATH, false).close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //write
        try {
            String json = gson.toJson(avlTree.getRoot());
            FileOutputStream fos = new FileOutputStream(file);
            if(json!=null)
                fos.write(json.getBytes(StandardCharsets.UTF_8));
            else
                fos.write("".getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
