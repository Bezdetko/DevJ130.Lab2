/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devj130.lab2;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bezdetk0@mail.ru
 */
public class DevJ130Lab2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Settings settings = new Settings();
        System.out.println("url="+settings.getValue(Settings.URL));
        System.out.println("user="+settings.getValue(Settings.URL));
        System.out.println("psw="+settings.getValue(Settings.PASSWORD));
        
        for (Authors a: Authors.getAuthors()){
            System.out.println(a);
        }
        
        for (Documents d: Documents.getDocuments()){
            System.out.println(d);
        }        
        
        DbServer dbServer = new DbServer();
         
        
        
        try {
             System.out.println("Добавлен новый автор (true) или обновлен существующию (false)? :"+dbServer.addAuthor(new Authors(4, "Chuck Palahniuk", null)));
             System.out.println("Добавлен новый автор (true) или обновлен существующию (false)? :"+dbServer.addAuthor(new Authors(0, "Chuck Palahniuk", "comments")));
             System.out.println("Добавлен новый автор (true) или обновлен существующию (false)? :" + dbServer.addAuthor(new Authors(5, "Me", null)));
             System.out.println("Добавлен новый автор (true) или обновлен существующию (false)? :" + dbServer.addAuthor(new Authors(6, "Vasya", null)));                
             System.out.println("Добавлен новый документ (true) или обновлен существующию (false)? :" + dbServer.addDocument(new Documents(5, "Choke", "random text", new Date(101, 0, 22) , Authors.getAuthors().get(3).getAuthor_id()), Authors.getAuthors().get(3)));
             System.out.println("Добавлен новый документ(true) или обновлен существующию (false)? :" + dbServer.addDocument(new Documents(6, "My doc", "sample", Authors.getAuthors().get(4).getAuthor_id()), Authors.getAuthors().get(4)));
             System.out.println("Добавлен новый документ(true) или обновлен существующию (false)? :" + dbServer.addDocument(new Documents(7, "Vasya doc", "simple text", Authors.getAuthors().get(5).getAuthor_id()), Authors.getAuthors().get(5)));
             System.out.println("Добавлен новый документ(true) или обновлен существующию (false)? :" + dbServer.addDocument(new Documents(5, null, "update3 text", 0), null));
             System.out.println("Добавлен новый документ(true) или обновлен существующию (false)? :" + dbServer.addDocument(new Documents(0, "Choke", "new text", Authors.getAuthors().get(3).getAuthor_id()), Authors.getAuthors().get(3)));
             System.out.println("--------------");
             List<Documents> docs;
            docs =  dbServer.findDocumentByAuthor(new Authors(4, "Arnold Grey", null));
            System.out.println("Список документов под авторством автора с id=4 или именем \"Arnold Grey\":");
            if (docs!=null){
            for (Documents d: docs){
                System.out.println(d.toString());
            }
            }
            else System.out.println("список пуст");
            System.out.println("--------------");
            docs =  dbServer.findDocumentByAuthor(new Authors(4, null, null));
            System.out.println("Список документов под авторством автора с id=4:");            
            if (docs!=null){
            for (Documents d: docs){
                System.out.println(d.toString());
            }
            }
            else System.out.println("список пуст");
            
            System.out.println("--------------");            
            docs =  dbServer.findDocumentByAuthor(new Authors(0, "Arnold Grey", null));
            System.out.println("Список документов под авторством автора с именем \"Arnold Grey\":");            
            if (docs!=null){
                System.out.println("--------------");
            for (Documents d: docs){
                System.out.println(d.toString());
            }
            }
            else System.out.println("список пуст");
            
//            docs =  dbServer.findDocumentByAuthor(new Authors(0, null, null));
//            if (docs!=null){
//                System.out.println("--------------");
//            for (Documents d: docs){
//                System.out.println(d.toString());
//            }
//            }
//            else System.out.println("список пуст");
            
            System.out.println("--------------");
            System.out.println("Список документов содержанием в имени или тексте фрагмента \"First\":");                        
            docs = dbServer.findDocumentByContent("First");
            if (docs!=null){
                System.out.println("--------------");
            for (Documents d: docs){
                System.out.println(d.toString());
            }
            }
            else System.out.println("список пуст");
            
            System.out.println("--------------");
            docs = dbServer.findDocumentByContent("asdfds");
            System.out.println("Список документов содержанием в имени или тексте фрагмента \"asdfds\":");             
            if (docs!=null){
                System.out.println("--------------");
            for (Documents d: docs){
                System.out.println(d.toString());
            }
            }
            else System.out.println("список пуст");
            
            System.out.println("--------------");
         
            
             System.out.println("--------------");
             System.out.println("Удалены зваписи об авторе и его документах?: " + dbServer.deleteAuthor(new Authors(4, "Chuck Palahniuk", null)));
             System.out.println("Удалены зваписи об авторе и его документах?: " + dbServer.deleteAuthor(new Authors(5, "", null)));
             System.out.println("Удалены зваписи об авторе и его документах?: " + dbServer.deleteAuthor(new Authors(0, "Vasya", null)));                
            
            
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
}
