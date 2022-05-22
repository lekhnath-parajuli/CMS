package Objects;

import Helpers.DBUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GetAttributesFromFile {
    private Connection connect;
    private DataBaseStructure DBS;

    public GetAttributesFromFile() throws SQLException {
        connect = DBUtils.getDbConnection();
        DBS = new DataBaseStructure();
    }

    //------------------------------------------------------------------------------------------------------------------

    //##########################################- Upload courses to database -##########################################

    public void LodeCourseAttrubutesFromFile() throws FileNotFoundException, SQLException {
        connect = DBUtils.getDbConnection();
        PreparedStatement statement;

        String CoursesTable = "INSERT INTO `courses` (`CourseID`,`CourseName`,`IsCancelled`) VALUES (?,?,?)";  //## add new course

        String ModulesTable = "INSERT INTO `modules` (`ModuleID`,`CourseID`,`ModuleName`,`PassMark`, `FullMark`, `IsOptional`, `Level`, `Sem`) VALUES(?,?,?,?,?,?,?,?)"; //## add all the modules\n" +

        ArrayList<String> CourseName                 = this.getFileData("CourseAdministrator\\CourseName.txt");
        ArrayList<String> CourseID                   = this.getFileData("CourseAdministrator\\CourseID.txt");
        ArrayList<String> ModuleName                 = this.getFileData("CourseAdministrator\\ModuleName.txt");
        ArrayList<String> ModuleID                   = this.getFileData("CourseAdministrator\\ModuleId.txt");
        ArrayList<String> IsOptional                 = this.getFileData("CourseAdministrator\\IsOptional.txt");
        ArrayList<String> ModulesBelongToWhichCourse = this.getFileData("CourseAdministrator\\ModuleBelongToWhichCourse.txt");
        ArrayList<String> ModulesBelongToWhichLavel  = this.getFileData("CourseAdministrator\\ModuleBelongToWhichLevel.txt");

        int previous = 0;
        System.out.println("uploading all courses and modules to the database !");
        for(int i=0; i < CourseName.size(); i++) {
            int percent = DBS.getPercent(CourseName.size(), i+1);

            if(percent % 10 == 0 && percent > previous) {
                System.out.print(percent+"% ");
            }
            previous = percent;
            Course Course = new Course();

            Course.CourseName = CourseName.get(i);
            Course.CourseID = CourseID.get(i);

            statement = connect.prepareStatement(CoursesTable);
            statement.setString(1, Course.CourseID);
            statement.setString(2, Course.CourseName);
            statement.setBoolean(3, Course.isCancelled);
            statement.executeUpdate();
            statement.close();

            for(int j=0; j<ModuleName.size(); j++) {
                Module module = new Module();
                if(ModulesBelongToWhichCourse.get(j).equals(Course.CourseID)) {
                    module.ModuleID = ModuleID.get(j);                             //## it loads all courses data
                    module.ModuleName = ModuleName.get(j);                         //## and upload it to database
                    module.Level = DBS.getLevel(Integer.parseInt(ModulesBelongToWhichLavel.get(j)));
                    module.Sem = DBS.getSem(Integer.parseInt(ModulesBelongToWhichLavel.get(j)));
                    module.isOptional = this.convertToBoolean(IsOptional.get(j));
                    module.FullMark = 100;
                    module.PassMark = 40;

                    statement = connect.prepareStatement(ModulesTable);
                    statement.setString(1, module.ModuleID);
                    statement.setString(2, Course.CourseID);
                    statement.setString(3, module.ModuleName);
                    statement.setInt(4, module.PassMark);
                    statement.setInt(5, module.FullMark);
                    statement.setBoolean(6, module.isOptional);
                    statement.setInt(7, module.Level);
                    statement.setInt(8, module.Sem);
                    statement.executeUpdate();
                    statement.close();
                }
            }
        }
        connect.close();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //##########################################- Upload Instructors to database -######################################

    public void LodeInstructorData() throws FileNotFoundException, SQLException {
        connect = DBUtils.getDbConnection();
        PreparedStatement statement;

        String InsertInstructor = "INSERT INTO  instructors (InstructorID,FirstName,LastName)" +  //## add new course
                "VALUES (?,?,?)";
        String GiveInstructorAmodule = "INSERT INTO instructor_modules " +
                "(InstructorID,ModuleID)" + //## add all the modules
                "VALUES (?,?)";

        System.out.println("uploading all Instructor related data to the database !");
        ArrayList<String> ModuleID                   = this.getFileData("CourseAdministrator\\ModuleId.txt");
        ArrayList<String> FirsetName                 = this.getFileData("Instructor\\InstructureName.txt");
        ArrayList<String> LastName                   = this.getFileData("Instructor\\InstructureSurname.txt");
        ArrayList<String> InstructorTeachingModule   = this.getFileData("Instructor\\InstructorTeachingModule.txt");

        int previous = 0;
        for(int i=0; i<InstructorTeachingModule.size(); i++) {
            int percent = DBS.getPercent(InstructorTeachingModule.size(), i+1);

            if(percent % 10 == 0 && percent > previous) {
                System.out.print(percent+"% ");
            }
            previous = percent;

            Instructor INS = new Instructor();
            Module module = new Module();
            INS.InstructorID = InstructorTeachingModule.get(i);                 //## it Load all Instructor data
            INS.FirstName = FirsetName.get(i);                                  //## and upload it to database
            INS.LastName = LastName.get(i);
            module.ModuleID = ModuleID.get(i);
            INS.Modules.add(module);


            if(DBS.notExists("InstructorID",INS.InstructorID, "instructors")) {
                statement = connect.prepareStatement(InsertInstructor);
                statement.setString(1, INS.InstructorID);
                statement.setString(2, INS.FirstName);
                statement.setString(3, INS.LastName);

                statement.executeUpdate();
                statement.close();
            }

            if(!DBS.notTeachingSameModule("InstructorID", "ModuleID", INS.InstructorID, INS.Modules.get(0).ModuleID, "instructor_modules")) {
                statement = connect.prepareStatement(GiveInstructorAmodule);
                statement.setString(1, INS.InstructorID);
                statement.setString(2, INS.Modules.get(0).ModuleID);
                statement.executeUpdate();
                statement.close();
            }
        }
        connect.close();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //##########################################- Upload Students to database -#########################################

    public void LodeStudentData() throws SQLException {
        connect = DBUtils.getDbConnection();
        PreparedStatement statement;

        ArrayList<String> StudentID    = this.getFileData("Student\\StudentID.txt");
        ArrayList<String> CourseID     = this.getFileData("Student\\StudentCourseIDChoice.txt");
        ArrayList<String> FirsetName   = this.getFileData("Student\\Names.txt");
        ArrayList<String> LastName     = this.getFileData("Student\\Surnames.txt");
        ArrayList<String> Level        = this.getFileData("Student\\StudentCourseLevelChoice.txt");

        System.out.println("uploading all Student related data to the database !");

        int previous = 0;
        for(int i=0; i<StudentID.size(); i++) {

            int percent = DBS.getPercent(StudentID.size(), i+1);

            if(percent % 10 == 0 && percent > previous) {
                System.out.print(percent+"% ");
            }
            previous = percent;

            ArrayList<Module> YourModules = new ArrayList<Module>();

            for(int j=1; j<3; j++) {
                if (DBS.courseLevelHaveOptional(CourseID.get(i), Level.get(i), j)) {
                    YourModules.addAll(DBS.getNonOptionalCourses(CourseID.get(i), Integer.parseInt(Level.get(i)), j));
                    YourModules.addAll(DBS.chooseAnyTwoModule(DBS.getOptionalCourses(CourseID.get(i), Integer.parseInt(Level.get(i)), j)));
                } else {
                    YourModules.addAll(DBS.getNonOptionalCourses(CourseID.get(i), Integer.parseInt(Level.get(i)), j));
                }
            }

            Student student = new Student();
            student.StudentID = StudentID.get(i);                               //## it load students data and modules
            student.FirstName = FirsetName.get(i);                              //## and levels and upload it to database
            student.LastName = LastName.get(i);
            student.Level = DBS.getLevel(Integer.parseInt(Level.get(i)));

            Course course = new Course();
            course.CourseID = CourseID.get(i);
            student.Course = course;

            student.modules = YourModules;


            if(!DBS.StudentAlreadyExist(student.StudentID)) {
                String insertStudentData = "INSERT INTO students (StudentID,CourseID,FirstName,LastName, Level)" +  //## add new course
                        "VALUES (?,?,?,?,?)";
                statement = connect.prepareStatement(insertStudentData);
                statement.setString(1, student.StudentID);
                statement.setString(2, student.Course.CourseID);
                statement.setString(3, student.FirstName);
                statement.setString(4, student.LastName);
                statement.setInt(5, student.Level);
                statement.executeUpdate();
                statement.close();

                String insertStudentModules = "INSERT INTO student_modules (StudentID, ModuleID, Sem)" +  //## add new course
                        "VALUES (?,?,?)";
                statement = connect.prepareStatement(insertStudentModules);

                for (Module m : student.modules) {
                    statement.setString(1, student.StudentID);
                    statement.setString(2, m.ModuleID);
                    statement.setInt(3, m.Sem);
                    statement.executeUpdate();
                }
                statement.close();
            }
        }
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //##########################################- Upload Student Marksheet to database -#################################

    public void initializeStudentMarks() throws SQLException {
        System.out.println("uploading all Student mark sheet related data to the database !");
        connect = DBUtils.getDbConnection();
        PreparedStatement statement;

        ArrayList<String> StudentID    = this.getFileData("Student\\StudentID.txt");

        int previous = 0;
        for(int i=0; i<StudentID.size(); i++) {
            String ID = StudentID.get(i);

            int percent = DBS.getPercent(StudentID.size(), i+1);
            if(percent % 10 == 0 && percent > previous) {
                System.out.print(percent+"% ");
            }
            previous = percent;

            Marksheet m = new Marksheet();
            Student s = new Student();
            s.StudentID = ID;
            m.Student = s;

            ArrayList<Module> mod = DBS.getAllModulesStudentStuding(ID);
            for(Module module: mod) {
                int mark = 0;
                while(mark < 39) {
                    Random rand = new Random();                                 //## it initialize Student marks of
                    mark = rand.nextInt(99);                             //## all levels
                }
                module.GainedMark = mark;
                m.module.add(module);
            }

            String insertCourses = "INSERT INTO studentMarksheet (StudentID, ModuleID, Mark, IsPass, HaveRecit)" +  //## add new course
                    "VALUES (?,?,?,?,?)";
            statement = connect.prepareStatement(insertCourses);

            for(Module M: m.module) {
                statement.setString(1, m.Student.StudentID);
                statement.setString(2, M.ModuleID);
                statement.setInt(3, M.GainedMark);
                statement.setBoolean(4, DBS.checkPassOrFail(M.ModuleID, M.GainedMark));
                statement.setBoolean(5, !DBS.checkPassOrFail(M.ModuleID, M.GainedMark));

                statement.executeUpdate();
            }
            statement.close();
        }

        connect.close();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //########################################- convert string to boolean value -#######################################

    public boolean convertToBoolean(String S) {
        boolean result = true;

        if (S.equals("false")) {
            result = false;
        }
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################################- get data from file -#####################################################

    public ArrayList getFileData(String path) {
        ArrayList Datas;
        Datas = new ArrayList<String>();

        path = new  File("src").getAbsolutePath() +"\\"+path;
        try {
            File F = new File(path);
            Scanner FileReader = new Scanner(F);
            while (FileReader.hasNextLine()) {
                Datas.add(FileReader.nextLine().trim());                 //## it reads and returns all the lines found
            }                                                           //## in the file
            FileReader.close();
        } catch (FileNotFoundException err) {
            System.out.println("File error");
            System.out.println(err);
        }
     return Datas;
    }
    //------------------------------------------------------------------------------------------------------------------
}
