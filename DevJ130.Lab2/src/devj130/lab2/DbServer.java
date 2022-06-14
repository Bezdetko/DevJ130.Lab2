/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devj130.lab2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bezdetk0@mail.ru
 */
public class DbServer implements IDbService{

    
/**
* Метод добавляет нового автора к базе данных, если все обязательные поля
* объекта author определены. В противном случае, метод пытается обновить
* уже существующие записи, используя заполненные поля класса для поиска
* подходящих записей. Например, если в объекте author указан id автора,
* поле имени автора пусто, а поле примечаний содержит текст, то у записи с
* заданным идентификатором обновляется поле примечаний.
*
* @param author именные данные автора.
* @return возвращает значение true, если создана новая запись, и значение
* false, если обновлена существующая запись.
* @throws DocumentException выбрасывается в случае, если поля объекта
* author заполнены неправильно и не удаётся создать новую запись или
* обновить уже существующую. Данное исключение также выбрасывается в случае
* общей ошибки доступа к базе данных
*/
    
    @Override
    public boolean addAuthor(Authors author) throws DocumentException {
        List<Authors> list = new ArrayList<>();
        Settings settings = new Settings();
        String sql;
       
        try (
                Connection connection = DriverManager.getConnection(
                        settings.getValue(Settings.URL),
                        settings.getValue(Settings.USER),
                        settings.getValue(Settings.PASSWORD))) {
//            list = Authors.getAuthors();   
            Statement stm = connection.createStatement();

            if (author.getAuthor_id() != 0 &&
                    author.getAuthor() != null && !author.getAuthor().trim().isEmpty()&& 
                    (author.getNotes() == null || author.getNotes().trim().isEmpty())) {
                sql = "INSERT INTO APP.Authors (id, fullName, comments) VALUES (" + author.getAuthor_id() + ", '"+author.getAuthor()+"', NULL)";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось создать новую запись");
                return true;             

                
            } else if (author.getAuthor_id() != 0 && 
                    author.getAuthor() != null && !author.getAuthor().trim().isEmpty() &&
                    !author.getNotes().trim().isEmpty() && author.getNotes() != null) {
                sql = "INSERT INTO APP.Authors (id, fullName, comments) VALUES (" + author.getAuthor_id() + ", '"+author.getAuthor()+"', '"+author.getNotes()+"')";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось создать новую запись");
                    return true;
                
            } else if (author.getAuthor_id() != 0 && 
                    (author.getAuthor() == null || author.getAuthor().trim().isEmpty()) &&
                    author.getNotes() != null &&!author.getNotes().trim().isEmpty()) {
                sql = "UPDATE APP.Authors SET comments = '" + author.getNotes() + "' WHERE id = " + author.getAuthor_id();
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось обновить существующую запись");
                return false;

            } else if (author.getAuthor_id() == 0 && 
                    (author.getAuthor() != null || !author.getAuthor().trim().isEmpty()) &&
                    author.getNotes() != null && !author.getNotes().trim().isEmpty()) {
                sql = "UPDATE APP.Authors SET comments = '" + author.getNotes() + "' WHERE FULLNAME = '" + author.getAuthor() + "'";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось обновить существующую запись");
                return false;                
            }
            else throw new DocumentException("Поля заполнены неправильно");

        } catch (SQLException ex) {
            throw new DocumentException("Cоединение не установлено " + ex.getMessage());
        }

    }    
    
/**
* Метод добавляет новый документ к базе данных, если все обязательные поля
* объектов doc и author определены. В противном случае, метод пытается
* обновить уже существующие записи, используя заполненные поля объектов для
* поиска подходящих записей.
*
* @param doc добавляемый или обновляемый документ.
* @param author ссылка на автора документа.
* @return возвращает значение true, если создан новый документ, и значение
* false, если обновлена уже существующая запись.
* @throws DocumentException выбрасывается в случае, если поля объектов doc
* и author заполнены неправильно и не удаётся создать новую запись или
* обновить уже существующую. Данное исключение также выбрасывается в случае
* общей ошибки доступа к базе данных
*/
    @Override
    public boolean addDocument(Documents doc, Authors author) throws DocumentException {
        Settings settings = new Settings();
        String sql;
        
        try (
                Connection connection = DriverManager.getConnection(
                        settings.getValue(Settings.URL),
                        settings.getValue(Settings.USER),
                        settings.getValue(Settings.PASSWORD))) {
            Statement stm = connection.createStatement();
            
            if (doc.getDocument_id() != 0 &&
                    doc.getTitle() != null && !doc.getTitle().trim().isEmpty()&& 
                    doc.getText()!= null && 
                    doc.getDate() != null && 
                    author != null                    
                    ) {
                sql = "INSERT INTO APP.Documents (id, title, text, createDate, idAuthor) VALUES "
                        + "(" + doc.getDocument_id() + ", '"+doc.getTitle()+"', '"+doc.getText()+"', '"+doc.getDate()+"', "+author.getAuthor_id()+")";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось создать новую запись");
                return true;             
            }
            else if (doc.getDocument_id() != 0 &&
                    doc.getTitle() != null && !doc.getTitle().trim().isEmpty()&& 
                    doc.getText()== null && 
                    doc.getDate() != null && 
                    author != null                    
                    ) {
                sql = "INSERT INTO APP.Documents (id, title, createDate, idAuthor) VALUES "
                        + "(" + doc.getDocument_id() + ", '"+doc.getTitle()+"', '"+doc.getDate()+"', "+author.getAuthor_id()+")";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось создать новую запись");
                return true;
            }
            else if (doc.getDocument_id() !=0 &&
                    (doc.getTitle() == null || !doc.getTitle().trim().isEmpty()) &&
                    doc.getText()!= null && 
                    doc.getDate() != null &&
                    author == null
                    ) {
                 sql = "UPDATE APP.Documents SET text = '"+doc.getText()+"', createDate = '"+doc.getDate()+"' WHERE ID = "+doc.getDocument_id();
//                       "UPDATE APP.Documents SET text = 'update3 text', createDate = '2021/05/22' WHERE ID = 5;"
                 stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось обновить существующую запись");
                return false;
            }
            else if (doc.getDocument_id() == 0  &&
                    doc.getTitle() != null && !doc.getTitle().trim().isEmpty() &&
                    doc.getText()!= null && 
                    doc.getDate() != null &&
                    author != null                  
                    ) {
                 sql = "UPDATE APP.Documents SET text = '"+doc.getText()+"', createDate = '"+doc.getDate()+"' WHERE title = '"+doc.getTitle()+"' AND idAuthor="+ author.getAuthor_id()+"";
                 stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) throw new DocumentException("Не удалось обновить существующую запись");
                return false;                
            }
            else throw new DocumentException("Поля заполнены неправильно"); 
                        
         } catch (SQLException ex) {
            throw new DocumentException("Cоединение не установлено " + ex.getMessage());
        }
    }
