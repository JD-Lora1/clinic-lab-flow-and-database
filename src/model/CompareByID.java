package model;

import java.util.Comparator;

public class CompareByID implements Comparator<Patient> {

    @Override
    public int compare(Patient p1, Patient p2) {
        return p1.getId().compareTo(p2.getId());
    }

    @Override
    public Comparator reversed() {
        return Comparator.super.reversed();
    }
}
