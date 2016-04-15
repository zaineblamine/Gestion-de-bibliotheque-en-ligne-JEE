package com.beans;

import static com.beans.User.getConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name = "admin")
@SessionScoped
public class Admin{

    private Long id;
    private String login;
    private String pwd;
    private String email;
    private String tel;
    private String name;
    private String adress;
        
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
    
    //-----------------login------------------------------------------------    
    
    public String login() {
        boolean result = authentificationVerif(getLogin(), getPwd());
        if (result) {
            // get Http Session and store username
            HttpSession session = Util.getSession();
            session.setAttribute("userName", getLogin());
 
            return "home_1";
        } else {
 
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Invalid Login!",
                    "Please Try Again!"));
            return "indexAdmin";
        }
    }
    
        public static boolean authentificationVerif(String user, String password){
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "select login, pwd from admin where login= ? and pwd= ? ");
            ps.setString(1, user);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) // found
            {
                System.out.println(rs.getString("login"));
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
     public String addAdmin() throws SQLException {
        boolean result =LoginDontExist(getLogin());
        if (result) {
             String req = "INSERT INTO admin VALUES (null, ? , ? , ? , ? , ? , ? );";
                  PreparedStatement stat=getConnection().prepareStatement(req);
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
        } else {
 
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Login exist!",
                    "Please Try Again!"));
 
            // invalidate session, and redirect to other pages
 
            //message = "Invalid Login. Please Try Again!";
        }
                    return "adminSpace";

    }
    
     public static boolean LoginDontExist(String login) {
        boolean b;
        Connection con = null;
        PreparedStatement ps1 = null;

        try {
            con =getConnection();
            ps1 = con.prepareStatement(
                    "select login from admin where login= ? ");
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
      return "indexAdmin";
   }
     public String goToBooks(){
           HttpSession session = Util.getSession();
            session.setAttribute("userName", getLogin());
            return "books";
     }   
    public Long getId() {
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
}