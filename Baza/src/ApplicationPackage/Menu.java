package ApplicationPackage;

import static ApplicationPackage.MainApp.*;
import static ApplicationPackage.Creators.*;
import static ApplicationPackage.DBConnect.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Menu extends JFrame
{
    private byte[] carAddImage = null;    
    enum formMode{
        ADDING, EDITING, VIEWING
    };
    formMode clientFormMode = formMode.ADDING;
    formMode carFormMode = formMode.ADDING;
    
    
    public Menu() 
    {
        initComponents();
        hidePanels();
        defaultSettings();
        try {
            UIManager.setLookAndFeel(wygladZmien);
        } catch (ClassNotFoundException ex) {} 
        catch (InstantiationException ex) {} 
        catch (IllegalAccessException ex) {} 
        catch (UnsupportedLookAndFeelException ex) {}
    }
    
    public void defaultSettings()
    {
        
       // client_addClient_Button.setEnabled(false);
        
        panel_client.setVisible(true);        
        refreshClientList(client_tableClients);
        client_panel_AddClient.setVisible(true);   
       
    }
    
    public void hidePanels()
    {
        panel_client.setVisible(false);
        panel_car.setVisible(false);
        panel_about.setVisible(false);
    }
    
    public void clearAddClientForm()
    {
        client_add_name.setText("");
        client_add_surname.setText("");
        client_add_pesel.setText("");
        client_add_nrtel.setText("");
        client_add_compname.setText("");
        client_add_nip.setText("");
    }
    
    public String validAddClientData()
    {
        if( client_add_name.getText().length() == 0)
            return "Nie wprowadzono imienia";
        else if( client_add_surname.getText().length() == 0)
            return "Nie wprowadzono nazwiska";
        else if( client_add_nrtel.getText().length() == 0)
            return "Nie wprowadzono numeru telefonu";
        else if( client_add_pesel.getText().length() == 0)
            return "Nie wprowadzono numeru PESEL";
       
        else if( client_add_pesel.getText().length() == 0 )
            return "Nieprawidłowa wartość w polu PESEL";
        else if (client_add_nip.getText().length() == 0 )
            return "Nieprawidłowa wartość w polu NIP";
        else if(client_add_compname.getText().length() == 0 )
            return "Jeśli wprowadzono nazwę firmy należy wprowadzić NIP";
        else if(client_add_nip.getText().length() ==0 )
            return "Jeśli wprowadzono NIP należy wprowadzić także numer firmy";
        return null;
    }
    public void searchClientWithFilter(JTable table, String filter, String searchFor)
    {   
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);        
        int counter = 1;         
        try {
            Class.forName("com.mysql.jdbc.Driver");            
            PreparedStatement statetment = con.prepareStatement(
                    "SELECT imie"+ ", nazwisko"+ ", pesel"+  " FROM klient"  + " WHERE is_Deleted"+ " = 0 AND " + filter + " LIKE '" + searchFor + "%'");
            ResultSet rs = statetment.executeQuery();
            while( rs.next())
            {
                dtm.addRow(new Object[]{counter++, rs.getString(1), rs.getString(2), rs.getString(3)});               
            }            
            rs.close();
            statetment.close();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        
    }
    public void refreshClientList(JTable table)
    {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);        
        int counter = 1;       
        try {                    
            PreparedStatement statetment = con.prepareStatement("SELECT imie"+ ", nazwisko"+ ", pesel"+ " FROM klient" + " WHERE is_Deleted = 0");
            ResultSet rs = statetment.executeQuery();
            while( rs.next())
            {
                dtm.addRow(new Object[]{counter++, rs.getString(1), rs.getString(2), rs.getString(3)});               
            }            
            rs.close();
            statetment.close();       
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
     
    public void clearAddCarForm()
    {
        car_add_name.setText("");
        car_add_model.setText("");
        car_add_rej.setText("");
        car_add_price.setText("");
        
        carAddImage = null;       
    }
    public void refreshCarList(JTable table)
    {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);        
        int counter = 1;       
        try {                    
            PreparedStatement statetment = con.prepareStatement("SELECT id_samochodu"  + ", marka" + ", model" + " FROM samochod"  + " WHERE dostepnosc" + " != 0");
            ResultSet rs = statetment.executeQuery();
            while( rs.next())
            {
                dtm.addRow(new Object[]{counter++,rs.getInt(1), rs.getString(2), rs.getString(3)});               
            }            
            rs.close();
            statetment.close();        
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public String validAddCarData()
    {
        if( car_add_name.getText().length() == 0)
            return "Nie wprowadzono marki";
        else if( car_add_model.getText().length() == 0)
            return "Nie wprowadzono modelu";
        else if( car_add_rej.getText().length() == 0)
            return "Nie wprowadzono numeru rejestracyjnego";
        else if( car_add_price.getText().length() == 0)
            return "Nie wprowadzono ceny";
      
        else
            return null;
    }
    
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loggedUser = new javax.swing.JLabel();
        edit_Service_Button = new javax.swing.JButton();
        edit_showHires_Button = new javax.swing.JButton();
        edit_carHire_Button = new javax.swing.JButton();
        edit_Raports_Button = new javax.swing.JButton();
        edit_aboutApp_Button = new javax.swing.JButton();
        edit_Cars_Button = new javax.swing.JButton();
        edit_Clients_Button = new javax.swing.JButton();
        panel_car = new javax.swing.JPanel();
        car_title_text = new javax.swing.JLabel();
        car_title_text2 = new javax.swing.JLabel();
        car_deleteCar = new javax.swing.JButton();
        car_log = new javax.swing.JLabel();
        car_listPane = new javax.swing.JScrollPane();
        car_tableCars = new javax.swing.JTable();
        car_addText = new javax.swing.JLabel();
        car_add_name_text = new javax.swing.JLabel();
        car_add_name = new javax.swing.JTextField();
        car_add_model = new javax.swing.JTextField();
        car_add_model_text = new javax.swing.JLabel();
        car_add_rej_text = new javax.swing.JLabel();
        car_add_rej = new javax.swing.JTextField();
        car_add_price = new javax.swing.JTextField();
        car_add_price_text = new javax.swing.JLabel();
        car_add_button = new javax.swing.JButton();
        car_add_log = new javax.swing.JLabel();
        car_add_clearForm = new javax.swing.JButton();
        panel_about = new javax.swing.JPanel();
        about_title_text = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        panel_client = new javax.swing.JPanel();
        client_title_text = new javax.swing.JLabel();
        client_panel_AddClient = new javax.swing.JPanel();
        client_add_log = new javax.swing.JLabel();
        client_tablePane = new javax.swing.JScrollPane();
        client_tableClients = new javax.swing.JTable();
        client_serach_text = new javax.swing.JLabel();
        client_search = new javax.swing.JTextField();
        client_add_deleteClient = new javax.swing.JButton();
        client_addText = new javax.swing.JLabel();
        client_add_name_text = new javax.swing.JLabel();
        client_add_pesel_text = new javax.swing.JLabel();
        client_add_pesel = new javax.swing.JTextField();
        client_add_name = new javax.swing.JTextField();
        client_add_nip = new javax.swing.JTextField();
        client_add_nip_text = new javax.swing.JLabel();
        client_add_clearForm = new javax.swing.JButton();
        client_add_AddButton = new javax.swing.JButton();
        client_add_compname = new javax.swing.JTextField();
        client_add_compname_text = new javax.swing.JLabel();
        client_add_nrtel_text = new javax.swing.JLabel();
        client_add_nrtel = new javax.swing.JTextField();
        client_add_surname_text = new javax.swing.JLabel();
        client_add_surname = new javax.swing.JTextField();
        frame_background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wypożyczalnia samochodów");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loggedUser.setForeground(new java.awt.Color(255, 255, 255));
        loggedUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(loggedUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 80, 330, 40));

        edit_Service_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_Service_Button.setText("Naprawa pojazdów");
        edit_Service_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_Service_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_Service_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 170, 50));

        edit_showHires_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_showHires_Button.setText("Lista wypożyczeń");
        edit_showHires_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_showHires_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_showHires_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 170, 50));

        edit_carHire_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_carHire_Button.setForeground(new java.awt.Color(51, 0, 255));
        edit_carHire_Button.setText("Dodaj wypożyczenie");
        edit_carHire_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_carHire_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_carHire_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 10, 170, 50));

        edit_Raports_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_Raports_Button.setText("Statystyki");
        edit_Raports_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_Raports_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_Raports_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 140, 150, 30));

        edit_aboutApp_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_aboutApp_Button.setText("Opis aplikacji");
        edit_aboutApp_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_aboutApp_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_aboutApp_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 180, 150, 30));

        edit_Cars_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_Cars_Button.setText("Samochody");
        edit_Cars_Button.setToolTipText("");
        edit_Cars_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_Cars_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_Cars_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 170, 50));

        edit_Clients_Button.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        edit_Clients_Button.setText("Klienci");
        edit_Clients_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_Clients_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(edit_Clients_Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 170, 50));

        panel_car.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        car_title_text.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        car_title_text.setText("Samochody:");
        panel_car.add(car_title_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 310, 50));

        car_title_text2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        car_title_text2.setForeground(new java.awt.Color(255, 255, 255));
        panel_car.add(car_title_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 310, 50));

        car_deleteCar.setText("Usuń pojazd");
        car_deleteCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_deleteCarActionPerformed(evt);
            }
        });
        panel_car.add(car_deleteCar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, 200, 30));

        car_log.setForeground(new java.awt.Color(255, 255, 255));
        panel_car.add(car_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(684, 670, 290, 30));

        car_tableCars.setAutoCreateRowSorter(true);
        car_tableCars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lp.", "id.", "Marka", "Model"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        car_tableCars.getTableHeader().setReorderingAllowed(false);
        car_tableCars.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                car_tableCarsMouseReleased(evt);
            }
        });
        car_listPane.setViewportView(car_tableCars);
        if (car_tableCars.getColumnModel().getColumnCount() > 0) {
            car_tableCars.getColumnModel().getColumn(0).setPreferredWidth(100);
            car_tableCars.getColumnModel().getColumn(2).setPreferredWidth(200);
            car_tableCars.getColumnModel().getColumn(3).setPreferredWidth(230);
        }

        panel_car.add(car_listPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 300, 160));

        car_addText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        car_addText.setText("Dodaj samochód:");
        panel_car.add(car_addText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 470, 40));

        car_add_name_text.setText("Marka:");
        panel_car.add(car_add_name_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 130, 30));

        car_add_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_add_nameKeyTyped(evt);
            }
        });
        panel_car.add(car_add_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 220, 30));

        car_add_model.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_add_modelKeyTyped(evt);
            }
        });
        panel_car.add(car_add_model, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 220, 30));

        car_add_model_text.setText("Model:");
        panel_car.add(car_add_model_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 130, 30));

        car_add_rej_text.setText("Numer rejestracyjny:");
        panel_car.add(car_add_rej_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 130, 30));

        car_add_rej.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_add_rejKeyTyped(evt);
            }
        });
        panel_car.add(car_add_rej, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 220, 30));

        car_add_price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_add_priceKeyTyped(evt);
            }
        });
        panel_car.add(car_add_price, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 220, 30));

        car_add_price_text.setText("Cena za dzień [zł]:");
        panel_car.add(car_add_price_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 130, 30));

        car_add_button.setText("Dodaj");
        car_add_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_add_buttonActionPerformed(evt);
            }
        });
        panel_car.add(car_add_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 170, 30));
        panel_car.add(car_add_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 650, 450, 30));

        car_add_clearForm.setText("Wyczyść formularz");
        car_add_clearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_add_clearFormActionPerformed(evt);
            }
        });
        panel_car.add(car_add_clearForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 150, 30));

        getContentPane().add(panel_car, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 810, 410));

        panel_about.setBackground(new java.awt.Color(255, 255, 204));
        panel_about.setOpaque(false);
        panel_about.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        about_title_text.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        about_title_text.setText("O aplikacji");
        panel_about.add(about_title_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 310, 50));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Projekt \"Wypożyczalnia Samochodów\" został stworzony przez Wiktora Szczepańskiego i Tomasza\nTokarskiego na zaliczenie przedmiotu \"Aplikacje Bazodanowe\". Założeniem programu jest  \numożliwienie użytkownikowi prostego zarządania bazą danych w firmie, która zajmuje się \nwypożyczaniem samochodów.\n\nDo bazy danych można dodawać klientów oraz samochody. Zapisywane są w oddzielnej\nencji \"Samochody\" i \"Klienci\". Encje te zawierają odpowiednie dla siebie pola. \n\nSamochody można wypożyczać. Dane wypożyczenia zapisywane są w encji \"Wypozyczenia\".\nEncja ta łączy się z encją słabą, która zawiera szczegóły i zwie się \"Szczegoly_wypozyczenia\".\nEncja \"Wypozyczenia\" łączy się bezpośrednio z encją \"Klient\" i \"Pracownik\".\n");
        jScrollPane1.setViewportView(jTextArea1);

        panel_about.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 540, 250));

        getContentPane().add(panel_about, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 810, 410));

        panel_client.setOpaque(false);
        panel_client.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        client_title_text.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        client_title_text.setText("Klienci");
        panel_client.add(client_title_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 310, 30));

        client_panel_AddClient.setBackground(new java.awt.Color(255, 255, 204));
        client_panel_AddClient.setOpaque(false);
        client_panel_AddClient.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        client_add_log.setForeground(new java.awt.Color(255, 255, 255));
        client_panel_AddClient.add(client_add_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 450, 30));

        panel_client.add(client_panel_AddClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 780, 230));

        client_tableClients.setAutoCreateRowSorter(true);
        client_tableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lp.", "Imię", "Nazwisko", "PESEL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        client_tableClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                client_tableClientsMouseReleased(evt);
            }
        });
        client_tablePane.setViewportView(client_tableClients);
        if (client_tableClients.getColumnModel().getColumnCount() > 0) {
            client_tableClients.getColumnModel().getColumn(0).setResizable(false);
            client_tableClients.getColumnModel().getColumn(0).setPreferredWidth(100);
            client_tableClients.getColumnModel().getColumn(1).setPreferredWidth(200);
            client_tableClients.getColumnModel().getColumn(2).setPreferredWidth(230);
        }

        panel_client.add(client_tablePane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 350, 110));

        client_serach_text.setText("Szukaj klienta(pesel):");
        panel_client.add(client_serach_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 210, 30));

        client_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_searchKeyTyped(evt);
            }
        });
        panel_client.add(client_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 220, 30));

        client_add_deleteClient.setText("Usuń zaznaczonego  klienta");
        client_add_deleteClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                client_add_deleteClientActionPerformed(evt);
            }
        });
        panel_client.add(client_add_deleteClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 230, 30));

        client_addText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        client_addText.setText("Dodaj Klienta:");
        panel_client.add(client_addText, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 470, 40));

        client_add_name_text.setText("Imię:");
        panel_client.add(client_add_name_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 100, 30));

        client_add_pesel_text.setText("PESEL:");
        panel_client.add(client_add_pesel_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 100, 30));

        client_add_pesel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_add_peselKeyTyped(evt);
            }
        });
        panel_client.add(client_add_pesel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 190, 30));

        client_add_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_add_nameKeyTyped(evt);
            }
        });
        panel_client.add(client_add_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 190, 30));

        client_add_nip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_add_nipKeyTyped(evt);
            }
        });
        panel_client.add(client_add_nip, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 190, 30));

        client_add_nip_text.setText("* NIP:");
        panel_client.add(client_add_nip_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 100, 30));

        client_add_clearForm.setText("Wyczyść formularz");
        client_add_clearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                client_add_clearFormActionPerformed(evt);
            }
        });
        panel_client.add(client_add_clearForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, 130, 30));

        client_add_AddButton.setText("Dodaj");
        client_add_AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                client_add_AddButtonActionPerformed(evt);
            }
        });
        panel_client.add(client_add_AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 390, 130, 30));

        client_add_compname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_add_compnameKeyTyped(evt);
            }
        });
        panel_client.add(client_add_compname, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 350, 190, 30));

        client_add_compname_text.setText("* Nazwa firmy:");
        panel_client.add(client_add_compname_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, 100, 30));

        client_add_nrtel_text.setText("Numer telefonu:");
        panel_client.add(client_add_nrtel_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 310, 100, 30));

        client_add_nrtel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_add_nrtelKeyTyped(evt);
            }
        });
        panel_client.add(client_add_nrtel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 310, 190, 30));

        client_add_surname_text.setText("Nazwisko:");
        panel_client.add(client_add_surname_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 270, 100, 30));

        client_add_surname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_add_surnameKeyTyped(evt);
            }
        });
        panel_client.add(client_add_surname, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 270, 190, 30));

        getContentPane().add(panel_client, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 760, 450));

        frame_background.setOpaque(true);
        getContentPane().add(frame_background, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 250, 1030, 340));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void edit_Clients_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_Clients_ButtonActionPerformed
        hidePanels();
        panel_client.setVisible(true);
        refreshClientList(client_tableClients);
    }//GEN-LAST:event_edit_Clients_ButtonActionPerformed

    private void client_add_clearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_client_add_clearFormActionPerformed
        clearAddClientForm();
        client_add_log.setText(null);
    }//GEN-LAST:event_client_add_clearFormActionPerformed

    private void client_add_nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_add_nameKeyTyped
        if(client_add_name.getText().length() >= 50 || ! Character.isLetter(evt.getKeyChar()))                    
            evt.consume();        
    }//GEN-LAST:event_client_add_nameKeyTyped

    private void client_add_surnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_add_surnameKeyTyped
        if(client_add_surname.getText().length() >= 65 || ! Character.isLetter(evt.getKeyChar()))                    
            evt.consume();  
    }//GEN-LAST:event_client_add_surnameKeyTyped

    private void client_add_peselKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_add_peselKeyTyped
        if(client_add_pesel.getText().length() >= 11 || ! Character.isDigit(evt.getKeyChar()))                    
            evt.consume();  
    }//GEN-LAST:event_client_add_peselKeyTyped

    private void client_add_nrtelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_add_nrtelKeyTyped
       if(client_add_nrtel.getText().length() >= 9 || ! Character.isDigit(evt.getKeyChar()))                    
            evt.consume();  
    }//GEN-LAST:event_client_add_nrtelKeyTyped

    private void client_add_compnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_add_compnameKeyTyped
        if(client_add_compname.getText().length() >= 65)                    
            evt.consume();  
    }//GEN-LAST:event_client_add_compnameKeyTyped

    private void client_add_nipKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_add_nipKeyTyped
        if(client_add_nip.getText().length() >= 9 || ! Character.isDigit(evt.getKeyChar()))                    
            evt.consume();  
    }//GEN-LAST:event_client_add_nipKeyTyped

    private void client_add_AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_client_add_AddButtonActionPerformed
        
		String validError = validAddClientData();
        if(validError == null)
        {    System.out.println("yo button ya");        
            client_add_log.setText(null);
            String [] clientData = {client_add_name.getText(),
                                    client_add_surname.getText(),
                                    client_add_pesel.getText(),
                                    client_add_nrtel.getText(),
                                    client_add_compname.getText(),
                                    client_add_nip.getText()
                                    };   
            for(int i=0; i<6;i++)
                System.out.println(clientData[i]);
            client_add_log.setForeground(Color.green);
           
                 System.out.println("this fine");   
                client_add_log.setText(addClient(clientData));
            
            
            refreshClientList(client_tableClients);
        }
        else
        {
            client_add_log.setForeground(Color.red);
            client_add_log.setText(validError);
        }
    }//GEN-LAST:event_client_add_AddButtonActionPerformed

    private void client_add_deleteClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_client_add_deleteClientActionPerformed
        if(client_tableClients.getSelectedRow() == -1)
        {
            client_add_log.setForeground(Color.red);
            client_add_log.setText("Najpierw zaznacz klienta na liście");
            return;
        }
        String userToDelete = (String) client_tableClients.getValueAt(client_tableClients.getSelectedRow(), 3);
        client_add_log.setText(deleteUser(userToDelete));
        refreshClientList(client_tableClients);
        clearAddClientForm();
        
    }//GEN-LAST:event_client_add_deleteClientActionPerformed

    private void client_tableClientsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_client_tableClientsMouseReleased
        if(clientFormMode == formMode.EDITING)
        {
            client_add_log.setText("");
            client_add_log.setForeground(Color.black);

            String []userData = new String[6];
            if((userData = getUserData((String)client_tableClients.getValueAt(client_tableClients.getSelectedRow(), 3))) != null)
            {
                client_add_name.setText(userData[0]);
                client_add_surname.setText(userData[1]);
                client_add_pesel.setText(userData[2]);
                client_add_nrtel.setText(userData[3]);
//                client_add_idnum.setText(userData[4]);
                client_add_compname.setText(userData[4]);
                client_add_nip.setText(userData[5]);
            }
        }        
    }//GEN-LAST:event_client_tableClientsMouseReleased

    private void client_searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_searchKeyTyped
        String searchFilter = "pesel";   
        if(evt == null)
        {
            refreshClientList(client_tableClients);
            return;
        }
        if(Character.isDigit(evt.getKeyChar()) || Character.isLetter(evt.getKeyChar()))
        {
            client_search.setText(client_search.getText() + evt.getKeyChar());
            evt.consume();
        }
        searchClientWithFilter(client_tableClients, searchFilter, client_search.getText());        
        if(client_search.getText().length() == 0)
        {
            refreshClientList(client_tableClients);
        }        
    }//GEN-LAST:event_client_searchKeyTyped

    private void edit_Cars_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_Cars_ButtonActionPerformed
        hidePanels();
        panel_car.setVisible(true);
        refreshCarList(car_tableCars);
    }//GEN-LAST:event_edit_Cars_ButtonActionPerformed

    private void car_add_nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_add_nameKeyTyped
        if(car_add_name.getText().length() >= 50)                    
            evt.consume();
    }//GEN-LAST:event_car_add_nameKeyTyped

    private void car_add_rejKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_add_rejKeyTyped
        if(car_add_rej.getText().length() >= 9)                    
            evt.consume();
    }//GEN-LAST:event_car_add_rejKeyTyped

    private void car_add_priceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_add_priceKeyTyped
        if(car_add_price.getText().length() >= 10 || !Character.isDigit(evt.getKeyChar()))                    
            evt.consume();
    }//GEN-LAST:event_car_add_priceKeyTyped

    private void car_add_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_add_buttonActionPerformed
        String validError = validAddCarData();
        if(validError == null)
        {
            String [] carData = {
                                car_add_name.getText(), // 0 - marka
                                car_add_model.getText(),// 1 - model
                                car_add_rej.getText(),// 2 - rej
                                car_add_price.getText(),// 3 - cena
                                
                                    };            
          
            car_add_log.setForeground(Color.green);
          car_add_log.setText(addCar(carData, carAddImage));
            refreshCarList(car_tableCars);
            clearAddCarForm();
        }
        else
        {
            car_add_log.setForeground(Color.red);
            car_add_log.setText(validError);
       }
        
    }//GEN-LAST:event_car_add_buttonActionPerformed

    private void car_add_clearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_add_clearFormActionPerformed
        clearAddCarForm();
        car_add_log.setForeground(Color.BLACK);
        car_add_log.setText("");
    }//GEN-LAST:event_car_add_clearFormActionPerformed

    private void car_tableCarsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_car_tableCarsMouseReleased
     
    }//GEN-LAST:event_car_tableCarsMouseReleased

    private void car_deleteCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_deleteCarActionPerformed
        if(car_tableCars.getSelectedRow() == -1)
        {
            car_log.setForeground(Color.red);
            car_log.setText("Najpierw zaznacz pojazd na liście");
            return;
        }
        int carId = (int)car_tableCars.getModel().getValueAt(car_tableCars.getSelectedRow(), 1);        
        System.out.println(carId);
        car_log.setText(deleteCar(carId));
        refreshCarList(car_tableCars);
    }//GEN-LAST:event_car_deleteCarActionPerformed

    private void car_add_modelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_add_modelKeyTyped
        if(car_add_model.getText().length() >= 50)                    
            evt.consume();
    }//GEN-LAST:event_car_add_modelKeyTyped

    private void edit_aboutApp_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_aboutApp_ButtonActionPerformed
        hidePanels();
        panel_about.setVisible(true);
    }//GEN-LAST:event_edit_aboutApp_ButtonActionPerformed

    private void edit_carHire_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_carHire_ButtonActionPerformed
        carHire = new CarHires23();
        carHire.setVisible(true);
        mainMenu.setVisible(false);
    }//GEN-LAST:event_edit_carHire_ButtonActionPerformed

    private void edit_showHires_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_showHires_ButtonActionPerformed
        mainMenu.setVisible(false);
        hires = new MainHires(); 
        hires.setVisible(true);
    }//GEN-LAST:event_edit_showHires_ButtonActionPerformed

    private void edit_Raports_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_Raports_ButtonActionPerformed
        mainMenu.setVisible(false);
        raport = new Raport();
        raport.setVisible(true);
    }//GEN-LAST:event_edit_Raports_ButtonActionPerformed

    private void edit_Service_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_Service_ButtonActionPerformed
        mainMenu.setVisible(false);
        service = new Repair();
        service.setVisible(true);
    }//GEN-LAST:event_edit_Service_ButtonActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel about_title_text;
    private javax.swing.JLabel car_addText;
    private javax.swing.JButton car_add_button;
    private javax.swing.JButton car_add_clearForm;
    private javax.swing.JLabel car_add_log;
    private javax.swing.JTextField car_add_model;
    private javax.swing.JLabel car_add_model_text;
    private javax.swing.JTextField car_add_name;
    private javax.swing.JLabel car_add_name_text;
    private javax.swing.JTextField car_add_price;
    private javax.swing.JLabel car_add_price_text;
    private javax.swing.JTextField car_add_rej;
    private javax.swing.JLabel car_add_rej_text;
    private javax.swing.JButton car_deleteCar;
    private javax.swing.JScrollPane car_listPane;
    private javax.swing.JLabel car_log;
    private javax.swing.JTable car_tableCars;
    private javax.swing.JLabel car_title_text;
    private javax.swing.JLabel car_title_text2;
    private javax.swing.JLabel client_addText;
    private javax.swing.JButton client_add_AddButton;
    private javax.swing.JButton client_add_clearForm;
    private javax.swing.JTextField client_add_compname;
    private javax.swing.JLabel client_add_compname_text;
    private javax.swing.JButton client_add_deleteClient;
    private javax.swing.JLabel client_add_log;
    private javax.swing.JTextField client_add_name;
    private javax.swing.JLabel client_add_name_text;
    private javax.swing.JTextField client_add_nip;
    private javax.swing.JLabel client_add_nip_text;
    private javax.swing.JTextField client_add_nrtel;
    private javax.swing.JLabel client_add_nrtel_text;
    private javax.swing.JTextField client_add_pesel;
    private javax.swing.JLabel client_add_pesel_text;
    private javax.swing.JTextField client_add_surname;
    private javax.swing.JLabel client_add_surname_text;
    private javax.swing.JPanel client_panel_AddClient;
    private javax.swing.JTextField client_search;
    private javax.swing.JLabel client_serach_text;
    private javax.swing.JTable client_tableClients;
    private javax.swing.JScrollPane client_tablePane;
    private javax.swing.JLabel client_title_text;
    private javax.swing.JButton edit_Cars_Button;
    private javax.swing.JButton edit_Clients_Button;
    private javax.swing.JButton edit_Raports_Button;
    private javax.swing.JButton edit_Service_Button;
    private javax.swing.JButton edit_aboutApp_Button;
    private javax.swing.JButton edit_carHire_Button;
    private javax.swing.JButton edit_showHires_Button;
    private javax.swing.JLabel frame_background;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel loggedUser;
    private javax.swing.JPanel panel_about;
    private javax.swing.JPanel panel_car;
    private javax.swing.JPanel panel_client;
    // End of variables declaration//GEN-END:variables
}
