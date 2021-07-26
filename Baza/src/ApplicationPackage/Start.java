package ApplicationPackage;

import static ApplicationPackage.DBConnect.*;
import static ApplicationPackage.MainApp.mainMenu;
import static ApplicationPackage.Creators.*;
import static ApplicationPackage.Creators.wygladZmien;
import static ApplicationPackage.MainApp.con;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Start extends JFrame
{       int login=0;
        String log,haslo;
         String  Data;
           PreparedStatement st;
        ResultSet rs;
    public Start() 
{
       
   /*    
       
       NIE DZIAŁA I JEST GŁUPIE
    try {
        Data="SELECT imie FROM pracownik";
        
        rs = st.executeQuery(Data);
    
    
        while(rs.next())
        {
            Login.addItem(rs.getString(1));
        }
        
        }catch(Exception e){
        JOptionPane.showMessageDialog(null, "ERROR");
       
        }
       
       
       
       
       */
        initComponents();
        try {
            UIManager.setLookAndFeel(wygladZmien);
        } catch (ClassNotFoundException ex) {} 
        catch (InstantiationException ex) {} 
        catch (IllegalAccessException ex) {} 
        catch (UnsupportedLookAndFeelException ex) {}
        createAppDesc();
        createAppDesc2();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JLabel();
        goToApp = new javax.swing.JButton();
        about = new javax.swing.JLabel();
        about1 = new javax.swing.JLabel();

        background.setOpaque(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 300));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        goToApp.setText("Login");
        goToApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToAppActionPerformed(evt);
            }
        });
        getContentPane().add(goToApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 160, 30));
        getContentPane().add(about, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 290, 170));
        getContentPane().add(about1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 350, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goToAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToAppActionPerformed
       if(login==0){
       this.dispose();
       mainMenu.setVisible(true);
       }
       else{
           System.out.print("Błedne hasło");
       } 
    }//GEN-LAST:event_goToAppActionPerformed
    public static void main(String args[]) 
    {
       new Start().setVisible(true);
    }
    
    public void createAppDesc()
    {
        String text = "<html>";
        
       
         text +=  authorName2 + "<br/>";
          text += "Nr. Albumu: "+"<br/>"; 
       
        text +=  authorNr2 + "<br/>";
        text += "</html>";
        about.setText(text);
        about.setFont(new Font("Tahoma", Font.PLAIN, 18));
        
    }
 public void createAppDesc2()
    {
        String text = "<html>";
        text += "Autorzy: "+"<br/>"; 
        text += authorName1 + "<br/>";
        text += "Nr. Albumu: "+"<br/>"; 
        text +=   authorNr1 + "<br/>";
        text += "</html>";
        about1.setText(text);
        about1.setFont(new Font("Tahoma", Font.PLAIN, 18));
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel about;
    private javax.swing.JLabel about1;
    private javax.swing.JLabel background;
    private javax.swing.JButton goToApp;
    // End of variables declaration//GEN-END:variables
}