/**
* Метод производит поиск документов по их автору.
*
* @param author автор документа. Объект может содержать неполную информацию
* об авторе. Например, объект может содержать только именные данные автора
* или только его идентификатор.
* @return возвращает массив всех найденных документов. Если в базе данных
* не найдено ни одного документа, то возвращается значение null.
* @throws DocumentException выбрасывается в случае, если поле объекта
* author заполнены неправильно или нелья выполнить поиск по его полям.
* Данное исключение также выбрасывается в случае общей ошибки доступа к
* базе данных
*/
    @Override
    public List<Documents> findDocumentByAuthor(Authors author) throws DocumentException {
        Settings settings = new Settings();
        String sql;
        
        try (
                Connection connection = DriverManager.getConnection(
                        settings.getValue(Settings.URL),
                        settings.getValue(Settings.USER),
                        settings.getValue(Settings.PASSWORD))) {
            Statement stm = connection.createStatement();
            if (author.getAuthor_id()!=1 && author.getAuthor()!=null){
            sql = "SELECT * FROM APP.DOCUMENTS WHERE APP.DOCUMENTS.IDAUTHOR = "+author.getAuthor_id()+" "
                    + "OR APP.DOCUMENTS.IDAUTHOR = (SELECT APP.AUTHORS.ID FROM APP.AUTHORS WHERE FULLNAME = '"+author.getAuthor()+"')";
            ResultSet rs = stm.executeQuery(sql);
            List <Documents> documents = new ArrayList<>();
            Documents document = null;
            while (rs.next()) {                
            document = new Documents(
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        rs.getDate(4), 
                        rs.getInt(5));
                        documents.add(document);
            } if (!documents.isEmpty())return documents;
            else return null;            
            }
            else if (author.getAuthor_id()!=0 && author.getAuthor()==null){
            sql = "SELECT * FROM APP.DOCUMENTS WHERE APP.DOCUMENTS.IDAUTHOR = "+author.getAuthor_id()+"";
            ResultSet rs = stm.executeQuery(sql);
            List <Documents> documents = new ArrayList<>();
            Documents document;
            while (rs.next()) {                
            document = new Documents(
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        rs.getDate(4), 
                        rs.getInt(5));
                        documents.add(document);
            } if (!documents.isEmpty())return documents;
            else return null;
            }
            else if (author.getAuthor_id()==0 && author.getAuthor()!=null){
            sql = "SELECT * FROM APP.DOCUMENTS WHERE APP.DOCUMENTS.IDAUTHOR = (SELECT APP.AUTHORS.ID FROM APP.AUTHORS WHERE FULLNAME = '"+author.getAuthor()+"')";
            ResultSet rs = stm.executeQuery(sql);
            List <Documents> documents = new ArrayList<>();
            Documents document;
            while (rs.next()) {                
            document = new Documents(
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        rs.getDate(4), 
                        rs.getInt(5));
                        documents.add(document);
            } if (!documents.isEmpty())return documents;
            else return null;            
            }
            else throw new DocumentException("Поиск невозможен, параметры введены некорректно");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new DocumentException("Соединение не установлено");
        }
    }

    
