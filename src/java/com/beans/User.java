package com.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name = "user")
@SessionScoped
public class User{

    private Long id;
    private String login;
    private String pwd;
    private String email;
    private String tel;
    private String name;
    private String adress;
        
    private String uname;
    private String password;
    private String cpassword;
    private String message;
    //-- getters and setters
    
    /**
     * @return the uname
     */
    public String getUname() {
        return uname;
    }

    /**
     * @param uname the uname to set
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the cpassword
     */
    public String getCpassword() {
        return cpassword;
    }

    /**
     * @param cpassword the cpassword to set
     */
    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
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
    
    public String loginProject() {
        boolean result = authentificationVerif(getLogin(), getPwd());
        if (result) {
            // get Http Session and store username
            HttpSession session = Util.getSession();
            session.setAttribute("userName", getUname());
 
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
    
        public static boolean authentificationVerif(String user, String password){
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "select login, pwd from users where login= ? and pwd= ? ");
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

               public String addUser() throws SQLException { // TESTED WITH SUCCESS
        /*if (alreadyExist(getLogin())) {
            return "home.xhtml?inscription=false";
        }*/
        String req = "INSERT INTO users VALUES (null, ? , ? , ? , ? , ? , ? );";
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

        return "home";
    }
     public String registerProject(){
        return "register";
    }
     public String register() throws SQLException {
        boolean result =LoginAlreadyExist(getLogin());
        if (result) {
             String req = "INSERT INTO users VALUES (null, ? , ? , ? , ? , ? , ? );";
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
 
            return "home";
        } else {
 
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Incorrect!",
                    "Please Try Again!"));
 
            // invalidate session, and redirect to other pages
 
            //message = "Invalid Login. Please Try Again!";
            return "register";
        }
    }
    
     public static boolean LoginAlreadyExist(String login) {
        boolean b;
        Connection con = null;
        PreparedStatement ps1 = null;

        try {
            con =getConnection();
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
      return "login";
   }

    /**
     * @return the id
     */
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