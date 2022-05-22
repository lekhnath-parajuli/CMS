package Objects;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author user
 */

public class MainInterface extends javax.swing.JFrame {
    private StudentObj STD = new StudentObj();
    private CourseadministratorObj CA = new CourseadministratorObj();
    private InstructorObj INS = new InstructorObj();

    private DataBaseStructure DBS;
    private String SelectedCourseID = "-1";
    private String SelectedModuleID = "-1";
    private Course newCourse = new Course();
    private String newCourseGeneratedID = "";
    private String newCourseModuleGeneratedID = "";
    private String SelectedIDFromTable = "-1";
    private int NewCourseLevel = -1;
    private int newCourseSem = 1;
    private boolean newCourseIsOptional = false;
    private int SelectedLevel = 0;
    private int SelectedSem = 1;
    private Instructor newInstructor = new Instructor();
    private String SelectedModule = "-1";
    private Student newStudent = new Student();

    private ArrayList<Module> YourSem1Modules = new ArrayList<Module>();
    private ArrayList<Module> YourSem2Modules = new ArrayList<Module>();

    private ArrayList<Module> YourSem1OptionalModules = new ArrayList<Module>();
    private ArrayList<Module> YourSem2OptionalModules = new ArrayList<Module>();

    private DefaultTableModel Model = new DefaultTableModel();
    private DefaultTableModel Mode1 = new DefaultTableModel();
    private DefaultTableModel Mode2 = new DefaultTableModel();
    private DefaultTableModel Mode3 = new DefaultTableModel();
    private DefaultTableModel Mode4 = new DefaultTableModel();
    private DefaultTableModel Mode5 = new DefaultTableModel();
    private DefaultTableModel Mode6 = new DefaultTableModel();

    private Module OptionalChoiceModules = new Module();

    ArrayList<KeyValue> keyValues1 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues2 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues3 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues4 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues5 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValueLevel = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValuesem = new ArrayList<KeyValue>();
    ArrayList<KeyValue> KeyValueIsOptional = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues6 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues7 = new ArrayList<KeyValue>();
    ArrayList<KeyValue> keyValues8 = new ArrayList<KeyValue>();

    private String DynamicText = "Enter Your ID...";
    private int currentNoOfModule = 0;
    private Student StudentMarksheetDetails = new Student();
    private boolean StudentHaveRecit = false;

    /**
     * Creates new form Objects.MainInterface
     */
    public MainInterface() throws SQLException {
        DBS = new DataBaseStructure();

        initComponents();
        StudentUI.setVisible(true);
        Students.setBackground(new Color(51,0,153));

        // student-Courses default settings
        StudentModulesPanel.setVisible(true);
        StudentInstructorsPanel.setVisible(false);
        StudentCoursesPanel.setBackground(new Color(51,0,153));
        StudentInstructorPanel.setBackground(new Color(0,0,102));

//         administrator courses default settings
        jPanel29.setVisible(false);
        jPanel25.setVisible(false);
        jPanel48.setVisible(true);
        jPanel49.setVisible(false);

        jPanel39.setVisible(true);
        jLabel9.setText("");
        jPanel38.setVisible(false);
        jPanel40.setVisible(false);
        jPanel41.setVisible(false);
        jPanel45.setVisible(false);
        jPanel13.setVisible(false);
        jPanel21.setVisible(false);
        jPanel23.setVisible(false);

        jPanel58.setBackground(new Color(51,0,153));
        jPanel56.setBackground(new Color(0,0,102));

        MyMethods();
        allItemListners();
        allMouseListener();
    }

    public void MyMethods() throws SQLException {
        SelectedCourseID = "-1";
        SelectedModuleID = "-1";
        newCourse = new Course();
        newCourseGeneratedID = "";
        newCourseModuleGeneratedID = "";
        SelectedIDFromTable = "-1";
        NewCourseLevel = -1;
        newCourseSem = 1;
        newCourseIsOptional = false;
        SelectedLevel = 0;
        SelectedSem = 1;

        jPanel29.setVisible(false);
        jPanel25.setVisible(false);
        jPanel48.setVisible(true);
        jPanel49.setVisible(false);

        jPanel39.setVisible(true);
        jLabel9.setText("");
        jPanel38.setVisible(false);
        jPanel40.setVisible(false);
        jPanel41.setVisible(false);
        jPanel45.setVisible(false);
        jPanel13.setVisible(false);
        jPanel21.setVisible(false);
        jPanel23.setVisible(false);

        setStudent_CoursesBox(DBS.getAvailableCourses());
        StudentSemCombobox();
        course_administrtorCourseBox(DBS.getAvailableCourses());
    }

    public void course_administrtorCourseBox(ResultSet resultSet) throws SQLException {
        keyValues1.clear();
        jComboBox1.removeAllItems();

        keyValues1.add(new KeyValue("-1", "Select A Course"));
        jComboBox1.addItem(keyValues1.get(0).getValue());
        jComboBox1.setSelectedIndex(0);

        while(resultSet.next()) {
            KeyValue obj = new KeyValue(resultSet.getString("CourseID"), resultSet.getString("CourseName"));
            keyValues1.add(obj);
            jComboBox1.addItem(obj.getValue());
        }
    }

    public void setStudent_CoursesBox(ResultSet resultSet) throws SQLException {
        keyValues4.clear();
        Student_CoursesBox.removeAllItems();
        keyValues4.add(new KeyValue("-1", "Select A Course"));
        Student_CoursesBox.addItem(keyValues4.get(0).getValue());
        Student_CoursesBox.setSelectedIndex(0);

        while(resultSet.next()) {
            KeyValue obj = new KeyValue(resultSet.getString("CourseID"), resultSet.getString("CourseName"));
            keyValues4.add(obj);
            Student_CoursesBox.addItem(obj.getValue());
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void allItemListners() {
        //#################################################- item listener for student Course box -#####################

        jComboBox1.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                keyValues2.clear();

                this.SelectedCourseID = keyValues1.get((Integer) jComboBox1.getSelectedIndex()).getKey();

                if(!this.SelectedCourseID.equals("-1")) {
                    jPanel48.setVisible(true);
                    jPanel49.setVisible(false);

                    jComboBox2.removeAllItems();
                    keyValues2.add(new KeyValue("-1", "Select A Level"));

                    jComboBox2.setModel(new DefaultComboBoxModel<>(new String[] {"Select A Level"}));

                    String[] Levels = new String[]{"4", "5", "6"};

                    for(String Level: Levels) {
                        KeyValue obj = new KeyValue(Level, Level);
                        keyValues2.add(obj);
                        jComboBox2.addItem(obj.getValue());
                    }
                } else {
                    jPanel39.setVisible(true);
                    jLabel9.setText("");
                    jComboBox2.removeAllItems();
                }
            }
        });

        //#################################################- item listener for student Levels box -#####################

        jComboBox2.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                keyValues3.clear();

