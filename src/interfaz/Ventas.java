package interfaz;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Ventas extends javax.swing.JFrame {
    Connection conexion;
    Statement sentencia;
    
    private final String id_usuario;
    private final int personal;
    private int compraT = 0;
    private double Total_anterior = 0, pago = 0;
    /*
        0 = TRABAJADOR
        1 = ADMINISTRADOR
    */
    private ArrayList<String> p_nom = new ArrayList<String>();
    private ArrayList<String> p_cant = new ArrayList<String>();
    private ArrayList<String> p_pn = new ArrayList<String>();
    private ArrayList<String> p_p = new ArrayList<String>();
    private double sum;

    DefaultTableModel tabla = new DefaultTableModel() {//Creamos un modelo de tabla, este sera para la seccion compras
        @Override
        public boolean isCellEditable(int Fila, int Colum) {
            return false;
        }
    };
   
    
    DefaultTableModel tblcon = new DefaultTableModel() {//Creamos un modelo de tabla, este0 sera para la seccion consultas, altas y editar
        @Override
        
        public boolean isCellEditable(int Fila, int Colum) {
            
            return false;
        }
    }; 
    
    
    DefaultTableModel Tblhis = new DefaultTableModel() { //Creamos un modelo de tabla, este sera para la seccion historial
        @Override
        public boolean isCellEditable(int Fila, int Colum) {
            return false;
        }
    }; 
    DefaultTableModel TblBaja = new DefaultTableModel() { //Creamos un modelo de tabla, este sera para la seccion bajas
        @Override
        public boolean isCellEditable(int Fila, int Colum) {
            return false;
        }
    }; 
    
    DefaultTableModel model;
    DefaultTableModel model2;
    
    public void reiniciar(){
          tblcon.setRowCount(0);
    }
    
    public void Mostrardatos(){
        //Tabla para compra
        tabla.addColumn("ID");
        tabla.addColumn("Nombre");
        tabla.addColumn("Cantidad");
        tabla.addColumn("Precio");
        tabla.addColumn("Precio Neto");
        //Tabla para consulta,alta y editar
        tblcon.addColumn("ID");
        tblcon.addColumn("Nombre");
        tblcon.addColumn("Precio");
        tblcon.addColumn("Descripcion");
        tblcon.addColumn("Cantidad");
        //tabla para historial
        Tblhis.addColumn("ID de venta");
        Tblhis.addColumn("Fecha");
        Tblhis.addColumn("ID de vendedor");
        Tblhis.addColumn("ID de producto");
        Tblhis.addColumn("Nombre");
        Tblhis.addColumn("Precio");
        Tblhis.addColumn("Cantidad");
        //Tabla para bajas
        TblBaja.addColumn("ID");
        TblBaja.addColumn("Nombre");
        TblBaja.addColumn("Precio");
        //Añadimos los modelos de tablas a las tablas
        JTb_Compras.setModel(tabla);        
        JTb_Altas.setModel(tblcon);
        JTb_Bajas.setModel(TblBaja);
        JTb_Editar.setModel(tblcon);
        JTb_Filtro.setModel(tblcon);
        JTb_Historial.setModel(Tblhis);
    }
        
    public Ventas(String id_usuario, int personal, ArrayList p_nom, ArrayList p_cant, ArrayList p_pn, ArrayList p_p, double T_a, double pago) {
        initComponents();
        Mostrardatos();//iniciamos la funcion para poner los modelo de tablas en las tablas
        conectatarBaseDatos();
        cargarDatos();
        cargarDatosB("");
        cargarDatosC("");
        getSum();
        setLocationRelativeTo(null); 
        //JLbl_ConfirmacionBa.setVisible(false);
        this.id_usuario = id_usuario;
        this.personal = personal;
        this.p_nom = p_nom;
        this.p_cant = p_cant;
        this.p_pn = p_pn;
        this.p_p = p_p;
        Total_anterior = T_a;
        this.pago = pago;
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
    
   
    
    public void cargarDatos() {
        
        String datos[] = new String[5];    //Variable que almacena los datos de la consulta
        String sql = "SELECT * FROM `Producto`";  //Consulta sql
        
        try {
            ResultSet resultado = sentencia.executeQuery(sql);  //Linea que ejecuta la consulta sql y almacena los datos en resultado

            while (resultado.next()) {                                    //Bucle que recorre la consulta obtenida
                datos[0] = resultado.getString("id");
                datos[1] = resultado.getString("Nombre_Producto");
                datos[2] = resultado.getString("Precio_Producto");
                datos[3] = resultado.getString("Descripcion");
                datos[4] = resultado.getString("Existencia");
              
               
                tblcon.addRow(datos);
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los Datos\n" + ex);
        }
      


    }
     
     
    public void cargarDatosB(String valor) {
    
        String[] titulos = {"ID", "Nombre", "Precio"};
        String[] registro = new String[3];

        String sql = "SELECT id,Nombre_Producto,Precio_Producto FROM Producto Where id LIKE'%" + valor +"%'";
        model = new DefaultTableModel(null, titulos);

        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registro[0] = rs.getString("id");
                registro[1] = rs.getString("Nombre_Producto");
                registro[2] = rs.getString("Precio_Producto");
                model.addRow(registro);
            }
            JTb_Bajas.setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    

    public void cargarDatosC(String valor) {

        String[] titulos = {"ID", "Nombre", "Precio", "Descripcion", "Cantidad"};
        String[] registro = new String[5];

        String campo = JCmBx_Filtro.getSelectedItem().toString();
        String sql="";
        String textbuscar = JTxtF_Filtro.getText();
        
        
        switch(campo){
            case "ID":
                //sql = "SELECT id,Nombre_Producto,Precio_Producto,Descripcion,Existencia FROM Producto Where CONCAT(id,Nombre_Producto,Precio_Producto,Descripcion,Existencia) LIKE'%" + valor + "%'";
                sql = "SELECT id,Nombre_Producto,Precio_Producto,Descripcion,Existencia FROM Producto Where id LIKE'%" + textbuscar +"%'" ;

                break;
            case "Nombre":
                sql = "SELECT id,Nombre_Producto,Precio_Producto,Descripcion,Existencia FROM Producto Where Nombre_Producto LIKE'%" + textbuscar +"%'" ;

                break;
            case "Cantidad":
                sql = "SELECT id,Nombre_Producto,Precio_Producto,Descripcion,Existencia FROM Producto Where Existencia LIKE'%" + textbuscar +"%'" ;

                break;
            case "Precio":
                sql = "SELECT id,Nombre_Producto,Precio_Producto,Descripcion,Existencia FROM Producto Where Precio_Producto LIKE'%" + textbuscar +"%'" ;

                break;
            default:
                
                break;
                    
        }
        
        
        model2 = new DefaultTableModel(null, titulos); //
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registro[0] = rs.getString("id");
                registro[1] = rs.getString("Nombre_Producto");
                registro[2] = rs.getString("Precio_Producto");
                registro[3] = rs.getString("Descripcion");
                registro[4] = rs.getString("Existencia");
                   //
                model2.addRow(registro);
            }//
            JTb_Filtro.setModel(model2);
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }

    private static String fechaActual(){
        Date fecha = new Date();
        SimpleDateFormat formtFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formtFecha.format(fecha);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        JTbdPn_Procesos = new javax.swing.JTabbedPane();
        JPn_Compra = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTb_Compras = new javax.swing.JTable();
        JLbl_ID = new javax.swing.JLabel();
        JTxtF_ID = new javax.swing.JTextField();
        JLbl_Cantidad = new javax.swing.JLabel();
        JTxtF_Cantidad = new javax.swing.JTextField();
        JBtn_Agregar = new javax.swing.JButton();
        JBtn_Eliminar = new javax.swing.JButton();
        JBtn_Limpiar = new javax.swing.JButton();
        JBtn_Confirmar = new javax.swing.JButton();
        JBtn_Visualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        JFTF_Total = new javax.swing.JFormattedTextField();
        JPn_Consulta = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTb_Filtro = new javax.swing.JTable();
        JLbl_FiltroC = new javax.swing.JLabel();
        JCmBx_Filtro = new javax.swing.JComboBox<>();
        JTxtF_Filtro = new javax.swing.JTextField();
        JBtn_BuscarF = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        JPn_Historial = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTb_Historial = new javax.swing.JTable();
        JBtn_ActualizarH = new javax.swing.JButton();
        JPn_Bajas = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        JTb_Bajas = new javax.swing.JTable();
        JLbl_IDBa = new javax.swing.JLabel();
        JTxtF_Bajas = new javax.swing.JTextField();
        JBtn_Baja = new javax.swing.JButton();
        JPn_Editar = new javax.swing.JPanel();
        JLbl_IDE = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        JTb_Editar = new javax.swing.JTable();
        JLbl_NombreE = new javax.swing.JLabel();
        JLbl_PrecioE = new javax.swing.JLabel();
        JLbl_DescE = new javax.swing.JLabel();
        JLbl_CantidadE = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        JTxtA_DescE = new javax.swing.JTextArea();
        JTxtF_IDE = new javax.swing.JTextField();
        JTxtF_NombreE = new javax.swing.JTextField();
        JTxtF_PrecioE = new javax.swing.JTextField();
        JTxtF_CantidadE = new javax.swing.JTextField();
        JBtn_BuscarE = new javax.swing.JButton();
        JBtn_AceptarE = new javax.swing.JButton();
        JPn_Altas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTb_Altas = new javax.swing.JTable();
        JLbl_NombreA = new javax.swing.JLabel();
        JLbl_PrecioA = new javax.swing.JLabel();
        JLbl_CantidadA = new javax.swing.JLabel();
        JLbl1_DescA = new javax.swing.JLabel();
        JTxtF_NombreA = new javax.swing.JTextField();
        JTxtF_PrecioA = new javax.swing.JTextField();
        JTxtF_CantidadA = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        JTxtA_DescA = new javax.swing.JTextArea();
        JLbl_PesoMxn = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        JTxtF_IDA1 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        JMn_CambiarU = new javax.swing.JMenu();
        JMn_Admnisitrar = new javax.swing.JMenu();
        JMnIm_Agregar = new javax.swing.JMenuItem();
        JMnIm_Borrar = new javax.swing.JMenuItem();
        JMnIm_Consultar = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        JTbdPn_Procesos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTbdPn_ProcesosMouseClicked(evt);
            }
        });

        JTb_Compras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTb_Compras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTb_ComprasKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(JTb_Compras);

        JLbl_ID.setText("ID:");

        JTxtF_ID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTxtF_IDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_IDKeyTyped(evt);
            }
        });

        JLbl_Cantidad.setText("Cantidad:");

        JTxtF_Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTxtF_CantidadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_CantidadKeyTyped(evt);
            }
        });

        JBtn_Agregar.setText("Agregar Articulo");
        JBtn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_AgregarActionPerformed(evt);
            }
        });

        JBtn_Eliminar.setText("Eliminar Articulo");
        JBtn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_EliminarActionPerformed(evt);
            }
        });

        JBtn_Limpiar.setBackground(new java.awt.Color(255, 153, 51));
        JBtn_Limpiar.setText("Limpiar");
        JBtn_Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_LimpiarActionPerformed(evt);
            }
        });

        JBtn_Confirmar.setBackground(new java.awt.Color(0, 204, 51));
        JBtn_Confirmar.setText("Confirmar\n   \nCompra"); // NOI18N
        JBtn_Confirmar.setActionCommand("Confirmar Compra");
        JBtn_Confirmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBtn_ConfirmarMouseClicked(evt);
            }
        });
        JBtn_Confirmar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JBtn_ConfirmarKeyPressed(evt);
            }
        });

        JBtn_Visualizar.setBackground(new java.awt.Color(204, 204, 204));
        JBtn_Visualizar.setForeground(new java.awt.Color(51, 51, 51));
        JBtn_Visualizar.setText("<html>\n<center>Visualizar recibo<br>\nreciente</center>");
        JBtn_Visualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JBtn_Visualizar.setEnabled(false);
        JBtn_Visualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_VisualizarActionPerformed(evt);
            }
        });

        jLabel2.setText("Total:");

        JFTF_Total.setEditable(false);

        javax.swing.GroupLayout JPn_CompraLayout = new javax.swing.GroupLayout(JPn_Compra);
        JPn_Compra.setLayout(JPn_CompraLayout);
        JPn_CompraLayout.setHorizontalGroup(
            JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_CompraLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPn_CompraLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JFTF_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPn_CompraLayout.createSequentialGroup()
                        .addComponent(JBtn_Limpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBtn_Confirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JBtn_Visualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(JPn_CompraLayout.createSequentialGroup()
                        .addComponent(JBtn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBtn_Eliminar)
                        .addGap(28, 28, 28))
                    .addGroup(JPn_CompraLayout.createSequentialGroup()
                        .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JLbl_Cantidad)
                            .addComponent(JLbl_ID))
                        .addGap(45, 45, 45)
                        .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JTxtF_ID, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(JTxtF_Cantidad))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        JPn_CompraLayout.setVerticalGroup(
            JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_CompraLayout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JFTF_Total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_CompraLayout.createSequentialGroup()
                .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JPn_CompraLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBtn_Visualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPn_CompraLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JLbl_ID)
                            .addComponent(JTxtF_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JTxtF_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JLbl_Cantidad))
                        .addGap(61, 61, 61)
                        .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JBtn_Agregar)
                            .addComponent(JBtn_Eliminar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(JPn_CompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JBtn_Confirmar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JBtn_Limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );

        JTbdPn_Procesos.addTab("Compra", JPn_Compra);

        JTb_Filtro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTb_Filtro.setEnabled(false);
        jScrollPane2.setViewportView(JTb_Filtro);

        JLbl_FiltroC.setText("Filtro:");

        JCmBx_Filtro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Cantidad", "Precio" }));
        JCmBx_Filtro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCmBx_FiltroItemStateChanged(evt);
            }
        });

        JTxtF_Filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_FiltroKeyTyped(evt);
            }
        });

        JBtn_BuscarF.setText("Buscar");
        JBtn_BuscarF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_BuscarFActionPerformed(evt);
            }
        });

        jButton3.setText("Eliminar filtros");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPn_ConsultaLayout = new javax.swing.GroupLayout(JPn_Consulta);
        JPn_Consulta.setLayout(JPn_ConsultaLayout);
        JPn_ConsultaLayout.setHorizontalGroup(
            JPn_ConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_ConsultaLayout.createSequentialGroup()
                .addGroup(JPn_ConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPn_ConsultaLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 943, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPn_ConsultaLayout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(JLbl_FiltroC, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JCmBx_Filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(JTxtF_Filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(JBtn_BuscarF)
                        .addGap(95, 95, 95)
                        .addComponent(jButton3)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        JPn_ConsultaLayout.setVerticalGroup(
            JPn_ConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_ConsultaLayout.createSequentialGroup()
                .addContainerGap(167, Short.MAX_VALUE)
                .addGroup(JPn_ConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLbl_FiltroC)
                    .addComponent(JCmBx_Filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTxtF_Filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBtn_BuscarF)
                    .addComponent(jButton3))
                .addGap(47, 47, 47)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        JTbdPn_Procesos.addTab("Consulta", JPn_Consulta);

        JTb_Historial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(JTb_Historial);

        JBtn_ActualizarH.setText("Actualizar");
        JBtn_ActualizarH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_ActualizarHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPn_HistorialLayout = new javax.swing.GroupLayout(JPn_Historial);
        JPn_Historial.setLayout(JPn_HistorialLayout);
        JPn_HistorialLayout.setHorizontalGroup(
            JPn_HistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_HistorialLayout.createSequentialGroup()
                .addGroup(JPn_HistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPn_HistorialLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 943, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPn_HistorialLayout.createSequentialGroup()
                        .addGap(400, 400, 400)
                        .addComponent(JBtn_ActualizarH, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        JPn_HistorialLayout.setVerticalGroup(
            JPn_HistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_HistorialLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                .addComponent(JBtn_ActualizarH, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        JTbdPn_Procesos.addTab("Historial", JPn_Historial);

        JTb_Bajas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTb_Bajas.setEnabled(false);
        jScrollPane6.setViewportView(JTb_Bajas);

        JLbl_IDBa.setText("ID:");

        JTxtF_Bajas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTxtF_BajasActionPerformed(evt);
            }
        });
        JTxtF_Bajas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_BajasKeyTyped(evt);
            }
        });

        JBtn_Baja.setText("Confirmar");
        JBtn_Baja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_BajaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPn_BajasLayout = new javax.swing.GroupLayout(JPn_Bajas);
        JPn_Bajas.setLayout(JPn_BajasLayout);
        JPn_BajasLayout.setHorizontalGroup(
            JPn_BajasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_BajasLayout.createSequentialGroup()
                .addGroup(JPn_BajasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPn_BajasLayout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(JLbl_IDBa, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(JTxtF_Bajas, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(JBtn_Baja, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPn_BajasLayout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(265, Short.MAX_VALUE))
        );
        JPn_BajasLayout.setVerticalGroup(
            JPn_BajasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_BajasLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(JPn_BajasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPn_BajasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTxtF_Bajas)
                        .addComponent(JBtn_Baja))
                    .addComponent(JLbl_IDBa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        JTbdPn_Procesos.addTab("Bajas", JPn_Bajas);

        JLbl_IDE.setText("ID:");

        JTb_Editar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(JTb_Editar);

        JLbl_NombreE.setText("Nombre:");

        JLbl_PrecioE.setText("Precio:");

        JLbl_DescE.setText("Descripcion:");

        JLbl_CantidadE.setText("Cantidad:");

        JTxtA_DescE.setColumns(20);
        JTxtA_DescE.setLineWrap(true);
        JTxtA_DescE.setRows(5);
        JTxtA_DescE.setWrapStyleWord(true);
        JTxtA_DescE.setEnabled(false);
        JTxtA_DescE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtA_DescEKeyTyped(evt);
            }
        });
        jScrollPane8.setViewportView(JTxtA_DescE);

        JTxtF_IDE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_IDEKeyTyped(evt);
            }
        });

        JTxtF_NombreE.setEnabled(false);
        JTxtF_NombreE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_NombreEKeyTyped(evt);
            }
        });

        JTxtF_PrecioE.setEnabled(false);
        JTxtF_PrecioE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTxtF_PrecioEActionPerformed(evt);
            }
        });
        JTxtF_PrecioE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_PrecioEKeyTyped(evt);
            }
        });

        JTxtF_CantidadE.setEnabled(false);
        JTxtF_CantidadE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTxtF_CantidadEActionPerformed(evt);
            }
        });
        JTxtF_CantidadE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_CantidadEKeyTyped(evt);
            }
        });

        JBtn_BuscarE.setText("Buscar");
        JBtn_BuscarE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_BuscarEActionPerformed(evt);
            }
        });

        JBtn_AceptarE.setText("Aceptar");
        JBtn_AceptarE.setEnabled(false);
        JBtn_AceptarE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBtn_AceptarEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPn_EditarLayout = new javax.swing.GroupLayout(JPn_Editar);
        JPn_Editar.setLayout(JPn_EditarLayout);
        JPn_EditarLayout.setHorizontalGroup(
            JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_EditarLayout.createSequentialGroup()
                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPn_EditarLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 936, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPn_EditarLayout.createSequentialGroup()
                        .addGap(172, 172, 172)
                        .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPn_EditarLayout.createSequentialGroup()
                                .addComponent(JLbl_DescE)
                                .addGap(36, 36, 36)
                                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JBtn_AceptarE)))
                            .addGroup(JPn_EditarLayout.createSequentialGroup()
                                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(JLbl_CantidadE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPn_EditarLayout.createSequentialGroup()
                                        .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(JLbl_PrecioE, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JLbl_NombreE, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JLbl_IDE, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(5, 5, 5)))
                                .addGap(54, 54, 54)
                                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPn_EditarLayout.createSequentialGroup()
                                        .addComponent(JTxtF_IDE, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(73, 73, 73)
                                        .addComponent(JBtn_BuscarE))
                                    .addComponent(JTxtF_PrecioE, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTxtF_CantidadE, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTxtF_NombreE, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        JPn_EditarLayout.setVerticalGroup(
            JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_EditarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_EditarLayout.createSequentialGroup()
                        .addComponent(JLbl_NombreE)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_EditarLayout.createSequentialGroup()
                        .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JTxtF_IDE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JBtn_BuscarE)
                            .addComponent(JLbl_IDE))
                        .addGap(18, 18, 18)
                        .addComponent(JTxtF_NombreE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLbl_PrecioE, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JTxtF_PrecioE, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLbl_CantidadE)
                    .addComponent(JTxtF_CantidadE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(JPn_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLbl_DescE))
                .addGap(18, 18, 18)
                .addComponent(JBtn_AceptarE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        JTbdPn_Procesos.addTab("Editar", JPn_Editar);

        JTb_Altas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(JTb_Altas);

        JLbl_NombreA.setText("Nombre:");

        JLbl_PrecioA.setText("Precio:");

        JLbl_CantidadA.setText("Cantidad:");

        JLbl1_DescA.setText("Descripcion:");

        JTxtF_NombreA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_NombreAKeyTyped(evt);
            }
        });

        JTxtF_PrecioA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_PrecioAKeyTyped(evt);
            }
        });

        JTxtF_CantidadA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_CantidadAKeyTyped(evt);
            }
        });

        JTxtA_DescA.setColumns(20);
        JTxtA_DescA.setLineWrap(true);
        JTxtA_DescA.setRows(5);
        JTxtA_DescA.setWrapStyleWord(true);
        JTxtA_DescA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtA_DescAKeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(JTxtA_DescA);

        JLbl_PesoMxn.setText("MXN");

        jButton1.setText("Confirmar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("ID");

        JTxtF_IDA1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTxtF_IDA1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout JPn_AltasLayout = new javax.swing.GroupLayout(JPn_Altas);
        JPn_Altas.setLayout(JPn_AltasLayout);
        JPn_AltasLayout.setHorizontalGroup(
            JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_AltasLayout.createSequentialGroup()
                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPn_AltasLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(157, 157, 157))
                    .addGroup(JPn_AltasLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLbl_NombreA)
                            .addComponent(JLbl_PrecioA)
                            .addComponent(JLbl_CantidadA)
                            .addComponent(JLbl1_DescA)
                            .addComponent(jLabel1))
                        .addGap(38, 38, 38)
                        .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                            .addGroup(JPn_AltasLayout.createSequentialGroup()
                                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JTxtF_NombreA, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTxtF_CantidadA, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTxtF_IDA1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(JPn_AltasLayout.createSequentialGroup()
                                        .addComponent(JTxtF_PrecioA, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(JLbl_PesoMxn)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        JPn_AltasLayout.setVerticalGroup(
            JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPn_AltasLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addGap(15, 15, 15))
            .addGroup(JPn_AltasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTxtF_IDA1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(26, 26, 26)
                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLbl_NombreA)
                    .addComponent(JTxtF_NombreA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLbl_PrecioA)
                    .addComponent(JTxtF_PrecioA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLbl_PesoMxn))
                .addGap(25, 25, 25)
                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLbl_CantidadA)
                    .addComponent(JTxtF_CantidadA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(JPn_AltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLbl1_DescA))
                .addGap(62, 62, 62)
                .addComponent(jButton1)
                .addGap(29, 29, 29))
        );

        JTbdPn_Procesos.addTab("Altas", JPn_Altas);

        JMn_CambiarU.setText("Cambiar de usuario");
        JMn_CambiarU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JMn_CambiarUMouseClicked(evt);
            }
        });
        jMenuBar1.add(JMn_CambiarU);

        JMn_Admnisitrar.setText("Administrador");

        JMnIm_Agregar.setText("Agregar Usuario");
        JMnIm_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMnIm_AgregarActionPerformed(evt);
            }
        });
        JMn_Admnisitrar.add(JMnIm_Agregar);

        JMnIm_Borrar.setText("Eliminar Usuario");
        JMnIm_Borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMnIm_BorrarActionPerformed(evt);
            }
        });
        JMn_Admnisitrar.add(JMnIm_Borrar);

        JMnIm_Consultar.setText("Consultar Usuario");
        JMnIm_Consultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMnIm_ConsultarActionPerformed(evt);
            }
        });
        JMn_Admnisitrar.add(JMnIm_Consultar);

        jMenuItem4.setText("Exportar ventas totales");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        JMn_Admnisitrar.add(jMenuItem4);

        jMenuBar1.add(JMn_Admnisitrar);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JTbdPn_Procesos)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JTbdPn_Procesos)
        );

        JTbdPn_Procesos.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void JBtn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_AgregarActionPerformed
        if(JTxtF_ID.getText().equals(""))
            JOptionPane.showMessageDialog(this, "Debe de ingresar la ID...");
            else if (JTxtF_Cantidad.getText().equals(""))
                JOptionPane.showMessageDialog(this, "Ingresa la cantidad...");
            else {
                try {
                    String id = JTxtF_ID.getText();

                    PreparedStatement st = conexion.prepareStatement("SELECT * FROM Producto WHERE Id = ?");
                    st.setString(1, id);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        int ex = Integer.parseInt(rs.getString("Existencia"));

                        int cant = Integer.parseInt(JTxtF_Cantidad.getText());
                        float prec = Float.parseFloat(rs.getString("Precio_Producto"));

                        int numFilas = tabla.getRowCount();
                        for (int i=0; i<numFilas; i++) {
                            if (id.equals(tabla.getValueAt(i, 0))) {
                                int suma_c = 0;
                                float n_precio_n;
                                
                                suma_c = cant + Integer.parseInt((String)tabla.getValueAt(i, 2));
                                
                                if (suma_c > ex)
                                        JOptionPane.showMessageDialog(null, "Existencia del producto: " + ex + ", la cantidad del producto excede...", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                else {
                                    n_precio_n = suma_c * prec;
                                    
                                    tabla.setValueAt(String.valueOf(suma_c), i, 2);
                                    tabla.setValueAt(String.valueOf(n_precio_n), i, 4);
                                    getSum();
                                }

                                JTxtF_Cantidad.setText("");
                                JTxtF_ID.setText("");
                                JTxtF_ID.requestFocus();
                                return;
                            }
                        }

                        if (cant > ex) {
                            JOptionPane.showMessageDialog(null, "Existencia del producto: " + ex + ", la cantidad del producto excede...", "Advertencia", JOptionPane.WARNING_MESSAGE);

                            JTxtF_Cantidad.setText("");
                            JTxtF_ID.setText("");
                            JTxtF_ID.requestFocus();
                            return;
                        }
                        String[] data = new String[5];

                        String nom = rs.getString("Nombre_Producto");
                        Float prec_n = prec * cant;

                        data[0] = id;
                        data[1] = nom;
                        data[2] = Integer.toString(cant);
                        data[3] = Float.toString(prec);
                        data[4] = Float.toString(prec_n);
                        tabla.addRow(data);

                        getSum();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "ID del producto no encontrado","Error",JOptionPane.WARNING_MESSAGE);
                    }
                    JTxtF_Cantidad.setText("");
                    JTxtF_ID.setText("");
                    JTxtF_ID.requestFocus();
                }
                catch(SQLException e) {}
            }
    }//GEN-LAST:event_JBtn_AgregarActionPerformed

    public void getSum() {
        sum = 0.0;
        for (int i=0; i<tabla.getRowCount(); i++) {
            sum += Double.parseDouble(tabla.getValueAt(i, 4).toString());
        }
        
        Locale usa = new Locale("en","US");
        NumberFormat dll_F = NumberFormat.getCurrencyInstance(usa);
        
        JFTF_Total.setText(dll_F.format(sum).toString());
    }
   
    private void JBtn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_EliminarActionPerformed
        int fila = JTb_Compras.getSelectedRow();
        int nFilasSelec = JTb_Compras.getSelectedRowCount(); 
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un ID.");
        }else if(nFilasSelec==1){
            int[] filasselec  = JTb_Compras.getSelectedRows();
            for (int i=filasselec.length-1; i>=0; i--) {
                //Obtener el "id" de la fila seleccionada, no del orden del for.
                tabla.removeRow(filasselec[i]);
            }
            JOptionPane.showMessageDialog(null, "Borrado realizado con éxito.");
            getSum();
        }else if(nFilasSelec>=2){
            int[] filasselec  = JTb_Compras.getSelectedRows();
            for (int i=filasselec.length-1; i>=0; i--) {
                tabla.removeRow(filasselec[i]);
            } 
            JOptionPane.showMessageDialog(null, "Borrado realizado con éxito.");
            getSum();
        }
    }//GEN-LAST:event_JBtn_EliminarActionPerformed

    private void JBtn_LimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_LimpiarActionPerformed
        while(tabla.getRowCount()>0){//Se ejecuta hasta que no tenga filas
            tabla.removeRow(0);
        }
        getSum();
    }//GEN-LAST:event_JBtn_LimpiarActionPerformed

    private void JBtn_VisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_VisualizarActionPerformed
        Ticket obj = new Ticket(id_usuario, personal, p_nom, p_cant, p_pn, p_p, Total_anterior, pago);
        obj.setVisible(true);
        obj.setEnabled(true);
    }//GEN-LAST:event_JBtn_VisualizarActionPerformed

    private void JMnIm_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMnIm_AgregarActionPerformed
        AnadirTrabajador anadir=new AnadirTrabajador(id_usuario, p_nom, p_cant, p_pn, p_p, Total_anterior, pago);
        anadir.setVisible(true);
        anadir.setEnabled(true);
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_JMnIm_AgregarActionPerformed

    private void JMnIm_BorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMnIm_BorrarActionPerformed
        BajarTrabajador bajar=new BajarTrabajador(id_usuario, p_nom, p_cant, p_pn, p_p, Total_anterior, pago);
        bajar.setVisible(true);
        bajar.setEnabled(true);
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_JMnIm_BorrarActionPerformed

    private void JMnIm_ConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMnIm_ConsultarActionPerformed
        ConsultarUsuario consu=new ConsultarUsuario(id_usuario, p_nom, p_cant, p_pn, p_p, Total_anterior, pago);
        consu.setVisible(true);
        consu.setEnabled(true);
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_JMnIm_ConsultarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String nom,can,pre,desc,id,fk;
        id=JTxtF_IDA1.getText();
        nom=JTxtF_NombreA.getText();
        can=JTxtF_CantidadA.getText();
        pre=JTxtF_PrecioA.getText();
        desc=JTxtA_DescA.getText();
        fk = "1";
        if (!id.equals("") && !nom.equals("") && !can.equals("") && !pre.equals("") && !desc.equals("")) {
            String sql = "insert into Producto(id,Nombre_Producto,Descripcion,Existencia,Precio_Producto,FK_id_Administrador) values " + "('" + id + "','" + nom + "','" + desc + "','" + can + "','" + pre + "','" + fk + "')";
            try {
                sentencia.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Producto Agregado\n");
                tblcon.setRowCount(0);

                cargarDatos();
                cargarDatosC("");
                cargarDatosB("");
                JTxtF_IDA1.setText(null);
                JTxtF_NombreA.setText(null);
                JTxtF_PrecioA.setText(null);
                JTxtF_CantidadA.setText(null);
                JTxtA_DescA.setText(null);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error, sus datos no fueron ingresados\n" + ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Los campos deben de estar llenos");
        }


        
       
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void JTxtF_CantidadAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_CantidadAKeyTyped
        char bb=evt.getKeyChar();
        
        if(Character.isLetter(bb)){
           getToolkit().beep();
           evt.consume();
            JOptionPane.showMessageDialog(rootPane, "Ingrese solo numeros\n");
           } 
    }//GEN-LAST:event_JTxtF_CantidadAKeyTyped

    private void JTxtF_PrecioAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_PrecioAKeyTyped
        char bb=evt.getKeyChar();
        
        if(Character.isLetter(bb)){
           getToolkit().beep();
           evt.consume();
            JOptionPane.showMessageDialog(rootPane, "Ingrese solo numeros\n");
           } 
    }//GEN-LAST:event_JTxtF_PrecioAKeyTyped

    private void JTxtF_CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_CantidadKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c==KeyEvent.VK_BACK_SPACE || c==KeyEvent.VK_DELETE))
            evt.consume();
        //Valida que lo que teclee sea un digito, en caso de no serlo se elimina.
    }//GEN-LAST:event_JTxtF_CantidadKeyTyped

    private void JTxtF_CantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_CantidadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if(JTxtF_ID.getText().equals(""))
            JOptionPane.showMessageDialog(this, "Debe de ingresar la ID...");
            else if (JTxtF_Cantidad.getText().equals(""))
                JOptionPane.showMessageDialog(this, "Ingresa la cantidad...");
            else {
                try {
                    String id = JTxtF_ID.getText();

                    PreparedStatement st = conexion.prepareStatement("SELECT * FROM Producto WHERE Id = ?");
                    st.setString(1, id);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        int ex = Integer.parseInt(rs.getString("Existencia"));

                        int cant = Integer.parseInt(JTxtF_Cantidad.getText());
                        float prec = Float.parseFloat(rs.getString("Precio_Producto"));

                        int numFilas = tabla.getRowCount();
                        for (int i=0; i<numFilas; i++) {
                            if (id.equals(tabla.getValueAt(i, 0))) {
                                int suma_c = 0;
                                float n_precio_n;
                                
                                suma_c = cant + Integer.parseInt((String)tabla.getValueAt(i, 2));
                                
                                if (suma_c > ex)
                                        JOptionPane.showMessageDialog(null, "Existencia del producto: " + ex + ", la cantidad del producto excede...", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                else {
                                    n_precio_n = suma_c * prec;
                                    
                                    tabla.setValueAt(String.valueOf(suma_c), i, 2);
                                    tabla.setValueAt(String.valueOf(n_precio_n), i, 4);
                                    getSum();
                                }

                                JTxtF_Cantidad.setText("");
                                JTxtF_ID.setText("");
                                JTxtF_ID.requestFocus();
                                return;
                            }
                        }

                        if (cant > ex) {
                            JOptionPane.showMessageDialog(null, "Existencia del producto: " + ex + ", la cantidad del producto excede...", "Advertencia", JOptionPane.WARNING_MESSAGE);

                            JTxtF_Cantidad.setText("");
                            JTxtF_ID.setText("");
                            JTxtF_ID.requestFocus();
                            return;
                        }
                        String[] data = new String[5];

                        String nom = rs.getString("Nombre_Producto");
                        Float prec_n = prec * cant;

                        data[0] = id;
                        data[1] = nom;
                        data[2] = Integer.toString(cant);
                        data[3] = Float.toString(prec);
                        data[4] = Float.toString(prec_n);
                        tabla.addRow(data);

                        getSum();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "ID del producto no encontrado","Error",JOptionPane.WARNING_MESSAGE);
                    }
                    JTxtF_Cantidad.setText("");
                    JTxtF_ID.setText("");
                    JTxtF_ID.requestFocus();
                }
                catch(SQLException e) {}
            }
        }
    }//GEN-LAST:event_JTxtF_CantidadKeyPressed

    private void JTxtF_FiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_FiltroKeyTyped
        if(JCmBx_Filtro.getSelectedItem().equals("ID")){
            if(JTxtF_Filtro.getText().length()>=10){
                evt.consume();
            }
        }else if(JCmBx_Filtro.getSelectedItem().equals("Nombre")){
            if(JTxtF_Filtro.getText().length()>=30){
                evt.consume();
            }
        }else if(JCmBx_Filtro.getSelectedItem().equals("Cantidad")){
            if(!Character.isDigit(evt.getKeyChar())){
                evt.consume();
            }
        }else if(JCmBx_Filtro.getSelectedItem().equals("Precio")){
            char car = evt.getKeyChar();
            if(car=='.'){
                
            }else if(!Character.isDigit(evt.getKeyChar())){
                evt.consume();
            }
        }
    }//GEN-LAST:event_JTxtF_FiltroKeyTyped

    private void JBtn_BuscarFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_BuscarFActionPerformed
       
 
        if(JCmBx_Filtro.getSelectedItem().equals("ID")){
              cargarDatosC(JTxtF_Filtro.getText());
        }else if(JCmBx_Filtro.getSelectedItem().equals("Nombre")){
             cargarDatosC(JTxtF_Filtro.getText());
       
        }else if(JCmBx_Filtro.getSelectedItem().equals("Cantidad")){
             cargarDatosC(JTxtF_Filtro.getText());
        
        }else if(JCmBx_Filtro.getSelectedItem().equals("Precio")){
             cargarDatosC(JTxtF_Filtro.getText());
            
        }
    }//GEN-LAST:event_JBtn_BuscarFActionPerformed

    private void JTxtF_BajasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_BajasKeyTyped
        if(JTxtF_Bajas.getText().length()>=10){
            evt.consume();
        }
        cargarDatosB(JTxtF_Bajas.getText());
    }//GEN-LAST:event_JTxtF_BajasKeyTyped

    private void JTxtF_IDEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_IDEKeyTyped
        if(JTxtF_IDE.getText().length()>=10){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtF_IDEKeyTyped

    private void JTxtF_NombreEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_NombreEKeyTyped
        if(JTxtF_NombreE.getText().length()>=45){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtF_NombreEKeyTyped

    private void JTxtF_CantidadEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_CantidadEKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtF_CantidadEKeyTyped

    private void JTxtA_DescEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtA_DescEKeyTyped
        if(JTxtA_DescE.getText().length()>=100){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtA_DescEKeyTyped

    private void JTxtF_NombreAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_NombreAKeyTyped
        if(JTxtF_NombreA.getText().length()>=45){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtF_NombreAKeyTyped

    private void JTxtA_DescAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtA_DescAKeyTyped
        if(JTxtA_DescA.getText().length()>=100){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtA_DescAKeyTyped

    private void JTxtF_IDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_IDKeyTyped
        if(JTxtF_ID.getText().length()>=10){
            evt.consume();
        }
    }//GEN-LAST:event_JTxtF_IDKeyTyped

    private void JTb_ComprasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTb_ComprasKeyPressed
        int fila = JTb_Compras.getSelectedRow();
        int nFilasSelec = JTb_Compras.getSelectedRowCount();
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) { 
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un ID.");
            }else if(nFilasSelec==1){
                int[] filasselec  = JTb_Compras.getSelectedRows();
                for (int i=filasselec.length-1; i>=0; i--) {
                    //Obtener el "id" de la fila seleccionada, no del orden del for.
                    tabla.removeRow(filasselec[i]);
                }
                getSum();
                JOptionPane.showMessageDialog(null, "Borrado realizado con éxito.");
            }else if(nFilasSelec>=2){
                int[] filasselec  = JTb_Compras.getSelectedRows();
                for (int i=filasselec.length-1; i>=0; i--) {
                    tabla.removeRow(filasselec[i]);
                } 
                getSum();
                JOptionPane.showMessageDialog(null, "Borrado realizado con éxito.");
            }
        }
    }//GEN-LAST:event_JTb_ComprasKeyPressed

    private void JTxtF_PrecioEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_PrecioEKeyTyped
        char car = evt.getKeyChar();
            if(car=='.'){
                
            }else if(!Character.isDigit(evt.getKeyChar())){
                evt.consume();
            }
    }//GEN-LAST:event_JTxtF_PrecioEKeyTyped

    private void JTxtF_IDA1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_IDA1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_JTxtF_IDA1KeyTyped

    private void JTxtF_CantidadEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTxtF_CantidadEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTxtF_CantidadEActionPerformed

    private void JBtn_BuscarEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_BuscarEActionPerformed
        // TODO add your handling code here:
        
         // TODO add your handling code here:
          String  id = this.JTxtF_IDE.getText();
        // String datos[] = new String[6];    //Variable que almacena los datos de la consulta
       try { 
           PreparedStatement pst=conexion.prepareStatement("SELECT * FROM Producto WHERE id= ?");
           pst.setString(1,id );
           ResultSet rs=pst.executeQuery();
           if(rs.next()){
               
               JTxtF_IDE.setText(rs.getString("id"));
               JTxtF_NombreE.setText(rs.getString("Nombre_Producto"));
               JTxtA_DescE.setText(rs.getString("Descripcion"));
               JTxtF_CantidadE.setText(rs.getString("Existencia"));
               JTxtF_PrecioE.setText(rs.getString("Precio_Producto"));
              
               
                 JOptionPane.showMessageDialog(null, "Producto Encontrado\n");
             
              JTxtF_IDE.setEnabled(false);
              JTxtF_NombreE.setEnabled(true); 
              JTxtF_PrecioE.setEnabled(true);
              JTxtF_CantidadE.setEnabled(true);
              JTxtA_DescE.setEnabled(true);
              JBtn_AceptarE.setEnabled(true);
  
           }else{
                JOptionPane.showMessageDialog(null, "Producto No Encontrado\n");
  
           }
   
       } catch (SQLException ex) {
           
       }
        
    }//GEN-LAST:event_JBtn_BuscarEActionPerformed

    private void JTxtF_PrecioEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTxtF_PrecioEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTxtF_PrecioEActionPerformed

    private void JBtn_AceptarEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_AceptarEActionPerformed
        
         try { 
        PreparedStatement pst=conexion.prepareStatement  ("UPDATE Producto SET Nombre_Producto = ?, Descripcion=?, Existencia=?,Precio_Producto=? where id=? " )  ;    
	pst.setString(1, JTxtF_NombreE.getText());
	pst.setString(2, JTxtA_DescE.getText());
	pst.setString(3, JTxtF_CantidadE.getText());
	pst.setString(4, JTxtF_PrecioE.getText());
        pst.setString(5, JTxtF_IDE.getText());
        
        
	int n = pst.executeUpdate();//valida si se guardaron los datos; si pst>0 entonces se guardaron
       
 
	if(n > 0)
	{
            JOptionPane.showMessageDialog(null, "Datos modificados exitosamente");
                
            tblcon.setRowCount(0);
            cargarDatos();//l momento de agregar un nuevo registro, actualiza la tabla
            cargarDatosB("");
            cargarDatosC("");
            JTxtF_IDE.setText(null);
            JTxtF_NombreE.setText(null);
            JTxtF_PrecioE.setText(null);
            JTxtF_CantidadE.setText(null);
            JTxtA_DescE.setText(null);
            
            JTxtF_IDE.setEnabled(true);
            JTxtF_NombreE.setEnabled(false);
            JTxtF_PrecioE.setEnabled(false);
            JTxtF_CantidadE.setEnabled(false);
            JTxtA_DescE.setEnabled(false);
            JBtn_AceptarE.setEnabled(false);
	}
            
            
        
          // JOptionPane.showMessageDialog(null, "Datos modificados\n"); 
           
       } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error\n"); 
            
       }
    }//GEN-LAST:event_JBtn_AceptarEActionPerformed

    private void JBtn_BajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_BajaActionPerformed
        //BORRADO
        String id = this.JTxtF_Bajas.getText();
        try {
            PreparedStatement pst = conexion.prepareStatement("SELECT * FROM Producto WHERE id=?");
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String sql = "DELETE FROM Producto WHERE id='" + id + "'";
                sentencia.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Producto borrado\n");
                tblcon.setRowCount(0);
                cargarDatos();
                TblBaja.setRowCount(0);
                cargarDatosB(JTxtF_Bajas.getText());
                cargarDatosC("");
                JTxtF_Bajas.setText(null);

            } else {
                JOptionPane.showMessageDialog(null, "Error, No se encontro el producto en la base de datos\n");
            }
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_JBtn_BajaActionPerformed

    private void JTxtF_BajasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTxtF_BajasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTxtF_BajasActionPerformed

    private boolean supera;
    private void JBtn_ConfirmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBtn_ConfirmarMouseClicked
        if (tabla.getRowCount() == 0)
            JOptionPane.showMessageDialog(null, "Lista vacia...", "Error", JOptionPane.WARNING_MESSAGE);
        else {
            //VALIDAR CANTIDAD A PAGAR SUFICIENTE (Mayor o igual al total)
            supera = false;
            
            do {
                String s_pago = JOptionPane.showInputDialog(null, "Ingresa la cantidad a pagar: ", "Pago", JOptionPane.INFORMATION_MESSAGE);
                if (s_pago == null) return;
                else {
                    try {
                        Double pago = Double.parseDouble(s_pago);
                        if (!valPago(pago)) {
                            JOptionPane.showMessageDialog(null, "Pago insuficiente, ingrese de nuevo", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                        else this.pago = pago;
                    }
                    catch(NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Dato no válido, ingresa nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } while(!supera);
            
            //ACTUALIZAR EXISTENCIA
            try {
                for (int i=0; i<tabla.getRowCount(); i++) {
                    String id = (String)tabla.getValueAt(i,0);
                    
                    PreparedStatement st1 = conexion.prepareStatement("SELECT * FROM Producto WHERE id=?");
                    st1.setString(1, id);
                    ResultSet rs1 = st1.executeQuery();
                    if (rs1.next()) {
                        int ex = Integer.valueOf(rs1.getString("Existencia"));
                        int n_ex = ex - Integer.parseInt((String)tabla.getValueAt(i, 2));

                        PreparedStatement st2 = conexion.prepareStatement("UPDATE Producto SET Existencia=? WHERE id=?");
                        st2.setString(1, Integer.toString(n_ex));
                        st2.setString(2, id);
                        st2.executeUpdate();
                        st2.close();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "No se encontró la id del producto en la BD", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            //REGISTRAR HISTORIAL DE VENTA
            //Venta
            String last_idVenta = "";
            
            try {
                PreparedStatement st = conexion.prepareStatement("INSERT INTO Venta(Id_Vendedor,Fecha,FK_Id_Administrador,FK_Id_Trabajador) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                st.setString(1,id_usuario);
                st.setString(2,fechaActual());
                if (personal == 1) {
                    st.setString(3,id_usuario);
                    st.setString(4,"0");
                }
                else {
                    st.setString(3,"0");
                    st.setString(4,id_usuario);
                }
                
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next())
                    last_idVenta = rs.getString(1);
                //Se localiza y guarda la ultima id de venta generada
                
                //Para reiniciar el acumulador:
                //https://www.youtube.com/watch?v=hI3wS5cPUcc&ab_channel=TomFragale
                st.close();
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            //Historial
            try {
                for (int i=0; i<tabla.getRowCount(); i++) {
                    PreparedStatement st = conexion.prepareStatement("INSERT INTO Historial(`FK_Id_Historial de Venta`,FK_Id_Producto,Nombre_Producto,Precio_Venta,Cantidad,Precio_Final) VALUES (?,?,?,?,?,?)");
                    st.setString(1, last_idVenta);
                    st.setString(2, (String)tabla.getValueAt(i,0));
                    st.setString(3, (String)tabla.getValueAt(i,1));
                    st.setString(4, (String)tabla.getValueAt(i,3));
                    st.setString(5, (String)tabla.getValueAt(i,2));
                    st.setString(6, (String)tabla.getValueAt(i,4));
                    
                    st.executeUpdate();
                    st.close();
                }
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            p_nom.clear();
            p_cant.clear();
            p_pn.clear();
            p_p.clear();
            for (int j=0; j<tabla.getRowCount(); j++) {
                p_nom.add((String)tabla.getValueAt(j, 1));
                p_cant.add((String)tabla.getValueAt(j, 2));
                p_pn.add((String)tabla.getValueAt(j, 4));
                p_p.add((String)tabla.getValueAt(j, 3));
            }
            //Pasar el total de columnas de nombres, cantidades y precios de la compra al ticket
            
            compraT = 0;
            while(tabla.getRowCount() > 0){
                compraT++;
                tabla.removeRow(0);
            }
            
            JBtn_Visualizar.setEnabled(true);
            
            Total_anterior = sum;
            
            Locale usa = new Locale("en","US");
            NumberFormat dll_F = NumberFormat.getCurrencyInstance(usa);
            JFTF_Total.setText(dll_F.format(0).toString());
            
            JOptionPane.showMessageDialog(null, "Compra realizada exitosamente.\nSu cambio es de: " + DtoM(pago - Total_anterior), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            JBtn_Visualizar.setEnabled(true);
            tblcon.setRowCount(0);
            cargarDatos();
            cargarDatosB("");
            cargarDatosC("");
        
        }
    }//GEN-LAST:event_JBtn_ConfirmarMouseClicked

    private String DtoM (Double n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }
    
    private boolean valPago(double p) {
        if (p >= sum) {
            supera = true;
            return true;
        }
        else return false;
    }
    
    private void JBtn_ActualizarHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBtn_ActualizarHActionPerformed
        while(Tblhis.getRowCount()>0){//Se ejecuta hasta que no tenga filas
            Tblhis.removeRow(0);
        }
        
        try {
            // TODO add your handling code here:
            String[] datos = new String[7];
            String fechaSQL = fechaActual();
            //System.out.println(fechaSQL);
            ResultSet rs2;
            int idH;
            //Ejecución de la intruccíon para ventas
            PreparedStatement st = conexion.prepareStatement("SELECT * FROM `Venta` WHERE `Fecha` = ?"); //Cambiar por string: 
            st.setString(1, fechaSQL);
            ResultSet rs = st.executeQuery();
            //Fin de la instrucción ventas

            //Leyendo la tabla Venta
            while (rs.next()) {
                //Primera inserción de datos
                datos[0] = String.valueOf(rs.getInt("Id"));
                datos[1] = rs.getString("Fecha");
                datos[2] = String.valueOf(rs.getInt("Id_Vendedor"));
                //primera insercion de datos

                //Ejecutando instrucción para historial
                st = conexion.prepareStatement("SELECT * FROM `Historial` WHERE `FK_Id_Historial de Venta` = ?");
                idH = Integer.parseInt(datos[0]);
                st.setInt(1, idH);
                rs2 = st.executeQuery();
                //Fin instrucción historial

                //Leyendo la tabala historial
                while (rs2.next()) {
                    datos[3] = rs2.getString("FK_Id_Producto");
                    datos[4] = rs2.getString("Nombre_Producto");
                    datos[5] = rs2.getString("Precio_Venta");
                    datos[6] = rs2.getString("Cantidad");
                    Tblhis.addRow(datos);
//                    for (int i = 0; i < datos.length; i++) {
//                        System.out.print(datos[i]+" - ");
//                    }
//                    System.out.println();
                }
                //Fin de la lectura de la tabla historial
            }
            //Fin de la lectura Venta

        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_JBtn_ActualizarHActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel registrosT = new DefaultTableModel();
        javax.swing.JTable tablaRegistros = new javax.swing.JTable();
        
        //-Enivar la cabecera de la tabla y modelo
        String[] cabecera = {"ID de Venta","Fecha","ID de Vendedor","ID de producto","Nombre","Precio","Cantidad"};
        registrosT.setColumnIdentifiers(cabecera);
        tablaRegistros.setModel(registrosT);
        //-Fin del envio de cabecera y modelo
        
        try {
            // TODO add your handling code here:
            String[] datos = new String[7];
            ResultSet rs2;
            int idH;
            //Ejecución de la intruccíon para ventas
            PreparedStatement st = conexion.prepareStatement("SELECT * FROM `Venta`"); //Cambiar por string:
            ResultSet rs = st.executeQuery();
            //Fin de la instrucción ventas

            //Leyendo la tabla Venta
            while (rs.next()) {
                //Primera inserción de datos
                datos[0] = String.valueOf(rs.getInt("Id"));
                datos[1] = rs.getString("Fecha");
                datos[2] = String.valueOf(rs.getInt("Id_Vendedor"));
                //primera insercion de datos

                //Ejecutando instrucción para historial
                st = conexion.prepareStatement("SELECT * FROM `Historial` WHERE `FK_Id_Historial de Venta` = ?");
                idH = Integer.parseInt(datos[0]);
                st.setInt(1, idH);
                rs2 = st.executeQuery();
                //Fin instrucción historial

                //Leyendo la tabala historial
                while (rs2.next()) {
                    datos[3] = rs2.getString("FK_Id_Producto");
                    datos[4] = rs2.getString("Nombre_Producto");
                    datos[5] = rs2.getString("Precio_Venta");
                    datos[6] = rs2.getString("Cantidad");
                    //Llenar el jTable
                    registrosT.addRow(datos);
                    for (int i = 0; i < datos.length; i++) {
                        System.out.print(datos[i]+" - ");
                    }
                    System.out.println();
                }
                //Fin de la lectura de la tabla historial
            }
            //Fin de la lectura Venta

        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        //hacer el export del JTabble tablaRegistros
        try{
            //Elejir el archivo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(this);
            File saveFile = fileChooser.getSelectedFile();
            //Fin de la eleccion
            
            //Hacer y guardar archivo
            if(saveFile != null){
                saveFile = new File(saveFile.toString()+".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet hoja = wb.createSheet("customer");
                
                //Encabezado
                Row rowCol = hoja.createRow(0);
                for (int i = 0; i < tablaRegistros.getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(tablaRegistros.getColumnName(i));
                }
                //Fin del encabezado
                
                //Datos
                for (int j = 0; j < tablaRegistros.getRowCount(); j++) {
                    Row row = hoja.createRow(j+1);
                    for (int k = 0; k < tablaRegistros.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if(tablaRegistros.getValueAt(j, k) != null){
                            cell.setCellValue(tablaRegistros.getValueAt(j, k).toString());
                        }
                    }
                }
                //Fin de los datos
                 
                //Creamos el archivo y lo abrimos
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
                openFile(saveFile.toString());
                //Fin de la abertura
            }else{
                JOptionPane.showMessageDialog(null, "Se genero un error al intentar guardar el archivo", "Error en la exportación", 0);
                
            }
            
            //Fin del archivo
            
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException io){
            System.out.println(io);
        }
        //Fin de la exportacion
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void JTxtF_IDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTxtF_IDKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
            JTxtF_Cantidad.requestFocus();
    }//GEN-LAST:event_JTxtF_IDKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        JTxtF_ID.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    private void JBtn_ConfirmarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JBtn_ConfirmarKeyPressed
        
    }//GEN-LAST:event_JBtn_ConfirmarKeyPressed

    private void JTbdPn_ProcesosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTbdPn_ProcesosMouseClicked
        
    }//GEN-LAST:event_JTbdPn_ProcesosMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JTxtF_Filtro.setText("");
        cargarDatosC("");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void JMn_CambiarUMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JMn_CambiarUMouseClicked
        index camTrabajador=new index();
        camTrabajador.setVisible(true);
        camTrabajador.setEnabled(true);
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_JMn_CambiarUMouseClicked

    private void JCmBx_FiltroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCmBx_FiltroItemStateChanged
        JTxtF_Filtro.setText(null);
    }//GEN-LAST:event_JCmBx_FiltroItemStateChanged

    private void openFile(String file){
        try{
            File path = new File(file);
            Desktop.getDesktop().open(path);
        }catch(IOException ioe){
            System.out.println(ioe);
        }
    }
    
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
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ArrayList<String> nulo = new ArrayList<>();
                new Ventas("",0,nulo,nulo,nulo,nulo,0,0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBtn_AceptarE;
    private javax.swing.JButton JBtn_ActualizarH;
    private javax.swing.JButton JBtn_Agregar;
    private javax.swing.JButton JBtn_Baja;
    private javax.swing.JButton JBtn_BuscarE;
    private javax.swing.JButton JBtn_BuscarF;
    private javax.swing.JButton JBtn_Confirmar;
    private javax.swing.JButton JBtn_Eliminar;
    private javax.swing.JButton JBtn_Limpiar;
    private javax.swing.JButton JBtn_Visualizar;
    private javax.swing.JComboBox<String> JCmBx_Filtro;
    private javax.swing.JFormattedTextField JFTF_Total;
    private javax.swing.JLabel JLbl1_DescA;
    private javax.swing.JLabel JLbl_Cantidad;
    private javax.swing.JLabel JLbl_CantidadA;
    private javax.swing.JLabel JLbl_CantidadE;
    private javax.swing.JLabel JLbl_DescE;
    private javax.swing.JLabel JLbl_FiltroC;
    private javax.swing.JLabel JLbl_ID;
    private javax.swing.JLabel JLbl_IDBa;
    private javax.swing.JLabel JLbl_IDE;
    private javax.swing.JLabel JLbl_NombreA;
    private javax.swing.JLabel JLbl_NombreE;
    private javax.swing.JLabel JLbl_PesoMxn;
    private javax.swing.JLabel JLbl_PrecioA;
    private javax.swing.JLabel JLbl_PrecioE;
    private javax.swing.JMenuItem JMnIm_Agregar;
    private javax.swing.JMenuItem JMnIm_Borrar;
    private javax.swing.JMenuItem JMnIm_Consultar;
    public static javax.swing.JMenu JMn_Admnisitrar;
    private javax.swing.JMenu JMn_CambiarU;
    private javax.swing.JPanel JPn_Altas;
    private javax.swing.JPanel JPn_Bajas;
    private javax.swing.JPanel JPn_Compra;
    private javax.swing.JPanel JPn_Consulta;
    private javax.swing.JPanel JPn_Editar;
    private javax.swing.JPanel JPn_Historial;
    private javax.swing.JTable JTb_Altas;
    private javax.swing.JTable JTb_Bajas;
    private javax.swing.JTable JTb_Compras;
    private javax.swing.JTable JTb_Editar;
    private javax.swing.JTable JTb_Filtro;
    private javax.swing.JTable JTb_Historial;
    public static javax.swing.JTabbedPane JTbdPn_Procesos;
    private javax.swing.JTextArea JTxtA_DescA;
    private javax.swing.JTextArea JTxtA_DescE;
    private javax.swing.JTextField JTxtF_Bajas;
    private javax.swing.JTextField JTxtF_Cantidad;
    private javax.swing.JTextField JTxtF_CantidadA;
    private javax.swing.JTextField JTxtF_CantidadE;
    private javax.swing.JTextField JTxtF_Filtro;
    private javax.swing.JTextField JTxtF_ID;
    private javax.swing.JTextField JTxtF_IDA1;
    private javax.swing.JTextField JTxtF_IDE;
    private javax.swing.JTextField JTxtF_NombreA;
    private javax.swing.JTextField JTxtF_NombreE;
    private javax.swing.JTextField JTxtF_PrecioA;
    private javax.swing.JTextField JTxtF_PrecioE;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    // End of variables declaration//GEN-END:variables
}