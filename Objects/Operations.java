package Objects;

import Helpers.DBUtils;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Operations {
    DataBaseStructure DBS;
    Connection connect;
    GetAttributesFromFile UploadFromFile;
    //------------------------------------------------------------------------------------------------------------------

    //##############################################- check if the database is already created -########################
    public Operations() throws SQLException {
        DBS = new DataBaseStructure();
    }

    public void createIfNotCreated() {
        try {
            boolean Found = DBS.findDatabaseIsCreatedOrNot();
            if(DBS.getMaxConnectionValue() < 5000) {
                DBS.setMaxConnections(10000);
            }
            if(!Found) {
                DBS.createDatabase();                                  //## if data base is not created then create
                DBS.createCoursesTable();                              //## and upload all data from file to database
                DBS.createModulesTable();
                DBS.createInstructorsTable();
                DBS.createInstrTeachingModules();
                DBS.createStudentTable();
                DBS.createStudentModuleTable();
                DBS.createStudentMarksheetTable();

                UploadFromFile = new GetAttributesFromFile();

                UploadFromFile.LodeCourseAttrubutesFromFile();
                UploadFromFile.LodeInstructorData();
                UploadFromFile.LodeStudentData();
                UploadFromFile.initializeStudentMarks();
            } else {
                System.out.println("DataBase Found !");
            }
        } catch(Exception err) {
            System.out.println(err);
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //######################################- add instructor to modules -###############################################

    public void addInstructor(Instructor instructor) throws SQLException {
        connect = DBUtils.getDbConnection();
        MainInterface m = new MainInterface();

        if(DBS.notExists("InstructorID",instructor.InstructorID, "instructors")) {
            String InsertInstructor = "INSERT INTO  instructors (InstructorID,FirstName,LastName)" +  //## add new course
                    "VALUES (?,?,?)";

            PreparedStatement statement = connect.prepareStatement(InsertInstructor);
            statement.setString(1, instructor.InstructorID);
            statement.setString(2, instructor.FirstName);
            statement.setString(3, instructor.LastName);

            statement.executeUpdate();
            statement.close();
        }

        if(!DBS.notTeachingSameModule("InstructorID", "ModuleID", instructor.InstructorID, instructor.Modules.get(0).ModuleID, "instructor_modules")) {
            String GiveInstructorAmodule = "INSERT INTO instructor_modules " +
                    "(InstructorID,ModuleID)" + //## add all the modules
                    "VALUES (?,?)";                                                //## to the course

            PreparedStatement statement2 = connect.prepareStatement(GiveInstructorAmodule);
            statement2.setString(1, instructor.InstructorID);
            statement2.setString(2, instructor.Modules.get(0).ModuleID);
            statement2.executeUpdate();
            statement2.close();
        } else {
            JOptionPane.showMessageDialog(m.jPanel44, " this instructor is already teaching this module !");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //###############################################- Add New Course -#################################################

    public void addNewCourse(Course Course) throws SQLException {
        connect = DBUtils.getDbConnection();

        String insertCourses = "INSERT INTO courses (CourseID,CourseName,IsCancelled)" +  //## add new course
                "VALUES (?,?,?)";

        PreparedStatement statement = connect.prepareStatement(insertCourses);
        statement.setString(1, Course.CourseID);
        statement.setString(2, Course.CourseName);
        statement.setBoolean(3, Course.isCancelled);

        statement.executeUpdate();
        statement.close();

        String insertModules = "INSERT INTO modules " +
                "(ModuleID,CourseID,ModuleName,PassMark, FullMark, IsOptional, Level, Sem)" + //## add all the modules
                "VALUES (?,?,?,?,?,?,?,?)";                                                //## to the course
        PreparedStatement statement2 = connect.prepareStatement(insertModules);

        for(Module module: Course.Modules) {
            statement2.setString(1, module.ModuleID);
            statement2.setString(2, Course.CourseID);
            statement2.setString(3, module.ModuleName);
            statement2.setInt(4, module.PassMark);
            statement2.setInt(5, module.FullMark);
            statement2.setBoolean(6, module.isOptional);
            statement2.setInt(7, module.Level);
            statement2.setInt(8, module.Sem);
            statement2.executeUpdate();
        }
        statement2.close();
    }

    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------

    //###############################################- Add New Course -#################################################

    public void newStudentEnrollment(Student student) throws SQLException {
        connect = DBUtils.getDbConnection();

        if(!DBS.StudentAlreadyExist(student.StudentID)) {
            String insertStudentData = "INSERT INTO students (StudentID,CourseID,FirstName,LastName, Level)" +  //## add new course
                    "VALUES (?,?,?,?,?)";
            PreparedStatement statement = connect.prepareStatement(insertStudentData);
            statement.setString(1, student.StudentID);
            statement.setString(2, student.Course.CourseID);
            statement.setString(3, student.FirstName);
            statement.setString(4, student.LastName);
            statement.setInt(5, student.Level);
            statement.executeUpdate();

            String insertStudentModules = "INSERT INTO student_modules (StudentID, ModuleID, Sem)" +  //## add new course
                    "VALUES (?,?,?)";
            PreparedStatement statement2 = connect.prepareStatement(insertStudentModules);

            for (Module m : student.modules) {
                statement2.setString(1, student.StudentID);
                statement2.setString(2, m.ModuleID);
                statement2.setInt(3, m.Sem);
                statement.close();

                statement2.executeUpdate();
            }
            statement2.close();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //###############################################- Add New Course -#################################################

    public void oldStudentEnrollment(Student student) throws SQLException {
        connect = DBUtils.getDbConnection();
        MainInterface I = new MainInterface();
        if(DBS.StudentAlreadyExist(student.StudentID)) {
            int StudentCurrentLeve = DBS.getStudentCurrentLevel(student.StudentID);

            if(DBS.StudentMarksheetIsCreated(student.StudentID)) {
                if (DBS.StudentHavePassedThePreviousSem(student.StudentID)) {
                    if(StudentCurrentLeve == (student.Level - 1)) {
                        String deleteMarksheet = "delete from student_modules where StudentID = \""+student.StudentID+"\"";
                        PreparedStatement statement2 = connect.prepareStatement(deleteMarksheet);
                        statement2.executeUpdate();

                        String UpdateLevel = "update students set Level = "+student.Level+" where StudentID = \""+student.StudentID+"\"";  //## add new course
                        PreparedStatement statement = connect.prepareStatement(UpdateLevel);
                        statement.executeUpdate();

                        String insertStudentData = "INSERT INTO student_modules (StudentID,ModuleID,Sem)" +  //## add new course
                                "VALUES (?,?,?)";
                        statement = connect.prepareStatement(insertStudentData);

                        for (Module m: student.modules) {
                            statement.setString(1, student.StudentID);
                            statement.setString(2, m.ModuleID);
                            statement.setInt(3, m.Sem);
                            statement.executeUpdate();
                        }
                        statement.close();
                    } else if(StudentCurrentLeve == student.Level) {
                        JOptionPane.showMessageDialog(I.Student_OldStudent, "You are already in Level "+StudentCurrentLeve+" !");
                    } else {
                        JOptionPane.showMessageDialog(I.Student_OldStudent, "Sorry You Have to be in previous Level to Enroll in this Level !");
                    }
                } else{
                    JOptionPane.showMessageDialog(I.Student_OldStudent, "Sorry You Did not Passed last sem!");
                }
            } else {
                JOptionPane.showMessageDialog(I.Student_OldStudent, "Sorry Your marksheet is not created yet!");
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    //######################################- get all the Modules on that Course and level -############################

    public void createMarksheet(Student M) throws SQLException {
        connect = DBUtils.getDbConnection();

        String deleteMarksheet = "delete from studentMarksheet where StudentID = \""+M.StudentID+"\"";
        PreparedStatement statement2 = connect.prepareStatement(deleteMarksheet);
        statement2.executeUpdate();

        String createMarksheet = "INSERT INTO studentMarksheet (StudentID, ModuleID, Mark, IsPass, HaveRecit)" +  //## add new course
                "VALUES (?,?,?,?,?)";
        PreparedStatement statement = connect.prepareStatement(createMarksheet);

        for(Module m: M.modules) {
            statement.setString(1, M.StudentID);
            statement.setString(2, m.ModuleID);
            statement.setInt(3, m.GainedMark);
            statement.setBoolean(4, !m.HaveResit);
            statement.setBoolean(5, m.HaveResit);

            statement.executeUpdate();
        }
        statement.close();
    }

    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------

    //######################################- get all the Modules on that Course and level -############################

    public void setModuleMark(String Module, int Mark) throws SQLException {
        connect = DBUtils.getDbConnection();

        String updateFullMark = "update modules set  FullMark = " + Mark+" where ModuleID = \""+Module+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void addModuleToTheLevel(String course, Module m) throws SQLException {
        connect = DBUtils.getDbConnection();

        String insertCourses = "INSERT INTO modules (ModuleID, CourseID, ModuleName, PassMark, FullMark, IsOptional, Level, Sem)" +  //## add new course
                "VALUES (?,?,?,?,?, ?,?,?)";
        PreparedStatement statement = connect.prepareStatement(insertCourses);

        statement.setString(1, m.ModuleID);
        statement.setString(2, course);
        statement.setString(3, m.ModuleName);
        statement.setInt(4, m.PassMark);
        statement.setInt(5, m.FullMark);
        statement.setBoolean(6, m.isOptional);
        statement.setInt(7, m.Level);
        statement.setInt(8, m.Sem);

        statement.executeUpdate();
    }

    public void calcelCourse(String Course) throws SQLException {
        connect = DBUtils.getDbConnection();

        String updateFullMark = "update courses set  IsCancelled = true where CourseID = \""+Course+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }

    public void addFromCancelled(String Course) throws SQLException {
        connect = DBUtils.getDbConnection();

        String updateFullMark = "update courses set  IsCancelled = false where CourseID = \""+Course+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }

    public void deleteCourse(String Course) throws SQLException {
        connect = DBUtils.getDbConnection();
        MainInterface m = new MainInterface();

        String updateFullMark = "Delete from courses where CourseID = \""+Course+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }

    public void renameModule(String Course, String Module, String NewName) throws SQLException {
        connect = DBUtils.getDbConnection();
        String updateFullMark = "update modules set  ModuleName = \""+NewName+"\" where ModuleID = \""+Module+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }

    public void renameCourse(String Course, String NewName) throws SQLException {
        connect = DBUtils.getDbConnection();
        String updateFullMark = "update courses set  CourseName = \""+NewName+"\" where CourseID = \""+Course+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }

    public void removeInstructor(String Instructor) throws SQLException {
        connect = DBUtils.getDbConnection();
        String updateFullMark = "delete from instructors where InstructorID = \""+Instructor+"\"";
        PreparedStatement statement = connect.prepareStatement(updateFullMark);
        statement.executeUpdate();
        statement.close();
    }
}