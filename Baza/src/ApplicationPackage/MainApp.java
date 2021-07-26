package ApplicationPackage;

import static ApplicationPackage.Creators.*;
import static ApplicationPackage.TimeConfig.*;
import static ApplicationPackage.DBConnect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;

public class MainApp 
{   
    public static Start welcome;
    public static Menu mainMenu;
    public static CarHires23 carHire;
    public static MainHires hires;
    public static Raport raport;
    public static Repair service;
    
    private static String userName ;
    private static String todayDate;
    private static String datePlusAlbumNumber;
    public static Connection con;
    
    public static String getTodayDate()
    {          
        return todayDate != null ? todayDate : "0000-00-00";
    }
    
    public static String getDatePlusAlbumNumber()
    {
        return datePlusAlbumNumber != null ? datePlusAlbumNumber : "0000-00-00";
    }
    
    public MainApp()
    {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
             con = DriverManager.getConnection("jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true&characterEncoding=utf8&",databaseUser, databasePassword);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(mainMenu, "Nie udało się połączyć z bazą danych. Aplikacja zostanie zamknięta. Sprawdź połączenie i spróbuj ponownie");
            System.err.println(ex);
            System.exit(1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainMenu, "Nie udało się połączyć z bazą danych. Aplikacja zostanie zamknięta. Sprawdź połączenie i spróbuj ponownie");            
            System.err.println(ex);
            System.exit(1);
        }
        
        String dateFormatString;
        switch(dateFormat)
        {
            case 1:
                dateFormatString = "dd-MM-yyyy";
                break;
            case 2:
                dateFormatString = "yyyy-MM-dd";
                break;
            default:
                dateFormatString = "dd-MM-yyyy";
                break;
        }
        
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);
        todayDate = sdf.format(today);   
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(todayDate));
            calendar.add(Calendar.DATE, albumNumberToInt());
            datePlusAlbumNumber = sdf.format(calendar.getTime());
        } catch (ParseException ex) {
            System.out.println("Błąd dodawania daty");
        }        
        
        mainMenu = new Menu();
        welcome = new Start();
        welcome.setVisible(true);
        //mainMenu.setVisible(true);
    }
   public static void main(String args[]) 
    {
          MainApp myApp = new MainApp();
    } 
}
