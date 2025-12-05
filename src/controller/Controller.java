/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import baza.DBBroker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Autor;
import model.Knjiga;
import model.User;
import model.Zanr;

/**
 *
 * @author ICHAGIC
 */
public class Controller {
    private DBBroker dbb;
    private List<Knjiga> listaKnjiga;
    private List<Autor> listaAutora;
    private List<User> listaUsera = new ArrayList<>();
    
    private static Controller instance;
    public static Controller getInstance(){
        if(instance == null){
            instance = new Controller();
        }
        return instance;
    }

    private Controller() {
        dbb = new DBBroker();
        
        User u1 = new User(1, "Boris", "boris");
        User u2 = new User(2, "Luka", "luka");
        User u3 = new User(3, "Uros", "uros");
        
        
        listaUsera.add(u1);
        listaUsera.add(u2);
        listaUsera.add(u3);
        //Ako dodje u lokalnoj memoriji da se cuva!!
        
        /*Autor autor1 = new Autor("Ivo", "Andric", 1892, "Biografija autora Ive Andrica bla bla");
        Autor autor2 = new Autor("Danilo", "Kis", 1935, "Biografija autora Danila Kisa bla bla");
        Autor autor3 = new Autor("Mesa", "Selimovic", 1910, "Biografija autora Mese Selimovica bla bla");
        
        Knjiga knjiga1 = new Knjiga("Na Drini cuprija", autor1, "1234567890", 1945, Zanr.DETEKTIVSKI);
        Knjiga knjiga2 = new Knjiga("Basta, pepeo", autor2, "0987654321", 1982, Zanr.ISTORIJSKI);
        Knjiga knjiga3 = new Knjiga("Tvrdjava", autor3, "12122334455", 1970, Zanr.NAUCNA_FANTASTIKA);
        
        listaKnjiga = new ArrayList<>();
        listaAutora = new ArrayList<>();
        
        listaKnjiga.add(knjiga1);
        listaKnjiga.add(knjiga2);
        listaKnjiga.add(knjiga3);
        
        listaAutora.add(autor1);
        listaAutora.add(autor2);
        listaAutora.add(autor3);*/
        
    }

    public List<Knjiga> getListaKnjiga() {
        return listaKnjiga;
    }

    public void setListaKnjiga(List<Knjiga> listaKnjiga) {
        this.listaKnjiga = listaKnjiga;
    }

    public List<Autor> getListaAutora() {
        return listaAutora;
    }

    public void setListaAutora(List<Autor> listaAutora) {
        this.listaAutora = listaAutora;
    }

    public List<User> getListaUsera() {
        return listaUsera;
    }

    public void setListaUsera(List<User> listaUsera) {
        this.listaUsera = listaUsera;
    }
    
    

    public void obrisiKnjigu(int id) {
        dbb.obrisiKnjigu(id);
        
        //listaKnjiga.remove(selektovaniRed);
    }

    public void dodajKnjigu(Knjiga novaKnjiga) {
        dbb.dodajKnjigu(novaKnjiga);
        //listaKnjiga.add(novaKnjiga);
    }

    public List<Knjiga> ucitajListuKnjigaIzBaze() {
        this.listaKnjiga = dbb.ucitajListuKnjigaIzBaze();
        return this.listaKnjiga;
    }

    public List<Autor> ucitajListuAutoraIzBaze() {
        return dbb.ucitajListuAutoraIzBaze();
    }

    public void azurirajKnjigu(Knjiga knjigazaizmenu) {
        dbb.azurirajKnjigu(knjigazaizmenu);
    }

    public boolean login1(String user, String pass) {
        for (User u : listaUsera) {
            if(u.getUsername().equals(user) && u.getPassword().equals(pass)){
                return true;
            }
        }
        return false;
    }

    public boolean login2(String user, String pass) {
        return dbb.login(user, pass);
    }

    public List<Knjiga> filtrirajListu(String autor, String naziv) {
        List<Knjiga> rezultat = new ArrayList<>();
        if(autor != null && naziv == null){
            for (Knjiga k : listaKnjiga) {
                String autorKnjige = k.getAutor().getIme() + " " + k.getAutor().getPrezime();
                if(autorKnjige.contains(autor)){
                    rezultat.add(k);
                }
            }
        }
        if(autor == null && naziv !=null){
            for (Knjiga k : listaKnjiga) {
                if(k.getNaslov().contains(naziv)){
                    rezultat.add(k);
                }
            }
        }
        if(autor != null && naziv !=null){
            for (Knjiga k : listaKnjiga) {
                String autorKnjige = k.getAutor().getIme() + " " + k.getAutor().getPrezime();
                if(autorKnjige.contains(autor) && k.getNaslov().contains(naziv)){
                    rezultat.add(k);
                }
            }
        }
        //2. nacin
        /*List<Knjiga> rezultat2 = new ArrayList<>();
        rezultat2 = listaKnjiga.stream()
                .filter(k -> (naziv!=null && k.getNaslov().contains(naziv)) &&
                        (autor != null && (k.getAutor().getIme() + " " + k.getAutor().getPrezime()).contains(autor)))
                .collect(Collectors.toList());*/
        
        
        
        return rezultat;
    }

    public List<Knjiga> filtrirajListu2(String autor, String naziv) {
        return dbb.filtrirajListu2(autor, naziv);
    }

    
}
