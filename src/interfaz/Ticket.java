package interfaz;

import java.awt.image.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.util.concurrent.ExecutionException;

public class Ticket extends javax.swing.JFrame {
    private String txt, id_usuario;
    private int personal;
    
    private ArrayList<String> p_nom = new ArrayList<>();
    private ArrayList<String> p_cant = new ArrayList<>();
    private ArrayList<String> p_pn = new ArrayList<>();
    private ArrayList<String> p_p = new ArrayList<>();
    private double T_a, pago;
    private ImageIcon image;
    
    public Ticket(String id_usuario, int personal, ArrayList p_nom, ArrayList p_cant, ArrayList p_pn, ArrayList p_p, double T_a, double pago) {
        image = new ImageIcon();
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(Ticket.DISPOSE_ON_CLOSE);
        this.id_usuario = id_usuario;
        this.personal = personal;
        this.p_nom = p_nom;
        this.p_cant = p_cant;
        this.p_pn = p_pn;
        this.p_p = p_p;
        this.T_a = T_a;
        this.pago = pago;
    }
    
    public void encabezado() {
        txt =  "****************************************\n";
        txt += "*           MONTOYA'S POINT            *\n";
        txt += "****************************************\n";
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image.getImage(), 0, 0, null);
            }
        };
        jBtn_Print = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        jBtn_Print.setText("Imprimir ticket");
        jBtn_Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_PrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtn_Print, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtn_Print)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Ventas obj = new Ventas(id_usuario, personal, p_nom, p_cant, p_pn, p_p, T_a, pago);
        obj.setEnabled(true);
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        int total_prods = p_nom.size();
        Dimension frm = this.getSize(),
                  pnl = jPanel1.getSize();
        
        this.setSize(new Dimension(frm.width, frm.height + (30 * total_prods)));
        jPanel1.setPreferredSize(new Dimension(pnl.width, pnl.height + (30 * total_prods)));
        jPanel1.setSize(new Dimension(pnl.width, pnl.height + (30 * total_prods)));
        
        int w = jPanel1.getWidth(),
            h = jPanel1.getHeight();
        
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        ImageIcon icon = new ImageIcon("src\\interfaz\\logo.png");
        
        int y = 20,
            headerRectHeight = 15,
            x = 16;
        
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        //Background
        
        g2d.setColor(Color.black);
        g2d.drawImage(icon.getImage(), 130, -10, 90, 90, rootPane); y += sdL(7);
        g2d.drawString("-------------------------------------",x,y); y += sdL(2);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2d.drawString("     MUEBLERIA MONTOYA     ",x,y); y += headerRectHeight;
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2d.drawString("     Dirección: Plza. Juárez #114    ",x,y); y += headerRectHeight;
        g2d.drawString("          Tel. 449 273 0487          ",x,y); y += headerRectHeight;
        g2d.drawString("-------------------------------------",x,y); y += headerRectHeight;
        
        g2d.drawString(" Descripción                 Precio  ",x,y); y += headerRectHeight;
        g2d.drawString("-------------------------------------",x,y); y += headerRectHeight;

        for (int s = 0; s < total_prods; s++) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2d.drawString(" "+p_nom.get(s)+"                            ",x,y); y += headerRectHeight;
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 13));
            g2d.drawString("      "+p_cant.get(s)+" * "+StoM(p_p.get(s)),x,y); g2d.drawString(space(StoM(p_pn.get(s))) + StoM(p_pn.get(s)),x,y);
            if (s == total_prods - 1) y += headerRectHeight;
            else y += headerRectHeight;
        }
        
        g2d.drawString("-------------------------------------",x,y);y += sdL(1);
        g2d.drawString(" Total     :",x,y); g2d.drawString(space(DtoM(T_a)) + DtoM(T_a),x,y);
        y += sdL(1);
        g2d.drawString("-------------------------------------",x,y);y += sdL(1);
        g2d.drawString(" Pago      :",x,y); g2d.drawString(space(DtoM(pago)) + DtoM(pago),x,y);
        y += sdL(1);
        g2d.drawString("-------------------------------------",x,y); y += sdL(1);
        g2d.drawString(" Cambio    :",x,y); g2d.drawString(space(DtoM(pago - T_a)) + DtoM(pago - T_a),x,y);
        y += sdL(2);

        g2d.drawString("*************************************",x,y);y += headerRectHeight;
        g2d.drawString("      ¡ GRACIAS, VUELVA PRONTO !     ",x,y); y += headerRectHeight;
        g2d.drawString("*************************************",x,y);
        
        g2d.dispose();
        
        try {
            ImageIO.write(bi, "jpg", new File("ticket.jpg"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.WARNING_MESSAGE);
        }
        //Crear el archivo ticket.jpg
        
        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() throws Exception {
                return ImageIO.read(new File("ticket.jpg"));
            }
            
            @Override
            protected void done() {
                try {
                    image.setImage(get());
                    jPanel1.repaint();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                catch(ExecutionException e) {
                    e.printStackTrace();
                }
                //System.out.println("Ticket actualizado");
            }
        }.execute();
        //Muestra de ticket en digital
        
        this.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened
    private int sdL (int n) {
        return 10 * n;
    }
    private String StoM (String t) {
        Double n = Double.parseDouble(t);
        return NumberFormat.getCurrencyInstance().format(n);
    }
    private String DtoM (Double n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }
    private String space (String txt) {
        String s = "                                    ";
        for (int i=0; i<txt.length(); i++) {
            s = s.substring(0, s.length() - 1);
        }
            
        return s;
    }
    
    private void jBtn_PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn_PrintActionPerformed
        PrinterJob pj = PrinterJob.getPrinterJob();        
        pj.setPrintable(new Print_tick(),getPageFormat(pj));
        try {
            pj.print();
        }
        catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error", "Error de impresión: " + ex, JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jBtn_PrintActionPerformed
    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    

        double bodyH = Double.valueOf(p_nom.size());
        
        double bodyHeight = bodyH * 1.5;  
        double headerHeight = 5.0;                  
        double footerHeight = 6;        
        double width = cm_to_pp(8); 
        double height = cm_to_pp(headerHeight+bodyHeight+footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(0,10,width,height - cm_to_pp(1));  

        pf.setOrientation(PageFormat.PORTRAIT);  
        pf.setPaper(paper);    

        return pf;
    }
    protected static double cm_to_pp(double cm) {            
	return toPPI(cm * 0.393600787);            
    }
 
    protected static double toPPI(double inch) {            
	return inch * 72d;            
    }
    public class Print_tick implements Printable {
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            int result = NO_SUCH_PAGE;
            if (pageIndex == 0) {
                Graphics2D g2d = (Graphics2D) graphics;                                                  
                g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY());
                try {
                    int total_prods = p_nom.size();;

                    int w = jPanel1.getWidth(),
                        h = jPanel1.getHeight();

                    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    ImageIcon icon = new ImageIcon("src\\interfaz\\logo.png");

                    int y = -4,
                        headerRectHeight = 15,
                        x = -4;
                     int y2 = -4,
                        x2 = -14;

                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9)); //13 (4 menos)
                    g2d.setColor(Color.white);
                    g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                    //Background

                    g2d.setColor(Color.black);
                    g2d.drawImage(icon.getImage(), 32, -10, 70, 70, rootPane); y += sdL(5);
                    g2d.drawString("\n\n",x,y); y += sdL(2);
                    g2d.drawString("-------------------------------------",x,y); y += sdL(2);
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 10));
                    g2d.drawString("     MUEBLERIA MONTOYA     ",x2,y); y += headerRectHeight;
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 6));
                    g2d.drawString("     Dirección: Plza. Juárez #114    ",x,y); y += headerRectHeight;
                    g2d.drawString("          Tel. 449 273 0487          ",x,y); y += headerRectHeight;
                    g2d.drawString("-------------------------------------",x,y); y += headerRectHeight;

                    g2d.drawString(" Descripción                 Precio  ",x,y); y += headerRectHeight;
                    g2d.drawString("-------------------------------------",x,y); y += headerRectHeight;

                    for (int s = 0; s < total_prods; s++) {
                        g2d.setFont(new Font("Monospaced", Font.BOLD, 6));
                        g2d.drawString(" "+p_nom.get(s)+"                            ",x,y); y += headerRectHeight;
                        g2d.setFont(new Font("Monospaced", Font.PLAIN, 6));
                        g2d.drawString("      "+p_cant.get(s)+" * "+StoM(p_p.get(s)),x,y); g2d.drawString(space(StoM(p_pn.get(s))) + StoM(p_pn.get(s)),x,y);
                        if (s == total_prods - 1) y += headerRectHeight;
                        else y += headerRectHeight;
                    }

                    g2d.drawString("-------------------------------------",x,y);y += sdL(1);
                    g2d.drawString(" Total     :",x,y); g2d.drawString(space(DtoM(T_a)) + DtoM(T_a),x,y);
                    y += sdL(1);
                    g2d.drawString("-------------------------------------",x,y);y += sdL(1);
                    g2d.drawString(" Pago      :",x,y); g2d.drawString(space(DtoM(pago)) + DtoM(pago),x,y);
                    y += sdL(1);
                    g2d.drawString("-------------------------------------",x,y); y += sdL(1);
                    g2d.drawString(" Cambio    :",x,y); g2d.drawString(space(DtoM(pago - T_a)) + DtoM(pago - T_a),x,y);
                    y += sdL(2);

                    g2d.drawString("*************************************",x,y);y += headerRectHeight;
                    g2d.drawString("      ¡ GRACIAS, VUELVA PRONTO !     ",x,y); y += headerRectHeight;
                    g2d.drawString("*************************************",x,y);
                    
                    g2d.dispose();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                
                result = PAGE_EXISTS;
            }
            
            return result;
        }
    }
    // REDIMENSIONAR TICKET DE IMPRESIÓN EN ESTA CLASE //
    
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
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ArrayList<String> nulo = new ArrayList<>();
                new Ticket("",0,nulo,nulo,nulo,nulo,0,0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtn_Print;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}