/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;
import com.jtattoo.plaf.noire.NoireLookAndFeel;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 *
 * @author Usuario
 */
public class index extends javax.swing.JFrame {
    /**
     * Creates new form index
     */
      Connection conexion;
    Statement sentencia;
    
    private ArrayList<String> p_nom = new ArrayList<String>();
    private ArrayList<String> p_cant = new ArrayList<String>();
    private ArrayList<String> p_pn = new ArrayList<String>();
    private ArrayList<String> p_p = new ArrayList<String>();
    
    public index() {
        initComponents();
        conectatarBaseDatos();
        setLocationRelativeTo(null);
    }
    
        
    public void conectatarBaseDatos() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); //Linea que carga el driver
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar Dirver");
        }
        try {
  conexion = DriverManager.getConnection("jdbc:ucanaccess://src\\base\\MDB.accdb");
//En esta parte tenemos que cambiar la ruta en la que se encuentra nuestra base de datos 
//Ejemplo "jdbc:ucanaccess://C:\\Proyecto.accdb" hace referencia que esta en el disco local C
            System.out.println(" exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la dirección de la base de datos");
        }
        try {
            sentencia = conexion.createStatement();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear la conexión con la base de datos");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JLbl_Titulo = new javax.swing.JLabel();
        JLbl_UsuarioT = new javax.swing.JLabel();
        JLbl_ContraT = new javax.swing.JLabel();
        JTxtF_UsuarioT = new javax.swing.JTextField();
        JBtn_AccederT = new javax.swing.JButton();
        JBtn_SalirT = new javax.swing.JButton();
        JPwdF_ContraT = new javax.swing.JPasswordField();
        jMenuBar1 = new javax.swing.JMenuBar();
        JMn_CambiarT = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JLbl_Titulo.setText("Ingresar como Trabajador");

        JLbl_UsuarioT.setText("Usuario");

        JLbl_ContraT.setText("Contraseña");

        JTxtF_UsuarioT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTxtF_UsuarioTActionPerformed(evt);
            }
        });
        JTxtF_UsuarioT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTxtF_UsuarioTKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_UsuarioTKeyTyped(evt);
            }
        });

        JBtn_AccederT.setText("Acceder");
        JBtn_AccederT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_AccederTActionPerformed(evt);
            }
        });

        JBtn_SalirT.setText("Salir");
        JBtn_SalirT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_SalirTActionPerformed(evt);
            }
        });

        JPwdF_ContraT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JPwdF_ContraTKeyPressed(evt);
            }
        });

        JMn_CambiarT.setText("Ingresar como Administrador");
        JMn_CambiarT.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                JMn_CambiarTMenuSelected(evt);
            }
        });
        jMenuBar1.add(JMn_CambiarT);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(JBtn_AccederT, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(236, 236, 236))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(JBtn_SalirT, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(JLbl_UsuarioT, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLbl_ContraT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTxtF_UsuarioT, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JPwdF_ContraT, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(JLbl_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(246, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(JLbl_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLbl_UsuarioT, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTxtF_UsuarioT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(JLbl_ContraT, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JPwdF_ContraT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addComponent(JBtn_AccederT, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(JBtn_SalirT, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBtn_AccederTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_AccederTActionPerformed
        try {
        String usuario=JTxtF_UsuarioT.getText();
        String password=String.valueOf(JPwdF_ContraT.getPassword());
        String query="SELECT * FROM Trabajador WHERE Id='"+usuario+"' and Contraseña='"+password+"'";
        sentencia=conexion.createStatement();
        ResultSet rs=sentencia.executeQuery(query);
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Ingresando...");
            Ventas acceder = new Ventas(usuario, 0, p_nom, p_cant, p_pn, p_p, 0, 0);
            Ventas.JMn_Admnisitrar.setEnabled(false);
            Ventas.JTbdPn_Procesos.setEnabledAt(5,false);
            Ventas.JTbdPn_Procesos.setEnabledAt(4,false);
            Ventas.JTbdPn_Procesos.setEnabledAt(3,false);
            acceder.setVisible(true);
            acceder.setEnabled(true);
            this.setVisible(false);
            this.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Error... Revise sus datos");
        }
    } catch (SQLException ex) {
        Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_JBtn_AccederTActionPerformed

    private void JBtn_SalirTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_SalirTActionPerformed
        System.exit(0);//Boton para cerrar la app
    }//GEN-LAST:event_JBtn_SalirTActionPerformed

    private void JMn_CambiarTMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_JMn_CambiarTMenuSelected
        IngresarAdmin camAdmin=new IngresarAdmin(p_nom, p_cant, p_pn, p_p, 0, 0);//Mostramos el otro JFrame y ocultamos este
        camAdmin.setVisible(true);
        camAdmin.setEnabled(true);
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_JMn_CambiarTMenuSelected

    private void JPwdF_ContraTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JPwdF_ContraTKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {//Evaluamos si la tecla presionada sea "ENTER" para acceder.
            try {
            String usuario=JTxtF_UsuarioT.getText();
            String password=String.valueOf(JPwdF_ContraT.getPassword());
            String query="SELECT * FROM Trabajador WHERE Id='"+usuario+"' and Contraseña='"+password+"'";
            sentencia=conexion.createStatement();
            ResultSet rs=sentencia.executeQuery(query);
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Ingresando...");
                Ventas acceder = new Ventas(usuario, 0, p_nom, p_cant, p_pn, p_p, 0, 0);
                Ventas.JMn_Admnisitrar.setEnabled(false);
                Ventas.JTbdPn_Procesos.setEnabledAt(5,false);
                Ventas.JTbdPn_Procesos.setEnabledAt(4,false);
                Ventas.JTbdPn_Procesos.setEnabledAt(3,false);
                acceder.setVisible(true);
                acceder.setEnabled(true);
                this.setVisible(false);
                this.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Error... Revise sus datos");
            }
            } catch (SQLException ex) {
                Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
    }//GEN-LAST:event_JPwdF_ContraTKeyPressed

    private void JTxtF_UsuarioTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_UsuarioTKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
            JPwdF_ContraT.requestFocus();
    }//GEN-LAST:event_JTxtF_UsuarioTKeyPressed

    private void JTxtF_UsuarioTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTxtF_UsuarioTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTxtF_UsuarioTActionPerformed

    private void JTxtF_UsuarioTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_UsuarioTKeyTyped
          char bb=evt.getKeyChar();
        
        if(Character.isLetter(bb)){
           getToolkit().beep();
           evt.consume();
            JOptionPane.showMessageDialog(rootPane, "Ingrese solo numeros\n");
           } 
    }//GEN-LAST:event_JTxtF_UsuarioTKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new NoireLookAndFeel());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<String> nulo = new ArrayList<>();
                new index().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBtn_AccederT;
    private javax.swing.JButton JBtn_SalirT;
    private javax.swing.JLabel JLbl_ContraT;
    private javax.swing.JLabel JLbl_Titulo;
    private javax.swing.JLabel JLbl_UsuarioT;
    private javax.swing.JMenu JMn_CambiarT;
    private javax.swing.JPasswordField JPwdF_ContraT;
    private javax.swing.JTextField JTxtF_UsuarioT;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
}
