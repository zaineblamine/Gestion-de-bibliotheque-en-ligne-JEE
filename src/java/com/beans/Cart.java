package com.beans;

import java.sql.SQLException;
import java.util.ArrayList;


public class Cart {
    private int idBook,idUser;
    private String title;
            
    public Cart() {
        
    }
     public ArrayList<Cart> getMessages() {
        return User.getCart();
    }

    /**
     * @return the idBook
     */
    public int getIdBook() {
        return idBook;
    }

    /**
     * @param idBook the idBook to set
     */
    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    /**
     * @return the idUser
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * @param idUser the idUser to set
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
       public void addToCart(Cart o) throws SQLException{
        
            User.reserver(o.getIdBook(), o.getIdUser());
    }
}