/**
* Метод производит поиск документов по их содержанию.
*
* @param content фрагмент текста (ключевые слова), который должен
* содержаться в заголовке или в основном тексте документа.
* @return возвращает массив найденных документов.Если в базе данных не
* найдено ни одного документа, удовлетворяющего условиям поиска, то
* возвращается значение null.
* @throws DocumentException выбрасывается в случае, если строка content
* равна null или является пустой. Данное исключение также выбрасывается в
* случае общей ошибки доступа к базе данных
*/
    @Override
    public List <Documents> findDocumentByContent(String content) throws DocumentException {
        Settings settings = new Settings();
        String sql;
        try (
                Connection connection = DriverManager.getConnection(
                        settings.getValue(Settings.URL),
                        settings.getValue(Settings.USER),
                        settings.getValue(Settings.PASSWORD))) {
            Statement stm = connection.createStatement();
            if (content==null || content.trim().isEmpty()) throw new DocumentException("строка content равна null или является пустой");
            sql = "SELECT * FROM APP.DOCUMENTS WHERE title LIKE '%"+content+"%' OR text LIKE '%"+content+"%'";
            ResultSet rs = stm.executeQuery(sql);
            List <Documents> documents = new ArrayList<>();
            Documents document;
            while (rs.next()) {                
            document = new Documents(
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        rs.getDate(4), 
                        rs.getInt(5));
                        documents.add(document);
            } if (!documents.isEmpty())return documents;
            else return null;          
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new DocumentException("Соединение не установлено");
        }
    }        

