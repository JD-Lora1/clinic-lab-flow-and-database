package ui;

import com.google.gson.Gson;
import model.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String FILEPATH = "DataBase.txt";
    //private static ArrayList<Pacient> pacients;
    private static AVL_Tree avlTree;
    private static Gson gson;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //pacients = new ArrayList<>();

        avlTree = new AVL_Tree();
        gson = new Gson();
        readJsonFile();
        Control control = new Control();

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

        writeJsonFile();
        //avlTree.inorder();
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

    public static void readUrl() throws IOException {
        URL url = new URL("http://domain.com/file.txt");
        BufferedReader read = new BufferedReader(
                new InputStreamReader(url.openStream()));
        String i;
        while ((i = read.readLine()) != null)
            System.out.println(i);
        read.close();
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
                try {
                    if (option.equals("Y"))
                        readUrl();
                    else if (option.equals("N")) {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
