package ApplicationPackage;

import static ApplicationPackage.MainApp.*;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect
{
    public static String databaseUser = "root";
    public static String databasePassword = "";
    public static String databaseHost = "localhost";
    public static String databaseName = "blog";
    public static String databasePort = "3306";
  
    
    ///// TABLES
    ///// CLIENT
    public static String clientTable = "klient";
    public static String client_id_klienta = "id_klienta";
    public static String client_imie = "imie";
    public static String client_nazwisko = "nazwisko";
    public static String client_pesel = "pesel";
    public static String client_nr_telefonu = "telefon";
    public static String client_nazwa_firmy = "nazwa_firmy";
    public static String client_nip = "nip";
    public static String client_isDeleted = "is_Deleted";
     
    /// SERVICE
    public static String serviceTable = "naprawa";
    public static String service_nr_naprawy = "nr_naprawy" ;
    public static String service_id_samochodu = "id_samochodu" ;
    public static String service_data_przyjecia = "data_przyjecia" ;
    public static String service_data_zwrotu = "data_zwrotu" ;
    public static String service_koszt_naprawy = "koszt_naprawy" ;
    public static String service_czy_ukonczono = "czy_ukonczono" ;
    
    /// EMPLOYEE
    public static String employeeTable = "pracownik" ;
    public static String employee_id_pracownika = "id_pracownika" ;
    public static String employee_imie = "imie" ;
    public static String employee_nazwisko = "nazwisko" ;
    public static String employee_login = "login" ;
    public static String employee_haslo = "haslo" ;
   // public static String employee_isDeleted = "is_Deleted" ;
    
    /// CAR
    public static String carTable = "samochod" ;
    public static String car_id_samochodu = "id_samochodu" ;
    public static String car_nr_rejestracyjny = "nr_rejestracyjny" ;
    public static String car_marka = "marka" ;
    public static String car_model = "model";
   
    public static String car_cena_dzien = "cena_dzien" ;
    public static String car_naprawa = "naprawa" ;
    public static String car_dostepnosc = "dostepnosc" ;
    
    /// RENT DETAILS
    public static String rdTable = "szczegoly" ;
    public static String rd_nr_szczegolow = "nr_szczegolow";
    public static String rd_id_samochodu = "id_samochodu";
    public static String rd_koszt_wypozyczenia = "koszt_wypozyczenia" ;
    public static String rd_data_od = "data_od" ;
    public static String rd_data_do = "data_do" ;
    public static String rd_kara = "kara" ;
    
    /// RENT
    public static String rentTable = "wypozyczenie" ;
    public static String rent_nr_wypozyczenia = "nr_wypozyczenia" ;
    public static String rent_id_klienta = "id_klienta" ;
    public static String rent_id_pracownika = "id_pracownika" ;
    public static String rent_szcegoly_wypozyczenia = "wypozyczenie" ;
    public static String rent_zwrot = "zwrot";
            
    public static String addClient( String[] clientData)
    {
		
        try {                     
            PreparedStatement statetment = con.prepareStatement("INSERT INTO " + clientTable + " ("+ client_id_klienta + ", "+ client_imie + ", "+ client_nazwisko + ", "+ client_pesel + ", " + client_nr_telefonu + ", " + client_nazwa_firmy + ", "+ client_nip +", "+ client_isDeleted + ") VALUES (NULL, ?, ?, ?, ?, ?, ?, 0 )");                               
            statetment.setString(1, clientData[0]);
            statetment.setString(2, clientData[1]);
            statetment.setString(3, clientData[2]);
            statetment.setString(4, clientData[3]);
            statetment.setString(5, clientData[4]);
            statetment.setString(6, clientData[5]);         
            statetment.execute();            
            statetment.close();          
            return "Pomyślnie dodano klienta!";         
        } catch (SQLException ex) {
            System.err.println(ex);    
            return "Błąd w ustanowieniu połączenia";
        }
    }
  
    
   public static String [] getUserData(String pesel)
   {
       String [] userData = new String[7];
       
        try {                    
            PreparedStatement statetment = con.prepareStatement("SELECT * FROM " + clientTable + " WHERE "+ client_pesel + " = \"" + pesel + "\"");
            ResultSet rs = statetment.executeQuery();
            while(rs.next())
            {
                userData[0] = rs.getString(2);
                userData[1] = rs.getString(3);
                userData[2] = rs.getString(4);
                userData[3] = rs.getString(5);
                userData[4] = rs.getString(6);
          userData[5] = rs.getString(7);
               userData[6] = rs.getString(8);                
            }
            rs.close();
            statetment.close();   
        } catch (SQLException ex) {
            System.err.println(ex); 
            return null;
        }
       return userData;
   }
   
   public static String updateClient(String [] clientData)
   {
       System.out.println("uptudejt"); 
        try {
            PreparedStatement statetment = con.prepareStatement("UPDATE " + clientTable + " SET  "+ client_imie + " = ?, "+ client_nazwisko + " = ?, "+ client_pesel + " = ?, "+ client_nr_telefonu + " = ?, "+ client_nazwa_firmy + " = ?, "+ client_nip + " = ? WHERE "+ client_pesel + " = \"" + clientData[2] + "\"");
            statetment.setString(1, clientData[0]);
            statetment.setString(2, clientData[1]);
            statetment.setString(3, clientData[2]);
            statetment.setString(4, clientData[3]);
            statetment.setString(5, clientData[4]);
            statetment.setString(6, clientData[5]);
          
            statetment.execute();
            statetment.close();            
            return "Zaktualizowano dane klienta";
        } catch (SQLException ex) {
            System.err.println(ex);    
            return "Błąd w ustanowieniu połączenia";
        }
   }
   public static String deleteUser(String userPesel)
   {
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE " + clientTable + " SET "+client_isDeleted+ "= 1 WHERE "+ client_pesel + " = \"" + userPesel + "\"");
            statement.execute();
            statement.close();
            return "Usunięto klienta";
        } catch (SQLException ex) {
            System.err.println(ex);    
            return "Błąd w ustanowieniu połączenia";
        }
   }
   
   /*public static String addEmployee(String [] employeeData)
   {
       try {
            PreparedStatement statetment = con.prepareStatement("INSERT INTO " + employeeTable + " ("+ employee_id_pracownika + ", "+ employee_imie + ", "+ employee_nazwisko + ", "+ employee_dzial + ", "+ employee_login + ", "+ employee_haslo + ", "+ employee_nr_telefonu_wewnetrznego + ", " + ") VALUES (NULL, ?, ?, ?, ?, ?, ?, 0 )");                                
            statetment.setString(1, employeeData[0]);
            statetment.setString(2, employeeData[1]);
            statetment.setString(3, employeeData[2]);
            statetment.setString(4, employeeData[3]);
            statetment.setString(5, employeeData[4]);
            statetment.setString(6, employeeData[5]);                       
            statetment.execute();            
            statetment.close();
            return "Pomyślnie dodano pracownika!";  
        } catch (SQLException ex) {
            System.err.println(ex);    
            return "Błąd w ustanowieniu połączenia";
        }
   }
   
   public static String deleteEmployee(String name, String surname)
   {
       try {            
            PreparedStatement statement = con.prepareStatement("UPDATE " + employeeTable + " SET " +employee_isDeleted+ " = 1 WHERE "+ employee_imie + " = \"" + name + "\" AND " + employee_nazwisko + " =\"" + surname + "\"");
            statement.execute();
            statement.close();
            return "Usunięto pracownika";
        } catch (SQLException ex) {
            System.err.println(ex);    
            return "Błąd w ustanowieniu połączenia";
        }
   }
   */
   public static String addCar(String [] carData, byte[] photo)
   {
       int lastInsertedId = -1;
       try { 
                                     
		   PreparedStatement statetment = con.prepareStatement("INSERT INTO " + carTable + " ("+ car_id_samochodu + ", "+ car_nr_rejestracyjny + ", "+ car_marka + ", "+ car_model + ", " + car_cena_dzien + ", "+ car_naprawa + ", "+ car_dostepnosc + ") VALUES (NULL, ?, ?, ?, ?, NULL, 1)", Statement.RETURN_GENERATED_KEYS);    
         
            statetment = con.prepareStatement("INSERT INTO " + carTable + " ("+ car_id_samochodu + ", "+ car_nr_rejestracyjny + ", "+ car_marka + ", "+ car_model + ", " + car_cena_dzien + ", "+ car_naprawa + ", "+ car_dostepnosc + ") VALUES (NULL, ?, ?, ?, ?, NULL, 1)");                                
            statetment.setString(1, carData[2]);
            statetment.setString(2, carData[0]);
            statetment.setString(3, carData[1]);
            statetment.setFloat(4, Float.valueOf(carData[3]));
            statetment.execute();
            statetment.close();
            return "Pomyślnie dodano pojazd!";  
        } catch (SQLException ex) {
            System.err.println(ex);    
            return "Błąd w ustanowieniu połączenia";
        }         
   }
   
   public static String deleteCar(int carId)
   {      
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE " + carTable + " SET " + car_dostepnosc + " = 0 WHERE " + car_id_samochodu + " = " + carId);
            statement.execute();
            return "Usunięto samochód";            
        } catch (SQLException ex) {
            System.err.println(ex);
            return "Błąd połączenia";
        }
   } 
}
