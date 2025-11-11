
package mini_project;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import mini_project.db.databaseconnection;
import mini_project.security.securityutil;

public class PasswordManager extends javax.swing.JFrame {
     
    private JLabel lblTitle,lblUser,lblPass;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnSave, btnRecover;
    
    public PasswordManager() {
        setTitle("Secure Password Manager");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        lblTitle = new JLabel("Password Manager");
        lblTitle.setFont(new java.awt.Font("Arial", 1, 20));
        lblTitle.setBounds(100, 10, 250, 30);
        add(lblTitle);
        
        lblUser = new JLabel("Name:");
        lblUser.setBounds(50, 60, 100, 25);
        add(lblUser);
        
        txtUser = new JTextField();
        txtUser.setBounds(150, 60, 180, 25);
        add(txtUser);
                
        lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 100, 100, 25);
        add(lblPass);
        
         txtPass = new JPasswordField();
        txtPass.setBounds(150, 100, 180, 25);
        add(txtPass);
        
        btnSave = new JButton("Save");
        btnSave.setBounds(80, 150, 100, 30);
        add(btnSave);
        
        
        btnRecover = new JButton("Recover");
        btnRecover.setBounds(200, 150, 100, 30);
        add(btnRecover);
       
        btnSave.addActionListener(e -> savePassword());
        btnRecover.addActionListener(e -> recoverPassword());
        initComponents();
    }
    
    private void savePassword(){
         String name = txtUser.getText().trim();
        String password = new String(txtPass.getPassword());

        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both name and password");
            return;
        }

        String salt = securityutil.generateSalt();
        String hash = securityutil.hashPassword(password, salt);
        String encrypted = securityutil.encryptAES(password, password, salt);

        String sql = "INSERT INTO passwords (name, password, password_hash, salt, password_encrypted) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, hash);
            ps.setString(4, salt);
            ps.setString(5, encrypted);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Password saved successfully!");
            txtUser.setText("");
            txtPass.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    private void recoverPassword(){
        String name = JOptionPane.showInputDialog(this, "Enter name to recover:");
        String enteredPassword = JOptionPane.showInputDialog(this, "Enter master password for decryption:");

        if (name == null || enteredPassword == null || name.isEmpty() || enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inputs cannot be empty!");
            return;
        }

        String sql = "SELECT password_encrypted, salt, password_hash FROM passwords WHERE name = ? LIMIT 1";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String enc = rs.getString("password_encrypted");
                String salt = rs.getString("salt");
                String storedHash = rs.getString("password_hash");

                String enteredHash = securityutil.hashPassword(enteredPassword, salt);
                if (!enteredHash.equals(storedHash)) {
                    JOptionPane.showMessageDialog(this, "Wrong password! Cannot decrypt.");
                    return;
                }

                String plain = securityutil.decryptAES(enc, enteredPassword, salt);
                JOptionPane.showMessageDialog(this, "Recovered password: " + plain);
            } else {
                JOptionPane.showMessageDialog(this, "No record found for name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PasswordManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
