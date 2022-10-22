package model;

import Comparators.CompareByID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class AVL_TreeTest {

    private AVL_Tree tree;

    private Comparator <Patient> comparator;

    public void setup0(){
        comparator = new CompareByID();
        tree = new AVL_Tree(comparator);
    }

    public void setup1(){
        tree.insert(new Patient("Juandi","1234",18,true));
        tree.insert(new Patient("Santi","12345",20,true));
        tree.insert(new Patient("Jeison","123456",19,true));
        tree.insert(new Patient("Jeimy","1234567",29,true));
    }

    public void setup2(){
        tree.insert(new Patient("p","1",24,true));
        tree.insert(new Patient("q","3",54,true));
    }

    @Test
    public void insertTest1(){
        setup0();
        Patient patient = tree.insert(new Patient("Jasmine Gomez","1006746894",39,true)).getPatient();
        Patient p = tree.getRoot().getPatient();
        assertEquals(patient.getName(),p.getName());
    }

    @Test
    public void heightTest1(){
        setup0();
        NodeTree patient = tree.insert(new Patient("Jaqueline Rivera","23",29,true));
        NodeTree patient2 = tree.insert(new Patient("Andres","24",30,true));
        tree.getHeight(patient2);
        assertEquals(patient2.height,1);
    }


}