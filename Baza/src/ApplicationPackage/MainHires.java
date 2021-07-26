package ApplicationPackage;

import static ApplicationPackage.MainApp.*;
import static ApplicationPackage.Creators.wygladZmien;
import static ApplicationPackage.DBConnect.*;
import com.sun.glass.events.KeyEvent;
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
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class MainHires extends javax.swing.JFrame {

    JDatePickerImpl datePickerFrom;

    HireData hireData = new HireData();

    public MainHires() {
        initComponents();
        panel_end.setVisible(false);
        end_backPanel.setVisible(false);
        panel_backNext.setVisible(false);
        loadHiresTable();
        createDataPicker();

        try {
            UIManager.setLookAndFeel(wygladZmien);
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }

    }

    public void loadHiresTable() {
        int hire_nr = -1;
        int client_id = -1;
        int details_nr = -1;
        int car_id = -1;
        int counter = 1;
        String client = "", car = "", date = "", value = "";
        DefaultTableModel dtm = (DefaultTableModel) hires_table.getModel();
        dtm.setRowCount(0);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM wypozyczenie" + " WHERE zwrot" + " = 0");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                hire_nr = rs.getInt(1);
                client_id = rs.getInt(2);
                details_nr = rs.getInt(3);
                for (int i = 1; i < 4; i++) {
                    System.out.print(rs.getInt(i));
                }
                //// klient
                PreparedStatement pst = con.prepareStatement("SELECT * FROM klient" + " WHERE id_klienta" + " = " + client_id);
                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    client = result.getString(2) + " " + result.getString(3) + " " + result.getString(4);
                }
                pst.close();
                result.close();
                /// pracownik
                /////
                pst = con.prepareStatement("SELECT * FROM szczegoly" + " WHERE nr_szczegolow" + " = " + details_nr);
                result = pst.executeQuery();
                while (result.next()) {
                    car_id = result.getInt(2);
                    value = String.valueOf(result.getFloat(3));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
                    date = sdf.format(result.getDate(4)) + " - " + sdf.format(result.getDate(5));
                }
                pst.close();
                result.close();

                pst = con.prepareStatement("SELECT * FROM samochod" + " WHERE id_samochodu" + " = " + car_id);
                result = pst.executeQuery();
                while (result.next()) {
                    car = result.getString(3) + " " + result.getString(4);
                }
                pst.close();
                result.close();
                dtm.addRow(new Object[]{counter++,
                    hire_nr,
                    client,
                    car,
                    date,
                    value
                });
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void loadHireDetails(int id) {
        int client_id = -1;
        int details_id = -1;
        int car_id = -1;

        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM wypozyczenie" + " WHERE nr_wypozyczenia" + " = " + id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                client_id = rs.getInt(2);
                details_id = rs.getInt(3);
            }
            rs.close();
            statement.close();

            statement = con.prepareStatement("SELECT * FROM klient" + " WHERE id_klienta" + " = " + client_id);
            rs = statement.executeQuery();
            while (rs.next()) {
                hireData.cName = rs.getString(2);
                hireData.cSurname = rs.getString(3);
                hireData.cPesel = rs.getString(4);
                hireData.cIdnum = rs.getString(6);
            }
            rs.close();
            statement.close();

            statement = con.prepareStatement("SELECT * FROM szczegoly" + " WHERE nr_szczegolow" + " = " + details_id);
            rs = statement.executeQuery();
            while (rs.next()) {
                hireData.vId = car_id = rs.getInt(2);
                hireData.dValue = rs.getFloat(3);
                hireData.dFrom = rs.getDate(4);
                hireData.dTo = rs.getDate(5);
            }
            statement.close();
            rs.close();

            statement = con.prepareStatement("SELECT * FROM samochod" + " WHERE id_samochodu" + " = " + car_id);
            rs = statement.executeQuery();
            while (rs.next()) {
                hireData.vRej = rs.getString(2);
                hireData.vMark = rs.getString(3);
                hireData.vModel = rs.getString(4);
                hireData.vPrice = rs.getFloat(6);
            }

            hireData.id = id;
            hireData.detailsNr = details_id;
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void buildHireDesc() {
        end_client_info.setText("<html>" + hireData.cName + " " + hireData.cSurname + "<br>PESEL: " + hireData.cPesel + "</html>");
        end_car_info.setText("<html>" + hireData.vMark + " " + hireData.vModel + "<br>Cena za dzień [zł]: " + hireData.vPrice + "<br>Numer rejestracyjny: " + hireData.vRej + "<br>ID: " + hireData.vId + "</html>");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateFrom = sdf.format(hireData.dFrom);
        String dateTo = sdf.format(hireData.dTo);
        int days = daysBetween(hireData.dFrom, hireData.dTo);
        end_date_info.setText("<html>" + dateFrom + " - " + dateTo + "<br>Liczba dni: " + days + "</html>");
        end_price_info.setText("<html>" + hireData.dValue + " zł" + "<br><br>Kara za nie oddanie w terminie [dzień]: +" + (int) (hireData.vPrice + 100) + " zł</html>");
    }

    public void createEndForm(int days) {
        if (days > 0) {
            hireData.paid = (float) (days * 100 + days * hireData.vPrice);
            end_reward_text.setText("Kara za nie oddanie w terminie: " + hireData.paid + " zł");
        } else {
            end_reward_text.setText("");
            hireData.paid = 0;
        }
        end_reward_total.setText("Łączna kara do zapłaty: " + String.valueOf(hireData.paid) + " zł");
    }

    public void endOrder() {
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE szczegoly" + " SET kara" + " = " + hireData.totalPaid + " WHERE nr_szczegolow" + " = " + hireData.detailsNr);
            statement.execute();
            statement.close();

            statement = con.prepareStatement("UPDATE wypozyczenie" + " SET zwrot" + " = 1 WHERE nr_wypozyczenia" + " = " + hireData.id);
            statement.execute();
            statement.close();

            statement = con.prepareStatement("UPDATE samochod" + " SET dostepnosc" + " = 1 WHERE id_samochodu" + " = " + hireData.vId);
            statement.execute();
            statement.close();
            JOptionPane.showMessageDialog(this, "Dokonano zwrotu pojazdu");
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        left_menu = new javax.swing.JButton();
        panel_hires = new javax.swing.JPanel();
        hires_pane = new javax.swing.JScrollPane();
        hires_table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        panel_end = new javax.swing.JPanel();
        end_title = new javax.swing.JLabel();
        end_client_info = new javax.swing.JLabel();
        end_text = new javax.swing.JLabel();
        end_text1 = new javax.swing.JLabel();
        end_car_info = new javax.swing.JLabel();
        end_text2 = new javax.swing.JLabel();
        end_date_info = new javax.swing.JLabel();
        end_text3 = new javax.swing.JLabel();
        end_price_info = new javax.swing.JLabel();
        end_back = new javax.swing.JButton();
        end_hireBack = new javax.swing.JButton();
        end_backPanel = new javax.swing.JPanel();
        panel_date = new javax.swing.JPanel();
        panel_backNext = new javax.swing.JPanel();
        end_reward_text2 = new javax.swing.JLabel();
        end_reward_text = new javax.swing.JLabel();
        end_reward_destroy = new javax.swing.JTextField();
        end_reward_total = new javax.swing.JLabel();
        end_endHire = new javax.swing.JButton();
        end_date = new javax.swing.JLabel();
        end_next = new javax.swing.JButton();
        end_log = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wypożyczalnia samochodów - Wypożyczenia");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Wypożyczenia");
        getContentPane().add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 60));

        left_menu.setText("Wróć do menu");
        left_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left_menuActionPerformed(evt);
            }
        });
        getContentPane().add(left_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 470, 162, 37));

        panel_hires.setOpaque(false);
        panel_hires.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        hires_table.setAutoCreateRowSorter(true);
        hires_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lp.", "Numer wypożyczenia", "Klient", "Pojazd", "Okres", "Wartośc "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        hires_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hires_tableMouseReleased(evt);
            }
        });
        hires_pane.setViewportView(hires_table);
        if (hires_table.getColumnModel().getColumnCount() > 0) {
            hires_table.getColumnModel().getColumn(0).setPreferredWidth(50);
            hires_table.getColumnModel().getColumn(1).setPreferredWidth(50);
            hires_table.getColumnModel().getColumn(2).setPreferredWidth(170);
            hires_table.getColumnModel().getColumn(3).setPreferredWidth(170);
            hires_table.getColumnModel().getColumn(4).setPreferredWidth(170);
            hires_table.getColumnModel().getColumn(5).setPreferredWidth(170);
        }

        panel_hires.add(hires_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 800, 150));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Kliknij, aby dokonać zwrotu");
        panel_hires.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 800, 50));

        getContentPane().add(panel_hires, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 900, 520));

        panel_end.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        end_title.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        end_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        end_title.setText("Wypożyczenie:");
        panel_end.add(end_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 350, 50));

        end_client_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_client_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_client_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_client_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 270, 70));

        end_text.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text.setText("Klient:");
        panel_end.add(end_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 240, 30));

        end_text1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text1.setText("Samochód:");
        panel_end.add(end_text1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 240, 30));

        end_car_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_car_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_car_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_car_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 270, 70));

        end_text2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text2.setText("Data wypożyczenia:");
        panel_end.add(end_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 240, 30));

        end_date_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_date_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_date_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_date_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 270, 70));

        end_text3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_text3.setText("Wartość:");
        panel_end.add(end_text3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 240, 30));

        end_price_info.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        end_price_info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        end_price_info.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panel_end.add(end_price_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 270, 70));

        end_back.setBackground(new java.awt.Color(255, 102, 102));
        end_back.setText("Wróć");
        end_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                end_backActionPerformed(evt);
            }
        });
        panel_end.add(end_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 150, 30));

        end_hireBack.setText("Przyjmij zwrot");
        end_hireBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                end_hireBackActionPerformed(evt);
            }
        });
        panel_end.add(end_hireBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 80, 250, 30));

        end_backPanel.setBackground(new java.awt.Color(204, 255, 255));
        end_backPanel.setOpaque(false);
        end_backPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_date.setOpaque(false);
        end_backPanel.add(panel_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 240, 40));

        panel_backNext.setBackground(new java.awt.Color(204, 255, 255));
        panel_backNext.setOpaque(false);
        panel_backNext.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        end_reward_text2.setText("Kara za uszkodzenia: ");
        panel_backNext.add(end_reward_text2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 140, 20));

        end_reward_text.setText("Kara za nie oddanie w terminie: ");
        panel_backNext.add(end_reward_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 300, 40));

        end_reward_destroy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                end_reward_destroyKeyTyped(evt);
            }
        });
        panel_backNext.add(end_reward_destroy, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 150, 20));

        end_reward_total.setText("Łączna kara do zapłaty: ");
        panel_backNext.add(end_reward_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 290, 30));

        end_endHire.setBackground(new java.awt.Color(204, 255, 204));
        end_endHire.setText("Zwrot");
        end_endHire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                end_endHireActionPerformed(evt);
            }
        });
        panel_backNext.add(end_endHire, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 200, 30));

        end_date.setText("Data zwrotu:");
        panel_backNext.add(end_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 30));

        end_backPanel.add(panel_backNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 230));

        end_next.setText("Dalej");
        end_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                end_nextActionPerformed(evt);
            }
        });
        end_backPanel.add(end_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 140, 30));
        end_backPanel.add(end_log, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 290, 30));

        panel_end.add(end_backPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 330, 230));

        getContentPane().add(panel_end, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 1000, 520));

        background.setOpaque(true);
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void left_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left_menuActionPerformed
        this.dispose();
        mainMenu.setVisible(true);
    }//GEN-LAST:event_left_menuActionPerformed

    private void hires_tableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hires_tableMouseReleased
        loadHireDetails((int) hires_table.getValueAt(hires_table.convertRowIndexToModel(hires_table.getSelectedRow()), 1));
        buildHireDesc();
        panel_hires.setVisible(false);
        panel_end.setVisible(true);
        Calendar cal = Calendar.getInstance();
        cal.setTime(hireData.dTo);
        datePickerFrom.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }//GEN-LAST:event_hires_tableMouseReleased

    private void end_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_end_backActionPerformed
        panel_hires.setVisible(true);
        panel_end.setVisible(false);
        hireData = new HireData();
        end_date.setVisible(true);
        panel_date.setVisible(true);
        panel_backNext.setVisible(false);
        end_next.setVisible(true);
    }//GEN-LAST:event_end_backActionPerformed

    private void end_hireBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_end_hireBackActionPerformed
        end_backPanel.setVisible(true);
        panel_date.setVisible(true);
        end_date.setVisible(true);
        end_next.setVisible(true);
        panel_backNext.setVisible(false);
    }//GEN-LAST:event_end_hireBackActionPerformed

    private void end_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_end_nextActionPerformed
        Date dateBack = (Date) datePickerFrom.getModel().getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        if (sdf.format(dateBack).equals(sdf.format(hireData.dFrom))) {
            end_log.setForeground(Color.red);
            end_log.setText("Błędna data");
            return;

        } else if (hireData.dFrom.after(dateBack)) {
            end_log.setForeground(Color.red);
            end_log.setText("Wybrana błędną date");
            return;
        } else if (hireData.dFrom.before(dateBack)) {
            int days = daysBetween(hireData.dTo, dateBack);
            createEndForm(days);
            end_date.setVisible(false);
            panel_date.setVisible(false);
            panel_backNext.setVisible(true);
            end_next.setVisible(false);
            end_log.setText("");
        }
    }//GEN-LAST:event_end_nextActionPerformed

    private void end_reward_destroyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_end_reward_destroyKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != KeyEvent.VK_BACKSPACE) {
            evt.consume();
        } else if (end_reward_destroy.getText().length() + evt.getKeyChar() != 0) {
            float p;
            String add = "";
            if (evt.getKeyChar() != KeyEvent.VK_BACKSPACE) {
                add += evt.getKeyChar();
                p = Float.valueOf(end_reward_destroy.getText() + add);
                hireData.totalPaid = hireData.paid + p;
            } else if (end_reward_destroy.getText().length() >= 1) {
                p = Float.valueOf(end_reward_destroy.getText());
                hireData.totalPaid = hireData.paid + p;
            } else {
                hireData.totalPaid = hireData.paid;
            }
        } else {
            hireData.totalPaid = hireData.paid;
        }

        end_reward_total.setText("Łączna kara do zapłaty: " + hireData.totalPaid + " zł");
    }//GEN-LAST:event_end_reward_destroyKeyTyped

    private void end_endHireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_end_endHireActionPerformed
        endOrder();
        panel_end.setVisible(false);
        loadHiresTable();
        panel_hires.setVisible(true);
        panel_backNext.setVisible(false);
        end_backPanel.setVisible(false);
        Calendar cal = Calendar.getInstance();
        cal.setTime(hireData.dTo);
        datePickerFrom.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        end_reward_destroy.setText("");
        end_reward_total.setText("Łączna kara do zapłaty: 0 zł");
    }//GEN-LAST:event_end_endHireActionPerformed

    public static void main(String args[]) {
        new MainHires().setVisible(true);
    }

    public class HireData {

        public int detailsNr;
        public int id;

        public float totalPaid;
        public float paid;
        /// Client Area
        public String cName;
        public String cSurname;
        public String cPesel;
        public String cIdnum;

        /// Car area
        public String vMark;
        public String vModel;
        public int vId;
        public float vPrice;
        public String vRej;
        /// Details Area
        public Date dFrom;
        public Date dTo;
        public float dValue;

        public int daysBetween(Date d1, Date d2) {
            return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        }

    }

    public void createDataPicker() {
        LocalDateTime now = LocalDateTime.now();
        UtilDateModel modelFrom = new UtilDateModel();
        modelFrom.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
        Properties from = new Properties();
        from.put("text.today", "Today");
        from.put("text.month", "Month");
        from.put("text.year", "Year");
        JDatePanelImpl dateFrom = new JDatePanelImpl(modelFrom, from);
        datePickerFrom = new JDatePickerImpl(dateFrom, new DateLabelFormatter());
        panel_date.add(datePickerFrom);
        datePickerFrom.getModel().setSelected(true);
    }

    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

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
    private javax.swing.JButton end_back;
    private javax.swing.JPanel end_backPanel;
    private javax.swing.JLabel end_car_info;
    private javax.swing.JLabel end_client_info;
    private javax.swing.JLabel end_date;
    private javax.swing.JLabel end_date_info;
    private javax.swing.JButton end_endHire;
    private javax.swing.JButton end_hireBack;
    private javax.swing.JLabel end_log;
    private javax.swing.JButton end_next;
    private javax.swing.JLabel end_price_info;
    private javax.swing.JTextField end_reward_destroy;
    private javax.swing.JLabel end_reward_text;
    private javax.swing.JLabel end_reward_text2;
    private javax.swing.JLabel end_reward_total;
    private javax.swing.JLabel end_text;
    private javax.swing.JLabel end_text1;
    private javax.swing.JLabel end_text2;
    private javax.swing.JLabel end_text3;
    private javax.swing.JLabel end_title;
    private javax.swing.JScrollPane hires_pane;
    private javax.swing.JTable hires_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton left_menu;
    private javax.swing.JPanel panel_backNext;
    private javax.swing.JPanel panel_date;
    private javax.swing.JPanel panel_end;
    private javax.swing.JPanel panel_hires;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
