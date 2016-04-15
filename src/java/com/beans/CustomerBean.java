package com.beans;
import static com.beans.Admin.getConnection;
import com.beans.User;
import static com.beans.User.getConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name = "customer")
@SessionScoped
public class CustomerBean {
    private String title, description, language, category;
    private int id,nb;
    
// insert getter setter here
public ArrayList<CustomerBean> getMessages() {
        return User.getCustomer();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void reserver(CustomerBean o) throws SQLException{
        if(o.getNb()>0){
            User.reserver(o.getId(), o.getNb());
            
        }
    }
    public int getNb() {
        return nb;
    }
    public void setNb(int nb) {
        this.nb = nb;
    }
      public void addBook() throws SQLException{
        Connection con =Admin.getConnection();
         String req = "INSERT INTO book VALUES (null, ? , ? , ? , ? , ? );";
            PreparedStatement stat = con.prepareStatement(req);
            try {
                stat.setString(1, getTitle());
                stat.setString(2, getDescription());
                stat.setString(3, getLanguage());
                stat.setString(4, getCategory());
                stat.setInt(5, getNb());
                stat.executeUpdate();
                System.out.println("-------------------------------------------");
                System.out.println(getNb());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
