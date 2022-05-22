package Objects;

import java.util.ArrayList;

public class Student {
    public String FirstName = "";
    public String LastName = "";
    public String StudentID = "";
    public int Level = 0;
    public int Sem = 0;
    public boolean IsPass = false;
    public boolean HaveRecit = false;
    public Course Course = new Course();
    public ArrayList<Module> modules = new ArrayList<>();
}
