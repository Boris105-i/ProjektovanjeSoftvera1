/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ICHAGIC
 */
public class Konekcija {
    //koristimo singlton da bismo obezbedili da postoji samo jedan tok komunikacije izmedju baze i nase aplikacije
    private static Konekcija instance;
    private Connection connection;
    private Konekcija(){
        try {
            String url = "jdbc:mysql://localhost:3306/ps_sql_1";
            connection = DriverManager.getConnection(url, "root", "");
            connection.setAutoCommit(false);
        }catch (SQLException ex) {
            Logger.getLogger(Konekcija.class.getName()).log(Level.SEVERE, null, ex);//preko try catch bloka obuhvatam gresku-3. opcija
        }
    }    
public static Konekcija getInstance(){
        if(instance == null){
            instance = new Konekcija();
        }
        return instance;
    }
    public Connection getConnection(){
        return connection;
    }
}
