package Comparators;

import model.Patient;

import java.util.Comparator;

public class CompareByName implements Comparator<Patient> {

    @Override
    public int compare(Patient p1, Patient p2) {
        return p1.getName().compareTo(p2.getName());
    }

    @Override
    public Comparator reversed() {
        return Comparator.super.reversed();
    }
}
