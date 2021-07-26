package ApplicationPackage;

import static ApplicationPackage.MainApp.*;
import static ApplicationPackage.Creators.albumNumberToInt;
import static ApplicationPackage.Creators.wygladZmien;
import static ApplicationPackage.DBConnect.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class CarHires23 extends javax.swing.JFrame 
{
    private JDatePickerImpl datePickerFrom;
    private JDatePickerImpl datePickerTo;
    
    ClientData clientData = new ClientData();
    CarData carData = new CarData();
    DateData dateData = new DateData();
    private float orderValue = 0;
    public CarHires23()
    {
        initComponents();
        defaultSettings();
        createDatePickers();
        try {
            UIManager.setLookAndFeel(wygladZmien);
        } catch (ClassNotFoundException ex) {} 
        catch (InstantiationException ex) {} 
        catch (IllegalAccessException ex) {} 
        catch (UnsupportedLookAndFeelException ex) {}
    }
    
    public void defaultSettings()
    {
        hidePanels();
        car_next.setVisible(false);
        panel_client.setVisible(true);
        mainMenu.refreshClientList(client_list);
      
    }    
    
    public void hidePanels()
    {
        panel_car.setVisible(false);
        panel_client.setVisible(false);
        panel_details.setVisible(false);
        panel_end.setVisible(false);        
    }
    public ClientData getClientId(String pesel)
    {   
        ClientData temp = new ClientData();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT id_klienta" +  ", imie" + ", nazwisko"  + ", pesel"  + " FROM klient" + " WHERE pesel" + " =\"" + pesel + "\"");
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                temp.id =  rs.getInt(1);
                temp.name = rs.getString(2);
                temp.surname = rs.getString(3);
                temp.pesel = rs.getString(4);
              
                return temp;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }
    
    public void refreshCarList()
    {
        String company = "";
        String model = "";
        String price = "";
        String airCon = "";
        
        
        String query = "SELECT id_samochodu"  + ", marka"  + ", model"  + ", cena_dzien"  + " FROM samochod"  /*+ " INNER JOIN szczegoly_samochodu"+ databasePostfix +" ON samochod"+ databasePostfix +".szczegoly_samochodu"+ fieldPostfix+" = szczegoly_samochodu" + databasePostfix + ".id_szczegolow" + fieldPostfix */+ " WHERE dostepnosc" + " = 1";        
        if(car_filters.isSelected())
        {
            if(car_filterMa.getText().length() > 0)
                company = " AND marka" + " LIKE \"" + car_filterMa.getText() + "%\"";
            if(car_filterMo.getText().length() > 0)
                model = " AND model"  + " LIKE \"" + car_filterMo.getText() + "%\"";
            if(car_filterPr.getText().length() > 0)
                price = " AND cena_dzien"  + " < " + car_filterPr.getText();
           
        }
        query += company + model + price + airCon;
        DefaultTableModel dtm = (DefaultTableModel) car_tableCars.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        int counter = 1;
        try {
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                dtm.addRow(new Object[]{counter++,rs.getInt(1), rs.getString(2),rs.getString(3),rs.getFloat(4)});
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(CarHires23.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buildCarDesc(int carId)
    {
        try {            
            PreparedStatement statement = con.prepareStatement("SELECT * FROM samochod" + " WHERE id_samochodu"+ " = " + carId);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                car_name.setText(rs.getString(3) + " " + rs.getString(4));
                car_rej.setText("<html>Nr rejestracyjny: <span style=\"color:red;\">" + rs.getString(2) + "</span></html>");
                car_price.setText("<html>Cena za dzień [zł]: <span style=\"color:red;\">" + rs.getFloat(5) + "</span></html>");
                
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public CarData getCarData(int carId)
    {
        CarData temp = new CarData();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT id_samochodu" + ", nr_rejestracyjny" + ", marka"  + ", model" + ", cena_dzien"  +" FROM samochod" + " WHERE id_samochodu"  + " =\"" + carId + "\"");
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                temp.id =  rs.getInt(1);
                temp.rej = rs.getString(2);
                temp.mark = rs.getString(3);
                temp.model = rs.getString(4);
                temp.price = rs.getFloat(5);
                return temp;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }
    
    
    public void createDatePickers()
    {
         LocalDateTime now = LocalDateTime.now();   
         UtilDateModel modelFrom = new UtilDateModel();
         modelFrom.setDate( now.getYear(), now.getMonthValue()-1 ,now.getDayOfMonth());
         Properties from = new Properties();
         from.put("text.today", "Today");
         from.put("text.month", "Month");
         from.put("text.year", "Year");         
         JDatePanelImpl dateFrom = new JDatePanelImpl(modelFrom, from);
         datePickerFrom = new JDatePickerImpl(dateFrom, new DateLabelFormatter());         
         details_panel_date.add(datePickerFrom);
         datePickerFrom.getModel().setSelected(true);
         
         UtilDateModel modelTo = new UtilDateModel();
         modelTo.setDate( now.getYear(), now.getMonthValue()-1 ,now.getDayOfMonth()+1);
         Properties to = new Properties();
         to.put("text.today", "Today");
         to.put("text.month", "Month");
         to.put("text.year", "Year");      
         JDatePanelImpl dateTo = new JDatePanelImpl(modelTo, to);
         datePickerTo = new JDatePickerImpl(dateTo, new DateLabelFormatter());   
         details_panel_date.add(datePickerTo);
         datePickerTo.getModel().setSelected(true);
    }
    
    public boolean validData()
    {
        Date from = (Date)datePickerFrom.getModel().getValue();
        Date to = (Date)datePickerTo.getModel().getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYY");
        if(from == null || to == null)
        {
            details_log.setText("Wybierz date wypożyczenia");
            return false;
        }
        else if(from.compareTo(to) > 0)
        {
            details_log.setText("Data \"do\" musi być późniejsza niż data \"od\"");
            return false;
        }
        else if( sdf.format(from).equals(sdf.format(to)))
        {
            details_log.setText("Wskazane daty są takie same");
            return false;
        }
        else
        {
            dateData.from = from;
            dateData.to = to;
            details_log.setText("");
            return true;
        }
        
    }
    
    public int daysBetween(Date d1, Date d2)
    {
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    public void loadOrderData()
    {
        end_client_info.setText("<html>" + clientData.name + " "+ clientData.surname + "<br>PESEL: " + clientData.pesel +"</html>");
        end_car_info.setText("<html>"+ carData.mark + " " + carData.model + "<br>Cena za dzień [zł]: " + carData.price + "<br>Numer rejestracyjny: " + carData.rej + "</html>");        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateFrom = sdf.format(dateData.from);
        String dateTo = sdf.format(dateData.to);        
        int days = daysBetween(dateData.from, dateData.to);
        end_date_info.setText("<html>" + dateFrom + " - " + dateTo + "<br>Liczba dni: " + days +"</html>" );
        orderValue = days * carData.price;
        end_price_info.setText("<html>" + orderValue +" zł" + "<br><br><span style= \"color:red;\">Kara za nie oddanie w terminie [każdy następny dzień]: +" + (int)(carData.price+ 100) + " zł</span></html>");
    }
    
    public boolean sendOrder()
    {
        int lastInsertedId = -1;
        try {           
            java.sql.Date sqlDateFrom = new java.sql.Date(dateData.from.getTime());
            java.sql.Date sqlDateTo = new java.sql.Date(dateData.to.getTime());  //System.out.println("sendOrder()= " + clientData.id);           
            PreparedStatement statement = con.prepareStatement("INSERT INTO szczegoly"  + " (nr_szczegolow" + ", id_samochodu" + ", koszt_wypozyczenia" + ", data_od"  + ", data_do"  + ", kara" + ") VALUES (NULL, ?, ?, ?, ?, 0)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, carData.id);System.out.println("sendOrder()= " + carData.id);  
            statement.setFloat(2, orderValue);
            statement.setDate(3, sqlDateFrom);
            statement.setDate(4, sqlDateTo);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next())
                lastInsertedId = rs.getInt(1);
            rs.close();
            statement.close();
			
            statement = con.prepareStatement("INSERT INTO wypozyczenie" + " (nr_wypozyczenia" +  ", id_klienta"+  ", szczegoly_wypozyczenia"+  ") VALUES (NULL, ?, ?)");
			
            statement.setInt(1, clientData.id);           
            statement.setInt(2, lastInsertedId);
            statement.execute();
            statement.close();
            
            statement = con.prepareStatement("UPDATE samochod" +  " SET dostepnosc" +  " = 2 WHERE id_samochodu" +  " = " + carData.id);
            statement.execute();
            statement.close();
            return true;
        } catch (SQLException ex) {
            System.err.println(ex);
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        status_back = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        panel_client = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        client_tablePane = new javax.swing.JScrollPane();
        client_list = new javax.swing.JTable();
        client_serach_text = new javax.swing.JLabel();
        client_search = new javax.swing.JTextField();
        client_next = new javax.swing.JButton();
        client_log = new javax.swing.JLabel();
        panel_details = new javax.swing.JPanel();
        details_back = new javax.swing.JButton();
        details_date_text = new javax.swing.JLabel();
        details_panel_date = new javax.swing.JPanel();
        details_dateTo = new javax.swing.JLabel();
        details_dateFrom = new javax.swing.JLabel();
        details_next = new javax.swing.JButton();
        details_log = new javax.swing.JLabel();
        panel_end = new javax.swing.JPanel();
        end_price_info = new javax.swing.JLabel();
        end_text3 = new javax.swing.JLabel();
        end_date_info = new javax.swing.JLabel();
        end_text2 = new javax.swing.JLabel();
        end_text1 = new javax.swing.JLabel();
        end_title = new javax.swing.JLabel();
        end_text = new javax.swing.JLabel();
        end_car_info = new javax.swing.JLabel();
        end_client_info = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        end_order = new javax.swing.JButton();
        panel_car = new javax.swing.JPanel();
        car_listPane = new javax.swing.JScrollPane();
        car_tableCars = new javax.swing.JTable();
        car_title_text2 = new javax.swing.JLabel();
        car_rej = new javax.swing.JLabel();
        car_price = new javax.swing.JLabel();
        car_vpower = new javax.swing.JLabel();
        car_color = new javax.swing.JLabel();
        car_airCon = new javax.swing.JLabel();
        car_trans = new javax.swing.JLabel();
        car_drive = new javax.swing.JLabel();
        car_year = new javax.swing.JLabel();
        car_body = new javax.swing.JLabel();
        car_name = new javax.swing.JLabel();
        car_filters = new javax.swing.JCheckBox();
        car_text1 = new javax.swing.JLabel();
        car_text2 = new javax.swing.JLabel();
        car_text3 = new javax.swing.JLabel();
        car_filterMa = new javax.swing.JTextField();
        car_filterMo = new javax.swing.JTextField();
        car_filterPr = new javax.swing.JTextField();
        car_next = new javax.swing.JButton();
        car_back = new javax.swing.JButton();
        car_log = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wypożyczalnia samochodów - Nowe wypożyczenie");
        setMinimumSize(new java.awt.Dimension(900, 500));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        status_back.setText("Wróc do menu");
        status_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status_backActionPerformed(evt);
            }
        });
        getContentPane().add(status_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 630, 120, 30));

        title.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Nowe wypożyczenie");
        getContentPane().add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 430, 60));

        panel_client.setBackground(new java.awt.Color(204, 255, 204));
        panel_client.setMinimumSize(new java.awt.Dimension(710, 500));
        panel_client.setOpaque(false);
        panel_client.setPreferredSize(new java.awt.Dimension(710, 500));
        panel_client.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Wybierz klienta:");
        panel_client.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 640, 50));

        client_list.setAutoCreateRowSorter(true);
        client_list.setModel(new javax.swing.table.DefaultTableModel(
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
        client_tablePane.setViewportView(client_list);

        panel_client.add(client_tablePane, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 640, 180));

        client_serach_text.setText("Szukaj klienta(pesel):");
        panel_client.add(client_serach_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 250, 30));

        client_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                client_searchKeyTyped(evt);
            }
        });
        panel_client.add(client_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 310, 220, 30));

        client_next.setText("Dalej");
        client_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                client_nextActionPerformed(evt);
            }
        });
        panel_client.add(client_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 310, 210, 30));
        panel_client.add(client_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 410, 30));

        getContentPane().add(panel_client, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 730, 620));

        panel_details.setBackground(new java.awt.Color(204, 255, 204));
        panel_details.setMinimumSize(new java.awt.Dimension(430, 500));
        panel_details.setOpaque(false);
        panel_details.setPreferredSize(new java.awt.Dimension(430, 500));
        panel_details.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        details_back.setText("Wróć");
        details_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                details_backActionPerformed(evt);
            }
        });
        panel_details.add(details_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 140, 30));

        details_date_text.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        details_date_text.setText("Data wypożyczenia:");
        panel_details.add(details_date_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 410, 50));

        details_panel_date.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        details_panel_date.setOpaque(false);
        panel_details.add(details_panel_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 230, 90));

        details_dateTo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        details_dateTo.setText("Data do: ");
        panel_details.add(details_dateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 100, 30));

        details_dateFrom.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        details_dateFrom.setText("Data od: ");
        panel_details.add(details_dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 30));

        details_next.setText("Podsumowanie");
        details_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                details_nextActionPerformed(evt);
            }
        });
        panel_details.add(details_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 160, 30));

        details_log.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        details_log.setForeground(new java.awt.Color(255, 255, 255));
        panel_details.add(details_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 420, 40));

        getContentPane().add(panel_details, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 730, 520));

        panel_end.setMinimumSize(new java.awt.Dimension(700, 500));
        panel_end.setPreferredSize(new java.awt.Dimension(700, 500));
        panel_end.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        end_price_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_price_info.setForeground(new java.awt.Color(0, 153, 51));
        end_price_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_price_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_price_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, 330, 70));

        end_text3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text3.setText("Wartość wypożyczenia:");
        panel_end.add(end_text3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 230, 370, 30));

        end_date_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_date_info.setForeground(new java.awt.Color(0, 153, 51));
        end_date_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_date_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_date_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 270, 70));

        end_text2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text2.setText("Okres:");
        panel_end.add(end_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 370, 30));

        end_text1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text1.setText("Pojazd:");
        panel_end.add(end_text1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 370, 30));

        end_title.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        end_title.setText("Podsumowanie wypożyczenia");
        panel_end.add(end_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 370, 50));

        end_text.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text.setText("Dane klienta:");
        panel_end.add(end_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 370, 30));

        end_car_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_car_info.setForeground(new java.awt.Color(0, 153, 51));
        end_car_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_car_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_car_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 280, 80));

        end_client_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_client_info.setForeground(new java.awt.Color(0, 153, 51));
        end_client_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_client_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_client_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 260, 80));

        jButton1.setText("Cofnij");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panel_end.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, 170, 30));

        end_order.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_order.setText("Akceptuj");
        end_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                end_orderActionPerformed(evt);
            }
        });
        panel_end.add(end_order, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 380, 190, 30));

        getContentPane().add(panel_end, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 730, 520));

        panel_car.setMinimumSize(new java.awt.Dimension(720, 500));
        panel_car.setPreferredSize(new java.awt.Dimension(720, 500));
        panel_car.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        car_tableCars.setAutoCreateRowSorter(true);
        car_tableCars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lp.", "id.", "Marka", "Model", "Cena/dzień[zł]"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            car_tableCars.getColumnModel().getColumn(0).setPreferredWidth(30);
            car_tableCars.getColumnModel().getColumn(1).setPreferredWidth(30);
            car_tableCars.getColumnModel().getColumn(2).setPreferredWidth(100);
            car_tableCars.getColumnModel().getColumn(3).setPreferredWidth(100);
            car_tableCars.getColumnModel().getColumn(4).setPreferredWidth(90);
        }

        panel_car.add(car_listPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 350, 160));

        car_title_text2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        car_title_text2.setText("Lista pojazdów:");
        panel_car.add(car_title_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 310, 50));

        car_rej.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_rej, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 240, 30));

        car_price.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_price, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 240, 30));

        car_vpower.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_vpower, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 280, 30));

        car_color.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_color, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 240, 30));

        car_airCon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_airCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 240, 30));

        car_trans.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_trans, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 240, 30));

        car_drive.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_drive, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 240, 30));

        car_year.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_year, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 240, 30));

        car_body.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panel_car.add(car_body, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 240, 30));

        car_name.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        panel_car.add(car_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 590, 50));

        car_filters.setText("Uwzględnij filtry ");
        car_filters.setOpaque(false);
        car_filters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_filtersActionPerformed(evt);
            }
        });
        panel_car.add(car_filters, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 240, 120, -1));

        car_text1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        car_text1.setText("Marka:");
        panel_car.add(car_text1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, 90, 20));

        car_text2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        car_text2.setText("Model: ");
        panel_car.add(car_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 310, 90, 20));

        car_text3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        car_text3.setText("Cena/dzień [zł] (mniejsza niż):");
        panel_car.add(car_text3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, 180, 20));

        car_filterMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_filterMaActionPerformed(evt);
            }
        });
        car_filterMa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_filterMaKeyTyped(evt);
            }
        });
        panel_car.add(car_filterMa, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 280, 170, 20));

        car_filterMo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_filterMoKeyTyped(evt);
            }
        });
        panel_car.add(car_filterMo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, 170, -1));

        car_filterPr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                car_filterPrKeyTyped(evt);
            }
        });
        panel_car.add(car_filterPr, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 340, 170, -1));

        car_next.setText("Dalej");
        car_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_nextActionPerformed(evt);
            }
        });
        panel_car.add(car_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 380, 210, 30));

        car_back.setText("Wróć");
        car_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                car_backActionPerformed(evt);
            }
        });
        panel_car.add(car_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 380, 140, 30));

        car_log.setForeground(new java.awt.Color(255, 255, 255));
        panel_car.add(car_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 690, 440, 30));

        getContentPane().add(panel_car, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 730, 520));

        background.setOpaque(true);
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(-410, 70, 1170, 420));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void client_searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_searchKeyTyped
        String searchFilter = "pesel";
        if(evt == null)
        {
            mainMenu.refreshClientList(client_list);
            return;
        }
        if(Character.isDigit(evt.getKeyChar()) || Character.isLetter(evt.getKeyChar()))
        {
            client_search.setText(client_search.getText() + evt.getKeyChar());
            evt.consume();
        }
        mainMenu.searchClientWithFilter(client_list, searchFilter, client_search.getText());
        if(client_search.getText().length() == 0)
        {
            mainMenu.refreshClientList(client_list);
        }
    }//GEN-LAST:event_client_searchKeyTyped

    private void client_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_client_nextActionPerformed
        if( client_list.getSelectedRow() == -1)
        {
            client_log.setForeground(Color.red);
            client_log.setText("Zaznacz klienta na liście");
            return;
        }
        String selectedPesel = (String) client_list.getModel().getValueAt(client_list.convertRowIndexToModel(client_list.getSelectedRow()), 3);
        clientData =  getClientId(selectedPesel);
        panel_client.setVisible(false);
        panel_car.setVisible(true);
        refreshCarList();
        client_log.setText("");
    }//GEN-LAST:event_client_nextActionPerformed

    private void car_tableCarsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_car_tableCarsMouseReleased
       
        int viewRow = car_tableCars.getSelectedRow();
        int modelRow = car_tableCars.convertRowIndexToModel(viewRow);        
        int id = (int)car_tableCars.getModel().getValueAt(modelRow, 1);
        car_next.setVisible(true);
        
        buildCarDesc(id);
        
       
    }//GEN-LAST:event_car_tableCarsMouseReleased

    private void car_filterMaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_filterMaKeyTyped
       if(Character.isLetter(evt.getKeyChar()) || Character.isDigit(evt.getKeyChar()))
        {            
            car_filterMa.setText(car_filterMa.getText() + evt.getKeyChar());       
            evt.consume();
        }
        refreshCarList();
    }//GEN-LAST:event_car_filterMaKeyTyped

    private void car_filterMoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_filterMoKeyTyped
        if(Character.isLetter(evt.getKeyChar()) || Character.isDigit(evt.getKeyChar()))
        {            
            car_filterMo.setText(car_filterMo.getText() + evt.getKeyChar());       
            evt.consume();
        }
        refreshCarList();
    }//GEN-LAST:event_car_filterMoKeyTyped

    private void car_filterPrKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_filterPrKeyTyped
        if(Character.isLetter(evt.getKeyChar()) || Character.isDigit(evt.getKeyChar()))
        {            
            car_filterPr.setText(car_filterPr.getText() + evt.getKeyChar());       
            evt.consume();
        }
        refreshCarList();
    }//GEN-LAST:event_car_filterPrKeyTyped

    private void car_filterMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_filterMaActionPerformed
        
    }//GEN-LAST:event_car_filterMaActionPerformed

    private void car_filtersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_filtersActionPerformed
        refreshCarList();
    }//GEN-LAST:event_car_filtersActionPerformed

    private void car_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_nextActionPerformed
        if(car_tableCars.getSelectedRow() == -1)
        {
            car_log.setForeground(Color.red);
            car_log.setText("Najpierw wybierz pojazd");
            return;
        }
        int selectedRow = car_tableCars.getSelectedRow();
        int modelRow = car_tableCars.convertRowIndexToModel(selectedRow);
        int carId = (int) car_tableCars.getModel().getValueAt(modelRow, 1);
        carData = getCarData(carId);
        panel_car.setVisible(false);
        panel_details.setVisible(true);
        car_log.setText("");
    }//GEN-LAST:event_car_nextActionPerformed

    private void status_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status_backActionPerformed
        this.dispose();
        mainMenu.setVisible(true);
    }//GEN-LAST:event_status_backActionPerformed

    private void car_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_car_backActionPerformed
        panel_car.setVisible(false);
        panel_client.setVisible(true);
    }//GEN-LAST:event_car_backActionPerformed

    private void details_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_details_backActionPerformed
        panel_details.setVisible(false);
        panel_car.setVisible(true);        
    }//GEN-LAST:event_details_backActionPerformed

    private void details_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_details_nextActionPerformed
        if(! validData())        
            return;
        panel_details.setVisible(false);
        panel_end.setVisible(true);
        loadOrderData();
    }//GEN-LAST:event_details_nextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        panel_end.setVisible(false);
        panel_details.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void end_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_end_orderActionPerformed
        if(sendOrder())
        {
            JOptionPane.showMessageDialog(this, "Pomyślnie wypożyczono samochód!");
            this.dispose();
            mainMenu.setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Pomyślnie wypożyczono samochód!");
            return;
        }
    }//GEN-LAST:event_end_orderActionPerformed

    public static void main(String args[])
    {
        new CarHires23().setVisible(true);
    }
    
    public class DateData
    {
        public Date from;
        public Date to;
    }
    
    public class CarData
    {
        public int id;
        public String rej;
        public String mark;
        public String model;
        public float price;
    }
    
    public class ClientData
    {
        public int id;
        public String name;
        public String surname;
        public String pesel;
        //public String idnum;
    }
    
    
    
    public class DateLabelFormatter extends AbstractFormatter 
    {
        private String datePattern = "dd-MM-yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel car_airCon;
    private javax.swing.JButton car_back;
    private javax.swing.JLabel car_body;
    private javax.swing.JLabel car_color;
    private javax.swing.JLabel car_drive;
    private javax.swing.JTextField car_filterMa;
    private javax.swing.JTextField car_filterMo;
    private javax.swing.JTextField car_filterPr;
    private javax.swing.JCheckBox car_filters;
    private javax.swing.JScrollPane car_listPane;
    private javax.swing.JLabel car_log;
    private javax.swing.JLabel car_name;
    private javax.swing.JButton car_next;
    private javax.swing.JLabel car_price;
    private javax.swing.JLabel car_rej;
    private javax.swing.JTable car_tableCars;
    private javax.swing.JLabel car_text1;
    private javax.swing.JLabel car_text2;
    private javax.swing.JLabel car_text3;
    private javax.swing.JLabel car_title_text2;
    private javax.swing.JLabel car_trans;
    private javax.swing.JLabel car_vpower;
    private javax.swing.JLabel car_year;
    private javax.swing.JTable client_list;
    private javax.swing.JLabel client_log;
    private javax.swing.JButton client_next;
    private javax.swing.JTextField client_search;
    private javax.swing.JLabel client_serach_text;
    private javax.swing.JScrollPane client_tablePane;
    private javax.swing.JButton details_back;
    private javax.swing.JLabel details_dateFrom;
    private javax.swing.JLabel details_dateTo;
    private javax.swing.JLabel details_date_text;
    private javax.swing.JLabel details_log;
    private javax.swing.JButton details_next;
    private javax.swing.JPanel details_panel_date;
    private javax.swing.JLabel end_car_info;
    private javax.swing.JLabel end_client_info;
    private javax.swing.JLabel end_date_info;
    private javax.swing.JButton end_order;
    private javax.swing.JLabel end_price_info;
    private javax.swing.JLabel end_text;
    private javax.swing.JLabel end_text1;
    private javax.swing.JLabel end_text2;
    private javax.swing.JLabel end_text3;
    private javax.swing.JLabel end_title;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panel_car;
    private javax.swing.JPanel panel_client;
    private javax.swing.JPanel panel_details;
    private javax.swing.JPanel panel_end;
    private javax.swing.JButton status_back;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