                NewCourseLevel = Integer.parseInt(keyValues2.get((Integer) jComboBox2.getSelectedIndex()).getKey());
                jComboBox3.removeAllItems();
                if(!(this.SelectedLevel == -1)) {
                    jPanel48.setVisible(true);
                    jPanel49.setVisible(false);
                    try {
                        ResultSet resultSet5 = DBS.getAllModulesOnThatLevel(SelectedCourseID, NewCourseLevel);

                        keyValues3.add(new KeyValue("-1", "Select A Module"));
                        jComboBox3.addItem(keyValues3.get(0).getValue());
                        jComboBox3.setSelectedIndex(0);

                        while(resultSet5.next()) {
                            KeyValue obj = new KeyValue(resultSet5.getString("ModuleID"), resultSet5.getString("ModuleName"));
                            keyValues3.add(obj);
                            jComboBox3.addItem(obj.getValue());
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    jPanel48.setVisible(true);
                    jPanel49.setVisible(false);
                    jComboBox3.removeAllItems();
                }
            } else {
                jComboBox3.removeAllItems();
            }
        });

        //#################################################- student Module choice -####################################
        jComboBox3.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                this.SelectedModuleID = keyValues3.get((Integer) jComboBox3.getSelectedIndex()).getKey();
                if(!this.SelectedModuleID.equals("-1")) {
                    jPanel48.setVisible(false);
                    jPanel49.setVisible(true);
                } else {
                    jPanel48.setVisible(true);
                    jPanel49.setVisible(false);
                }
            }
        });

        //#################################################- select Level -#############################################
        Student_CoursesBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                keyValues5.clear();

                this.SelectedCourseID = keyValues4.get((Integer) Student_CoursesBox.getSelectedIndex()).getKey();

                if(!this.SelectedCourseID.equals("-1")) {
                    Student_ModulesBox.removeAllItems();
                    keyValues5.add(new KeyValue("-1", "Select A Level"));

                    Student_ModulesBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Select A Level"}));

                    String[] Levels = new String[]{"4", "5", "6"};

                    for(String Level: Levels) {
                        KeyValue obj = new KeyValue(Level, Level);
                        keyValues5.add(obj);
                        Student_ModulesBox.addItem(obj.getValue());
                    }
                } else {
                    Student_ModulesBox.removeAllItems();
                }
            }
        });

        //#################################################- new Student data -#############################################
        Student_ModulesBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                this.SelectedLevel = Integer.parseInt(keyValues5.get((Integer) Student_ModulesBox.getSelectedIndex()).getKey());
                try {
                    if(SelectedLevel != -1) {
                        if(SelectedLevel == 4) {
                            jPanel22.setVisible(false);
                            jPanel61.setVisible(true);
                            SelectedSem = 1;
                            this.newStudent.StudentID = DBS.getID("NP03A19", "students", "StudentID");
                            DBS.previousLergest = -1;
                            jLabel17.setText("Your ID:: "+this.newStudent.StudentID);
                            YourModules(this.SelectedCourseID, this.SelectedLevel, DBS.LevelHaveOptionalModules(this.SelectedCourseID, this.SelectedLevel));
                            Student_OptionalModules2.setSelectedIndex(0);
                            showModules(this.SelectedCourseID, this.SelectedLevel, 1);
                        } else {
                            StudentModulesPanel.setVisible(true);
                            StudentInstructorsPanel.setVisible(false);

                            StudentCoursesPanel.setBackground(new Color(51,0,153));
                            StudentInstructorPanel.setBackground(new Color(0,0,102));

                            SelectedSem = 1;
                            jPanel22.setVisible(true);
                            jPanel61.setVisible(false);
                            YourModules(this.SelectedCourseID, this.SelectedLevel, DBS.LevelHaveOptionalModules(this.SelectedCourseID, this.SelectedLevel));
                            Student_OptionalModules2.setSelectedIndex(0);
                            showModules(this.SelectedCourseID, this.SelectedLevel, 1);
                        }
                    } else {
                        Model.setRowCount(0);
                        String Header[] = {"modules"};
                        Model.setColumnIdentifiers(Header);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        //#################################################- available modules -#############################################
        CourseAdministrator_CourseLavelsBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                keyValuesem.clear();
                KeyValueIsOptional.clear();

                this.NewCourseLevel = Integer.parseInt(keyValueLevel.get(CourseAdministrator_CourseLavelsBox.getSelectedIndex()).getKey());
                if(NewCourseLevel == 6) {

                    keyValuesem.clear();
                    jComboBox4.removeAllItems();
                    String[] sems = new String[]{"1st sem", "2nd Sem"};

                    for (int i = 1; i < 3; i++) {
                        KeyValue obj = new KeyValue(Integer.toString(i), sems[i - 1]);
                        keyValuesem.add(obj);
                        jComboBox4.addItem(obj.getValue());
                    }

                    jComboBox5.removeAllItems();
                    String[] IsOptional = new String[]{"Compulsory Module", "Optional Module"};
                    KeyValueIsOptional.add(new KeyValue("false", IsOptional[0]));
                    jComboBox5.addItem(KeyValueIsOptional.get(0).getValue());
                    KeyValueIsOptional.add(new KeyValue("true", IsOptional[1]));
                    jComboBox5.addItem(KeyValueIsOptional.get(1).getValue());
                    System.out.println(this.NewCourseLevel);
                } else if(!(NewCourseLevel == -1)) {
                    keyValuesem.clear();
                    jComboBox4.removeAllItems();
                    String[] sems = new String[]{"1st sem", "2nd Sem"};

                    for (int i = 1; i < 3; i++) {
                        KeyValue obj = new KeyValue(Integer.toString(i), sems[i - 1]);
                        keyValuesem.add(obj);
                        jComboBox4.addItem(obj.getValue());
                    }
                } else {
                    newCourseIsOptional = false;
                    jComboBox5.removeAllItems();
                    jComboBox4.removeAllItems();
                }
            }
        });

        //#############################################################-#############################################
        jComboBox4.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                this.newCourseSem = Integer.parseInt(keyValuesem.get(jComboBox4.getSelectedIndex()).getKey());
            }
        });

        //#############################################################-#############################################
        jComboBox5.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                this.newCourseIsOptional = Boolean.parseBoolean(KeyValueIsOptional.get(jComboBox5.getSelectedIndex()).getKey());
            }
        });

        //#############################################################-#############################################
        Student_OptionalModules.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                if(Student_OptionalModules.getSelectedIndex() > 0) {
                    System.out.println(SelectedSem);
                    if(SelectedSem == 1) {
                        OptionalChoiceModules = YourSem1OptionalModules.get(Student_OptionalModules.getSelectedIndex() - 1);
                    } else if(SelectedSem == 2) {
                        OptionalChoiceModules = YourSem2OptionalModules.get(Student_OptionalModules.getSelectedIndex() - 1);
                    }
                }
            }
        });

        //############################################################# select from optional modules #############################################
        Student_OptionalModules2.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.SelectedSem = Integer.parseInt(keyValues6.get((Integer) Student_OptionalModules2.getSelectedIndex()).getKey());
                try {
                    showModules(this.SelectedCourseID, this.SelectedLevel, this.SelectedSem);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        //############################################################# select from optional modules #############################################
        Student_OptionalModules3.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.SelectedSem = Integer.parseInt(keyValues7.get((Integer) Student_OptionalModules3.getSelectedIndex()).getKey());
                try {
                    showModules(this.SelectedCourseID, this.SelectedLevel, this.SelectedSem);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        CourseAdministrator_HaveRecit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    StudentHaveRecit = CourseAdministrator_HaveRecit.getSelectedIndex() != 0;
                }
            }
        });
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void allMouseListener() {
        //##############################################- add new Instructor -##########################################

        jButton11.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(!SelectedIDFromTable.equals("-1")) {
                    try {
                        newInstructor.InstructorID = SelectedIDFromTable;
                        Module m= new Module();
                        m.ModuleID = SelectedModuleID;
                        newInstructor.Modules.add(m);
                        CA.addInstructor(newInstructor);

                        jTextField27.setText("Enter First Name...");
                        jTextField28.setText("Enter Last Name...");
                        refreshInstructors();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    if(jTextField27.getText().trim().length() < 1 || jTextField27.getText().trim().equals("Enter First Name...") || jTextField28.getText().trim().length() < 1 || jTextField28.getText().trim().equals("Enter Last Name...")) {
                        JOptionPane.showMessageDialog(jPanel44, " please fill all the fields !");
                    } else {
                        try {
                            newInstructor.InstructorID = DBS.getID("TP03A19", "instructors", "InstructorID");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        jLabel15.setText(newInstructor.InstructorID);
                        DBS.previousLergest = -1;

                        newInstructor.FirstName = jTextField27.getText().trim();
                        newInstructor.LastName = jTextField28.getText().trim();
                        Module m= new Module();
                        m.ModuleID = SelectedModuleID;
                        newInstructor.Modules.add(m);
                        try {
                            CA.addInstructor(newInstructor);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(jPanel44, " added !");
                        jTextField27.setText("Enter First Name...");
                        jTextField28.setText("Enter Last Name...");
                        refreshInstructors();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        //##############################################- add new Modules -#############################################

        CourseAdministrator_AddModule.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(NewCourseLevel != -1) {
                    DBS.previousLergest = -1;
                    try {
                        newCourseModuleGeneratedID = DBS.getID("MODID", "modules", "ModuleID");
                        DBS.previousLergest = -1;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    jLabel14.setText(newCourseModuleGeneratedID);

                    jPanel38.setVisible(false);
                    jPanel39.setVisible(false);
                    jPanel40.setVisible(false);
                    jPanel41.setVisible(false);
                    jPanel45.setVisible(true);
                    jPanel13.setVisible(false);
                    jPanel21.setVisible(false);
                    jPanel23.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(jPanel44, "please select a level on which you want to add module !");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });


        //##############################################- add new Modules -#############################################

        jButton18.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                if(jTextField20.getText().trim().length() < 1|| jTextField20.getText().trim().equals("Enter Module Name...") || jTextField21.getText().trim().length() < 1 || jTextField21.getText().trim().equals("sem, Optional")) {
                    JOptionPane.showMessageDialog(jPanel44, "please fill all the fields !");
                } else {
                    Module m = new Module();
                    try {
                        try{
                            m.ModuleID = newCourseModuleGeneratedID;
                            m.ModuleName = jTextField20.getText().trim();
                            m.Sem = Integer.parseInt(jTextField21.getText().trim().split(",")[0].trim());
                            m.Level = NewCourseLevel;
                            m.PassMark = 40;
                            m.FullMark = 100;
                            m.isOptional = Boolean.parseBoolean(jTextField21.getText().trim().split(",")[1].trim());
                        } catch (Exception exception) {
                            jLabel5.setText("please enter sem 1 or 2, true or false if module if optional then write true else false");
                        }

                        if(m.Sem > 0 && m.Sem < 3)  {
                            CA.addModuleToTheLevel(SelectedCourseID, m);
                            jTextField20.setText("Enter Module Name...");
                            jTextField21.setText("");

                            newCourseModuleGeneratedID = DBS.getID("MODID", "modules", "ModuleID");
                            jLabel14.setText(newCourseModuleGeneratedID);
                            DBS.previousLergest = -1;
                            MyMethods();
                        } else {
                            JOptionPane.showMessageDialog(jPanel44, "please see how to put data at top of this panel !");
                        }
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(jPanel44, "please enter number on sem !");
                    }

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });


        //##############################################- rename Course -###############################################
        CourseAdministrator_RenameModuleSave.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(jTextField19.getText().trim().equals("Rename Course...") || jTextField19.getText().trim().length() < 1) {
                    JOptionPane.showMessageDialog(jPanel44, "please fill all the fields !");
                } else {
                    try {
                        CA.renameCourse(SelectedCourseID, jTextField19.getText().trim());
                        JOptionPane.showMessageDialog(jPanel44, "renamed !");
                        jTextField19.setText("Rename Course...");
                        MyMethods();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        //##############################################- remove Instructor -###############################################
        jButton12.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(SelectedIDFromTable.equals("-1")) {
                    if((jTextField29.getText().trim().length() < 1) || jTextField29.getText().trim().equals("Enter Instructor ID...")) {
                        JOptionPane.showMessageDialog(jPanel44, "please fill the field !");
                    } else {
                        try {
                            if(DBS.TeacherAlreadyExist(jTextField29.getText().trim().toUpperCase(Locale.ROOT))) {
                                CA.removeInstructor(jTextField29.getText().trim().toUpperCase(Locale.ROOT));
                                JOptionPane.showMessageDialog(jPanel44, "removed !");
                                jTextField29.setText("Enter Instructor ID...");
                                refreshInstructors();
                            } else {
                                JOptionPane.showMessageDialog(jPanel44, "Instructor ID mismatched !");
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                } else {
                    try {
                        CA.removeInstructor(SelectedIDFromTable);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(jPanel44, "removed !");
                    jTextField29.setText("Enter Instructor ID...");
                    refreshInstructors();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        //##############################################- rename modules -###############################################
        jButton13.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(jTextField30.getText().trim().equals("Rename Module...") || jTextField30.getText().trim().length() < 1) {
                    JOptionPane.showMessageDialog(jPanel44, "please fill all the fields !");
                } else {
                    try {
                        CA.renameModule(SelectedCourseID, SelectedModuleID, jTextField30.getText().trim());
                        JOptionPane.showMessageDialog(jPanel44, "renamed !");
                        jTextField30.setText("Rename Module...");
                        MyMethods();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        //##############################################- old student enrollment -###############################################
        Student_OldStudentEnrollment.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(CheckOldStudentEnrolmentIsValid()) {
                    try {
                        STD.oldStudentEnrollment(newStudent);
                        jTextField26.setText("Enter Your Student ID...");
                        YourModules(SelectedCourseID, SelectedLevel, DBS.LevelHaveOptionalModules(SelectedCourseID, SelectedLevel));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        //##############################################- add modules -###############################################

        Student_AddModules.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if(!DBS.LevelHaveOptionalModules(SelectedCourseID, SelectedLevel)) {
                        JOptionPane.showMessageDialog(Student_OldStudent, "No Optional Modules On this Level you Can Enroll Directly !");
                    } else {
                        if(Student_OptionalModules.getSelectedIndex() != 0) {
                            if (OptionalChoiceModules.Sem == 1 && YourSem1Modules.size() !=4) {
                                YourSem1Modules.add(OptionalChoiceModules);
                                YourSem1OptionalModules.remove(OptionalChoiceModules);
                                showModules(SelectedCourseID, SelectedLevel, SelectedSem);
                            } else if (OptionalChoiceModules.Sem == 2 && YourSem2Modules.size() !=4 ) {
                                YourSem2Modules.add(OptionalChoiceModules);
                                YourSem2OptionalModules.remove(OptionalChoiceModules);
                                showModules(SelectedCourseID, SelectedLevel, SelectedSem);
                            } else {
                                JOptionPane.showMessageDialog(Student_OldStudent, "Already added 4 modules to this Sem !");
                            }
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        //##############################################- new all enrollment -###############################################
        Student_newStudentEnrollment1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(CheckNewStudentEnrolmentIsValid()) {
                    try {
                        STD.newStudentEnrollment(newStudent);
                        Student_NewStudentName1.setText("Enter First Name...");
                        Student_NewStudentSurname1.setText("Enter Last Name...");
                        newStudent.StudentID = DBS.getID("NP03A19", "students", "StudentID");
                        DBS.previousLergest = -1;
                        jLabel17.setText("Your ID:: "+newStudent.StudentID);
                        JOptionPane.showMessageDialog(Student_OldStudent, "enrolled !");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        //##############################################- show instructor details -###############################################
        jTextField32.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    try {
                        if(DBS.StudentAlreadyExist(jTextField32.getText().trim().toUpperCase(Locale.ROOT))) {
                            ArrayList<Module> Modules = DBS.getAllModulesStudentStuding(jTextField32.getText().trim().toUpperCase(Locale.ROOT));
                            ArrayList<Instructor> ins = DBS.getStudentInstructors(Modules);
                            Mode2.setRowCount(0);
                            String Header[] = {"InstructorID","FirstName","LastName"};
                            Mode2.setColumnIdentifiers(Header);
                            Student_InstructorTable.setModel(Mode2);
                            for(Instructor instructor: ins) {
                                Mode2.addRow(new Object[]{instructor.InstructorID, instructor.FirstName, instructor.LastName});
                            }
                        } else {
                            JOptionPane.showMessageDialog(StudentInstructorsPanel, "Student ID MissMach !");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        //##############################################- student modules choice -###############################################
        jTextField24.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    Mode3.setRowCount(0);
                    Mode4.setRowCount(0);
                    try {
                        if(DBS.TeacherAlreadyExist(jTextField24.getText().trim().toUpperCase(Locale.ROOT))) {
                            ArrayList<Module>  Mod = DBS.getTeacherModules(jTextField24.getText().trim().toUpperCase(Locale.ROOT));
                            ArrayList<Student> Std = DBS.getStudentStudingModule(Mod);

                            String[] Header = {"ModuleID","ModuleName","Level"};
                            Mode3.setColumnIdentifiers(Header);
                            jTable2.setModel(Mode3);
                            for(Module Module: Mod) {
                                Mode3.addRow(new Object[]{Module.ModuleID, Module.ModuleName, Module.Level});
                            }

                            String[] Header2 = {"StudentID","FirstName","LastName", "Level"};
                            Mode4.setColumnIdentifiers(Header2);
                            jTable8.setModel(Mode4);
                            for(Student Student: Std) {
                                Mode4.addRow(new Object[]{Student.StudentID, Student.FirstName, Student.LastName, Student.Level});
                            }
                        } else {
                            JOptionPane.showMessageDialog(jPanel36, "Teacher ID MissMach !");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //##############################################- calcel course -###############################################

        CourseAdministrator_CancelCourse.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!SelectedCourseID.equals("-1")) {
                    int input = JOptionPane.showConfirmDialog(jPanel44, "are you sure you want to cancel this course ?");
                    if(input == 0) {
                        try {
                            CA.calcelCourse(SelectedCourseID);
                            MyMethods();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        jPanel38.setVisible(false);
                        jPanel39.setVisible(false);
                        jPanel40.setVisible(true);
                        jPanel41.setVisible(false);
                        jPanel45.setVisible(false);
                        jPanel13.setVisible(false);
                        jPanel21.setVisible(false);
                        jPanel23.setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(jPanel44, "please select a course first !");
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //##############################################- add mark to module -###############################################

        jButton26.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if(!(jTextField24.getText().trim().equals("Teacher ID..."))) {
                        if(DBS.ModuleExists(jTextField31.getText().trim().toUpperCase(Locale.ROOT))) {
                            if(DBS.ModuleInstructorIS(jTextField31.getText().trim().toUpperCase(Locale.ROOT), jTextField24.getText().trim())){
                                INS.setModuleMark(jTextField31.getText().trim().toUpperCase(Locale.ROOT), Integer.parseInt(jTextField23.getText()));
                                jTextField23.setText("Enter Full Mark...");
                                jTextField24.setText("Teacher ID...");
                            } else {
                                JOptionPane.showMessageDialog(jPanel55, "Only Module Instructor Can add mark");
                            }
                        } else {
                            JOptionPane.showMessageDialog(jPanel55, "Module ID MissMach !");
                        }
                    } else {
                        JOptionPane.showMessageDialog(jPanel55, "Please Enter Your Teacher ID above");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //##############################################- add new course -###############################################

        CourseAdministrator_AddCourse.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
                keyValueLevel.clear();
                CourseAdministrator_CourseLavelsBox.removeAllItems();
                jPanel38.setVisible(true);
                jPanel39.setVisible(false);
                jPanel40.setVisible(false);
                jPanel41.setVisible(false);
                jPanel45.setVisible(false);
                jPanel13.setVisible(false);
                jPanel21.setVisible(false);
                jPanel23.setVisible(false);

                CourseAdministrator_CourseLavelsBox.removeAllItems();
                keyValueLevel.add(new KeyValue("-1", "Select A Level"));
                CourseAdministrator_CourseLavelsBox.addItem(keyValueLevel.get(0).getValue());

                String[] Levels = new String[]{"4", "5", "6"};
                try {
                    newCourseGeneratedID = DBS.getID("CSID0", "courses", "CourseID");
                    jLabel8.setText(newCourseGeneratedID);
                    DBS.previousLergest = -1;
                    newCourseModuleGeneratedID = DBS.getID("MODID", "modules", "ModuleID");
                    jLabel13.setText(newCourseModuleGeneratedID);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                for(String Level: Levels) {
                    KeyValue obj = new KeyValue(Level, Level);
                    keyValueLevel.add(obj);
                    CourseAdministrator_CourseLavelsBox.addItem(obj.getValue());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //##############################################- select instructor from table -###############################################
        CourseAdministrator_Table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                CourseAdministrator_Table.setSelectionBackground(Color.CYAN);
                CourseAdministrator_Table.setSelectionForeground(Color.blue);
                SelectedIDFromTable = Mode5.getValueAt(CourseAdministrator_Table.getSelectedRow(), 0).toString();

                if(jPanel21.isVisible()) {
                    jTextField29.setText(SelectedIDFromTable);
                } else if(jPanel13.isVisible()) {
                    jLabel15.setText(SelectedIDFromTable);
                    jTextField27.setText(Mode5.getValueAt(CourseAdministrator_Table.getSelectedRow(), 1).toString());
                    jTextField28.setText(Mode5.getValueAt(CourseAdministrator_Table.getSelectedRow(), 2).toString());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //##############################################- course add next modules -###############################################

        CourseAdministrator_AddNextModule.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!(NewCourseLevel == -1)) {
                    if((newCourseGeneratedID.equals("-1")) || (jTextField15.getText().trim().length() < 1) || jTextField15.getText().trim().equals("Course Name...") || (jTextField17.getText().trim().length() < 1 || jTextField17.getText().trim().equals("Enter Module Name..."))) {
                        JOptionPane.showMessageDialog(jPanel44, "please fill all the fields !");
                    } else {
                        newCourse.CourseID = newCourseGeneratedID;
                        newCourse.CourseName = jTextField15.getText().trim();
                        newCourse.isCancelled = false;

                        Module md = new Module();
                        md.ModuleID = newCourseModuleGeneratedID;
                        md.ModuleName = jTextField17.getText().trim();
                        md.Sem = newCourseSem;
                        md.Level = NewCourseLevel;
                        md.isOptional = newCourseIsOptional;
                        md.PassMark = 40;
                        md.FullMark = 100;

                        int Level4Sem1Modules = 0;
                        int Level4Sem2Modules = 0;
                        int Level5Sem1Modules = 0;
                        int Level5Sem2Modules = 0;
                        int Level6Sem1Modules = 0;
                        int Level6Sem2Modules = 0;
                        int Level6Sem1OptionalModules = 0;
                        int Level6Sem2OptionalModules = 0;

                        for(Module m: newCourse.Modules) {
                            if(m.Level == 4 && m.Sem == 1) {
                                Level4Sem1Modules += 1;
                            } else if (m.Level == 4 && m.Sem == 2) {
                                Level4Sem2Modules += 1;
                            } else if(m.Level == 5 && m.Sem == 1) {
                                Level5Sem1Modules += 1;
                            } else if(m.Level == 5 && m.Sem == 2) {
                                Level5Sem2Modules += 1;
                            } else if(m.Level == 6 && m.Sem == 1 && !m.isOptional) {
                                Level6Sem1Modules += 1;
                            } else if(m.Level == 6 && m.Sem == 2 && !m.isOptional) {
                                Level6Sem2Modules += 1;
                            } else if(m.Level == 6 && m.Sem == 1) {
                                Level6Sem1OptionalModules += 1;
                            } else if(m.Level == 6 && m.Sem == 2) {
                                Level6Sem2OptionalModules += 1;
                            }
                        }

                        if(md.Sem == 1 && Level4Sem1Modules == 4 && md.Level==4) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all required modules to this level 4 sem 1\nyou cannot add another on this sem !");
                        } else if(md.Sem == 2 && Level4Sem2Modules == 4 && md.Level==4) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all required modules to this level 4 sem 2\nyou cannot add another on this sem !");
                        } else if(md.Sem == 1 && Level5Sem1Modules == 4 && md.Level == 5) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all required modules to this level 5 sem 1 \nyou cannot add another on this sem !");
                        } else if(md.Sem == 2 && Level5Sem2Modules == 4 && md.Level == 5) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all required modules to this level 5 sem 1 \nyou cannot add another on this sem !");
                        } else if(md.Sem == 1 && Level6Sem1Modules == 2 && md.Level == 6 && !md.isOptional) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all  compulsory modules to this level 6 sem 1\nyou cannot add another compulsory module on this sem !");
                        }  else if(md.Sem == 2 && Level6Sem2Modules == 2 && md.Level == 6 && !md.isOptional) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all  compulsory modules to this level 6 sem 2\nyou cannot add another compulsory module on this sem !");
                        } else if(md.Sem == 1 && Level6Sem1OptionalModules == 3 && md.isOptional && md.Level == 6) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all required Optional modules to this level 6 sem 1 \nyou cannot add another Optional module on this sem !");
                        }  else if(md.Sem == 2 && Level6Sem2OptionalModules == 3 && md.isOptional && md.Level == 6) {
                            JOptionPane.showMessageDialog(jPanel44, "you already added all required Optional modules to this level 6 sem 2 \nyou cannot add another Optional module on this sem !");
                        } else {
                            newCourse.Modules.add(md);
                            String Header[] = {"CourseID","CourseName", "ModuleID", "ModuleName", "Level", "Sem"};
                            Mode5.setColumnIdentifiers(Header);
                            CourseAdministrator_Table.setModel(Mode5);
                            Mode5.setRowCount(0);

                            for(Module m: newCourse.Modules) {
                                Mode5.addRow(new Object[]{newCourse.CourseID, newCourse.CourseName, m.ModuleID, m.ModuleName, m.Level, m.Sem});
                            }

                            try {
                                newCourseModuleGeneratedID = DBS.getID("MODID", "modules", "ModuleID");
                                jLabel13.setText(newCourseModuleGeneratedID);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //##############################################- delete course -###############################################
        CourseAdministrator_DeleteCourse.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!SelectedCourseID.equals("-1")) {
                    int input = JOptionPane.showConfirmDialog(jPanel44, "are you sure you want to permanently delete this course ?");
                    if(input == 0) {
                        jPanel38.setVisible(false);
                        jPanel39.setVisible(true);
                        jPanel40.setVisible(false);
                        jPanel41.setVisible(false);
                        jPanel45.setVisible(false);
                        jPanel13.setVisible(false);
                        jPanel21.setVisible(false);
                        jPanel23.setVisible(false);
                        try {
                            CA.deleteCourse(SelectedCourseID);
                            jLabel9.setText("Sucessfully Deleted the Course");
                            MyMethods();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(jPanel44, "please select a course first !");
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //##############################################- add new Course -###############################################
        CourseAdministrator_SaveCourse.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int Level4Sem1Modules = 0;
                int Level4Sem2Modules = 0;
                int Level5Sem1Modules = 0;
                int Level5Sem2Modules = 0;
                int Level6Sem1Modules = 0;
                int Level6Sem2Modules = 0;
                int Level6Sem1OptionalModules = 0;
                int Level6Sem2OptionalModules = 0;

                for(Module m: newCourse.Modules) {
                    if(m.Level == 4 && m.Sem == 1) {
                        Level4Sem1Modules += 1;
                    } else if (m.Level == 4 && m.Sem == 2) {
                        Level4Sem2Modules += 1;
                    } else if(m.Level == 5 && m.Sem == 1) {
                        Level5Sem1Modules += 1;
                    } else if(m.Level == 5 && m.Sem == 2) {
                        Level5Sem2Modules += 1;
                    } else if(m.Level == 6 && m.Sem == 1 && !m.isOptional) {
                        Level6Sem1Modules += 1;
                    } else if(m.Level == 6 && m.Sem == 2 && !m.isOptional) {
                        Level6Sem2Modules += 1;
                    } else if(m.Level == 6 && m.Sem == 1) {
                        Level6Sem1OptionalModules += 1;
                    } else if(m.Level == 6 && m.Sem == 2) {
                        Level6Sem2OptionalModules += 1;
                    }
                }

                try {
                    if(SelectedIDFromTable.equals("-1") || !DBS.getCanceledCourses().next()) {
                        if(Level4Sem1Modules == 4 && Level4Sem2Modules == 4 &&
                                Level5Sem1Modules == 4 && Level5Sem2Modules == 4 &&
                                Level6Sem1Modules == 2 && Level6Sem2Modules == 2 &&
                                Level6Sem1OptionalModules == 3 && Level6Sem2OptionalModules == 3) {
                            try {
                                CA.addNewCourse(newCourse);
                                JOptionPane.showMessageDialog(jPanel44, "successfully added the course !");
                                refresh();
                                MyMethods();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(jPanel44, "Note:: \nyou have to add 4 modules on sem 1 and sem 2 of level 4 and 5\n" +
                                    "and on level 6 you have to add 2 2 compulsary modules on sem 1 and sem 2" +
                                    "\n and 3 3 optional on sem 1 and sem 2 then only you can add this course !");
                        }
                    } else if(DBS.getCanceledCourses().next() && CourseAdministrator_Table.getColumnCount() == 3){
                        try {
                            CA.addFromCancelled(SelectedIDFromTable);
                            MyMethods();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(jPanel44, "successfully added the course !");
                        refresh();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        CourseAdministrator_SearchButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                String ID = CourseAdministrator_SearchBar.getText().trim().toUpperCase(Locale.ROOT);
                if(ID.length() < 1) {
                    JOptionPane.showMessageDialog(jPanel8, "please enter student id !");
                } else {
                    try {
                        ArrayList<Student> S = DBS.getIndividualMarkSheet(ID);
                        if(DBS.StudentAlreadyExist(ID)) {
                            if(DBS.findIfMarksheetIsCreatedOrNot(ID)){
                                for(Student s: S) {
                                    if(s.StudentID.equals(ID)) {
                                        Mode6.setRowCount(0);
                                        boolean Recit = false;
                                        int TotalMark = 0;
                                        int TotalMarkgained = 0;
                                        int TotalModules = 0;

                                        String[] Header = {"", "", "", "", "", ""};

                                        Mode6.setColumnIdentifiers(Header);
                                        jTable9.setModel(Mode6);

                                        Mode6.addRow(new Object[]{"", "", "", "", "", ""});
                                        Mode6.addRow(new Object[]{"ID:: ", s.StudentID, "", "" ,"", ""});
                                        Mode6.addRow(new Object[]{"Name:: ", s.FirstName, s.LastName, "" ,"", ""});
                                        Mode6.addRow(new Object[]{"Course:: ", s.Course.CourseID ,s.Course.CourseName, "", "", ""});

                                        Mode6.addRow(new Object[]{"", "", "", "", "", ""});
                                        Mode6.addRow(new Object[]{"ModuleID", "ModuleName", "passMark", "FullMark", "Grade", "Resit"});
                                        Mode6.addRow(new Object[]{"", "", "", "", "", ""});

                                        for (Module m : s.modules) {
                                            if(!Recit) {
                                                if(!DBS.checkPassOrFail(m.ModuleID, m.GainedMark)) {
                                                    Recit = true;
                                                }
                                            }
                                            TotalMarkgained += m.GainedMark;
                                            TotalMark += m.FullMark;
                                            Mode6.addRow(new Object[]{m.ModuleID, m.ModuleName, m.PassMark, m.FullMark, DBS.findGrade(m.GainedMark), m.HaveResit});
                                        }
                                        float GPA = ((TotalMarkgained * 100f)/TotalMark)/25f;
                                        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                                        Mode6.addRow(new Object[]{"", "", "Total:: ", TotalMarkgained, "Grade:: ", DBS.findGrade(TotalMarkgained/TotalModules)});
                                        Mode6.addRow(new Object[]{"", "", "Resit:: ", Recit, "GPA:: ", GPA,""});
                                        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                                        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(jPanel8, "Your Mark sheet is not created yet !");
                            }
                        } else {
                            JOptionPane.showMessageDialog(jPanel8, "student ID miss match !");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        CourseAdministrator_AssignNextMark.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // current work
                String ID = jTextField25.getText().trim().toUpperCase(Locale.ROOT);
                if(currentNoOfModule == 0) {
                        if (!(jTextField25.getText().trim().length() < 1 || jTextField25.getText().trim().equals(DynamicText))) {
                            try {
                                if (DBS.StudentAlreadyExist(ID)) {
                                    StudentMarksheetDetails = DBS.getStudentMarksheetdetails(ID);
                                    UpdateCurrentStudentMarksheetTable();
                                    jTextField25.setText("");
                                    DynamicText = "Mark on " + StudentMarksheetDetails.modules.get(0).ModuleID + ".?";
                                    currentNoOfModule += 1;
                                } else {
                                    JOptionPane.showMessageDialog(jPanel8, "Student ID Mis Match !");
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(jPanel8, "please Enter your student ID !");
                        }
                    } else {
                        if (currentNoOfModule < StudentMarksheetDetails.modules.size() + 1) {
                            if (!(jTextField25.getText().trim().length() < 1 || jTextField25.getText().trim().equals(DynamicText))) {
                                try {
                                    int mark = Integer.parseInt(jTextField25.getText().trim());

                                    StudentMarksheetDetails.modules.get(currentNoOfModule - 1).GainedMark = mark;
                                    StudentMarksheetDetails.modules.get(currentNoOfModule - 1).HaveResit = StudentHaveRecit;

                                    jTextField25.setText("");
                                    if(currentNoOfModule < StudentMarksheetDetails.modules.size()) {
                                        DynamicText = "Mark on " + StudentMarksheetDetails.modules.get(currentNoOfModule).ModuleID + ".?";
                                    } else {
                                        DynamicText = "Click Generate !";
                                    }

                                    currentNoOfModule += 1;
                                    UpdateCurrentStudentMarksheetTable();
                                } catch (NumberFormatException err) {
                                    JOptionPane.showMessageDialog(jPanel8, "please enter number value !");
                                } catch (Exception err) {
                                    System.out.println();
                                }
                            } else {
                                JOptionPane.showMessageDialog(jPanel8, "please Enter your student ID !");
                            }
                        }
                    }
                }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        jTextField25.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    if(currentNoOfModule < StudentMarksheetDetails.modules.size()) {
                        int mark = Integer.parseInt(jTextField25.getText().trim());
                        autoSelectHaveRecitOrNot(mark);
                    }
                } catch (NumberFormatException | SQLException nfe) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    if(currentNoOfModule < StudentMarksheetDetails.modules.size()) {
                        int mark = Integer.parseInt(jTextField25.getText().trim());
                        autoSelectHaveRecitOrNot(mark);
                    }
                } catch (NumberFormatException | SQLException nfe) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    if(currentNoOfModule < StudentMarksheetDetails.modules.size()) {
                        int mark = Integer.parseInt(jTextField25.getText().trim());
                        autoSelectHaveRecitOrNot(mark);
                    }
                } catch (NumberFormatException | SQLException nfe) {
                }
            }
        });

        CourseAdministrator_GenerateMarksheet.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(currentNoOfModule == StudentMarksheetDetails.modules.size() + 1) {
                    try {
                        CA.createMarksheet(StudentMarksheetDetails);
                        JOptionPane.showMessageDialog(jPanel8, "Saved !");
                        currentNoOfModule = 0;

                        DynamicText = "Enter Your ID...";
                        jTextField25.setText(DynamicText);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(jPanel8, "please add mark to all modules first !");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    public void autoSelectHaveRecitOrNot(int mark) throws SQLException {
        if(DBS.checkPassOrFail(StudentMarksheetDetails.modules.get(currentNoOfModule - 1).ModuleID, mark)) {
            CourseAdministrator_HaveRecit.setSelectedIndex(0);
            StudentHaveRecit = CourseAdministrator_HaveRecit.getSelectedIndex() != 0;
        } else {
            CourseAdministrator_HaveRecit.setSelectedIndex(1);
            StudentHaveRecit = CourseAdministrator_HaveRecit.getSelectedIndex() != 0;
        }
    }


    public void UpdateCurrentStudentMarksheetTable() throws SQLException {
        Mode6.setRowCount(0);
        boolean Recit = false;
        int TotalMark = 0;
        int TotalMarkgained = 0;
        int TotalModules = 0;

        String[] Header = {"", "", "", "", "", "", ""};

        Mode6.setColumnIdentifiers(Header);
        jTable9.setModel(Mode6);
        jTable9.getColumnModel().getColumn(1).setPreferredWidth(200);

        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
        Mode6.addRow(new Object[]{"ID:: ", StudentMarksheetDetails.StudentID, "", "" ,"", "", ""});
        Mode6.addRow(new Object[]{"Name:: ", StudentMarksheetDetails.FirstName +" "+StudentMarksheetDetails.LastName, "", "" ,"", "", ""});
        Mode6.addRow(new Object[]{"Course:: ",StudentMarksheetDetails.Course.CourseName,"","", "","", ""});
        Mode6.addRow(new Object[]{"Level:: ", StudentMarksheetDetails.Level, "", "", "", "", ""});

        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
        Mode6.addRow(new Object[]{"ModuleID", "ModuleName", "passMark", "FullMark","Mark", "Grade", "Resit"});
        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});

        for (Module m : StudentMarksheetDetails.modules) {
            if(!Recit) {
                if(!DBS.checkPassOrFail(m.ModuleID, m.GainedMark)) {
                    Recit = true;
                }
            }
            TotalModules += 1;
            TotalMarkgained += m.GainedMark;
            TotalMark += m.FullMark;
            Mode6.addRow(new Object[]{m.ModuleID, m.ModuleName, m.PassMark, m.FullMark,m.GainedMark ,DBS.findGrade(m.GainedMark), m.HaveResit});
        }

        float GPA = ((TotalMarkgained * 100f)/TotalMark)/25f;
        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
        Mode6.addRow(new Object[]{"", "", "Total:: ", TotalMarkgained, "Grade:: ", DBS.findGrade(TotalMarkgained/TotalModules)});
        Mode6.addRow(new Object[]{"", "", "Resit:: ", Recit, "GPA:: ", GPA,""});
        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
        Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public void showAllMarksheet() throws SQLException {
        ArrayList<Student> S = DBS.getStudentMarksheet();

        Mode6.setRowCount(0);
        String[] Header = {"", "", "", "", "", "",""};
        Mode6.setColumnIdentifiers(Header);
        jTable9.setModel(Mode6);

        jTable9.getColumnModel().getColumn(1).setPreferredWidth(200);

        for(Student s: S) {
            if (DBS.findIfMarksheetIsCreatedOrNot(s.StudentID)) {
                jTable9.clearSelection();

                boolean Recit = false;
                int TotalMark = 0;
                int TotalMarkgained = 0;
                int TotalModules = 0;

                Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                Mode6.addRow(new Object[]{"ID:: ", s.StudentID, "", "" ,"", "", ""});
                Mode6.addRow(new Object[]{"Name:: ", s.FirstName +" "+s.LastName, "", "" ,"", "", ""});
                Mode6.addRow(new Object[]{"Course:: ",s.Course.CourseName,"","", "","", ""});
                Mode6.addRow(new Object[]{"Level:: ", s.Level, "", "", "", "", ""});

                Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                Mode6.addRow(new Object[]{"ModuleID", "ModuleName", "passMark", "FullMark", "Mark", "Grade", "Resit"});
                Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});

                for (Module m : s.modules) {
                    if (!Recit) {
                        if (!DBS.checkPassOrFail(m.ModuleID, m.GainedMark)) {
                            Recit = true;
                        }
                    }
                    TotalModules += 1;
                    TotalMarkgained += m.GainedMark;
                    TotalMark += m.FullMark;
                    Mode6.addRow(new Object[]{m.ModuleID, m.ModuleName, m.PassMark, m.FullMark,m.GainedMark ,DBS.findGrade(m.GainedMark), m.HaveResit});
                }

                float GPA = ((TotalMarkgained * 100f)/TotalMark)/25f;

                Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                Mode6.addRow(new Object[]{"", "", "Total:: ", TotalMarkgained, "Grade:: ", DBS.findGrade(TotalMarkgained/TotalModules)});
                Mode6.addRow(new Object[]{"", "", "Resit:: ", Recit, "GPA:: ", GPA,""});
                Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
                Mode6.addRow(new Object[]{"", "", "", "", "", "", ""});
            }
        }
    }

    public boolean CheckOldStudentEnrolmentIsValid() {
        boolean isValied = true;
        newStudent.StudentID = jTextField26.getText().trim().toUpperCase(Locale.ROOT);
        newStudent.Course.CourseID = SelectedCourseID;
        newStudent.Level = SelectedLevel;

        if((YourSem1Modules.size() < 4) || (YourSem2Modules.size() < 4)) {
            JOptionPane.showMessageDialog(Student_OldStudent, "please add 2 Modules on your Sem1 and Sem2 Modules from Optionals");
            isValied = false;
        } else if(newStudent.StudentID.equals("Enter Your Student ID...") || (newStudent.StudentID.length() < 11)) {
            JOptionPane.showMessageDialog(Student_OldStudent, "please Enter Your Student ID Correctly !");
        }
        newStudent.modules.clear();
        for(Module m: YourSem1Modules) {
            m.Sem = 1;
            newStudent.modules.add(m);
        }
        for(Module m: YourSem2Modules) {
            m.Sem = 2;
            newStudent.modules.add(m);
        }
        return isValied;
    }

    public boolean CheckNewStudentEnrolmentIsValid() {
        newStudent.Course.CourseID = SelectedCourseID;
        newStudent.Level = SelectedLevel;
        boolean isValied = true;

        newStudent.FirstName = Student_NewStudentName1.getText().trim();
        newStudent.LastName = Student_NewStudentSurname1.getText().trim();

        if((newStudent.FirstName.length() < 2 || newStudent.LastName.length() < 2) || (newStudent.FirstName.equals("Enter First Name...") || newStudent.LastName.equals("Enter Last Name..."))) {
            JOptionPane.showMessageDialog(Student_OldStudent, "Pleases Enter your Name and Surname Correctly !");
            isValied = false;
        }

        newStudent.modules.clear();
        for(Module m: YourSem1Modules) {
            m.Sem = 1;
            newStudent.modules.add(m);
        }
        for(Module m: YourSem2Modules) {
            m.Sem = 2;
            newStudent.modules.add(m);
        }

        return isValied;
    }

    public void refreshInstructors() {
        SelectedIDFromTable = "-1";
        try {
            ResultSet resultSet = DBS.getAllTeachers();
            if(resultSet.next()) {
                jLabel5.setText("if you don's want to add new Instructor then choose form old Instructors and hit save");
                String Header[] = {"InstructorID","FirstName", "LastName"};

                Mode5.setColumnIdentifiers(Header);
                CourseAdministrator_Table.setModel(Mode5);
                Mode5.setRowCount(0);
                Mode5.addRow(new Object[]{resultSet.getString("InstructorID"), resultSet.getString("FirstName"), resultSet.getString("LastName")});

                while(resultSet.next()) {
                    Mode5.addRow(new Object[]{resultSet.getString("InstructorID"), resultSet.getString("FirstName"), resultSet.getString("LastName")});
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void refresh() {
        SelectedIDFromTable = "-1";
        try {
            Mode5.setRowCount(0);
            ResultSet resultSet = DBS.getCanceledCourses();
            if(resultSet.next()) {
                jLabel5.setText("if you want to choose from cancelled then select one and click save");
                String Header[] = {"CourseID","CourseName", "IsCancelled"};

                Mode5.setColumnIdentifiers(Header);
                CourseAdministrator_Table.setModel(Mode5);
                Mode5.addRow(new Object[]{resultSet.getString("CourseID"), resultSet.getString("CourseName"), resultSet.getString("IsCancelled")});

                while(resultSet.next()) {
                    Mode5.addRow(new Object[]{resultSet.getString("CourseID"), resultSet.getString("CourseName"), resultSet.getString("IsCancelled")});
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void YourModules(String Course, int Level, boolean HaveOptional) throws SQLException {
        YourSem1Modules.clear();
        YourSem2Modules.clear();
        YourSem1OptionalModules.clear();
        YourSem2OptionalModules.clear();

        if(HaveOptional) {
            Student_OptionalModules.removeAllItems();
            ResultSet OptionalModuleOnSem1 = DBS.getModulesOnCourse(Course, Level, 1, true);
            ResultSet OptionalModuleOnSem2 = DBS.getModulesOnCourse(Course, Level, 2, true);
            ResultSet CompulsoryModuleOnSem1 = DBS.getModulesOnCourse(Course, Level, 1, false);
            ResultSet CompulsoryModuleOnSem2 = DBS.getModulesOnCourse(Course, Level, 2, false);

            while(CompulsoryModuleOnSem1.next()) {
                Module m = new Module();
                m.ModuleID = CompulsoryModuleOnSem1.getString("ModuleID");
                m.ModuleName = CompulsoryModuleOnSem1.getString("ModuleName");
                m.isOptional = CompulsoryModuleOnSem1.getBoolean("IsOptional");
                m.Sem = CompulsoryModuleOnSem1.getInt("Sem");
                this.YourSem1Modules.add(m);
            }

            while(CompulsoryModuleOnSem2.next()) {
                Module m = new Module();
                m.ModuleID = CompulsoryModuleOnSem2.getString("ModuleID");
                m.ModuleName = CompulsoryModuleOnSem2.getString("ModuleName");
                m.isOptional = CompulsoryModuleOnSem2.getBoolean("IsOptional");
                m.Sem = CompulsoryModuleOnSem2.getInt("Sem");
                this.YourSem2Modules.add(m);
            }

            while(OptionalModuleOnSem1.next()) {
                Module m = new Module();
                m.ModuleID = OptionalModuleOnSem1.getString("ModuleID");
                m.ModuleName = OptionalModuleOnSem1.getString("ModuleName");
                m.isOptional = OptionalModuleOnSem1.getBoolean("IsOptional");
                m.Sem = OptionalModuleOnSem1.getInt("Sem");
                this.YourSem1OptionalModules.add(m);
            }

            while(OptionalModuleOnSem2.next()) {
                Module m = new Module();
                m.ModuleID = OptionalModuleOnSem2.getString("ModuleID");
                m.ModuleName = OptionalModuleOnSem2.getString("ModuleName");
                m.isOptional = OptionalModuleOnSem2.getBoolean("IsOptional");
                m.Sem = OptionalModuleOnSem2.getInt("Sem");
                this.YourSem2OptionalModules.add(m);
            }
        } else {
            Student_OptionalModules.removeAllItems();
            Student_OptionalModules.addItem("No Optional Modules");
            ResultSet CompulsoryModuleOnSem1 = DBS.getModulesOnCourse(Course, Level, 1, false);
            ResultSet CompulsoryModuleOnSem2 = DBS.getModulesOnCourse(Course, Level, 2, false);

            while(CompulsoryModuleOnSem1.next()) {
                Module m = new Module();
                m.ModuleID = CompulsoryModuleOnSem1.getString("ModuleID");
                m.ModuleName = CompulsoryModuleOnSem1.getString("ModuleName");
                m.isOptional = CompulsoryModuleOnSem1.getBoolean("IsOptional");
                m.Sem = CompulsoryModuleOnSem1.getInt("Sem");
                this.YourSem1Modules.add(m);
            }
            while(CompulsoryModuleOnSem2.next()) {
                Module m = new Module();
                m.ModuleID = CompulsoryModuleOnSem2.getString("ModuleID");
                m.ModuleName = CompulsoryModuleOnSem2.getString("ModuleName");
                m.isOptional = CompulsoryModuleOnSem2.getBoolean("IsOptional");
                m.Sem = CompulsoryModuleOnSem2.getInt("Sem");
                this.YourSem2Modules.add(m);
            }
        }
    }

    public void showModules(String Course, int Level, int Sem) throws SQLException {
        String Header[] = {"Modules On Level "+Level+" Sem "+Sem};
        Model.setColumnIdentifiers(Header);
        Student_StudentSelectedModulesTable.setModel(Model);
        if(!DBS.SemHaveOptionalModules(Course, Level, Sem)) {
            if (Sem == 1) {
                Model.setRowCount(0);
                for (Module m : this.YourSem1Modules) {
                    Model.addRow(new Object[]{m.ModuleName});
                }
            } else if (Sem == 2) {
                Model.setRowCount(0);
                for (Module m : this.YourSem2Modules) {
                    Model.addRow(new Object[]{m.ModuleName});
                }
            }
        }else if(DBS.SemHaveOptionalModules(Course, Level, Sem)) {
            if (Sem == 1) {
                Student_OptionalModules.removeAllItems();
                Model.setRowCount(0);
                Student_OptionalModules.addItem("Choose from Optional Modules");
                for (Module m : this.YourSem1Modules) {
                    Model.addRow(new Object[]{m.ModuleName});
                }

                if(Level == 4) {
                    for (Module m: this.YourSem1OptionalModules) {
                        Student_OptionalModules3.addItem(m.ModuleName);
                    }
                } else {
                    for (Module m: this.YourSem1OptionalModules) {
                        Student_OptionalModules.addItem(m.ModuleName);
                    }
                }
            } else if (Sem == 2) {
                Model.setRowCount(0);
                Student_OptionalModules.removeAllItems();
                Student_OptionalModules.addItem("Choose from Optional Modules");
                for (Module m : this.YourSem2Modules) {
                    Model.addRow(new Object[]{m.ModuleName});
                }
                if(Level == 4) {
                    for (Module m: this.YourSem2OptionalModules) {
                        Student_OptionalModules3.addItem(m.ModuleName);
                    }
                } else {
                    for (Module m: this.YourSem2OptionalModules) {
                        Student_OptionalModules.addItem(m.ModuleName);
                    }
                }
            }
        }

        String Header2[] = {"ModuleID","ModuleName","IsOptionalModel", "Level", "Sem"};
        Mode1.setColumnIdentifiers(Header2);
        Studetnt_CourseTable.setModel(Mode1);
        Mode1.setRowCount(0);
        for(Module m: YourSem1Modules) {
            Mode1.addRow(new Object[]{m.ModuleID, m.ModuleName, m.isOptional, SelectedLevel, m.Sem});
        }
        for(Module m: YourSem2Modules) {
            Mode1.addRow(new Object[]{m.ModuleID, m.ModuleName, m.isOptional, SelectedLevel, m.Sem});
        }
    }

    public void StudentSemCombobox() throws SQLException {
        // this represent semester
        keyValues6.clear();
        keyValues7.clear();

        Student_OptionalModules2.removeAllItems();
        String[] sems = new String[]{"1st sem", "2nd Sem"};

        for (int i = 1; i < 3; i++) {
            KeyValue obj = new KeyValue(Integer.toString(i), sems[i - 1]);
            keyValues6.add(obj);
            Student_OptionalModules2.addItem(obj.getValue());
        }

        Student_OptionalModules2.setSelectedIndex(0);

        // this represent semester
        Student_OptionalModules3.removeAllItems();
        sems = new String[]{"1st sem", "2nd Sem"};

        for (int i = 1; i < 3; i++) {
            KeyValue obj = new KeyValue(Integer.toString(i), sems[i - 1]);
            keyValues7.add(obj);
            Student_OptionalModules3.addItem(obj.getValue());
        }

        Student_OptionalModules3.setSelectedIndex(0);
    }

    public void TextFieldPlaceHolder(javax.swing.JTextField field, String placeHolderText) {
        if(field.getText().equals("")) {
            field.setText(placeHolderText);
        } else if(field.getText().equals(placeHolderText)) {
            field.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        Students = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        CourseAdministrator = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Instructor = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        StudentUI = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        StudentCoursesPanel = new javax.swing.JPanel();
        StudentCoursesButton = new javax.swing.JLabel();
        StudentInstructorPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        Student_CoursesBox = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        Student_ModulesBox = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        CoursesNewStudent = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        Student_OldStudent = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        Student_OptionalModules = new javax.swing.JComboBox<>();
        Student_AddModules = new javax.swing.JButton();
        Student_OldStudentEnrollment = new javax.swing.JButton();
        jTextField26 = new javax.swing.JTextField();
        Student_OptionalModules2 = new javax.swing.JComboBox<>();
        jPanel61 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        Student_NewStudentName1 = new javax.swing.JTextField();
        Student_NewStudentSurname1 = new javax.swing.JTextField();
        Student_newStudentEnrollment1 = new javax.swing.JButton();
        Student_OptionalModules3 = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Student_StudentSelectedModulesTable = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        StudentInstructorsPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        Student_InstructorTable = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jTextField32 = new javax.swing.JTextField();
        StudentModulesPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Studetnt_CourseTable = new javax.swing.JTable();
        CourseAdministratorUI = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel57 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel51 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        CourseAdministrator_RenameModule = new javax.swing.JButton();
        CourseAdministrator_RemoveInstructor = new javax.swing.JButton();
        CourseAdministrator_AddInstructor = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        CourseAdministrator_AddCourse = new javax.swing.JButton();
        CourseAdministrator_DeleteCourse = new javax.swing.JButton();
        CourseAdministrator_CancelCourse = new javax.swing.JButton();
        CourseAdministrator_RenameCourse = new javax.swing.JButton();
        CourseAdministrator_AddModule = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        CourseAdministrator_CourseLavelsBox = new javax.swing.JComboBox<>();
        jTextField15 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        CourseAdministrator_AddNextModule = new javax.swing.JButton();
        CourseAdministrator_SaveCourse = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel39 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jTextField19 = new javax.swing.JTextField();
        CourseAdministrator_RenameModuleSave = new javax.swing.JButton();
        jPanel45 = new javax.swing.JPanel();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jTextField27 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jTextField28 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jTextField29 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jTextField30 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        CourseAdministrator_Table = new javax.swing.JTable();
        jPanel29 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        CourseAdministrator_SearchButton = new javax.swing.JButton();
        CourseAdministrator_SearchBar = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        CourseAdministrator_GenerateMarksheet = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel59 = new javax.swing.JPanel();
        CourseAdministrator_HaveRecit = new javax.swing.JComboBox<>();
        jPanel60 = new javax.swing.JPanel();
        jTextField25 = new javax.swing.JTextField();
        CourseAdministrator_AssignNextMark = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        InstructorUI = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jTextField24 = new javax.swing.JTextField();
        jPanel36 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel43 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jPanel54 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jTextField23 = new javax.swing.JTextField();
        jButton26 = new javax.swing.JButton();
        jTextField31 = new javax.swing.JTextField();

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane6.setViewportView(jTable6);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 453));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        Students.setBackground(new java.awt.Color(0, 0, 102));
        Students.setPreferredSize(new java.awt.Dimension(150, 30));
        Students.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                StudentsMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                StudentsMouseEntered(evt);
            }
        });
        Students.setLayout(new java.awt.CardLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Student");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
        });
        Students.add(jLabel2, "card2");

        jPanel1.add(Students, new java.awt.GridBagConstraints());

        CourseAdministrator.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator.setPreferredSize(new java.awt.Dimension(150, 30));
        CourseAdministrator.setLayout(new java.awt.CardLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Course Administrator");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
        });
        CourseAdministrator.add(jLabel3, "card2");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel1.add(CourseAdministrator, gridBagConstraints);

        Instructor.setBackground(new java.awt.Color(0, 0, 102));
        Instructor.setPreferredSize(new java.awt.Dimension(150, 30));
        Instructor.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Instructor");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
        });
        Instructor.add(jLabel1, "card2");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel1.add(Instructor, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setLayout(new java.awt.CardLayout());

        StudentUI.setForeground(new java.awt.Color(51, 51, 51));
        StudentUI.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(679, 20));
        jPanel3.setLayout(new java.awt.CardLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setForeground(new Color(200, 0, 0));
        jPanel3.add(jLabel4, "card2");

        StudentUI.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(0, 0, 102));
        jPanel5.setPreferredSize(new java.awt.Dimension(679, 35));

        StudentCoursesPanel.setBackground(new java.awt.Color(0, 0, 102));
        StudentCoursesPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        StudentCoursesPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        StudentCoursesPanel.setLayout(new java.awt.CardLayout());

        StudentCoursesButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        StudentCoursesButton.setForeground(new java.awt.Color(204, 204, 204));
        StudentCoursesButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        StudentCoursesButton.setText("Courses");
        StudentCoursesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        StudentCoursesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentCoursesButtonMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                StudentCoursesButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                StudentCoursesButtonMouseEntered(evt);
            }
        });
        StudentCoursesPanel.add(StudentCoursesButton, "card2");

        StudentInstructorPanel.setBackground(new java.awt.Color(0, 0, 102));
        StudentInstructorPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        StudentInstructorPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        StudentInstructorPanel.setLayout(new java.awt.CardLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Instructors");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
        });
        StudentInstructorPanel.add(jLabel7, "card2");

        jPanel9.setBackground(new java.awt.Color(51, 0, 153));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        jPanel9.setPreferredSize(new java.awt.Dimension(250, 30));

        Student_CoursesBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(Student_CoursesBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Student_CoursesBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel14.setBackground(new java.awt.Color(51, 0, 153));
        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        jPanel14.setPreferredSize(new java.awt.Dimension(100, 30));

        Student_ModulesBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Student_ModulesBox, 0, 207, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Student_ModulesBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(StudentCoursesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(StudentInstructorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(StudentCoursesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(StudentInstructorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(0, 204, 204));
        jPanel10.setPreferredSize(new java.awt.Dimension(21, 403));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 21, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 520, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel10, java.awt.BorderLayout.LINE_START);

        CoursesNewStudent.setLayout(new java.awt.BorderLayout());

        jPanel12.setPreferredSize(new java.awt.Dimension(250, 403));
        jPanel12.setLayout(new java.awt.CardLayout());

        Student_OldStudent.setLayout(new java.awt.BorderLayout());

        jPanel17.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new java.awt.CardLayout());

        jPanel22.setBackground(new java.awt.Color(0, 204, 204));

        Student_AddModules.setBackground(new java.awt.Color(0, 0, 102));
        Student_AddModules.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Student_AddModules.setForeground(new java.awt.Color(204, 204, 204));
        Student_AddModules.setText("Add Module");
        Student_AddModules.setBorder(null);
        Student_AddModules.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Student_AddModules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Student_AddModulesActionPerformed(evt);
            }
        });

        Student_OldStudentEnrollment.setBackground(new java.awt.Color(0, 0, 102));
        Student_OldStudentEnrollment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Student_OldStudentEnrollment.setForeground(new java.awt.Color(204, 204, 204));
        Student_OldStudentEnrollment.setText("Enroll");
        Student_OldStudentEnrollment.setBorder(null);
        Student_OldStudentEnrollment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Student_OldStudentEnrollment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Student_OldStudentEnrollmentActionPerformed(evt);
            }
        });

        jTextField26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField26.setForeground(new java.awt.Color(204, 204, 204));
        jTextField26.setText("Enter Your Student ID...");
        jTextField26.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField26.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField26FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField26FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(Student_AddModules, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Student_OldStudentEnrollment, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                        .addComponent(jTextField26)
                        .addComponent(Student_OptionalModules2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Student_OptionalModules, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Student_OptionalModules2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(Student_OptionalModules, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(Student_OldStudentEnrollment, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Student_AddModules, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37))
        );

        jPanel20.add(jPanel22, "card2");

        jPanel61.setBackground(new java.awt.Color(0, 204, 204));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("jLabel8");

        Student_NewStudentName1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Student_NewStudentName1.setForeground(new java.awt.Color(204, 204, 204));
        Student_NewStudentName1.setText("Enter First Name...");
        Student_NewStudentName1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        Student_NewStudentName1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Student_NewStudentName1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                Student_NewStudentName1FocusLost(evt);
            }
        });
        Student_NewStudentName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Student_NewStudentName1ActionPerformed(evt);
            }
        });

        Student_NewStudentSurname1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Student_NewStudentSurname1.setForeground(new java.awt.Color(204, 204, 204));
        Student_NewStudentSurname1.setText("Enter Last Name...");
        Student_NewStudentSurname1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        Student_NewStudentSurname1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Student_NewStudentSurname1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                Student_NewStudentSurname1FocusLost(evt);
            }
        });
        Student_NewStudentSurname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Student_NewStudentSurname1ActionPerformed(evt);
            }
        });

        Student_newStudentEnrollment1.setBackground(new java.awt.Color(0, 0, 102));
        Student_newStudentEnrollment1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Student_newStudentEnrollment1.setForeground(new java.awt.Color(204, 204, 204));
        Student_newStudentEnrollment1.setText("Enroll");
        Student_newStudentEnrollment1.setBorder(null);
        Student_newStudentEnrollment1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Student_newStudentEnrollment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Student_newStudentEnrollment1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
        jPanel61.setLayout(jPanel61Layout);
        jPanel61Layout.setHorizontalGroup(
                jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel61Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Student_NewStudentName1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Student_NewStudentSurname1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Student_newStudentEnrollment1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(42, Short.MAX_VALUE))
                        .addComponent(Student_OptionalModules3, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel61Layout.setVerticalGroup(
                jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel61Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(Student_OptionalModules3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Student_NewStudentName1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Student_NewStudentSurname1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Student_newStudentEnrollment1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        jPanel20.add(jPanel61, "card3");

        jPanel17.add(jPanel20, java.awt.BorderLayout.CENTER);

        Student_OldStudent.add(jPanel17, java.awt.BorderLayout.PAGE_END);

        jPanel18.setLayout(new java.awt.BorderLayout());

        Student_StudentSelectedModulesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null},
                        {null},
                        {null},
                        {null}
                },
                new String [] {
                        "Modules"
                }
        ));
        Student_StudentSelectedModulesTable.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane1.setViewportView(Student_StudentSelectedModulesTable);

        jPanel18.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        Student_OldStudent.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel12.add(Student_OldStudent, "card3");

        CoursesNewStudent.add(jPanel12, java.awt.BorderLayout.LINE_START);

        jPanel24.setLayout(new java.awt.CardLayout());

        StudentInstructorsPanel.setLayout(new java.awt.BorderLayout());

        Student_InstructorTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null},
                        {null},
                        {null},
                        {null}
                },
                new String [] {
                        "Instructors"
                }
        ));
        Student_InstructorTable.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane7.setViewportView(Student_InstructorTable);

        StudentInstructorsPanel.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jPanel19.setBackground(new java.awt.Color(0, 204, 204));
        jPanel19.setPreferredSize(new java.awt.Dimension(448, 35));

        jTextField32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField32.setForeground(new java.awt.Color(204, 204, 204));
        jTextField32.setText("Enter Your Student ID...");
        jTextField32.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField32.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField32FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField32FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                .addContainerGap(269, Short.MAX_VALUE)
                                .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
        );
        jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        StudentInstructorsPanel.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        jPanel24.add(StudentInstructorsPanel, "card2");

        StudentModulesPanel.setLayout(new java.awt.BorderLayout());

        Studetnt_CourseTable.setAutoCreateRowSorter(true);
        Studetnt_CourseTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null},
                        {null},
                        {null},
                        {null}
                },
                new String [] {
                        "Modules"
                }
        ));
        Studetnt_CourseTable.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane3.setViewportView(Studetnt_CourseTable);

        StudentModulesPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel24.add(StudentModulesPanel, "card3");

        CoursesNewStudent.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel6.add(CoursesNewStudent, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        StudentUI.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel2.add(StudentUI, "card2");

        CourseAdministratorUI.setLayout(new java.awt.BorderLayout());

        jPanel26.setBackground(new java.awt.Color(0, 204, 204));
        jPanel26.setPreferredSize(new java.awt.Dimension(679, 20));
        jPanel26.setLayout(new java.awt.CardLayout());

        jLabel5.setBackground(new java.awt.Color(0, 204, 204));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setForeground(new Color(200, 0, 0));
        jPanel26.add(jLabel5, "card2");

        CourseAdministratorUI.add(jPanel26, java.awt.BorderLayout.PAGE_START);

        jPanel27.setLayout(new java.awt.BorderLayout());

        jPanel28.setBackground(new java.awt.Color(0, 0, 102));
        jPanel28.setPreferredSize(new java.awt.Dimension(679, 35));
        jPanel28.setVerifyInputWhenFocusTarget(false);
        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(0, 0, 102));
        jPanel15.setPreferredSize(new java.awt.Dimension(300, 35));

        jComboBox1.setBorder(null);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel28.add(jPanel15, java.awt.BorderLayout.LINE_START);

        jPanel57.setBackground(new java.awt.Color(0, 0, 102));

        jPanel56.setBackground(new java.awt.Color(0, 0, 102));
        jPanel56.setLayout(new java.awt.CardLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Student Performance");
        jLabel6.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    jLabel6MouseClicked(evt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                try {
                    jLabel6MouseEntered(evt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        jPanel56.add(jLabel6, "card2");

        jPanel58.setBackground(new java.awt.Color(0, 0, 102));
        jPanel58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel58MouseClicked(evt);
            }
        });
        jPanel58.setLayout(new java.awt.CardLayout());

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Courses ");
        jLabel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(255, 255, 255)));
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
        });
        jPanel58.add(jLabel11, "card2");

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
                jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel57Layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel58, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel57Layout.setVerticalGroup(
                jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel58, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel28.add(jPanel57, java.awt.BorderLayout.CENTER);

        jPanel27.add(jPanel28, java.awt.BorderLayout.PAGE_START);

        jPanel53.setLayout(new java.awt.CardLayout());

        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel52.setPreferredSize(new java.awt.Dimension(679, 100));
        jPanel52.setLayout(new java.awt.BorderLayout());

        jPanel46.setBackground(new java.awt.Color(0, 204, 204));
        jPanel46.setPreferredSize(new java.awt.Dimension(300, 75));

        jComboBox2.setBorder(null);

        jComboBox3.setBorder(null);

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
                jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel46Layout.setVerticalGroup(
                jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel52.add(jPanel46, java.awt.BorderLayout.LINE_START);

        jPanel51.setBackground(new java.awt.Color(0, 204, 204));
        jPanel51.setLayout(new java.awt.CardLayout());

        jPanel50.setPreferredSize(new java.awt.Dimension(400, 75));
        jPanel50.setLayout(new java.awt.CardLayout());

        jPanel49.setBackground(new java.awt.Color(0, 204, 204));
        jPanel49.setPreferredSize(new java.awt.Dimension(400, 75));
        jPanel49.setLayout(new java.awt.GridBagLayout());

        CourseAdministrator_RenameModule.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_RenameModule.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_RenameModule.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_RenameModule.setText("Rename Module");
        CourseAdministrator_RenameModule.setBorder(null);
        CourseAdministrator_RenameModule.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_RenameModule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CourseAdministrator_RenameModuleMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 6, 35, 5);
        jPanel49.add(CourseAdministrator_RenameModule, gridBagConstraints);

        CourseAdministrator_RemoveInstructor.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_RemoveInstructor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_RemoveInstructor.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_RemoveInstructor.setText("Remove Instructor");
        CourseAdministrator_RemoveInstructor.setBorder(null);
        CourseAdministrator_RemoveInstructor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_RemoveInstructor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CourseAdministrator_RemoveInstructorMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 6, 35, 0);
        jPanel49.add(CourseAdministrator_RemoveInstructor, gridBagConstraints);

        CourseAdministrator_AddInstructor.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_AddInstructor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_AddInstructor.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_AddInstructor.setText("Add Instructor");
        CourseAdministrator_AddInstructor.setBorder(null);
        CourseAdministrator_AddInstructor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_AddInstructor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    CourseAdministrator_AddInstructorMouseClicked(evt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        CourseAdministrator_AddInstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CourseAdministrator_AddInstructorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 10, 35, 0);
        jPanel49.add(CourseAdministrator_AddInstructor, gridBagConstraints);

        jPanel50.add(jPanel49, "card3");

        jPanel48.setBackground(new java.awt.Color(0, 204, 204));
        jPanel48.setMinimumSize(new java.awt.Dimension(300, 46));
        jPanel48.setLayout(new java.awt.GridBagLayout());

        CourseAdministrator_AddCourse.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_AddCourse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_AddCourse.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_AddCourse.setText("Add Course");
        CourseAdministrator_AddCourse.setBorder(null);
        CourseAdministrator_AddCourse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_AddCourse.setPreferredSize(new java.awt.Dimension(100, 30));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 31;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel48.add(CourseAdministrator_AddCourse, gridBagConstraints);

        CourseAdministrator_DeleteCourse.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_DeleteCourse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_DeleteCourse.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_DeleteCourse.setText("Delete Course");
        CourseAdministrator_DeleteCourse.setBorder(null);
        CourseAdministrator_DeleteCourse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_DeleteCourse.setPreferredSize(new java.awt.Dimension(100, 30));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 17;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 4, 0);
        jPanel48.add(CourseAdministrator_DeleteCourse, gridBagConstraints);

        CourseAdministrator_CancelCourse.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_CancelCourse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_CancelCourse.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_CancelCourse.setText("Cancel Course");
        CourseAdministrator_CancelCourse.setBorder(null);
        CourseAdministrator_CancelCourse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_CancelCourse.setPreferredSize(new java.awt.Dimension(100, 30));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 29;
        gridBagConstraints.ipady = 61;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 0);
        jPanel48.add(CourseAdministrator_CancelCourse, gridBagConstraints);

        CourseAdministrator_RenameCourse.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_RenameCourse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_RenameCourse.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_RenameCourse.setText("Rename");
        CourseAdministrator_RenameCourse.setBorder(null);
        CourseAdministrator_RenameCourse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_RenameCourse.setPreferredSize(new java.awt.Dimension(100, 30));
        CourseAdministrator_RenameCourse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CourseAdministrator_RenameCourseMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 51;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 131);
        jPanel48.add(CourseAdministrator_RenameCourse, gridBagConstraints);

        CourseAdministrator_AddModule.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_AddModule.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_AddModule.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_AddModule.setText("Add Module");
        CourseAdministrator_AddModule.setBorder(null);
        CourseAdministrator_AddModule.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CourseAdministrator_AddModule.setPreferredSize(new java.awt.Dimension(100, 30));
        CourseAdministrator_AddModule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CourseAdministrator_AddModuleMouseClicked(evt);
            }
        });
        CourseAdministrator_AddModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 4, 131);
        jPanel48.add(CourseAdministrator_AddModule, gridBagConstraints);

        jPanel50.add(jPanel48, "card2");

        jPanel51.add(jPanel50, "card2");

        jPanel52.add(jPanel51, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel52, java.awt.BorderLayout.PAGE_START);

        jPanel30.setLayout(new java.awt.BorderLayout());

        jPanel37.setPreferredSize(new java.awt.Dimension(200, 328));
        jPanel37.setLayout(new java.awt.CardLayout());

        jPanel38.setBackground(new java.awt.Color(0, 204, 204));

        jTextField15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField15.setForeground(new java.awt.Color(204, 204, 204));
        jTextField15.setText("Course Name...");
        jTextField15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField15.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField15FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField15FocusLost(evt);
            }
        });
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jTextField17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField17.setForeground(new java.awt.Color(204, 204, 204));
        jTextField17.setText("Enter Module Name...");
        jTextField17.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField17.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField17FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField17FocusLost(evt);
            }
        });
        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });

        CourseAdministrator_AddNextModule.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_AddNextModule.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_AddNextModule.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_AddNextModule.setText("Next Module");
        CourseAdministrator_AddNextModule.setBorder(null);
        CourseAdministrator_AddNextModule.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        CourseAdministrator_SaveCourse.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_SaveCourse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_SaveCourse.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_SaveCourse.setText("Save");
        CourseAdministrator_SaveCourse.setBorder(null);
        CourseAdministrator_SaveCourse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("jLabel8");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
                jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel38Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CourseAdministrator_CourseLavelsBox, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel38Layout.createSequentialGroup()
                                                .addComponent(CourseAdministrator_AddNextModule, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6)
                                                .addComponent(CourseAdministrator_SaveCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16))
        );
        jPanel38Layout.setVerticalGroup(
                jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel38Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(CourseAdministrator_CourseLavelsBox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(CourseAdministrator_AddNextModule, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CourseAdministrator_SaveCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel37.add(jPanel38, "card2");

        jPanel39.setBackground(new java.awt.Color(0, 204, 204));
        jPanel39.setLayout(new java.awt.CardLayout());

        jLabel9.setBackground(new java.awt.Color(0, 204, 204));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Sucessfully Deleted the Course");
        jPanel39.add(jLabel9, "card2");

        jPanel37.add(jPanel39, "card3");

        jPanel40.setBackground(new java.awt.Color(0, 204, 204));
        jPanel40.setLayout(new java.awt.CardLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Sucessfully Cancelled the Course");
        jPanel40.add(jLabel10, "card2");

        jPanel37.add(jPanel40, "card4");

        jPanel41.setBackground(new java.awt.Color(0, 204, 204));
        jPanel41.setLayout(new java.awt.GridBagLayout());

        jTextField19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField19.setForeground(new java.awt.Color(204, 204, 204));
        jTextField19.setText("Rename Course...");
        jTextField19.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField19.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField19FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField19FocusLost(evt);
            }
        });
        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 174;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(53, 10, 0, 16);
        jPanel41.add(jTextField19, gridBagConstraints);

        CourseAdministrator_RenameModuleSave.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_RenameModuleSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_RenameModuleSave.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_RenameModuleSave.setText("Save");
        CourseAdministrator_RenameModuleSave.setBorder(null);
        CourseAdministrator_RenameModuleSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 10, 288, 0);
        jPanel41.add(CourseAdministrator_RenameModuleSave, gridBagConstraints);

        jPanel37.add(jPanel41, "card5");

        jPanel45.setBackground(new java.awt.Color(0, 204, 204));
        jPanel45.setLayout(new java.awt.GridBagLayout());

        jTextField20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField20.setForeground(new java.awt.Color(204, 204, 204));
        jTextField20.setText("Enter Module Name...");
        jTextField20.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField20.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField20FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField20FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 10);
        jPanel45.add(jTextField20, gridBagConstraints);

        jTextField21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField21.setForeground(new java.awt.Color(204, 204, 204));
        jTextField21.setText("sem, Optional");
        jTextField21.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField21.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField21FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField21FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 10, 0, 10);
        jPanel45.add(jTextField21, gridBagConstraints);

        jButton18.setBackground(new java.awt.Color(0, 0, 102));
        jButton18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton18.setForeground(new java.awt.Color(204, 204, 204));
        jButton18.setText("Add");
        jButton18.setBorder(null);
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 10, 177, 0);
        jPanel45.add(jButton18, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 128;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(68, 10, 0, 10);
        jPanel45.add(jLabel14, gridBagConstraints);

        jPanel37.add(jPanel45, "card6");

        jPanel13.setBackground(new java.awt.Color(0, 204, 204));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        jTextField27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField27.setForeground(new java.awt.Color(204, 204, 204));
        jTextField27.setText("Enter First Name...");
        jTextField27.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField27.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField27FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField27FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 10);
        jPanel13.add(jTextField27, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("jLabel15");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 128;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(72, 10, 0, 10);
        jPanel13.add(jLabel15, gridBagConstraints);

        jButton11.setBackground(new java.awt.Color(0, 0, 102));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(204, 204, 204));
        jButton11.setText("Add");
        jButton11.setBorder(null);
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.ipady = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 180, 0);
        jPanel13.add(jButton11, gridBagConstraints);

        jTextField28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField28.setForeground(new java.awt.Color(204, 204, 204));
        jTextField28.setText("Enter Last Name...");
        jTextField28.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField28.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField28FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField28FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 10);
        jPanel13.add(jTextField28, gridBagConstraints);

        jPanel37.add(jPanel13, "card7");

        jPanel21.setBackground(new java.awt.Color(0, 204, 204));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jTextField29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField29.setForeground(new java.awt.Color(204, 204, 204));
        jTextField29.setText("Enter Instructor ID...");
        jTextField29.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField29.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField29FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField29FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(98, 10, 0, 10);
        jPanel21.add(jTextField29, gridBagConstraints);

        jButton12.setBackground(new java.awt.Color(0, 0, 102));
        jButton12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(204, 204, 204));
        jButton12.setText("Remove");
        jButton12.setBorder(null);
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.ipady = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 10, 230, 0);
        jPanel21.add(jButton12, gridBagConstraints);

        jPanel37.add(jPanel21, "card8");

        jPanel23.setBackground(new java.awt.Color(0, 204, 204));
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jTextField30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField30.setForeground(new java.awt.Color(204, 204, 204));
        jTextField30.setText("Rename Module...");
        jTextField30.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField30.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField30FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField30FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(95, 10, 0, 10);
        jPanel23.add(jTextField30, gridBagConstraints);

        jButton13.setBackground(new java.awt.Color(0, 0, 102));
        jButton13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton13.setForeground(new java.awt.Color(204, 204, 204));
        jButton13.setText("Rename");
        jButton13.setBorder(null);
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.ipady = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 240, 0);
        jPanel23.add(jButton13, gridBagConstraints);

        jPanel37.add(jPanel23, "card9");

        jPanel30.add(jPanel37, java.awt.BorderLayout.LINE_END);

        jPanel44.setLayout(new java.awt.BorderLayout());

        CourseAdministrator_Table.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane5.setViewportView(CourseAdministrator_Table);

        jPanel44.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel30.add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel30, java.awt.BorderLayout.CENTER);

        jPanel53.add(jPanel25, "card2");

        jPanel29.setLayout(new java.awt.BorderLayout());

        jPanel31.setBackground(new java.awt.Color(0, 204, 204));
        jPanel31.setPreferredSize(new java.awt.Dimension(679, 30));

        CourseAdministrator_SearchButton.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_SearchButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_SearchButton.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_SearchButton.setText("Search");
        CourseAdministrator_SearchButton.setBorder(null);
        CourseAdministrator_SearchButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        CourseAdministrator_SearchBar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_SearchBar.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_SearchBar.setText("Student ID...");
        CourseAdministrator_SearchBar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        CourseAdministrator_SearchBar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CourseAdministrator_SearchBarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                CourseAdministrator_SearchBarFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
                jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel31Layout.createSequentialGroup()
                                .addContainerGap(250, Short.MAX_VALUE)
                                .addComponent(CourseAdministrator_SearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CourseAdministrator_SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(201, 201, 201))
        );
        jPanel31Layout.setVerticalGroup(
                jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(CourseAdministrator_SearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                .addComponent(CourseAdministrator_SearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel29.add(jPanel31, java.awt.BorderLayout.PAGE_START);

        jPanel32.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(200, 459));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(0, 204, 204));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        CourseAdministrator_GenerateMarksheet.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_GenerateMarksheet.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_GenerateMarksheet.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_GenerateMarksheet.setText("Generate");
        CourseAdministrator_GenerateMarksheet.setBorder(null);
        CourseAdministrator_GenerateMarksheet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 125;
        gridBagConstraints.ipady = 42;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel11.add(CourseAdministrator_GenerateMarksheet, gridBagConstraints);

        jPanel7.add(jPanel11, java.awt.BorderLayout.PAGE_END);

        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel59.setBackground(new java.awt.Color(0, 204, 204));

        CourseAdministrator_HaveRecit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Don't Have Resit", "Have Resit"}));
        CourseAdministrator_HaveRecit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
                jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel59Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(CourseAdministrator_HaveRecit, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel59Layout.setVerticalGroup(
                jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel59Layout.createSequentialGroup()
                                .addComponent(CourseAdministrator_HaveRecit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 36, Short.MAX_VALUE))
        );

        jPanel16.add(jPanel59, java.awt.BorderLayout.PAGE_END);

        jPanel60.setBackground(new java.awt.Color(0, 204, 204));
        jPanel60.setLayout(new java.awt.GridBagLayout());

        jTextField25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField25.setForeground(new java.awt.Color(204, 204, 204));
        jTextField25.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField25.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField25FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField25FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 180;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(98, 10, 0, 10);
        jPanel60.add(jTextField25, gridBagConstraints);

        CourseAdministrator_AssignNextMark.setBackground(new java.awt.Color(0, 0, 102));
        CourseAdministrator_AssignNextMark.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CourseAdministrator_AssignNextMark.setForeground(new java.awt.Color(204, 204, 204));
        CourseAdministrator_AssignNextMark.setText("Next");
        CourseAdministrator_AssignNextMark.setBorder(null);
        CourseAdministrator_AssignNextMark.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 87, 0);
        jPanel60.add(CourseAdministrator_AssignNextMark, gridBagConstraints);

        jPanel16.add(jPanel60, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel32.add(jPanel7, java.awt.BorderLayout.LINE_END);

        jPanel8.setLayout(new java.awt.BorderLayout());
        jTable9.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane9.setViewportView(jTable9);

        jPanel8.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel32.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel32, java.awt.BorderLayout.CENTER);

        jPanel53.add(jPanel29, "card3");

        jPanel27.add(jPanel53, java.awt.BorderLayout.CENTER);

        CourseAdministratorUI.add(jPanel27, java.awt.BorderLayout.CENTER);

        jPanel2.add(CourseAdministratorUI, "card3");

        InstructorUI.setLayout(new java.awt.BorderLayout());

        jPanel33.setBackground(new java.awt.Color(0, 204, 204));
        jPanel33.setPreferredSize(new java.awt.Dimension(679, 20));
        jPanel33.setLayout(new java.awt.CardLayout());

        jLabel12.setBackground(new java.awt.Color(0, 204, 204));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setForeground(new Color(200, 0, 0));
        jPanel33.add(jLabel12, "card2");

        InstructorUI.add(jPanel33, java.awt.BorderLayout.PAGE_START);

        jPanel34.setLayout(new java.awt.BorderLayout());

        jPanel35.setBackground(new java.awt.Color(0, 0, 102));
        jPanel35.setPreferredSize(new java.awt.Dimension(679, 35));
        jPanel35.setLayout(new java.awt.GridBagLayout());

        jButton10.setText("Assign Marks");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 84;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 223, 0, 1);
        jPanel35.add(jButton10, gridBagConstraints);

        jTextField24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField24.setForeground(new java.awt.Color(204, 204, 204));
        jTextField24.setText("Teacher ID...");
        jTextField24.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField24.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField24FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField24FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 34, 0, 0);
        jPanel35.add(jTextField24, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);

        jPanel34.add(jPanel35, java.awt.BorderLayout.PAGE_START);

        jPanel36.setLayout(new java.awt.BorderLayout());

        jPanel42.setPreferredSize(new java.awt.Dimension(350, 403));
        jPanel42.setLayout(new java.awt.BorderLayout());

        jTable2.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane2.setViewportView(jTable2);

        jPanel42.add(jScrollPane2, java.awt.BorderLayout.CENTER);


        jPanel36.add(jPanel42, java.awt.BorderLayout.LINE_START);

        jPanel43.setBackground(new java.awt.Color(0, 204, 204));
        jPanel43.setLayout(new java.awt.BorderLayout());

        jPanel47.setPreferredSize(new java.awt.Dimension(300, 403));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jTable8.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane8.setViewportView(jTable8);

        jPanel47.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jPanel43.add(jPanel47, java.awt.BorderLayout.LINE_START);

        jPanel54.setBackground(new java.awt.Color(0, 204, 204));
        jPanel54.setPreferredSize(new java.awt.Dimension(181, 250));
        jPanel54.setLayout(new java.awt.CardLayout());

        jPanel55.setBackground(new java.awt.Color(0, 204, 204));
        jPanel55.setPreferredSize(new java.awt.Dimension(181, 250));
        jPanel55.setLayout(new java.awt.GridBagLayout());

        jTextField23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField23.setForeground(new java.awt.Color(204, 204, 204));
        jTextField23.setText("Enter Full Mark...");
        jTextField23.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField23.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField23FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField23FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 161;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 8);
        jPanel55.add(jTextField23, gridBagConstraints);

        jButton26.setBackground(new java.awt.Color(0, 0, 102));
        jButton26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton26.setForeground(new java.awt.Color(204, 204, 204));
        jButton26.setText("Save");
        jButton26.setBorder(null);
        jButton26.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 265, 0);
        jPanel55.add(jButton26, gridBagConstraints);

        jTextField31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField31.setForeground(new java.awt.Color(204, 204, 204));
        jTextField31.setText("Module ID...");
        jTextField31.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 0, 153)));
        jTextField31.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField31FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField31FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 161;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(121, 10, 0, 8);
        jPanel55.add(jTextField31, gridBagConstraints);

        jPanel54.add(jPanel55, "card2");

        jPanel43.add(jPanel54, java.awt.BorderLayout.CENTER);

        jPanel36.add(jPanel43, java.awt.BorderLayout.CENTER);

        jPanel34.add(jPanel36, java.awt.BorderLayout.CENTER);

        InstructorUI.add(jPanel34, java.awt.BorderLayout.CENTER);

        jPanel2.add(InstructorUI, "card4");

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (StudentUI.isVisible() == false) {
            Students.setBackground(new Color(51,0,153));
        }

    }

    private void StudentsMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void StudentsMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (StudentUI.isVisible() == false) {
            Students.setBackground(new Color(0,0,102));
        }
    }

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        CourseAdministrator.setBackground(new Color(51,0,153));
        if (CourseAdministratorUI.isVisible() == false) {
            CourseAdministrator.setBackground(new Color(51,0,153));
        }
    }

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (CourseAdministratorUI.isVisible() == false) {
            CourseAdministrator.setBackground(new Color(0,0,102));
        }
    }

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (InstructorUI.isVisible() == false) {
            Instructor.setBackground(new Color(51,0,153));
        }
    }

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (InstructorUI.isVisible() == false) {
            Instructor.setBackground(new Color(0,0,102));
        }
    }

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        SelectedCourseID = "-1";
        SelectedModuleID = "-1";
        newCourse = new Course();
        newCourseGeneratedID = "";
        newCourseModuleGeneratedID = "";
        SelectedIDFromTable = "-1";
        NewCourseLevel = -1;
        newCourseSem = 1;
        newCourseIsOptional = false;
        SelectedLevel = 0;
        SelectedSem = 1;

        if(evt.getSource() == jLabel2) {
            StudentUI.setVisible(true);
            CourseAdministratorUI.setVisible(false);
            InstructorUI.setVisible(false);

            Students.setBackground(new Color(51,0,153));
            CourseAdministrator.setBackground(new Color(0,0,102));
            Instructor.setBackground(new Color(0,0,102));
            StudentCoursesPanel.setBackground(new Color(51,0,153));
            StudentInstructorPanel.setBackground(new Color(0,0,102));
        }
    }

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        SelectedCourseID = "-1";
        SelectedModuleID = "-1";
        newCourse = new Course();
        newCourseGeneratedID = "";
        newCourseModuleGeneratedID = "";
        SelectedIDFromTable = "-1";
        NewCourseLevel = -1;
        newCourseSem = 1;
        newCourseIsOptional = false;
        SelectedLevel = 0;
        SelectedSem = 1;

        if(evt.getSource() == jLabel3) {
            StudentUI.setVisible(false);
            CourseAdministratorUI.setVisible(true);
            InstructorUI.setVisible(false);

            Students.setBackground(new Color(0,0,102));
            CourseAdministrator.setBackground(new Color(51,0,153));
            Instructor.setBackground(new Color(0,0,102));
        }
    }

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        SelectedCourseID = "-1";
        SelectedModuleID = "-1";
        newCourse = new Course();
        newCourseGeneratedID = "";
        newCourseModuleGeneratedID = "";
        SelectedIDFromTable = "-1";
        NewCourseLevel = -1;
        newCourseSem = 1;
        newCourseIsOptional = false;
        SelectedLevel = 0;
        SelectedSem = 1;

        if(evt.getSource() == jLabel1) {
            jPanel54.setVisible(false);

            StudentUI.setVisible(false);
            CourseAdministratorUI.setVisible(false);
            InstructorUI.setVisible(true);

            Students.setBackground(new Color(0,0,102));
            CourseAdministrator.setBackground(new Color(0,0,102));
            Instructor.setBackground(new Color(51,0,153));
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        StudentModulesPanel.setVisible(false);
        StudentInstructorsPanel.setVisible(true);

        StudentCoursesPanel.setBackground(new Color(0,0,102));
        StudentInstructorPanel.setBackground(new Color(51,0,153));
    }

    private void StudentCoursesButtonMouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        StudentCoursesPanel.setBackground(new Color(51,0,153));
    }

    private void StudentCoursesButtonMouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (StudentModulesPanel.isVisible() == false) {
            StudentCoursesPanel.setBackground(new Color(0,0,102));
        }
    }

    private void StudentCoursesButtonMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        StudentModulesPanel.setVisible(true);
        StudentInstructorsPanel.setVisible(false);

        StudentCoursesPanel.setBackground(new Color(51,0,153));
        StudentInstructorPanel.setBackground(new Color(0,0,102));
    }

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (StudentInstructorsPanel.isVisible() == false) {
            StudentInstructorPanel.setBackground(new Color(51,0,153));
        }
    }

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (StudentInstructorsPanel.isVisible() == false) {
            StudentInstructorPanel.setBackground(new Color(0,0,102));
        }
    }

    private void CourseAdministrator_RenameCourseMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if(SelectedCourseID.equals("-1")) {
            JOptionPane.showMessageDialog(jPanel44, "please select a course first !");
        } else {
            jPanel38.setVisible(false);
            jPanel39.setVisible(false);
            jPanel40.setVisible(false);
            jPanel41.setVisible(true);
            jPanel45.setVisible(false);
            jPanel13.setVisible(false);
            jPanel21.setVisible(false);
            jPanel23.setVisible(false);
        }
    }

    private void CourseAdministrator_AddModuleMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        jPanel38.setVisible(false);
        jPanel39.setVisible(false);
        jPanel40.setVisible(false);
        jPanel41.setVisible(false);
        jPanel45.setVisible(true);
        jPanel13.setVisible(false);
        jPanel21.setVisible(false);
        jPanel23.setVisible(false);
    }

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) throws SQLException {
        // TODO add your handling code here:
        jPanel29.setVisible(true);
        jPanel25.setVisible(false);

        jPanel58.setBackground(new Color(0,0,102));
        jPanel56.setBackground(new Color(51,0,153));
        this.currentNoOfModule = 0;

        jTextField25.setText(this.DynamicText);
        JOptionPane.showMessageDialog(jPanel2, "getting ready wait for a second !");
        showAllMarksheet();
    }

    private void jPanel58MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        jPanel29.setVisible(false);
        jPanel25.setVisible(true);

        jPanel58.setBackground(new Color(51,0,153));
        jPanel56.setBackground(new Color(0,0,102));
    }

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) throws SQLException {
        // TODO add your handling code here:
        if (jPanel29.isVisible() == false) {
            jPanel56.setBackground(new Color(51,0,153));
        }
    }

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (jPanel29.isVisible() == false) {
            jPanel56.setBackground(new Color(0,0,102));
        }
    }

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if(jPanel25.isVisible() == false) {
            jPanel58.setBackground(new Color(51,0,153));
        }
    }

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if(jPanel25.isVisible() == false) {
            jPanel58.setBackground(new Color(0,0,102));
        }
    }

    private void Student_AddModulesActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void Student_OldStudentEnrollmentActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        jPanel54.setVisible(true);
    }

    private void jTextField15FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refresh();
        TextFieldPlaceHolder(jTextField15, "Course Name...");
    }

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField15FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refresh();
        TextFieldPlaceHolder(jTextField15, "Course Name...");
    }

    private void jTextField17FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refresh();
        TextFieldPlaceHolder(jTextField17, "Enter Module Name...");
    }

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField17FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refresh();
        TextFieldPlaceHolder(jTextField17, "Enter Module Name...");
    }

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField19FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField19, "Rename Course...");
    }

    private void jTextField19FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField19, "Rename Course...");
    }

    private void jTextField20FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField20, "Enter Module Name...");

    }

    private void jTextField20FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField20, "Enter Module Name...");
    }

    private void jTextField21FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField21, "sem, Optional");
    }

    private void jTextField21FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField21, "sem, Optional");
    }

    private void jTextField27FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refreshInstructors();
        SelectedIDFromTable = "-1";
        TextFieldPlaceHolder(jTextField27, "Enter First Name...");

    }

    private void jTextField27FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refreshInstructors();
        SelectedIDFromTable = "-1";
        TextFieldPlaceHolder(jTextField27, "Enter First Name...");
    }

    private void jTextField28FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refreshInstructors();
        SelectedIDFromTable = "-1";
        TextFieldPlaceHolder(jTextField28, "Enter Last Name...");
    }

    private void jTextField28FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        refreshInstructors();
        SelectedIDFromTable = "-1";
        TextFieldPlaceHolder(jTextField28, "Enter Last Name...");
    }

    private void jTextField29FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField29, "Enter Instructor ID...");
    }

    private void jTextField29FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField29, "Enter Instructor ID...");
    }

    private void jTextField30FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField30, "Rename Module...");
    }

    private void jTextField30FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField30, "Rename Module...");
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void CourseAdministrator_AddInstructorMouseClicked(java.awt.event.MouseEvent evt) throws SQLException {
        // TODO add your handling code here:
        refreshInstructors();

        newInstructor.InstructorID = DBS.getID("TP03A19", "instructors", "InstructorID");
        jLabel15.setText(newInstructor.InstructorID);
        DBS.previousLergest = -1;

        jPanel38.setVisible(false);
        jPanel39.setVisible(false);
        jPanel40.setVisible(false);
        jPanel41.setVisible(false);
        jPanel45.setVisible(false);
        jPanel13.setVisible(true);
        jPanel21.setVisible(false);
        jPanel23.setVisible(false);
    }

    private void CourseAdministrator_RemoveInstructorMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        refreshInstructors();

        jPanel38.setVisible(false);
        jPanel39.setVisible(false);
        jPanel40.setVisible(false);
        jPanel41.setVisible(false);
        jPanel45.setVisible(false);
        jPanel13.setVisible(false);
        jPanel21.setVisible(true);
        jPanel23.setVisible(false);
    }

    private void CourseAdministrator_RenameModuleMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if(SelectedModuleID.equals("-1")) {
            JOptionPane.showMessageDialog(jPanel44, "please select a Module First !");
        } else {
            Mode5.setRowCount(0);
            jPanel38.setVisible(false);
            jPanel39.setVisible(false);
            jPanel40.setVisible(false);
            jPanel41.setVisible(false);
            jPanel45.setVisible(false);
            jPanel13.setVisible(false);
            jPanel21.setVisible(false);
            jPanel23.setVisible(true);
        }
    }

    private void jTextField25FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField25, this.DynamicText);
    }

    private void jTextField25FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField25, this.DynamicText);
    }

    private void CourseAdministrator_SearchBarFocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(CourseAdministrator_SearchBar, "Student ID...");
    }

    private void CourseAdministrator_SearchBarFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(CourseAdministrator_SearchBar, "Student ID...");
    }

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField31FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField31, "Module ID...");
    }

    private void jTextField31FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField31, "Module ID...");
    }

    private void jTextField23FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField23, "Enter Full Mark...");
    }

    private void jTextField23FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField23, "Enter Full Mark...");
    }

    private void jTextField24FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField24, "Teacher ID...");
    }

    private void jTextField24FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField24, "Teacher ID...");
    }

    private void jTextField26FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField26, "Enter Your Student ID...");
    }

    private void jTextField26FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField26, "Enter Your Student ID...");
    }

    private void Student_NewStudentName1FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(Student_NewStudentName1, "Enter First Name...");
    }

    private void Student_NewStudentName1FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(Student_NewStudentName1, "Enter First Name...");
    }

    private void Student_NewStudentSurname1FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(Student_NewStudentSurname1, "Enter Last Name...");
    }

    private void Student_NewStudentSurname1FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(Student_NewStudentSurname1, "Enter Last Name...");
    }

    private void Student_NewStudentSurname1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void Student_newStudentEnrollment1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void CourseAdministrator_AddInstructorActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField32FocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField32, "Enter Your Student ID...");
    }

    private void jTextField32FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        TextFieldPlaceHolder(jTextField32, "Enter Your Student ID...");
    }

    private void Student_NewStudentName1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify
    private javax.swing.JPanel CourseAdministrator;
    private javax.swing.JPanel CourseAdministratorUI;
    private javax.swing.JButton CourseAdministrator_AddCourse;
    private javax.swing.JButton CourseAdministrator_AddInstructor;
    private javax.swing.JButton CourseAdministrator_AddModule;
    private javax.swing.JButton CourseAdministrator_AddNextModule;
    private javax.swing.JButton CourseAdministrator_AssignNextMark;
    private javax.swing.JButton CourseAdministrator_CancelCourse;
    private javax.swing.JComboBox<String> CourseAdministrator_CourseLavelsBox;
    private javax.swing.JButton CourseAdministrator_DeleteCourse;
    private javax.swing.JButton CourseAdministrator_GenerateMarksheet;
    private javax.swing.JComboBox<String> CourseAdministrator_HaveRecit;
    private javax.swing.JButton CourseAdministrator_RemoveInstructor;
    private javax.swing.JButton CourseAdministrator_RenameCourse;
    private javax.swing.JButton CourseAdministrator_RenameModule;
    private javax.swing.JButton CourseAdministrator_RenameModuleSave;
    private javax.swing.JButton CourseAdministrator_SaveCourse;
    private javax.swing.JTextField CourseAdministrator_SearchBar;
    private javax.swing.JButton CourseAdministrator_SearchButton;
    private javax.swing.JTable CourseAdministrator_Table;
    private javax.swing.JPanel CoursesNewStudent;
    private javax.swing.JPanel Instructor;
    private javax.swing.JPanel InstructorUI;
    private javax.swing.JLabel StudentCoursesButton;
    private javax.swing.JPanel StudentCoursesPanel;
    private javax.swing.JPanel StudentInstructorPanel;
    private javax.swing.JPanel StudentInstructorsPanel;
    private javax.swing.JPanel StudentModulesPanel;
    private javax.swing.JPanel StudentUI;
    private javax.swing.JButton Student_AddModules;
    private javax.swing.JComboBox<String> Student_CoursesBox;
    private javax.swing.JTable Student_InstructorTable;
    private javax.swing.JComboBox<String> Student_ModulesBox;
    private javax.swing.JTextField Student_NewStudentName1;
    private javax.swing.JTextField Student_NewStudentSurname1;
    public javax.swing.JPanel Student_OldStudent;
    private javax.swing.JButton Student_OldStudentEnrollment;
    private javax.swing.JComboBox<String> Student_OptionalModules;
    private javax.swing.JComboBox<String> Student_OptionalModules2;
    private javax.swing.JComboBox<String> Student_OptionalModules3;
    private javax.swing.JTable Student_StudentSelectedModulesTable;
    private javax.swing.JButton Student_newStudentEnrollment1;
    private javax.swing.JPanel Students;
    private javax.swing.JTable Studetnt_CourseTable;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton26;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    public javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    // End of variables declaration
}