/**
* Метод удаляет автора из базы данных. Всесте с автором удаляются и все
* документы, которые ссылаются на удаляемого автора.
*
* @param author удаляемый автор. Объект может содержать неполные данные
* автора, например, только идентификатор автора.
* @return значение true, если запись автора успешно удалена, и значение
* false - в противном случае.
* @throws DocumentException выбрасывается в случае, если поля объекта
* author заполнены неправильно или ссылка author равна null, а также случае
* общей ошибки доступа к базе данных.
*/
    @Override
    public boolean deleteAuthor(Authors author) throws DocumentException {
        Settings settings = new Settings();
        String sql;
        List<Authors> list = new ArrayList<>();        
        try (
                Connection connection = DriverManager.getConnection(
                        settings.getValue(Settings.URL),
                        settings.getValue(Settings.USER),
                        settings.getValue(Settings.PASSWORD))) {
            Statement stm = connection.createStatement();
            if (author.getAuthor_id()!=0 && author.getAuthor()!=null && !author.getAuthor().trim().isEmpty()){
                sql = "SELECT * FROM APP.AUTHORS WHERE ID ="+author.getAuthor_id()+" AND FULLNAME = '"+author.getAuthor()+"'";
                ResultSet rs = stm.executeQuery(sql);
                Authors tmpAuthor = null;
//                while (rs.next()) {
                    if(rs.next()==false) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует!!!!");
//                    rs.next();
                    tmpAuthor = new Authors(
                            rs.getInt(1), 
                            rs.getString(2), 
                            rs.getString(3));
//                    list.add(tmpAuthor);
//                }  
                if (tmpAuthor==null) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует");                               
                sql = "DELETE FROM APP.Documents WHERE idAuthor="+author.getAuthor_id()+"";
                stm.executeUpdate(sql);
                sql = "DELETE FROM APP.Authors WHERE id="+author.getAuthor_id()+"";
                stm.executeUpdate(sql);                
                if (stm.getUpdateCount() == -1) return false;
                return true;
            }
            else if (author.getAuthor_id()!=0 && (author.getAuthor()==null || author.getAuthor().trim().isEmpty())){
                sql = "SELECT * FROM APP.AUTHORS WHERE ID ="+author.getAuthor_id()+"";
                ResultSet rs = stm.executeQuery(sql);
                Authors tmpAuthor = null;
//                while (rs.next()) {
                    if(rs.next()==false) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует!!!!");
//                    rs.next();
                    tmpAuthor = new Authors(
                            rs.getInt(1), 
                            rs.getString(2), 
                            rs.getString(3));
//                    list.add(tmpAuthor);
//                }  
                if (tmpAuthor==null) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует");
                sql = "DELETE FROM APP.Documents WHERE idAuthor="+author.getAuthor_id()+"";
                stm.executeUpdate(sql);
                sql = "DELETE FROM APP.Authors WHERE id="+author.getAuthor_id()+"";
                stm.executeUpdate(sql);                
                if (stm.getUpdateCount() == -1) return false;
                return true;                
            }
            else if (author.getAuthor_id()==0 && author.getAuthor()!=null && !author.getAuthor().trim().isEmpty()){
                sql = "SELECT * FROM APP.AUTHORS WHERE FULLNAME ='"+author.getAuthor()+"'";
                ResultSet rs = stm.executeQuery(sql);
                Authors tmpAuthor = null;
//                while (rs.next()) {
                    if(rs.next()==false) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует!!!!");
//                    rs.next();
                    tmpAuthor = new Authors(
                            rs.getInt(1), 
                            rs.getString(2), 
                            rs.getString(3));
//                    list.add(tmpAuthor);
//                }
                if (tmpAuthor==null) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует");
                sql = "DELETE FROM APP.Documents WHERE idAuthor="+tmpAuthor.getAuthor_id()+"";
                stm.executeUpdate(sql);
                sql = "DELETE FROM APP.Authors WHERE id="+tmpAuthor.getAuthor_id()+"";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) return false;
                return true;       
            }
            else throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует");  

        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + "!!");
            throw new DocumentException("Соединение не установлено");
        }
    }
    
    
/**
* Метод удаляет автора из базы данных по его идентификатору. Всесте с
* автором удаляются и все документы, которые ссылаются на удаляемого
* автора.
*
* @param id идентификатор удаляемого автора.
* @return значение true, если запись автора успешно удалена, и значение
* false - в противном случае.
* @throws DocumentException выбрасывается в случае общей ошибки доступа к
* базе данных.
*/
    @Override
    public boolean deleteAuthor(int id) throws DocumentException {
        Settings settings = new Settings();
        String sql;
        List<Authors> list = new ArrayList<>();        
        try (
                Connection connection = DriverManager.getConnection(
                        settings.getValue(Settings.URL),
                        settings.getValue(Settings.USER),
                        settings.getValue(Settings.PASSWORD))) {
            Statement stm = connection.createStatement();
            if (id<1) throw new DocumentException("Id введен <1");
            else {
                sql = "SELECT * FROM APP.AUTHORS WHERE ID ="+id+"";
                ResultSet rs = stm.executeQuery(sql);
                Authors tmpAuthor = null;
                if(rs.next()==false) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует!!!!");
                tmpAuthor = new Authors(
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3));
                if (tmpAuthor==null) throw new DocumentException("поля объекта author заполнены неправильно, такого автора не существует");
                sql = "DELETE FROM APP.Documents WHERE idAuthor="+id+"";
                stm.executeUpdate(sql);
                sql = "DELETE FROM APP.Authors WHERE id="+id+"";
                stm.executeUpdate(sql);
                if (stm.getUpdateCount() == -1) return false;
                return true;                               
            }    
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + "!!");
            throw new DocumentException("Соединение не установлено");
        }
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
