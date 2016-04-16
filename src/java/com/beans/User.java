package com.beans;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "user")
@SessionScoped
public class User {

    public static final String PROP_MESSAGEERR = "messageErr";

    private Long id;
    private String login;
    private String pwd;
    private String email;
    private String tel;
    private String name;
    private String adress;
    private String state;
    private String messageErr = "";

    //--------------------database connection-------------------------------------------------
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bibliothèque",
                    "root", "mysql");
            return con;
        } catch (Exception ex) {
            System.out.println("Database.getConnection() Error -->" + ex.getMessage());
            return null;
        }
    }

    public static void close(Connection con) {
        try {
            con.close();
        } catch (Exception ex) {
        }
    }
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    //-----------------login------------------------------------------------    
    public String login() {
        boolean result = authentificationVerif(getLogin(), getPwd());
        if (result) {
            // get Http Session and store username
            HttpSession session = Util.getSession();
            session.setAttribute("userName", getLogin());

            return "home";
        } else {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Invalid Login!",
                            "Please Try Again!"));
            return "index";
        }
    }

    public static boolean authentificationVerif(String user, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "select * from users where login= ? and pwd= ? ");
            
            ps.setString(1, user);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) // found
            {
                /*int id=rs.getInt("id");
                String login=rs.getString("login");
                System.out.println(rs.getString("login"));
                HttpSession session= Util.getSession();*/                
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error in login() -->" + ex.getMessage());
            return false;
        } finally {
            close(con);
        }
    }
    //-------------------register-----------------------------------------------------

    public String registerProject() {
        return "register";
    }

public String addUser() throws SQLException {
        boolean result = LoginDontExist(getLogin());
        if (result) {
            String req = "INSERT INTO users VALUES (null, ? , ? , ? , ? , ? , ?,'disabled' );";
            PreparedStatement stat = getConnection().prepareStatement(req);
            try {
                stat.setString(1, getLogin());
                stat.setString(2, getPwd());
                stat.setString(3, getEmail());
                stat.setString(4, getTel());
                stat.setString(5, getName());
                stat.setString(6, getAdress());

                stat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpSession session = Util.getSession();
            session.setAttribute("userName", getLogin());
            return "adminApproval";
        } else {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Login exist!!",
                            "Please Try Again!"));

            // invalidate session, and redirect to other pages
            setMessageErr("Invalid Login. Please Try Again!");
            return "index";
        }
    }

    public static boolean LoginDontExist(String login) {
        boolean b;
        Connection con = null;
        PreparedStatement ps1 = null;

        try {
            con = getConnection();
            ps1 = con.prepareStatement(
                    "select login from users where login= ? ");
            ps1.setString(1, login);

            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) // found
            {
                System.out.println(rs1.getString("login"));
                b = false;
            } else {
                b = true;
            }
            return b;

        } catch (Exception ex) {
            System.out.println("Error in register() -->" + ex.getMessage());
            return false;
        } finally {
            close(con);
        }

    }
    //-------------------logout----  

    public String logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "index";
    }

    public String goToBooks() {
        HttpSession session = Util.getSession();
        session.setAttribute("userName", getLogin());
        return "books";
    }

      public Long getId() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
    try {
            con = getConnection();
            ps = con.prepareStatement("select id from users where login='"+getLogin()+"'");
            ps.setLong(1,id);
                    }
        catch (Exception ex) {
            System.out.println("Database.getConnection() Error -->" + ex.getMessage());
            return null;
        }
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the adress
     */
    public String getAdress() {
        return adress;
    }

    /**
     * @param adress the adress to set
     */
    public void setAdress(String adress) {
        this.adress = adress;
    }

    /**
     * @return the messageErr
     */
    public String getMessageErr() {
        return messageErr;
    }

    /**
     * @param messageErr the messageErr to set
     */
      public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }
    
    public void setMessageErr(String messageErr) {
        java.lang.String oldMessageErr = this.messageErr;
        this.messageErr = messageErr;
        propertyChangeSupport.firePropertyChange(PROP_MESSAGEERR, oldMessageErr, messageErr);
    }

    public static ArrayList<CustomerBean> getCustomer() {
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("select * from book");
            ArrayList<CustomerBean> al = new ArrayList<CustomerBean>();
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                CustomerBean e = new CustomerBean();
                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setDescription(rs.getString("description"));
                e.setLanguage(rs.getString("language"));
                e.setNb(rs.getInt("numberDisp"));
                al.add(e);
                found = true;
            }
            rs.close();
            if (found) {
                return al;
            } else {
                return null; // no entires found
            }
        } catch (Exception e) {
            System.out.println("Error In getCustomer() -->" + e.getMessage());
            return (null);
        }
    }
    public static void reserver(int id,int nb) throws SQLException{
       try{
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("update book set numberDisp='"+(nb-1)+"' where id='"+id+"';");
      // PrepareStatement ps=con.prepareStatement("insert into panier values")
       int rs = ps.executeUpdate();
         }
         catch (Exception e) {
            System.out.println("Error In getCustomer() -->" + e.getMessage());
        }
    }
    public static void enable(int id) throws SQLException{
       try{
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("update users set state='enabled' where id='"+id+"';");
      //PreparedStatement ps=con.prepareStatement("insert into users values('5','mejdi','mejdi','mejdi@gmail.com','98564123','mejdi','chebbaa','disabled');");
       int rs = ps.executeUpdate();
         }
         catch (Exception e) {
            System.out.println("Error In getCustomer() -->" + e.getMessage());
        }
    }
    
      public static void disable(int id) throws SQLException{
       try{
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("update users set state='disabled' where id='"+id+"';");
       int rs = ps.executeUpdate();
         }
         catch (Exception e) {
            System.out.println("Error In getCustomer() -->" + e.getMessage());
        }
    }
     
    public static ArrayList<WaitlistBean> getWaitlist() {
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("select * from users");
            ArrayList<WaitlistBean> al = new ArrayList<WaitlistBean>();
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                WaitlistBean e = new WaitlistBean();
                e.setId(rs.getInt("id"));
                e.setLogin(rs.getString("login"));
                e.setEmail(rs.getString("email"));
                e.setName(rs.getString("name"));
                e.setAdress(rs.getString("adress"));
                e.setState(rs.getString("state"));
                al.add(e);
                found = true;
            }
            rs.close();
            if (found) {
                return al;
            } else {
                return null; // no entries found
            }
        } catch (Exception e) {
            System.out.println("Error In getRequest() -->" + e.getMessage());
            return (null);
        }
    }
}