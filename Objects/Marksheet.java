package Objects;

import java.util.ArrayList;

public class Marksheet {
    public Student Student;
    public ArrayList<Module> module = new ArrayList<Module>();
    public Course Course = new Course();
    public boolean isPass = true;
    public boolean HaveRecit = false;
    public int gainedmark = 0;
}
