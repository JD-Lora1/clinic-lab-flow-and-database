package ui;

import com.google.gson.Gson;
import model.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
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
        readJsonFile();
        Control control = new Control();

        try {
            //Default: Windows(exe)
            // Save on remote
            powershellCommand("exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //menu
        String opt = "";
        while (!opt.equals("0")){
            System.out.println("Choose an option:" +
                    "\n  1.Search a patient" +
                    "\n  2.Add a new patient" +
                    "\n  0.Exit");
            opt = sc.nextLine();

            switch (opt){
                case "1":break;
                case "2":
                    System.out.println("Please provide the name");
                    String name = sc.nextLine();
                    System.out.println("Now, write the id");
                    String id = sc.nextLine();
                    avlTree.insert(new Node(control.addPacient(name,Integer.parseInt(id))));
                    break;
            }
        }
        if (avlTree.getRoot()!=null){
            writeJsonFile();
        }
    }

    public static String menu(){
        String opt = "";
        while (!opt.equals("0")){
            System.out.println("Choose an option:" +
                    "\n  1.Search a patient" +
                    "\n  2.Add a new patient" +
                    "\n  0.Exit\n");
            switch (opt){
                case "1":break;
                case "2":
                    break;
            }
        }
        return opt;
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
            html = elementsByTd.get(1).text();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node node = gson.fromJson(html, Node.class);
        avlTree.setRoot(node);
    }

    public static void powershellCommand(String os) throws IOException {
        /*File file = new File("powershellCommands.txt");
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String command ="";
        String line;

        while ((line = br.readLine()) != null) {
            command += line;
        }*/
        String[] commands = new String[3];
        commands[0] = "powershell.exe git add DataBase.txt";
        commands[1] = "powershell.exe git commit -m 'commit prueba4'";
        commands[2] = "powershell.exe git push";

        // Executing the command
        Process powerShellProcess1 = Runtime.getRuntime().exec(commands[0]);
        try {
            powerShellProcess1.waitFor();
        } catch (InterruptedException e) {
            powerShellProcess1.destroy();
        }
        Process powerShellProcess2 = Runtime.getRuntime().exec(commands[1]);
        try {
            powerShellProcess2.waitFor();
        } catch (InterruptedException e) {
            powerShellProcess2.destroy();
        }
        Process powerShellProcess3 = Runtime.getRuntime().exec(commands[2]);
        try {
            powerShellProcess3.waitFor();
        } catch (InterruptedException e) {
            powerShellProcess3.destroy();
        }

        /*Process powerShellProcess1 = null;
        try {
            powerShellProcess1 = Runtime.getRuntime().exec(commands);

            int exitValue = powerShellProcess1.waitFor();
            System.out.println("exit value: " + exitValue);

        } catch (InterruptedException e) {
            //thread was interrupted.
            if(powerShellProcess1!=null) { powerShellProcess1.destroy(); }
            //reset interrupted flag
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            //an other error occurred
            if(powerShellProcess1!=null) { powerShellProcess1.destroy(); }
        }

        //2
        Process powerShellProcess2 = null;
        try {
            powerShellProcess2 = Runtime.getRuntime().exec(commands[1]);
            int exitValue = powerShellProcess2.waitFor();
            System.out.println("exit value: " + exitValue);

        } catch (InterruptedException e) {
            //thread was interrupted.
            if(powerShellProcess2!=null) { powerShellProcess2.destroy(); }
            //reset interrupted flag
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            //an other error occurred
            if(powerShellProcess2!=null) { powerShellProcess2.destroy(); }
        }
        //3
        try {
            powerShellProcess1 = Runtime.getRuntime().exec(commands[2]);

            int exitValue = powerShellProcess1.waitFor();
            System.out.println("exit value: " + exitValue);

        } catch (InterruptedException e) {
            //thread was interrupted.
            if(powerShellProcess1!=null) { powerShellProcess1.destroy(); }
            //reset interrupted flag
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            //an other error occurred
            if(powerShellProcess1!=null) { powerShellProcess1.destroy(); }
        }*/
        //powerShellProcess1.getOutputStream().close();

        // Getting the results
        //powerShellProcess1.getOutputStream().close();
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
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
