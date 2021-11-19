package attendancemanagement;

import java.sql.*;
import java.util.*;

public class AttendanceManagement {
    
    String url = "jdbc:mysql://localhost:3306/project";
    String username = "root";
    String password = "shanu";
    String[] subjects = new String[3];
    int[][] total_attendance = new int[3][3];
    String[] lastStatus = new String[3];
    List<String> stu_name = new ArrayList<>();
    List<Integer> pre_att = new ArrayList<>();
    List<Integer> stu_id = new ArrayList<>();
    int totalClasses;
    static String u_name;
    static int u_id;
    public boolean validate(String tab_name,int id,String user_name,String pass_word){
        String query = "select username,password from "+tab_name+" where id = " + id;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            String user = rs.getString(1);
            String pass = rs.getString(2);
            st.close();
            con.close();
            if(user.equals(user_name) && pass.equals(pass_word)){
                u_name = user_name;
                u_id = id;
                return true;
            }
        }
        catch(ClassNotFoundException | SQLException ex){
            System.out.println(ex);
            return false;
        }
        return false;
    }
    public boolean Create(String tab_name,int stu_id,String user_name,String pass_word,String branch){
        String query = "insert into project."+tab_name+" values ("+stu_id+",'"+user_name+"','"+pass_word+"')";
        if(tab_name.equals("Student")){
            query = "insert into project."+tab_name+" values ("+stu_id+",'"+user_name+"','"+pass_word+"','"+branch+"')";
        }
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            int row_added = st.executeUpdate(query);
            st.close();
            con.close();
            if(row_added==1){
                return true;
            }
        }
        catch(ClassNotFoundException | SQLException ex){
            System.out.println(ex);
            return false;
        }
        return false;
    }
    public void getSubjects(int id){
        String query = "select * from project.subjects where id = " +id;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            int i=0;
            while(rs.next()){
                subjects[i] = rs.getString(2);
                total_attendance[i][0] = rs.getInt(3);
                total_attendance[i][1] = rs.getInt(4);
                lastStatus[i] = rs.getString(5);
                i++;
            }
            st.close();
            con.close();
        }
        catch(ClassNotFoundException | SQLException ex){
            System.out.println(ex);
        }
    }
    public void getStudentsname(String branch,String subname){
        stu_name.clear();
        String query = "select username,attendClasses,totalclasses,project.student.id from project.student,project.subjects where branch = '"+branch+"' and courses = '"+subname+"' and project.student.id=project.subjects.id";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                stu_name.add(rs.getString(1));
                pre_att.add(rs.getInt(2));
                totalClasses = rs.getInt(3);
                stu_id.add(rs.getInt(4));
            }
            st.close();
            con.close();
        }
        catch(ClassNotFoundException | SQLException ex){
            System.out.println(ex);
        }
    }
    public void markAttendance(List<Boolean> list,String sub){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            for(int i=0;i<list.size();i++){
                int j = pre_att.get(i); 
                String status = "Absent";
                if(list.get(i)){
                    j++;
                    status = "Present";
                }
                
                String query = "update project.subjects set attendClasses ="+j+" ,totalclasses = "+(totalClasses + 1)+" ,last_Status = '"+status+"'where courses ='"+sub +"' and id = "+stu_id.get(i);
                System.out.println(query);
                st.executeUpdate(query);
            }
            st.close();
            con.close();
        }
        catch(ClassNotFoundException | SQLException ex){
            System.out.println(ex);
        }
        System.out.println("Attendance Marked!!");
    }
}
