package Objects;

import Helpers.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class DataBaseStructure {
    private Connection connectDataBase;
    private Connection connection;
    public int previousLergest = -1;

    //------------------------------------------------------------------------------------------------------------------
    //############################################- SQL Queries -#######################################################

    private String CreateDataBaseQuery = "create database np03a190308";

    private String CoursesTableQuery = "create table courses(" +
            "CourseID varchar(100) not null," +
            "CourseName varchar(50) not null," +
            "IsCancelled boolean not null," +
            "UNIQUE(CourseID)," +
            "primary key(CourseID)" +
            ");";

    private String ModulesTableQuery = "create table modules(" +
            "ModuleID varchar(100) not null," +
            "CourseID varchar(100) not null," +
            "ModuleName varchar(100) not null," +
            "PassMark int not null," +
            "FullMark int not null," +
            "IsOptional boolean not Null," +
            "Level int not null," +
            "Sem int not null," +
            "primary key(ModuleID)," +
            "UNIQUE(ModuleID)," +
            "foreign key(CourseID) references courses(CourseID) " +
            "on delete cascade "+
            "on update cascade "+
            ");";

    private String InstructorTableQuery = "create table instructors(" +
            "InstructorID varchar(100) not null," +
            "FirstName varchar(100) not null," +
            "LastName varchar(100) not null," +
            "primary key(InstructorID)," +
            "UNIQUE(InstructorID)" +
            ");";

    private String InstructorTeachingModules = "create table instructor_modules(" +
            "InstructorID varchar(100) not null," +
            "ModuleID varchar(100) not null," +
            "CONSTRAINT module_id_fk "+
            "foreign key(ModuleID) references modules(ModuleID) " +
            "on delete cascade "+
            "on update cascade, "+
            "foreign key(InstructorID) references instructors(InstructorID) " +
            "on delete cascade "+
            "on update cascade "+
            ");";

    private String StudentTableQuery = "create table students(" +
            "StudentID varchar(100) not null," +
            "CourseID varchar(100) not null," +
            "FirstName varchar(100) not null," +
            "LastName varchar(100) not null," +
            "Level int not null," +
            "primary key(StudentID)," +
            "foreign key(CourseID) references courses(CourseID) " +
            "on delete cascade "+
            "on update cascade "+
            ");";

    private String StudentModuleTableQuery = "create table student_modules(" +
            "StudentID varchar(100) not null," +
            "ModuleID varchar(100) not null," +
            "Sem int not null," +
            "foreign key(StudentID) references students(StudentID) " +
            "on delete cascade "+
            "on update cascade, "+
            "foreign key(ModuleID) references modules(ModuleID) " +
            "on delete cascade "+
            "on update cascade "+
            ");";

    private String StudentMarksheetQuery = "create table studentMarksheet(" +
            "StudentID varchar(100) not null," +
            "ModuleID varchar(100) not null," +
            "Mark int default 0," +
            "IsPass boolean default false," +
            "HaveRecit boolean default false," +
            "foreign key(StudentID) references students(StudentID) " +
            "on delete cascade "+
            "on update cascade, "+
            "foreign key(ModuleID) references modules(ModuleID) " +
            "on delete cascade "+
            "on update cascade "+
            ");";

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>- Creating Database and its tables ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //------------------------------------------------------------------------------------------------------------------

    public DataBaseStructure() throws SQLException {
        connectDataBase = DBUtils.getDbConnectionDataBase();

        if(this.findDatabaseIsCreatedOrNot()) {
            connection = DBUtils.getDbConnection();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    //#############################- if Id exist already -##############################################################

    public boolean notExists(String col, String ID, String Table) throws SQLException {
        connection = DBUtils.getDbConnection();
        String Query = "SELECT " + col + " FROM " + Table;

        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        boolean dataFound = true;

        while (resultSet.next()) {
            String DBname = resultSet.getString("InstructorID");
            if (DBname.equals(ID)) {
                dataFound = false;
                break;
            }
        }
        return dataFound;
    }

    public int getMaxConnectionValue() throws SQLException {
        String Query = "show variables like \"max_connections\" ";
        PreparedStatement statement = connectDataBase.prepareStatement(Query);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        int maxNoOfConnections = resultSet.getInt(2);
        return maxNoOfConnections;
    }

    public void setMaxConnections(int max) throws SQLException {
        String Query = "set global max_connections = "+max+";";
        PreparedStatement statement = connectDataBase.prepareStatement(Query);
        statement.executeUpdate();
    }

    //------------------------------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------------------------
    //#############################- if Id exist already -##############################################################

    public boolean notTeachingSameModule(String col, String col2, String ID, String ID2, String Table) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "SELECT * FROM " + Table + " where " + col + "=\"" + ID + "\" and " + col2 + "=\"" + ID2 + "\";";

        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    //------------------------------------------------------------------------------------------------------------------

    //#####################################- it checks whether the data base already exists or not -####################

    public boolean findDatabaseIsCreatedOrNot() throws SQLException {
        String Query = "show databases";
        PreparedStatement statement = connectDataBase.prepareStatement(Query);

        ResultSet resultSet = statement.executeQuery();
        boolean databaseFound = false;

        while (resultSet.next()) {
            String DBname = resultSet.getString(1);
            if (DBname.equals("np03a190308")) {
                databaseFound = true;
            }
        }
        return databaseFound;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#####################################- create Database -##########################################################

    public void createDatabase() throws SQLException {
        connectDataBase = DBUtils.getDbConnectionDataBase();

        System.out.println("Creating A database !");
        connectDataBase = DBUtils.getDbConnectionDataBase();
        PreparedStatement statement = connectDataBase.prepareStatement(this.CreateDataBaseQuery);
        statement.executeUpdate();
        connection = DBUtils.getDbConnection();
        System.out.println("\nFinished !\n\n");
        connectDataBase.close();
    }

    //------------------------------------------------------------------------------------------------------------------

    //#####################################- create Courses Table -#####################################################

    public void createCoursesTable() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating Courses Table !");
        PreparedStatement statement = connection.prepareStatement(this.CoursesTableQuery);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //#####################################- create Modules Table -#####################################################

    public void createModulesTable() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating Modules table !");
        PreparedStatement statement = connection.prepareStatement(this.ModulesTableQuery);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //#####################################- create Instructor Table -##################################################

    public void createInstructorsTable() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating instructor table !");
        PreparedStatement statement = connection.prepareStatement(this.InstructorTableQuery);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //#########################- create Table showing teacher teaching which modules -##################################

    public void createInstrTeachingModules() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating instructor Teaching which modules table !");
        PreparedStatement statement = connection.prepareStatement(this.InstructorTeachingModules);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //#########################- create Table showing teacher teaching which modules -##################################

    public void createStudentTable() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating Students table !");
        PreparedStatement statement = connection.prepareStatement(this.StudentTableQuery);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //#########################- create Table showing teacher teaching which modules -##################################

    public void createStudentModuleTable() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating Student Modules Choice table !");
        PreparedStatement statement = connection.prepareStatement(this.StudentModuleTableQuery);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }

    //------------------------------------------------------------------------------------------------------------------

    //#########################- create Table showing teacher teaching which modules -##################################

    public void createStudentMarksheetTable() throws SQLException {
        connection = DBUtils.getDbConnection();

        System.out.println("Creating Student Mark sheet table !");
        PreparedStatement statement = connection.prepareStatement(this.StudentMarksheetQuery);
        statement.executeUpdate();
        System.out.println("\nFinished !\n\n");
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>- Helper methods >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //------------------------------------------------------------------------------------------------------------------

    //#####################################- check if the student is pass or fail on that modules -#####################

    public boolean checkPassOrFail(String module, int mark) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select PassMark from modules where ModuleID = \"" + module + "\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int passMark = resultSet.getInt("PassMark");

        boolean result = false;

        if (mark >= passMark) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    //###############################- Returns all the module that student Have chosen to study -#######################

    public ArrayList<Module> getAllModulesStudentStuding(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();


        String Query = "select ModuleID from student_modules where StudentID = \"" + ID + "\"";

        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Module> mod = new ArrayList<Module>();

        while (resultSet.next()) {
            Module m = new Module();
            m.ModuleID = resultSet.getString("ModuleID");
            mod.add(m);
        }

        return mod;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#####################- it check the level on which student is studing have optional module or not -###############

    public boolean courseLevelHaveOptional(String Course, String Level, int sem) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from modules where CourseID = \"" + Course + "\" and Level = " + Level + " and IsOptional = true and sem = " + sem;
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    //------------------------------------------------------------------------------------------------------------------

    //##################################- it returns compulsory modules for student -###################################

    public ArrayList<Module> getNonOptionalCourses(String course, int Level, int sem) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from modules where CourseID = \"" + course + "\" and Level = " + Level + " and IsOptional = false and Sem = " + sem;
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Module> mod = new ArrayList<Module>();

        while (resultSet.next()) {
            Module m = new Module();
            m.ModuleID = resultSet.getString("ModuleID");
            m.Sem = resultSet.getInt("Sem");
            mod.add(m);
        }

        return mod;
    }

    //------------------------------------------------------------------------------------------------------------------

    //##################################- it returns optional modules for student -#####################################

    public ArrayList<Module> getOptionalCourses(String course, int Level, int sem) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from modules where CourseID = \"" + course + "\" and Level = " + Level + " and IsOptional = true and Sem = " + sem;
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Module> mod = new ArrayList<Module>();

        while (resultSet.next()) {
            Module m = new Module();
            m.ModuleID = resultSet.getString("ModuleID");
            m.Sem = resultSet.getInt("Sem");
            mod.add(m);
        }

        return mod;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#########################################- optional module choosing bot -#########################################

    public ArrayList<Module> chooseAnyTwoModule(ArrayList<Module> mod) {
        Random rand = new Random();
        int index = rand.nextInt(mod.size());
        mod.remove(mod.get(index));
        return mod;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#########################################- returns a grade for mark -#############################################

    public String findGrade(int mark) {
        String Grade = "E";

        if (mark > 90 && mark < 101) {
            Grade = "A+";
        } else if (mark > 80 & mark < 91) {
            Grade = "A";
        } else if (mark > 70 & mark < 81) {
            Grade = "B+";
        } else if (mark > 60 & mark < 71) {
            Grade = "B";
        } else if (mark > 50 & mark < 61) {
            Grade = "C+";
        } else if (mark > 40 & mark < 51) {
            Grade = "C";
        } else if (mark > 30 & mark < 41) {
            Grade = "D+";
        } else if (mark > 20 & mark < 31) {
            Grade = "D";
        } else {
            Grade = "E";
        }
        return Grade;
    }

    //------------------------------------------------------------------------------------------------------------------

    //########################- it returns Level of the semester -######################################################

    public int getLevel(int sem) {
        int Level = 0;
        if (sem == 1 || sem == 2) {
            Level = 4;
        } else if (sem == 3 || sem == 4) {
            Level = 5;
        } else if (sem == 5 || sem == 6) {
            Level = 6;
        }

        return Level;
    }

    //------------------------------------------------------------------------------------------------------------------

    //########################- it returns Semester of the Level -######################################################

    public int getSem(int n) {
        int sem = 0;

        if (n == 1) {
            sem = 1;
        } else if (n == 2) {
            sem = 2;
        } else if (n == 3) {
            sem = 1;
        } else if (n == 4) {
            sem = 2;
        } else if (n == 5) {
            sem = 1;
        } else if (n == 6) {
            sem = 2;
        }

        return sem;
    }

    //------------------------------------------------------------------------------------------------------------------

    //########################- it returns Semester of the Level -######################################################

    public int getPercent(float total, float done) {
        return Math.round((done / total) * 100);
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################- it returns all Courses Available courses -###############################################

    public ResultSet getAvailableCourses() throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from courses where IsCancelled = false";
        PreparedStatement statement = connection.prepareStatement(Query);

        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################- it returns all Modules on that courses -###############################################

    public boolean LevelHaveOptionalModules(String Course, int Level) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from modules where CourseID =\""+Course+"\" and Level = "+Level+" and IsOptional = true ";
        PreparedStatement statement = connection.prepareStatement(Query);


        return statement.executeQuery().next();
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################- it returns all Modules on that courses -###############################################

    public boolean SemHaveOptionalModules(String Course, int Level, int sem) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from modules where CourseID =\""+Course+"\" and Level = "+Level+" and Sem = "+sem+" and IsOptional = true ";
        PreparedStatement statement = connection.prepareStatement(Query);

        return statement.executeQuery().next();
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################- it returns all Modules on that courses -###############################################

    public ResultSet getModulesOnCourse(String Course, int Level, int Sem, boolean IsOptional) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from modules where CourseID =\""+Course+"\" and Level = "+Level+" and sem = "+Sem+" and IsOptional = "+IsOptional;
        PreparedStatement statement = connection.prepareStatement(Query);


        return statement.executeQuery();
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################- it returns all Modules on that courses -###############################################

    public String getID(String preString, String table, String col) throws SQLException {
        connection = DBUtils.getDbConnection();

        String ID = "";

        String Query = "select "+col+" from "+table;
        PreparedStatement statement = connection.prepareStatement(Query);

        ResultSet resultSet = statement.executeQuery();
        int CurrentLargest = 0;
        if(previousLergest == -1) {
            while(resultSet.next()) {
                int num = Integer.parseInt(resultSet.getString(col).split(preString)[1]);
                if (num > CurrentLargest) {
                    CurrentLargest = num;
                }
            }
        } else {
            CurrentLargest = previousLergest;
        }

        previousLergest = CurrentLargest + 1;
        
        if(Integer.toString(CurrentLargest).length() < 4) {
            for(int i=0; i<4 - Integer.toString(CurrentLargest).length(); i++) {
                ID += "0";
            }
            ID = preString + ID + (CurrentLargest+1);
        } else {
            ID = preString + (CurrentLargest+1);
        }


        return ID;
    }

    //------------------------------------------------------------------------------------------------------------------

    //#######################- Check if the student already exist or not -##############################################
    public boolean StudentAlreadyExist(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from students where StudentID =\""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ;
    }

    public boolean findIfMarksheetIsCreatedOrNot(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from studentMarksheet where StudentID =\""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ;
    }

    public int getStudentCurrentLevel(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        int Level = 0;
        String Query = "select * from students where StudentID =\""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            Level = resultSet.getInt("Level");
        }
        return Level;
    }



    public boolean StudentMarksheetIsCreated(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from studentMarksheet where StudentID =\""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public boolean StudentHavePassedThePreviousSem(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        boolean result = true;
        String Query = "select * from studentMarksheet where StudentID =\""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            result = resultSet.getBoolean("HaveRecit");
            if(result) {
                break;
            }
        }

        return !result;
    }

    public ArrayList<Instructor> getStudentInstructors(ArrayList<Module> modules) throws SQLException {
        connection = DBUtils.getDbConnection();

        ArrayList<Instructor> Instructors = new ArrayList<Instructor>();
        for(Module m: modules) {
            String Query = "Select * from instructor_modules where ModuleID = \""+m.ModuleID+"\"";
            PreparedStatement statement = connection.prepareStatement(Query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                String Query1 = "Select * from instructors where InstructorID = \""+resultSet.getString("InstructorID")+"\"";
                PreparedStatement statement1 = connection.prepareStatement(Query1);
                ResultSet resultSet1 = statement1.executeQuery();
                while(resultSet1.next()) {
                    Instructor ins = new Instructor();
                    ins.InstructorID = resultSet1.getString("InstructorID");
                    ins.FirstName = resultSet1.getString("FirstName");
                    ins.LastName = resultSet1.getString("LastName");
                    Instructors.add(ins);
                }
            }
        }

        return Instructors;
    }

    public boolean TeacherAlreadyExist(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "Select * from Instructors where InstructorID = \""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public ArrayList<Module> getTeacherModules(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        ArrayList<Module> Module = new ArrayList<Module>();
        String Query = "Select * from instructor_modules where InstructorID = \""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            String Query1 = "Select * from modules where ModuleID = \""+resultSet.getString("ModuleID")+"\"";
            PreparedStatement statement1 = connection.prepareStatement(Query1);
            ResultSet resultSet1 = statement1.executeQuery();
            while(resultSet1.next()) {
                Module mod = new Module();
                mod.ModuleID = resultSet1.getString("ModuleID");
                mod.ModuleName = resultSet1.getString("ModuleName");
                mod.Level = resultSet1.getInt("Level");
                Module.add(mod);
            }
        }

        return Module;
    }

    public ArrayList<Student> getStudentStudingModule(ArrayList<Module> mod) throws SQLException {
        connection = DBUtils.getDbConnection();

        ArrayList<Student> Students = new ArrayList<Student>();
        for(Module m: mod) {
            String Query = "Select * from student_modules where ModuleID = \""+m.ModuleID+"\"";
            PreparedStatement statement = connection.prepareStatement(Query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                String Query1 = "Select * from students where StudentID = \""+resultSet.getString("StudentID")+"\"";
                PreparedStatement statement1 = connection.prepareStatement(Query1);
                ResultSet resultSet1 = statement1.executeQuery();
                while(resultSet1.next()) {
                    Student STD = new Student();
                    STD.StudentID = resultSet1.getString("StudentID");
                    STD.FirstName = resultSet1.getString("FirstName");
                    STD.LastName = resultSet1.getString("LastName");
                    STD.Level = resultSet1.getInt("Level");
                    Students.add(STD);
                }
            }
        }

        return Students;
    }

    public boolean ModuleExists(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "Select * from modules where ModuleID = \""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public boolean ModuleInstructorIS(String Module, String Instructor) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "Select * from instructor_modules where ModuleID = \""+Module+"\" and InstructorID = \""+Instructor+"\"";
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public ResultSet getAllModulesOnThatLevel(String Course, int Level) throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "Select * from modules where CourseID = \""+Course+"\" and Level = "+Level;
        PreparedStatement statement = connection.prepareStatement(Query);
        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    public ResultSet getCanceledCourses() throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from courses where IsCancelled = true";
        PreparedStatement statement = connection.prepareStatement(Query);

        ResultSet resultSet = statement.executeQuery();


        return resultSet;
    }

    public ResultSet getAllTeachers() throws SQLException {
        connection = DBUtils.getDbConnection();

        String Query = "select * from instructors";
        PreparedStatement statement = connection.prepareStatement(Query);

        ResultSet resultSet = statement.executeQuery();


        return resultSet;
    }

    public ArrayList<Student> getStudentMarksheet() throws SQLException {
        connection = DBUtils.getDbConnection();
        ArrayList<Student> S = new ArrayList<>();


        String Students = "select * from students";
        PreparedStatement statement = connection.prepareStatement(Students);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {
            Student s = new Student();
            s.StudentID = resultSet.getString("StudentID");
            s.FirstName = resultSet.getString("FirstName");
            s.LastName = resultSet.getString("LastName");
            s.Course.CourseID = resultSet.getString("CourseID");
            s.Level = resultSet.getInt("Level");

            String Query = "select * from studentMarksheet where StudentID = \""+s.StudentID+"\"";
            PreparedStatement statement1 = connection.prepareStatement(Query);
            ResultSet resultSet1 = statement1.executeQuery();

                while(resultSet1.next()) {
                Module m = new Module();
                m.ModuleID = resultSet1.getString("ModuleID");
                m.GainedMark = resultSet1.getInt("Mark");
                s.IsPass = resultSet1.getBoolean("IsPass");
                m.HaveResit = resultSet1.getBoolean("HaveRecit");

                String Query1 = "select * from modules where ModuleID = \""+m.ModuleID+"\"";
                PreparedStatement statement2 = connection.prepareStatement(Query1);
                ResultSet resultSet2 = statement2.executeQuery();
                resultSet2.next();
                m.ModuleName = resultSet2.getString("ModuleName");
                m.PassMark = resultSet2.getInt("PassMark");
                m.FullMark = resultSet2.getInt("FullMark");
                s.modules.add(m);
            }
            String Query2 = "select * from courses where CourseID = \""+s.Course.CourseID+"\"";
            PreparedStatement statement3 = connection.prepareStatement(Query2);
            ResultSet resultSet2 = statement3.executeQuery();
            resultSet2.next();
            s.Course.CourseName = resultSet2.getString("CourseName");
            S.add(s);
        }
        statement.close();
        return S;
    }

    public ArrayList<Student> getIndividualMarkSheet(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();
        ArrayList<Student> S = new ArrayList<>();


        String Students = "select * from students where StudentID=\""+ID+"\"";
        PreparedStatement statement = connection.prepareStatement(Students);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {
            Student s = new Student();
            s.StudentID = resultSet.getString("StudentID");
            s.FirstName = resultSet.getString("FirstName");
            s.LastName = resultSet.getString("LastName");
            s.Course.CourseID = resultSet.getString("CourseID");
            s.Level = resultSet.getInt("Level");

            String Query = "select * from studentMarksheet where StudentID = \""+s.StudentID+"\"";
            PreparedStatement statement1 = connection.prepareStatement(Query);
            ResultSet resultSet1 = statement1.executeQuery();

            while(resultSet1.next()) {
                Module m = new Module();
                m.ModuleID = resultSet1.getString("ModuleID");
                m.GainedMark = resultSet1.getInt("Mark");
                s.IsPass = resultSet1.getBoolean("IsPass");
                m.HaveResit = resultSet1.getBoolean("HaveRecit");

                String Query1 = "select * from modules where ModuleID = \""+m.ModuleID+"\"";
                PreparedStatement statement2 = connection.prepareStatement(Query1);
                ResultSet resultSet2 = statement2.executeQuery();
                resultSet2.next();
                m.ModuleName = resultSet2.getString("ModuleName");
                m.PassMark = resultSet2.getInt("PassMark");
                m.FullMark = resultSet2.getInt("FullMark");
                s.modules.add(m);
            }
            String Query2 = "select * from courses where CourseID = \""+s.Course.CourseID+"\"";
            PreparedStatement statement3 = connection.prepareStatement(Query2);
            ResultSet resultSet2 = statement3.executeQuery();
            resultSet2.next();
            s.Course.CourseName = resultSet2.getString("CourseName");
            S.add(s);
        }
        statement.close();
        return  S;
    }


    public Student getStudentMarksheetdetails(String ID) throws SQLException {
        connection = DBUtils.getDbConnection();
        Student S = new Student();
        if(StudentMarksheetIsCreated(ID)) {
            S = getIndividualMarkSheet(ID).get(0);
        } else {
            ArrayList<Student> S1 = new ArrayList<>();

            String Students = "select * from students where StudentID=\""+ID+"\"";
            PreparedStatement statement = connection.prepareStatement(Students);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Student s = new Student();
                s.StudentID = ID;
                s.FirstName = resultSet.getString("FirstName");
                s.LastName = resultSet.getString("LastName");
                s.Course.CourseID = resultSet.getString("CourseID");
                s.Level = resultSet.getInt("Level");

                String Query = "select * from student_modules where StudentID = \""+ID+"\"";
                PreparedStatement statement1 = connection.prepareStatement(Query);
                ResultSet resultSet1 = statement1.executeQuery();

                while(resultSet1.next()) {
                    Module m = new Module();
                    m.ModuleID = resultSet1.getString("ModuleID");

                    String Query1 = "select * from modules where ModuleID = \""+m.ModuleID+"\"";
                    PreparedStatement statement2 = connection.prepareStatement(Query1);
                    ResultSet resultSet2 = statement2.executeQuery();
                    resultSet2.next();
                    m.ModuleName = resultSet2.getString("ModuleName");
                    m.PassMark = resultSet2.getInt("PassMark");
                    m.FullMark = resultSet2.getInt("FullMark");
                    m.HaveResit = false;
                    s.modules.add(m);
                    System.out.println("mod");
                }
                String Query2 = "select * from courses where CourseID = \""+s.Course.CourseID+"\"";
                PreparedStatement statement3 = connection.prepareStatement(Query2);
                ResultSet resultSet2 = statement3.executeQuery();
                resultSet2.next();
                s.Course.CourseName = resultSet2.getString("CourseName");
                S1.add(s);
            }
            statement.close();
            S = S1.get(0);
        }
        return S;
    }
}
