/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;

import java.sql.PreparedStatement;
import java.sql.Statement;//Ovo vrv moram sam
import java.sql.ResultSet;//isto
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Autor;
import model.Knjiga;
import model.Zanr;

/**
 *
 * @author ICHAGIC
 */
public class DBBroker {

    public List<Knjiga> ucitajListuKnjigaIzBaze() {
        List<Knjiga> lista = new ArrayList<>();
        try {
            String upit = "SELECT * FROM knjiga k JOIN autor a ON k.autorid = a.id";
            Statement st = Konekcija.getInstance().getConnection().createStatement();//Omogucava da izvrsimo sql upit
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){//Dokle god postoji resulSet.next
                int id = rs.getInt("k.id");//U zagradi naziv kolone
                String naslov = rs.getString("k.naslov");
                int godinaIzdanja = rs.getInt("k.godinaIzdanja");
                String ISBN = rs.getString("k.ISBN");
                String zanr = rs.getString("k.zanr");
                Zanr z = Zanr.valueOf(zanr);//pretvorili String u enum
                
                int idA = rs.getInt("a.id");
                String ime = rs.getString("a.ime");
                String prezime = rs.getString("a.prezime");
                String biografija = rs.getString("a.biografija");
                int godinaRodj = rs.getInt("a.godinaRodjenja");
                Autor a = new Autor(idA, ime, prezime, godinaRodj, biografija);
                Knjiga k = new Knjiga(id, naslov, a, ISBN, godinaIzdanja, z);
                lista.add(k);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    public List<Autor> ucitajListuAutoraIzBaze() {
        List<Autor> lista = new ArrayList<>();
        try {
            String upit = "SELECT * FROM autor a";
            Statement st = Konekcija.getInstance().getConnection().createStatement();//Omogucava da izvrsimo sql upit
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){//Dokle god postoji resulSet.next
                int idA = rs.getInt("a.id");
                String ime = rs.getString("a.ime");
                String prezime = rs.getString("a.prezime");
                String biografija = rs.getString("a.biografija");
                int godinaRodj = rs.getInt("a.godinaRodjenja");
                Autor a = new Autor(idA, ime, prezime, godinaRodj, biografija);
                lista.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    public void obrisiKnjigu(int id) {
        try {
            String upit = "DELETE FROM knjiga WHERE id=?";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setInt(1, id);//Ovom linijom zamenjujem ?
            ps.executeUpdate();
            Konekcija.getInstance().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dodajKnjigu(Knjiga novaKnjiga) {
        try {
            String upit = "INSERT INTO knjiga(id, naslov, autorid, ISBN, godinaIzdanja, zanr)"
                    + " VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setInt(1, novaKnjiga.getId());//1 se odnosi na prvi ?
            ps.setString(2, novaKnjiga.getNaslov());//2 na drugi ? i tako dalje. Zato menjam prvi parametar u zagradi
            ps.setInt(3, novaKnjiga.getAutor().getId());
            ps.setString(4, novaKnjiga.getISBN());
            ps.setInt(5, novaKnjiga.getGodinaIzdanja());
            ps.setString(6, String.valueOf(novaKnjiga.getZanr()));
            
            ps.executeUpdate();
            Konekcija.getInstance().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void azurirajKnjigu(Knjiga knjigazaizmenu) {
        try {
            String upit = "UPDATE knjiga SET naslov =?, autorid=?, godinaIzdanja=?, zanr=? WHERE id=?";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, knjigazaizmenu.getNaslov());
            ps.setInt(2, knjigazaizmenu.getAutor().getId());
            ps.setInt(3, knjigazaizmenu.getGodinaIzdanja());
            ps.setString(4, String.valueOf(knjigazaizmenu.getZanr()));
            ps.setInt(5, knjigazaizmenu.getId());
            
            ps.executeUpdate();
            Konekcija.getInstance().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean login(String user, String pass) {
        try {
            String upit = "SELECT * FROM user WHERE username=? AND PASSWORD=?";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){//Ako postoji bar jedan red u tabeli to znaci da se korisnik ulogovao
                return true;
            }else{
                return false;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Knjiga> filtrirajListu2(String autor, String naziv) {//autor = "Ivo Andric"
        List<Knjiga> lista = new ArrayList<>();
        try {
        String upit = "SELECT * FROM knjiga k JOIN autor a ON k.autorid = a.id WHERE 1=1";//naslov = ? AND ime = ? AND prezime = ?";
        if(naziv!=null){
            upit +=" AND naslov ='" + naziv + "'";
        }
        if(autor!=null){
            String[] podaci = autor.split(" ");//["Ivo", "Andric"]
            if(podaci[0] != null){
                upit +=" AND ime ='" + podaci[0] + "'";
            }
            if(podaci.length >= 2 && podaci[1] != null){ 
                upit +=" AND prezime ='" + podaci[1] + "'";
            }
        }

        Statement st = Konekcija.getInstance().getConnection().createStatement();
        ResultSet rs = st.executeQuery(upit);
            while(rs.next()){
                int id = rs.getInt("k.id");//U zagradi naziv kolone
                String naslov = rs.getString("k.naslov");
                int godinaIzdanja = rs.getInt("k.godinaIzdanja");
                String ISBN = rs.getString("k.ISBN");
                String zanr = rs.getString("k.zanr");
                Zanr z = Zanr.valueOf(zanr);//pretvorili String u enum
                
                int idA = rs.getInt("a.id");
                String ime = rs.getString("a.ime");
                String prezime = rs.getString("a.prezime");
                String biografija = rs.getString("a.biografija");
                int godinaRodj = rs.getInt("a.godinaRodjenja");
                Autor a = new Autor(idA, ime, prezime, godinaRodj, biografija);
                Knjiga k = new Knjiga(id, naslov, a, ISBN, godinaIzdanja, z);
                lista.add(k);
            }
        }catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}