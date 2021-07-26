package ApplicationPackage;

import static ApplicationPackage.MainApp.*;
import static ApplicationPackage.Creators.wygladZmien;
import static ApplicationPackage.DBConnect.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Comparator;
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
import javax.swing.table.TableRowSorter;
import javax.swing.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class Raport extends JFrame 
{   
    JDatePickerImpl dateFromPicker;
    JDatePickerImpl dateToPicker;
    public TableData tableData = new TableData();

    public Raport() 
    {
        initComponents();
        refreshDetailsList();
        try {
            UIManager.setLookAndFeel(wygladZmien);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}
        panel_date.setVisible(false);
        date_text.setVisible(false);
        createDatePickers();
        left_date_show.setVisible(false);
        car_pane1.setVisible(true);
       
       
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
        dateFromPicker = new JDatePickerImpl(dateFrom, new DateLabelFormatter());         
        panel_date.add(dateFromPicker);
        dateFromPicker.getModel().setSelected(true);
        
        UtilDateModel modelTo = new UtilDateModel(); 
        modelTo.setDate( now.getYear(), now.getMonthValue()-1 ,now.getDayOfMonth()+1);
        Properties to = new Properties();
        to.put("text.today", "Today");
        to.put("text.month", "Month");
        to.put("text.year", "Year");  
        JDatePanelImpl dateTo = new JDatePanelImpl(modelTo, to);
        dateToPicker = new JDatePickerImpl(dateTo, new DateLabelFormatter());         
        panel_date.add(dateToPicker);
        dateToPicker.getModel().setSelected(true);        
    }
    
     public void refreshDetailsList()
    {
        car_list1.setModel(new DefaultTableModel(new Object[0][], new String[] {
                "Lp.", "id", "Nazwa", "Nr rejestracyjny", "Wypożyczeń", "Dni wypożyczone", "Naprawy", "Koszt wypożyczeń" }) {
            Class[] types = { Integer.class, Integer.class, String.class,
                    String.class, Integer.class, Integer.class, Integer.class, Float.class  };
            boolean[] canEdit = { false, false, false, false, false, false, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            public boolean isCellEditable(int columnIndex) {
                return this.canEdit[columnIndex];
            }
        });
        DefaultTableModel dtm = (DefaultTableModel) car_list1.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        int counter = 1;
        try {
            String query = "SELECT * FROM samochod"  + " WHERE dostepnosc ="+ "1";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            while (result.next()) 
            {
                tableData.lp = counter++;
                tableData.id = result.getInt(1);
                tableData.name = result.getString(3) + " " + result.getString(4);
                tableData.nrrej = result.getString(2);
                
                query = "SELECT COUNT(*) FROM wypozyczenie"  + " INNER JOIN szczegoly" + " ON wypozyczenie"  + ".szczegoly_wypozyczenia" + " = szczegoly" + ".nr_szczegolow" + " WHERE id_samochodu" + " = " + tableData.id + " AND zwrot" + " = 1";
                PreparedStatement statement = con.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.hiresCount = rs.getInt(1);
                }
                rs.close();
                statement.close();
                
                query = "SELECT SUM(DATEDIFF(data_do"  + ", data_od" + ")) FROM wypozyczenie" + " INNER JOIN szczegoly" +  " ON wypozyczenie" +  ".szczegoly_wypozyczenia" +  " = szczegoly" +  ".nr_szczegolow"  + " WHERE id_samochodu"  + " = " + tableData.id + " AND zwrot" + " = 1";
                statement = con.prepareStatement(query);
                rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.hiresDays = rs.getInt(1);
                }
                rs.close();
                statement.close();
                
                query = "SELECT COUNT(*) FROM naprawa"  + " WHERE id_samochodu"  + " = " + tableData.id + " AND czy_ukonczono"  + " = 1";                
                statement = con.prepareStatement(query);
                rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.servies = rs.getInt(1);
                }                
                rs.close();
                statement.close();
                
                query = "SELECT SUM(koszt_wypozyczenia" + ") FROM wypozyczenie"  + " INNER JOIN szczegoly"  + " ON wypozyczenie"  + ".szczegoly_wypozyczenia" + " = szczegoly" +  ".nr_szczegolow" +  " WHERE id_samochodu" +  " = " + tableData.id + " AND zwrot" + " = 1";
                statement = con.prepareStatement(query);
                rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.hiresPrice = rs.getFloat(1);
                }
                rs.close();
                statement.close();
                dtm.addRow(new Object[]{
                                        tableData.lp,
                                        tableData.id,
                                        tableData.name,
                                        tableData.nrrej,
                                        tableData.hiresCount,
                                        tableData.hiresDays,
                                        tableData.servies,
                                        tableData.hiresPrice
                                        }); 
            }
          result.close();
          pst.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        car_list1.setEnabled(false);
    }
    
    public void refreshWithDate(Date from, Date to)
    {   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
         String dateFrom = sdf.format(from);
         String dateTo =  sdf.format(to);
         car_list1.setModel(new DefaultTableModel(new Object[0][], new String[] {
                "Lp.", "id", "Nazwa", "Nr rejestracyjny", "Wypożyczeń", "Dni wypożyczone", "Naprawy", "Koszt wypożyczeń" }) {
            Class[] types = { Integer.class, Integer.class, String.class,
                    String.class, Integer.class, Integer.class, Integer.class, Float.class  };
            boolean[] canEdit = { true, false, false, false, false, false, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            public boolean isCellEditable(int columnIndex) {
                return this.canEdit[columnIndex];
            }
        });
        
        DefaultTableModel dtm = (DefaultTableModel) car_list1.getModel();
        
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();   
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        int counter = 1;
        try {
            String query = "SELECT * FROM samochod"  + " WHERE dostepnosc" + " != 0";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            while (result.next()) 
            {
                tableData.lp = counter++;
                tableData.id = result.getInt(1);
                tableData.name = result.getString(3) + " " + result.getString(4);
                tableData.nrrej = result.getString(2);
                
                query = "SELECT COUNT(*) FROM wypozyczenie"  + " INNER JOIN szczegoly"  + " ON wypozyczenie"  + ".szczegoly_wypozyczenia" +  " = szczegoly" +  ".nr_szczegolow" +  " WHERE id_samochodu" +  " = " + tableData.id + " AND zwrot"  + " = 1" + " AND data_od"  + " >= '"  + "' AND data_do"  + " <= '" + dateTo +"'";
                PreparedStatement statement = con.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.hiresCount = rs.getInt(1);
                }
                rs.close();
                statement.close();
                
                query = "SELECT SUM(DATEDIFF(data_do"  + ", data_od"  + ")) FROM wypozyczenie"  + " INNER JOIN szczegoly"  + " ON wypozyczenie"  + ".szczegoly_wypozyczenia"  + " = szczegoly"  + ".nr_szczegolow"  + " WHERE id_samochodu"  + " = " + tableData.id + " AND zwrot"  + " = 1" + " AND data_od" + " >= '" + dateFrom + "' AND data_do"  + " <= '" + dateTo +"'";;
                statement = con.prepareStatement(query);
                rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.hiresDays = rs.getInt(1);
                }
                rs.close();
                statement.close();
                
                query = "SELECT COUNT(*) FROM naprawa"  + " WHERE id_samochodu"  + " = " + tableData.id + " AND czy_ukonczono"  + " = 1";                
                statement = con.prepareStatement(query);
                rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.servies = rs.getInt(1);
                }                
                rs.close();
                statement.close();
                
                query = "SELECT SUM(koszt_wypozyczenia"  + ") FROM wypozyczenie" + " INNER JOIN szczegoly"  + " ON wypozyczenie"  + ".szczegoly_wypozyczenia"  + " = szczegoly" + ".nr_szczegolow"  + " WHERE id_samochodu"  + " = " + tableData.id + " AND zwrot"  + " = 1" + " AND data_od"  + " >= '" + dateFrom + "' AND data_do"  + " <= '" + dateTo +"'";;
                statement = con.prepareStatement(query);
                rs = statement.executeQuery();
                while(rs.next())
                {
                    tableData.hiresPrice = rs.getFloat(1);
                }
                rs.close();
                statement.close();
                dtm.addRow(new Object[]{
                                        tableData.lp,
                                        tableData.id,
                                        tableData.name,
                                        tableData.nrrej,
                                        tableData.hiresCount,
                                        tableData.hiresDays,
                                        tableData.servies,
                                        tableData.hiresPrice
                                        }); 
            }
          result.close();
          pst.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
         
         
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        left_backToMenu = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        left_log = new javax.swing.JLabel();
        date_text = new javax.swing.JLabel();
        car_pane1 = new javax.swing.JScrollPane();
        car_list1 = new javax.swing.JTable();
        left_buttonDate = new javax.swing.JButton();
        left_buttonAll = new javax.swing.JButton();
        left_date_show = new javax.swing.JButton();
        panel_date = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wypożyczalnia samochodów - Raporty");
        setMinimumSize(new java.awt.Dimension(700, 500));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        left_backToMenu.setText("Wróć do menu");
        left_backToMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_backToMenuActionPerformed(evt);
            }
        });
        getContentPane().add(left_backToMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 170, 30));

        title.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Statystyki wypożyczeń");
        getContentPane().add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 60));

        left_log.setForeground(new java.awt.Color(255, 0, 0));
        getContentPane().add(left_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 510, 240, 30));

        date_text.setText("<html>Od:<br><br>Do:");
        getContentPane().add(date_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 300, 60, 90));

        car_list1.setAutoCreateRowSorter(true);
        car_list1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Lp.", "id", "Nazwa", "Nr rejestracyjny", "Wypożyczeń", "Dni wypożyczone", "Naprawy", "Koszt wypożyczeń"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        car_list1.setMaximumSize(new java.awt.Dimension(20000, 64));
        car_pane1.setViewportView(car_list1);
        if (car_list1.getColumnModel().getColumnCount() > 0) {
            car_list1.getColumnModel().getColumn(0).setPreferredWidth(50);
            car_list1.getColumnModel().getColumn(1).setPreferredWidth(50);
            car_list1.getColumnModel().getColumn(2).setPreferredWidth(200);
            car_list1.getColumnModel().getColumn(3).setPreferredWidth(180);
            car_list1.getColumnModel().getColumn(4).setPreferredWidth(70);
            car_list1.getColumnModel().getColumn(5).setPreferredWidth(80);
            car_list1.getColumnModel().getColumn(6).setPreferredWidth(60);
            car_list1.getColumnModel().getColumn(7).setPreferredWidth(120);
        }

        getContentPane().add(car_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 550, 150));

        left_buttonDate.setText("Pokaż w przedziale czasowym");
        left_buttonDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_buttonDateActionPerformed(evt);
            }
        });
        getContentPane().add(left_buttonDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, 240, 30));

        left_buttonAll.setText("Pokaż całość");
        left_buttonAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_buttonAllActionPerformed(evt);
            }
        });
        getContentPane().add(left_buttonAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 240, 30));

        left_date_show.setText("Akceptuj");
        left_date_show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_date_showActionPerformed(evt);
            }
        });
        getContentPane().add(left_date_show, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, 120, 30));

        panel_date.setOpaque(false);
        getContentPane().add(panel_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 260, 80));

        jButton4.setText("Koszty Wypożyczenia");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 200, 30));

        jButton3.setText("Najcześciej popsute");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 200, 30));

        jButton2.setText("Najdłużej wypożyczane");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 200, 30));

        jButton1.setText("Najczęściej wypożyczane");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 200, 30));

        background.setForeground(new java.awt.Color(255, 255, 255));
        background.setMinimumSize(new java.awt.Dimension(900, 500));
        background.setOpaque(true);
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void left_backToMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_backToMenuActionPerformed
        this.dispose();
        mainMenu.setVisible(true);
    }//GEN-LAST:event_left_backToMenuActionPerformed

    private void left_buttonAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_buttonAllActionPerformed
        panel_date.setVisible(false);
        refreshDetailsList();
        date_text.setVisible(false);
        left_log.setText("");
        left_date_show.setVisible(false);
    }//GEN-LAST:event_left_buttonAllActionPerformed

    private void left_buttonDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_buttonDateActionPerformed
        panel_date.setVisible(true);
        date_text.setVisible(true);
        left_log.setText("");
        left_date_show.setVisible(true);
    }//GEN-LAST:event_left_buttonDateActionPerformed

    private void left_date_showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_date_showActionPerformed
        left_log.setText("");
        Date dateFrom = (Date) dateFromPicker.getModel().getValue();
        Date dateTo = (Date) dateToPicker.getModel().getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYY");
        if( dateFrom == null || dateTo == null)
        {
            left_log.setText("Najpierw wybierz date");
            return;
        }
        if(sdf.format(dateFrom).equals(sdf.format(dateTo)))
        {
            left_log.setText("Wskaza daty są takie same");
            return;
        }
        else if(dateFrom.compareTo(dateTo) > 0)
        {
            left_log.setText("<html>Data \"do\" musi być późniejsza<br> niż data \"od\"<html>");
            return;
        }
        else
        {
            refreshWithDate(dateFrom, dateTo);
        }
    }//GEN-LAST:event_left_date_showActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int size = car_list1.getRowCount();
        System.out.println(size);
        String [] text = new String[size];
        int [] values = new int[size];
        for(int i = 0; i < size; i++)
        {
            text[i] = (String) car_list1.getModel().getValueAt(i,2);
            values[i] =(int) car_list1.getModel().getValueAt(i, 4);
        }
        ChartBar c = new ChartBar("Wykres","Najczęściej wypożyczane samochody","Samochód","Ilość napraw", text, values);
        c.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       int size = car_list1.getRowCount();
       if(size < 1)
        {
            JOptionPane.showMessageDialog(car_pane1, "W podanym okresie czasu brak wypożyczeń");
            return;
        }
        String [] text = new String[size];
        int [] values = new int[size];
        for(int i = 0; i < size; i++)
        {
            text[i] = (String) car_list1.getModel().getValueAt(i,2);
            values[i] =(int) car_list1.getModel().getValueAt(i, 5);
        }
        Chart c = new Chart("Wykres","Najdłużej wypożyczane samochody", text, values);
        c.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int size = car_list1.getRowCount();
        if(size < 1)
        {
            JOptionPane.showMessageDialog(car_pane1, "W podanym okresie czasu brak wypożyczeń");
            return;
        }
        String [] text = new String[size];
        int [] values = new int[size];
        for(int i = 0; i < size; i++)
        {
            text[i] = (String) car_list1.getModel().getValueAt(i,2);
            values[i] =(int) car_list1.getModel().getValueAt(i, 6);
        }
        Chart c = new Chart("Wykres","Najczęściej psujące sie samochody", text, values);
        c.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int size = car_list1.getRowCount();
        if(size < 1)
        {
            JOptionPane.showMessageDialog(car_pane1, "W podanym okresie czasu brak wypożyczeń");
            return;
        }
        String [] text = new String[size];
        int [] values = new int[size];
        for(int i = 0; i < size; i++)
        {
            text[i] = (String) car_list1.getModel().getValueAt(i,2);
            float f= (float) car_list1.getModel().getValueAt(i, 7);
            values[i] =(int)f;
        }
        ChartBar c = new ChartBar("Wykres","Koszty wypożyczenia","Samochód","Kwota", text, values);
        
        c.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed
    
    public static void main(String args[]) 
    {
       new Raport().setVisible(true);
    }
    
    public class TableData
    {
        public int lp;
        public int id;
        public String name;
        public String nrrej;
        public int hiresCount;
        public int hiresDays;
        public int servies;
        public float hiresPrice;
        
        public TableData()
        {
            id = hiresCount = hiresDays = servies = lp = 0;
            name = nrrej = "";
            hiresPrice = 0;
        }
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
    private javax.swing.JTable car_list1;
    private javax.swing.JScrollPane car_pane1;
    private javax.swing.JLabel date_text;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton left_backToMenu;
    private javax.swing.JButton left_buttonAll;
    private javax.swing.JButton left_buttonDate;
    private javax.swing.JButton left_date_show;
    private javax.swing.JLabel left_log;
    private javax.swing.JPanel panel_date;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
