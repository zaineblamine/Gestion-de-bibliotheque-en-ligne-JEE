/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "books")
@SessionScoped
public class Books implements Serializable {

    private HttpSession sess;
    private int id;
    private String title;
    private String writer;
    private String type;
    private String description;
    private String language;
    private String path;
    private int quantity;

    public Books(int id, String title, String writer, String type, String description, String language, String path, int quantity) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.type = type;
        this.description = description;
        this.language = language;
        this.path = path;
        this.quantity = quantity;

    }
public Books(){}
    private UploadedFile file;
    private String Disponble;
    private int jours;
    private ArrayList<Books> listereservation;

    /*public Books(int ide, int id, Date d1, Date d2) {
        this.ide = ide;
        this.id = id;
        this.d1 = d1;
        this.d2 = d2;
    }*/
 /*
    public ArrayList<Books> getListereservation() {
     String sql ="SELECT r.did,r.id,login,nom,prenom,cin,email,titre,auteur,dateres,datelimite FROM books d,demandereservation r,etudiant e where d.id=r.did and e.id=r.id";
        Statement stmt;
     
        try {
            stmt = getConnexion().createStatement();
            ResultSet rs =stmt.executeQuery(sql);
            ArrayList<Books> listdemandes =new ArrayList<>();
            while (rs.next()) {
                listdemandes.add(new Books(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8),rs.getString(9), rs.getDate(10),rs.getDate(11)));
            }
            return listdemandes;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }   
    }*/
    public void setListereservation(ArrayList<Books> listereservation) {
        this.listereservation = listereservation;
    }

    private ArrayList<String> listcategorie;
    private ArrayList<Books> listdocuments;
    private ArrayList<Books> listreser;
    private static Connection connexion = connect();

    public static Connection getConnexion() {
        return connexion;
    }

    public static void setConnexion(Connection aConnexion) {
        connexion = aConnexion;
    }

    /* public ArrayList<Books> listdocuments(){
        String sql ="SELECT * FROM books";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();
            ResultSet rs =stmt.executeQuery(sql);
            ArrayList<Books> listedocuments =new ArrayList<>();
            while (rs.next()) {                
                listedocuments.add(new Books(Integer.parseInt(rs.getString(1)), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5) ,rs.getString(6),rs.getString(7),Integer.parseInt(rs.getString(8)),Integer.parseInt(rs.getString(9))));
            }
            return listedocuments;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        
    }*/
    public ArrayList<String> getListcategorie() {
        String sql = "SELECT categorie FROM book";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> listedocuments = new ArrayList<>();
            while (rs.next()) {
                listedocuments.add(rs.getString(1));
            }
            return listedocuments;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void setListcategorie(ArrayList<String> listcategorie) {
        this.listcategorie = listcategorie;
    }

    public boolean veriftitre(String Titre) {
        String sql = "SELECT titre FROM book";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (rs.getString(1).equals(Titre)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    public String ajouterdocument() throws IOException {
        FileOutputStream output;
        try (InputStream input = file.getInputstream()) {

            output = new FileOutputStream("");

            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
            input.close();
            output.close();
        }
        String path2 = "img/" + getTitle() + ".jpg";

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (!veriftitre(getTitle())) {
            String sql = "insert into books (title,writer,type,description,langage,quantity) values('" + getTitle() + "','" + getWriter() + "','" + getType() + "','" + getDescription() + "','" + getLanguage() + "','" + getQuantity() + "')";
            Statement stmt;
            try {
                stmt = getConnexion().createStatement();
                stmt.executeUpdate(sql);
                facesContext.addMessage("doc", new FacesMessage("success"));
                return "documentsuccess";
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                facesContext.addMessage("doc", new FacesMessage("echoue"));
                return "documentechoue";
            }

        } else {
            facesContext.addMessage("doc", new FacesMessage("Titre existe deja"));

            return "documentechoue";

        }

    }

    public String supprimeredocument() {
        try {
            String sql = "delete from books where id like '" + getId() + "';";

            Statement stmt = getConnexion().createStatement();
            stmt.executeUpdate(sql);
            return "supprimersucc";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "supprimerech";
        }
    }

    /**
     * @return the listdocuments
     */
    /* public ArrayList<Books> getListdocuments() throws IOException {
            String sql ="SELECT id,titre,auteur,description,path,quantite FROM books order by date DESC LIMIT 6";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();
            ResultSet rs =stmt.executeQuery(sql);
            ArrayList<Books> listedocuments =new ArrayList();
            
			  
            while (rs.next()) {  
                if(rs.getInt(6)==0){
                 listedocuments.add(new Books(Integer.parseInt(rs.getString(1)),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),"Non Disponible"));
            }
                else{
                    listedocuments.add(new Books(Integer.parseInt(rs.getString(1)),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),"Disponible"));
                
                
                }
            }
            
            return listedocuments;
    }
         catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }}*/
    public String getDisponble() {
        return Disponble;
    }

    public void setDisponble(String Disponble) {
        this.Disponble = Disponble;
    }

    /*public Books(int id, String title, String auteur,  String description, String path,String disponible) {
        this.id = id;
        this.title = title;
        this.auteur = auteur;
        this.categorie = categorie;
        this.description = description;
        this.path = path;
        this.Disponble=disponible;
    }

    public Books(String title, String auteur, String categorie, String scategorie) {
        this.title = title;
        this.auteur = auteur;
        this.categorie = categorie;
        this.scategorie = scategorie;
    }

    public Books(int id, String title, String auteur) {
        this.id = id;
        this.title = title;
        this.auteur = auteur;
    }

    
    public void setListdocuments(ArrayList<books> listdocuments) {
        this.listdocuments = listdocuments;
    }
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String recherche() {
        return "recherche";
    }

    /**
     * @return the content
     * @throws java.io.IOException
     */
    /*ublic ArrayList<Books> getRecherche() throws IOException {
        try {
            String sql = "";
            if ((getWriter().equals("")) && (getTitle().equals(""))) {
                sql = "SELECT id,title,writer,description,path,quantity FROM books where type='" + getType() + "'";
            } else {
                if (getWriter().equals("") == false && getTitle().equals("") == false) {
                    sql = "SELECT id,title,writer,description,path,quantity FROM books where writer='%" + getWriter() + "%' and title like '%" + getTitle() + "%' and Type='" + getType() + "'";

                } else {
                    if (getAuteur().equals("") == false && getTitle().equals("") == false) {
                        sql = "SELECT id,title,writer,description,path,quantity FROM books where writer='%" + getWriter() + "%'and title like '%" + getTitle() + "%'and Type='" + getType() +"'";

                    } else {
                        if (getAuteur().equals("") == false) {
                            sql = "SELECT id,title,writer,description,path,quantity FROM books where writer='%" + getWriter() + "%'and type='" + getType() + "'";
                        } else {
                            if (getTitle().equals("") == false) {
                                sql = "SELECT id,title,writer,description,path,quantity FROM books where title like '%" + getTitle() + "%'and type='" + getType() + "'";

                            } 
                            }
                        }
                    }
                }
            
            System.out.print(getAuteur() + getTitle() + getAnnees());
            System.out.print(sql);

            Statement stmt;

            stmt = getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Books> listedocuments = new ArrayList();

            while (rs.next()) {
                System.out.print(rs.getInt(6));
                if (rs.getInt(6) == 0) {
                    listedocuments.add(new Books(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), "Non Disponible"));

                } else {
                    listedocuments.add(new Books(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), "Disponible"));

                }
            }

            return listedocuments;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }
*/
    public String reserver() {

        String sql = "select titre,auteur,description from books where id='" + getId() + "'";
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        sess = req.getSession();

        Statement stmt;
        try {
            stmt = connexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println(getId());
                setTitle(rs.getString(1));
                setWriter(rs.getString(2));
                setDescription(rs.getString(3));
                sess.setAttribute("idb", getId());
            }
            return "reserver";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "document";
        }

    }

/*    public String ajoutdemandereservation(int id, int idb) {

        String sql = "insert into demandereservation (did,id,dateres,datelimite) values('" + idb + "','" + id + "',NOW(),ADDDATE(NOW(),'" + getJours() + "'))";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();
            stmt.executeUpdate(sql);
            return "document";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "reserver";
        }

    }

    public String ajoutreservation() {

        String sql = "insert into reservation (did,id,dateres,datelimite) values('" + getId() + "','" + getIde() + "','" + getD1() + "','" + getD2() + "')";
        String sql2 = "delete from demandereservation where did='" + getId() + "'and id='" + getIde() + "' and dateres='" + getD1() + "'and datelimite='" + getD2() + "'";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            return "administration";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "administration";
        }

    }

    public String suprimerservation() {

        System.out.print(getId() + getIde());
        String sql2 = "delete from  demandereservation where did='" + getId() + "'and id='" + getIde() + "' and dateres='" + getD1() + "'and datelimite='" + getD2() + "'";
        Statement stmt;
        try {
            stmt = getConnexion().createStatement();

            stmt.executeUpdate(sql2);
            return "administration";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "administration";
        }

    }

    public ArrayList<Books> getListreser() {
        return listreser;
    }

    public void setListreser(ArrayList<Books> listreser) {
        this.listreser = listreser;
    }

    public HttpSession getSess() {
        return sess;
    }

    public void setSess(HttpSession sess) {
        this.sess = sess;
    }
*/
    public static Connection connect() {
        Connection conn;
        String url = "jdbc:mysql://localhost/projet";
        String name = "root";
        String pwd = "mysql";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, name, pwd);
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the writer
     */
    public String getWriter() {
        return writer;
    }

    /**
     * @param writer the writer to set
     */
    public void setWriter(String writer) {
        this.writer = writer;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}