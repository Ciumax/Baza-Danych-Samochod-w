package ApplicationPackage;

import static ApplicationPackage.MainApp.*;
import static ApplicationPackage.TimeConfig.*;
import static ApplicationPackage.Creators.wygladZmien;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class Repair extends javax.swing.JFrame 
{
    
    JDatePickerImpl dateFromPicker;
    JDatePickerImpl dateToPicker;

    public Repair() 
    {
        initComponents();
        try {
            UIManager.setLookAndFeel(wygladZmien);
        } catch (ClassNotFoundException ex) {} 
        catch (InstantiationException ex) {} 
        catch (IllegalAccessException ex) {} 
        catch (UnsupportedLookAndFeelException ex) {}
        hidePanels();
        panel_leftCar.setVisible(true);
        refreshCarList();
        refreshCarSeriveList();
     

    }
    
    public void hidePanels()
    {
        panel_getCar.setVisible(false);
        panel_leftCar.setVisible(false);
    }
    
    

    public void refreshCarList()
    {
        String query = "SELECT id_samochodu"  + ", marka"  + ", model"  + ", cena_dzien" + ", nr_rejestracyjny" + " FROM samochod"  + /*" INNER JOIN szczegoly_samochodu"+ databasePostfix +" ON samochod"+ databasePostfix +".szczegoly_samochodu"+ fieldPostfix+" = szczegoly_samochodu" + databasePostfix + ".id_szczegolow" + fieldPostfix +*/ " WHERE dostepnosc"  + " = 1";        
         
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
                dtm.addRow(new Object[]{counter++,rs.getInt(1), rs.getString(2),rs.getString(3),rs.getFloat(4), rs.getString(5)});
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public void refreshCarSeriveList()
    {
       
        String query = "SELECT id_samochodu" + ", marka"  + ", model" + ", cena_dzien" +  ", nr_rejestracyjny" +  " FROM samochod" +  /*" INNER JOIN szczegoly_samochodu"+ databasePostfix +" ON samochod"+ databasePostfix +".szczegoly_samochodu"+ fieldPostfix+" = szczegoly_samochodu" + databasePostfix + ".id_szczegolow" + fieldPostfix +*/ " WHERE dostepnosc" + " = 3";        
          
        DefaultTableModel dtm = (DefaultTableModel) car_service.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        int counter = 1;
        try {
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                dtm.addRow(new Object[]{counter++,rs.getInt(1), rs.getString(2),rs.getString(3),rs.getFloat(4), rs.getString(5)});
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public void leaveCarInService(int carId)
    {
        try {
            String query = "INSERT INTO naprawa"  + " VALUES (NULL, ?, ?, 0)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, carId);
            statement.setFloat(2, Float.valueOf(details_price.getText()));
            statement.execute();
            statement.close();
            
            query = "UPDATE samochod" + " SET dostepnosc" + " = 3 WHERE id_samochodu"  + " = " + carId ;
            statement = con.prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        
    }
    
    public void getCar(int carId)
    {
         
        try {
            String query = "UPDATE naprawa"  + " SET czy_ukonczono" + " = 1 WHERE id_samochodu" + " = " + carId + " AND czy_ukonczono"  + " = 0 ";
            PreparedStatement statement = con.prepareStatement(query);
            statement.execute();
            statement.close();
            
            query = "UPDATE samochod" +  " SET dostepnosc"+ " = 1 WHERE id_samochodu" + " = " + carId;
            statement = con.prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        left_getCar1 = new javax.swing.JButton();
        left_getCar = new javax.swing.JButton();
        left_leftCar = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        panel_getCar = new javax.swing.JPanel();
        get_title = new javax.swing.JLabel();
        car_service_pane = new javax.swing.JScrollPane();
        car_service = new javax.swing.JTable();
        details_leave1 = new javax.swing.JButton();
        details_log1 = new javax.swing.JLabel();
        panel_leftCar = new javax.swing.JPanel();
        car_title_text2 = new javax.swing.JLabel();
        car_listPane = new javax.swing.JScrollPane();
        car_tableCars = new javax.swing.JTable();
        details_price = new javax.swing.JTextField();
        details_text = new javax.swing.JLabel();
        details_leave = new javax.swing.JButton();
        details_log = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wypożyczalnia samochodów - Serwis pojazdów");
        setMinimumSize(new java.awt.Dimension(700, 550));
        setPreferredSize(new java.awt.Dimension(700, 500));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        left_getCar1.setText("Wróć");
        left_getCar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_getCar1ActionPerformed(evt);
            }
        });
        getContentPane().add(left_getCar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 150, 30));

        left_getCar.setText("Odbierz");
        left_getCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_getCarActionPerformed(evt);
            }
        });
        getContentPane().add(left_getCar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 150, 30));

        left_leftCar.setText("Oddaj");
        left_leftCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_leftCarActionPerformed(evt);
            }
        });
        getContentPane().add(left_leftCar, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 150, 30));

        title.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Naprawa");
        getContentPane().add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(-90, 0, 390, 60));

        panel_getCar.setOpaque(false);
        panel_getCar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        get_title.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        get_title.setText("Odbierz pojazd");
        panel_getCar.add(get_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 320, 40));

        car_service.setAutoCreateRowSorter(true);
        car_service.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lp.", "id.", "Marka", "Model", "Cena/dzień[zł]", "Numer rejestracyjny"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        car_service.getTableHeader().setReorderingAllowed(false);
        car_service.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                car_serviceMouseReleased(evt);
            }
        });
        car_service_pane.setViewportView(car_service);
        if (car_service.getColumnModel().getColumnCount() > 0) {
            car_service.getColumnModel().getColumn(0).setPreferredWidth(50);
            car_service.getColumnModel().getColumn(1).setPreferredWidth(50);
        }

        panel_getCar.add(car_service_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 620, 180));

        details_leave1.setText("Odbierz");
        details_leave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                details_leave1ActionPerformed(evt);
            }
        });
        panel_getCar.add(details_leave1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 180, 30));
        panel_getCar.add(details_log1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 440, 260, 30));

        getContentPane().add(panel_getCar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 720, 350));

        panel_leftCar.setPreferredSize(new java.awt.Dimension(600, 500));
        panel_leftCar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        car_title_text2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        car_title_text2.setText("Oddaj do serwisu");
        panel_leftCar.add(car_title_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 310, 50));

        car_tableCars.setAutoCreateRowSorter(true);
        car_tableCars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lp.", "id.", "Marka", "Model", "Cena/dzień[zł]", "Numer rejestracyjny"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
            car_tableCars.getColumnModel().getColumn(0).setPreferredWidth(50);
            car_tableCars.getColumnModel().getColumn(1).setPreferredWidth(50);
        }

        panel_leftCar.add(car_listPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 560, 210));

        details_price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                details_priceKeyTyped(evt);
            }
        });
        panel_leftCar.add(details_price, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 130, 20));

        details_text.setText("Kosz naprawy [zł]:");
        panel_leftCar.add(details_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 110, 20));

        details_leave.setText("Oddaj");
        details_leave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                details_leaveActionPerformed(evt);
            }
        });
        panel_leftCar.add(details_leave, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 310, 180, 30));

        details_log.setForeground(new java.awt.Color(255, 255, 255));
        panel_leftCar.add(details_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 550, 260, 30));

        getContentPane().add(panel_leftCar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 720, 350));

        background.setOpaque(true);
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(-370, 60, 1120, 440));
        background.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void left_leftCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_leftCarActionPerformed
        hidePanels();
        panel_leftCar.setVisible(true);
        panel_getCar.setVisible(false);
        details_log.setText("");
        refreshCarList();
    }//GEN-LAST:event_left_leftCarActionPerformed

    private void left_getCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_getCarActionPerformed
        hidePanels();
        panel_getCar.setVisible(true);
        refreshCarSeriveList();
    }//GEN-LAST:event_left_getCarActionPerformed

    private void car_tableCarsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_car_tableCarsMouseReleased
        
        
    }//GEN-LAST:event_car_tableCarsMouseReleased

    private void details_leaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_details_leaveActionPerformed
        if(details_price.getText().length() <= 0)
        {
            details_log.setForeground(Color.red);
            details_log.setText("Wprowadź koszt naprawy");
            return;
        }
        details_log.setText("");
        
        leaveCarInService((int) car_tableCars.getModel().getValueAt(car_tableCars.convertRowIndexToModel(car_tableCars.getSelectedRow()), 1));
        refreshCarList();
        
        
    }//GEN-LAST:event_details_leaveActionPerformed

    private void details_priceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_details_priceKeyTyped
        if( ! Character.isDigit(evt.getKeyChar()))
        {
            evt.consume();
        }
    }//GEN-LAST:event_details_priceKeyTyped

    private void car_serviceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_car_serviceMouseReleased
    }//GEN-LAST:event_car_serviceMouseReleased

    private void details_leave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_details_leave1ActionPerformed
        getCar((int) car_service.getModel().getValueAt(car_service.convertRowIndexToModel(car_service.getSelectedRow()), 1));
        refreshCarSeriveList();
    }//GEN-LAST:event_details_leave1ActionPerformed

    private void left_getCar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_getCar1ActionPerformed
        this.dispose();
        mainMenu.setVisible(true);
    }//GEN-LAST:event_left_getCar1ActionPerformed

    public static void main(String args[]) 
    {
        new Repair().setVisible(true);
    }

    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter 
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
    private javax.swing.JScrollPane car_listPane;
    private javax.swing.JTable car_service;
    private javax.swing.JScrollPane car_service_pane;
    private javax.swing.JTable car_tableCars;
    private javax.swing.JLabel car_title_text2;
    private javax.swing.JButton details_leave;
    private javax.swing.JButton details_leave1;
    private javax.swing.JLabel details_log;
    private javax.swing.JLabel details_log1;
    private javax.swing.JTextField details_price;
    private javax.swing.JLabel details_text;
    private javax.swing.JLabel get_title;
    private javax.swing.JButton left_getCar;
    private javax.swing.JButton left_getCar1;
    private javax.swing.JButton left_leftCar;
    private javax.swing.JPanel panel_getCar;
    private javax.swing.JPanel panel_leftCar;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
