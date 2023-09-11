package Formulario;

import Logica.Cls_Asistencia;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import java.text.Normalizer;

import java.io.IOException;

public class FormControlAsistenica extends javax.swing.JFrame {
    Exportar obj;

    public FormControlAsistenica() {
        initComponents();
        // -----------------------
        // Agregar un DocumentListener al campo de búsqueda
    TLBuscarAlumno.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void changedUpdate(javax.swing.event.DocumentEvent evt) {
            buscarAlumnos();
        }

        public void insertUpdate(javax.swing.event.DocumentEvent evt) {
            buscarAlumnos();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent evt) {
            buscarAlumnos();
        }
    });
        // ------------------------
        // Configura el modo de selección de la tabla a SINGLE_SELECTION
        jTableAsistencia.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
      
        //----------------------------------------------------------------------
        // Obtener la fecha y hora actual
        Date fechaActual = new Date();

        // Formatear la fecha y hora en el formato deseado
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        String fecha = formatoFecha.format(fechaActual);
        String hora = formatoHora.format(fechaActual);

        // Establecer la fecha y la hora en los campos de texto
        TLFecha.setText(fecha);
        TLHora.setText(hora);

        CP = new Cls_Asistencia();
        listar();
        listarAsistencia();
    }
    
    private void limpiar(){
        TLDni.setText("");
        TLNombre.setText("");
        TLApellP.setText("");
        TLApellMat.setText("");
        TLDiasAsistencia.setText("");
        TLEdad.setText("");
        TLTel.setText("");
        TLFecha.setText("");
        TLHora.setText("");
        ComboBoxEstado.setSelectedIndex(0);
        
        TLDni.requestFocus();    
    }
    private final Cls_Asistencia CP;
    int num = 0;
    
    private void buscarAlumnos() {
    // Obtener el valor de búsqueda desde el componente JTextField
    String busqueda = TLBuscarAlumno.getText().trim().toLowerCase(); // Convertir a minúsculas

    // Crear un nuevo modelo de tabla para los resultados de la búsqueda
    DefaultTableModel modeloResultados = new DefaultTableModel();
    modeloResultados.addColumn("DNI");
    modeloResultados.addColumn("Nombre");
    modeloResultados.addColumn("Apellido Pat");
    modeloResultados.addColumn("Apellido Mat");
    modeloResultados.addColumn("Dias Asistencia");
    modeloResultados.addColumn("Edad");
    modeloResultados.addColumn("Teléfono");

    // Obtener el modelo de la tabla original (jTableAlumno)
    DefaultTableModel modeloAlumno = (DefaultTableModel) jTableAlumno.getModel();

    // Variable para rastrear si se encontraron resultados
    boolean resultadosEncontrados = false;

    // Iterar a través de las filas de la tabla original
    for (int fila = 0; fila < modeloAlumno.getRowCount(); fila++) {
        String dni = modeloAlumno.getValueAt(fila, 0).toString().toLowerCase(); // Obtener el DNI de la fila y convertir a minúsculas
        String nombre = modeloAlumno.getValueAt(fila, 1).toString().toLowerCase(); // Obtener el nombre de la fila y convertir a minúsculas
        String apellidoPat = modeloAlumno.getValueAt(fila, 2).toString().toLowerCase(); // Obtener el apellido paterno de la fila y convertir a minúsculas
        String apellidoMat = modeloAlumno.getValueAt(fila, 3).toString().toLowerCase(); // Obtener el apellido materno de la fila y convertir a minúsculas

        // Verificar si alguno de los campos contiene la búsqueda
        if (busqueda.isEmpty() || dni.contains(busqueda) ||
            nombre.contains(busqueda) ||
            apellidoPat.contains(busqueda) ||
            apellidoMat.contains(busqueda)) {

            Object[] filaResultado = new Object[7];
            filaResultado[0] = dni;
            filaResultado[1] = modeloAlumno.getValueAt(fila, 1);
            filaResultado[2] = modeloAlumno.getValueAt(fila, 2);
            filaResultado[3] = modeloAlumno.getValueAt(fila, 3);
            filaResultado[4] = modeloAlumno.getValueAt(fila, 4);
            filaResultado[5] = modeloAlumno.getValueAt(fila, 5);
            filaResultado[6] = modeloAlumno.getValueAt(fila, 6);
            modeloResultados.addRow(filaResultado);

            // Indicar que se encontraron resultados
            resultadosEncontrados = true;
        }
    }

    // Establecer el modelo de resultados en la tabla (jTableAlumno)
    jTableAlumno.setModel(modeloResultados);

    // Si no se encontraron resultados, mostrar un mensaje al usuario y restaurar la tabla original
    if (!resultadosEncontrados) {
        JOptionPane.showMessageDialog(this, "No se encontró ningún resultado para la búsqueda.", "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
        // Restaurar la tabla original
        jTableAlumno.setModel(modeloAlumno);
        listar();
    }
}
    
    private void guardar(){
        
        String DNI = TLDni.getText();
        String Nombre = TLNombre.getText();
        String Fecha = TLFecha.getText();
        String Estado = ComboBoxEstado.getSelectedItem().toString();
        String Hora = TLHora.getText();
        
        // Verificar si el campo DNI está vacío
        if (DNI.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el DNI", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener la operación de guardado
        }
        
        // Verificamos si el DNI y la fecha  existe en la tabla
        if (buscarPorDNIYFecha(DNI,Fecha)==true) {
            JOptionPane.showMessageDialog(this, "Ya se registró un alumno con el mismo DNI y fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener la operación de guardado
        }
        
        // Verificar si el usuario no ha cambiado el estado
        if (Estado.equals("Seleccionar Estado")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un estado válido", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return; // Detener la operación de guardado
        }
        
        int respuesta = CP.insertDatosAsistencia(DNI, Nombre, Fecha, Estado, Hora);
                if (respuesta > 0){
                    listarAsistencia();  
                }            
    }
    
    private boolean buscarPorDNIYFecha(String dniBusqueda, String fechaBusqueda) {
        // Obtener el modelo de la tabla original (jTableAlumno)
        DefaultTableModel modeloAlumno = (DefaultTableModel) jTableAsistencia.getModel();

        // Variable para rastrear si se encontró el DNI y la fecha coincidió
        boolean dniYFechaEncontrados = false;

        // Obtener la fecha actual del sistema en el formato deseado (yyyy-MM-dd)
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActualFormateada = formatoFecha.format(fechaActual);

        // Iterar a través de las filas de la tabla original
        for (int fila = 0; fila < modeloAlumno.getRowCount(); fila++) {
            String dni = modeloAlumno.getValueAt(fila, 1).toString(); // Obtener el DNI de la fila
            String fecha = modeloAlumno.getValueAt(fila, 3).toString(); // Obtener la fecha de la fila

            // Utilizar Normalizer para eliminar diacríticos del DNI
            dni = Normalizer.normalize(dni, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            // Si el DNI coincide con la búsqueda y la fecha coincide con la fecha actual, establecer dniYFechaEncontrados en true
            if (dni.equalsIgnoreCase(dniBusqueda) && fecha.equalsIgnoreCase(fechaActualFormateada)) {
                dniYFechaEncontrados = true;
                break; // Salir del bucle, ya que se encontró el DNI y la fecha coincidió
            }
        }

        return dniYFechaEncontrados;
       }
    
    private void listar(){
        jTableAlumno.setModel(CP.getDatos());
    }
    private void listarAsistencia(){
        jTableAsistencia.setModel(CP.getDatoss());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        TLFecha = new javax.swing.JTextField();
        TLHora = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAsistencia = new javax.swing.JTable();
        BDescargar = new javax.swing.JButton();
        BLimpiar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        TLDni = new javax.swing.JTextField();
        TLNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        TLApellP = new javax.swing.JTextField();
        TLApellMat = new javax.swing.JTextField();
        TLEdad = new javax.swing.JTextField();
        ComboBoxEstado = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TLTel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        BSalir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        BGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        BBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAlumno = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        TLDiasAsistencia = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        TLBuscarAlumno = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        TLBuscarFecha = new javax.swing.JTextField();
        BBuscarAsistencia = new javax.swing.JButton();
        BTEliminar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        FONDO = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 50, 100));
        jLabel9.setText("FECHA");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, -1, -1));

        TLFecha.setEditable(false);
        TLFecha.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLFechaActionPerformed(evt);
            }
        });
        jPanel1.add(TLFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 150, -1));

        TLHora.setEditable(false);
        TLHora.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLHoraActionPerformed(evt);
            }
        });
        jPanel1.add(TLHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 360, 150, -1));

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 50, 100));
        jLabel10.setText("HORA");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, 20));

        jTableAsistencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "CÓDIGO", "NOMBRE", "FECHA", "ESTADO", "HORA"
            }
        ));
        jTableAsistencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAsistenciaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableAsistencia);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 730, 140));

        BDescargar.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BDescargar.setText("Descargar Lista");
        BDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDescargarActionPerformed(evt);
            }
        });
        jPanel1.add(BDescargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 470, -1, -1));

        BLimpiar.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BLimpiar.setText("Limpiar");
        BLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(BLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        jLabel7.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 50, 100));
        jLabel7.setText("TELEFONO:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, -1));

        TLDni.setEditable(false);
        TLDni.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLDniActionPerformed(evt);
            }
        });
        jPanel1.add(TLDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 150, -1));

        TLNombre.setEditable(false);
        TLNombre.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLNombreActionPerformed(evt);
            }
        });
        jPanel1.add(TLNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 150, -1));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 50, 100));
        jLabel1.setText("CONTROL DE ASISTENCIA");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        TLApellP.setEditable(false);
        TLApellP.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLApellP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLApellPActionPerformed(evt);
            }
        });
        jPanel1.add(TLApellP, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 150, -1));

        TLApellMat.setEditable(false);
        TLApellMat.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLApellMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLApellMatActionPerformed(evt);
            }
        });
        jPanel1.add(TLApellMat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 150, -1));

        TLEdad.setEditable(false);
        TLEdad.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLEdad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLEdadActionPerformed(evt);
            }
        });
        jPanel1.add(TLEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 150, -1));

        ComboBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PRESENTE\t", "AUSENTE", "TARDE", " " }));
        ComboBoxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxEstadoActionPerformed(evt);
            }
        });
        jPanel1.add(ComboBoxEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 150, -1));

        jLabel8.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 50, 100));
        jLabel8.setText("ESTADO:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 50, 100));
        jLabel2.setText("CÓDIGO:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        TLTel.setEditable(false);
        TLTel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLTelActionPerformed(evt);
            }
        });
        jPanel1.add(TLTel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 150, -1));

        jLabel3.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 50, 100));
        jLabel3.setText("NOMBRE");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel4.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 50, 100));
        jLabel4.setText("APELLIDO PAT :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        BSalir.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BSalir.setText("Salir");
        BSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSalirActionPerformed(evt);
            }
        });
        jPanel1.add(BSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 50, 100));
        jLabel5.setText("Buscar Datos:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 290, 100, -1));

        BGuardar.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BGuardar.setText("Registrar Asistencia");
        BGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(BGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 440, -1, -1));

        jLabel6.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 50, 100));
        jLabel6.setText("EDAD:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        BBuscar.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BBuscar.setText("Buscar");
        BBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(BBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 90, -1, -1));

        jTableAlumno.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jTableAlumno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "APELL_PAT", "APELL_MAT", "DIA(S)", "EDAD", "TELEFONO"
            }
        ));
        jTableAlumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAlumnoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableAlumno);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 730, 150));

        jLabel12.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 50, 100));
        jLabel12.setText("APELLIDO MAT :");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        TLDiasAsistencia.setEditable(false);
        TLDiasAsistencia.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLDiasAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLDiasAsistenciaActionPerformed(evt);
            }
        });
        jPanel1.add(TLDiasAsistencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 150, -1));

        jLabel11.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 50, 100));
        jLabel11.setText("DIAS ASISTENCIA:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        TLBuscarAlumno.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLBuscarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLBuscarAlumnoActionPerformed(evt);
            }
        });
        jPanel1.add(TLBuscarAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, 240, -1));

        jLabel14.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 50, 100));
        jLabel14.setText("Buscar Alumno:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 120, -1));

        TLBuscarFecha.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        TLBuscarFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLBuscarFechaActionPerformed(evt);
            }
        });
        jPanel1.add(TLBuscarFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 290, 170, -1));

        BBuscarAsistencia.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BBuscarAsistencia.setText("Buscar");
        BBuscarAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BBuscarAsistenciaActionPerformed(evt);
            }
        });
        jPanel1.add(BBuscarAsistencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 290, -1, -1));

        BTEliminar.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        BTEliminar.setText("Eliminar Registro");
        BTEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(BTEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 470, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logoITALI2.png"))); // NOI18N
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, -20, 270, 140));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo png (2).png"))); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 140, 110));

        FONDO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoBlanco.jpg"))); // NOI18N
        jPanel1.add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 550));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1060, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTEliminarActionPerformed
        int fila = jTableAsistencia.getSelectedRowCount();
        if (fila < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
        } else {
            String selectedValue = jTableAsistencia.getValueAt(jTableAsistencia.getSelectedRow(), 0).toString();
            try {
                int idAsistencia = Integer.parseInt(selectedValue);
                if (CP.deleteDatos(idAsistencia) > 0) {
                    listarAsistencia();
                    limpiar();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El valor seleccionado no es un número válido.");
            }
        }
    }//GEN-LAST:event_BTEliminarActionPerformed

    private void BBuscarAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BBuscarAsistenciaActionPerformed
        // TODO add your handling code here:
        // Obtener el nombre de búsqueda desde un componente JTextField (por ejemplo, TLNombreBusqueda)
        String IDBusqueda = TLBuscarFecha.getText().trim();
        String DNIBusqueda = TLBuscarFecha.getText().trim();
        String NOMBREBusq = TLBuscarFecha.getText().trim();
        String FECHABusq = TLBuscarFecha.getText().trim();

        // Crear un nuevo modelo de tabla para los resultados de la búsqueda
        DefaultTableModel modeloResultados = new DefaultTableModel();
        modeloResultados.addColumn("ID");
        modeloResultados.addColumn("DNI");
        modeloResultados.addColumn("NOMBRE");
        modeloResultados.addColumn("FECHA");
        modeloResultados.addColumn("ESTADO");
        modeloResultados.addColumn("HORA");

        // Obtener el modelo de la tabla original (jTableAlumno)
        DefaultTableModel modeloAsistencia = (DefaultTableModel) jTableAsistencia.getModel();

        // Variable para rastrear si se encontraron resultados
        boolean resultadosEncontrados = false;

        // Iterar a través de las filas de la tabla original
        for (int fila = 0; fila < modeloAsistencia.getRowCount(); fila++) {
            String ID = modeloAsistencia.getValueAt(fila, 0).toString(); // Obtener el DNI de la fila
            String DNI = modeloAsistencia.getValueAt(fila, 1).toString(); // Obtener el nombre de la fila
            String Nombre = modeloAsistencia.getValueAt(fila, 2).toString(); // Obtener el apellido paterno de la fila
            String Fecha = modeloAsistencia.getValueAt(fila, 3).toString(); // Obtener el apellido materno de la fila

            // Utilizar Normalizer para eliminar diacríticos
            ID = Normalizer.normalize(ID, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            DNI = Normalizer.normalize(DNI, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            Nombre = Normalizer.normalize(Nombre, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            Fecha = Normalizer.normalize(Fecha, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            // Si el nombre coincide con la búsqueda, agregar la fila al modelo de resultados
            if (ID.equalsIgnoreCase(IDBusqueda) ||
                DNI.equalsIgnoreCase(DNIBusqueda) ||
                Nombre.equalsIgnoreCase(NOMBREBusq) ||
                Fecha.equalsIgnoreCase(FECHABusq)) {

                Object[] filaResultado = new Object[6];
                filaResultado[0] = ID;
                filaResultado[1] = DNI;
                filaResultado[2] = Nombre;
                filaResultado[3] = Fecha;
                filaResultado[4] = modeloAsistencia.getValueAt(fila, 4);
                filaResultado[5] = modeloAsistencia.getValueAt(fila, 5);
                modeloResultados.addRow(filaResultado);

                // Indicar que se encontraron resultados
                resultadosEncontrados = true;
            }
        }

        // Si no se encontraron resultados, mostrar un mensaje al usuario
        if (!resultadosEncontrados) {
            JOptionPane.showMessageDialog(this, "No se encontró el registro. Intente nuevamente.", "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
            // Puedes restaurar la tabla original o dejarla vacía según tus necesidades
            jTableAsistencia.setModel(modeloAsistencia); // Restaura la tabla original
        } else {
            // Establecer el modelo de resultados en la tabla (jTableAlumno) solo si se encontraron resultados
            jTableAsistencia.setModel(modeloResultados);
        }

    }//GEN-LAST:event_BBuscarAsistenciaActionPerformed

    private void TLBuscarFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLBuscarFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLBuscarFechaActionPerformed

    private void TLBuscarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLBuscarAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLBuscarAlumnoActionPerformed

    private void TLDiasAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLDiasAsistenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLDiasAsistenciaActionPerformed

    private void jTableAlumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAlumnoMouseClicked
        int fila = jTableAlumno.getSelectedRowCount();
        if (fila < 1){
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
        }else{
            int row = jTableAlumno.getSelectedRow();
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
            String fecha = formatoFecha.format(fechaActual);
            String hora = formatoHora.format(fechaActual);
            TLDni.setText(jTableAlumno.getValueAt(row, 0).toString());
            TLNombre.setText(jTableAlumno.getValueAt(row, 1).toString());
            TLApellP.setText(jTableAlumno.getValueAt(row, 2).toString());
            TLApellMat.setText(jTableAlumno.getValueAt(row, 3).toString());
            TLDiasAsistencia.setText(jTableAlumno.getValueAt(row, 4).toString());
            TLEdad.setText(jTableAlumno.getValueAt(row, 5).toString());
            TLTel.setText(jTableAlumno.getValueAt(row, 6).toString());
            TLFecha.setText(fecha);
            TLHora.setText(hora);
            num = 1;
        }
        listar();
    }//GEN-LAST:event_jTableAlumnoMouseClicked

    private void BBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BBuscarActionPerformed
            // Obtener los valores de búsqueda desde los componentes JTextField
    String dniBusqueda = TLBuscarAlumno.getText().trim();
    String nombreBusqueda = TLBuscarAlumno.getText().trim();
    String apellidoBusqueda = TLBuscarAlumno.getText().trim();

    // Crear un nuevo modelo de tabla para los resultados de la búsqueda
    DefaultTableModel modeloResultados = new DefaultTableModel();
    modeloResultados.addColumn("DNI");
    modeloResultados.addColumn("Nombre");
    modeloResultados.addColumn("Apellido Pat");
    modeloResultados.addColumn("Apellido Mat");
    modeloResultados.addColumn("Dias Asistencia");
    modeloResultados.addColumn("Edad");
    modeloResultados.addColumn("Teléfono");

    // Obtener el modelo de la tabla original (jTableAlumno)
    DefaultTableModel modeloAlumno = (DefaultTableModel) jTableAlumno.getModel();

    // Variable para rastrear si se encontraron resultados
    boolean resultadosEncontrados = false;

    // Iterar a través de las filas de la tabla original
    for (int fila = 0; fila < modeloAlumno.getRowCount(); fila++) {
        String dni = modeloAlumno.getValueAt(fila, 0).toString(); // Obtener el DNI de la fila
        String nombre = modeloAlumno.getValueAt(fila, 1).toString(); // Obtener el nombre de la fila
        String apellidoPat = modeloAlumno.getValueAt(fila, 2).toString(); // Obtener el apellido paterno de la fila
        String apellidoMat = modeloAlumno.getValueAt(fila, 3).toString(); // Obtener el apellido materno de la fila

        // Utilizar Normalizer para eliminar diacríticos
        dni = Normalizer.normalize(dni, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        nombre = Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        apellidoPat = Normalizer.normalize(apellidoPat, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        apellidoMat = Normalizer.normalize(apellidoMat, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Verificar si alguno de los campos coincide con la búsqueda
        if (dni.equalsIgnoreCase(dniBusqueda) ||
            nombre.equalsIgnoreCase(nombreBusqueda) ||
            apellidoPat.equalsIgnoreCase(apellidoBusqueda) ||
            apellidoMat.equalsIgnoreCase(apellidoBusqueda)) {

            Object[] filaResultado = new Object[7];
            filaResultado[0] = dni;
            filaResultado[1] = nombre;
            filaResultado[2] = apellidoPat;
            filaResultado[3] = apellidoMat;
            filaResultado[4] = modeloAlumno.getValueAt(fila, 4);
            filaResultado[5] = modeloAlumno.getValueAt(fila, 5);
            filaResultado[6] = modeloAlumno.getValueAt(fila, 6);
            modeloResultados.addRow(filaResultado);

            // Indicar que se encontraron resultados
            resultadosEncontrados = true;
        }
    }

    // Si no se encontraron resultados, mostrar un mensaje al usuario
    if (!resultadosEncontrados) {
        JOptionPane.showMessageDialog(this, "No se encontró el registro. Intente nuevamente.", "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
        // Puedes restaurar la tabla original o dejarla vacía según tus necesidades
        jTableAlumno.setModel(modeloAlumno); // Restaura la tabla original
    } else {
        // Establecer el modelo de resultados en la tabla (jTableAlumno) solo si se encontraron resultados
        jTableAlumno.setModel(modeloResultados);
    }


    }//GEN-LAST:event_BBuscarActionPerformed

    private void BGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_BGuardarActionPerformed

    private void BSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSalirActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_BSalirActionPerformed

    private void TLTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLTelActionPerformed

    private void ComboBoxEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxEstadoActionPerformed
        // TODO add your handling code here:
        // Obtener la fecha y hora actual
        Date fechaActual = new Date();

        // Formatear la fecha y hora en el formato deseado
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        String fecha = formatoFecha.format(fechaActual);
        String hora = formatoHora.format(fechaActual);

        // Ahora puedes utilizar las variables 'fecha' y 'hora' para guardarlas en la base de datos o realizar otras operaciones
        // Por ejemplo, puedes asignarlas a los campos correspondientes en tu formulario
        TLFecha.setText(fecha);
        TLHora.setText(hora);

        // Resto de tu lógica para guardar los datos en la base de datos
    }//GEN-LAST:event_ComboBoxEstadoActionPerformed

    private void TLEdadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLEdadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLEdadActionPerformed

    private void TLApellMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLApellMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLApellMatActionPerformed

    private void TLApellPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLApellPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLApellPActionPerformed

    private void TLNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLNombreActionPerformed

    private void TLDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLDniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLDniActionPerformed

    private void BLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BLimpiarActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_BLimpiarActionPerformed

    private void BDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDescargarActionPerformed
        // TODO add your handling code here:
        try{
            obj = new Exportar();// manda a llamar la clase exportar
            obj.exportarExcel(jTableAsistencia); // mandamos el metodo exportar excel
        } catch (IOException ex){
        }
    }//GEN-LAST:event_BDescargarActionPerformed

    private void jTableAsistenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAsistenciaMouseClicked
        int fila = jTableAsistencia.getSelectedRowCount();
        limpiar();
        if (fila < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
        } else {
            int row = jTableAsistencia.getSelectedRow();
            TLDni.setText(jTableAsistencia.getValueAt(row, 1).toString());
            TLNombre.setText(jTableAsistencia.getValueAt(row, 2).toString());
            TLFecha.setText(jTableAsistencia.getValueAt(row, 3).toString());
            String estado = jTableAsistencia.getValueAt(row, 4).toString();
            // Itera a través de los elementos del ComboBoxEstado
            for (int i = 0; i < ComboBoxEstado.getItemCount(); i++) {
                if (ComboBoxEstado.getItemAt(i).equals(estado)) {
                    ComboBoxEstado.setSelectedIndex(i); // Selecciona el elemento que coincide
                    break; // Sal del bucle una vez que se haya encontrado la coincidencia
                }
            }
            TLHora.setText(jTableAsistencia.getValueAt(row, 5).toString());

            num = 1;
        }
        listarAsistencia();
    }//GEN-LAST:event_jTableAsistenciaMouseClicked

    private void TLHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLHoraActionPerformed

    private void TLFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TLFechaActionPerformed

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
            java.util.logging.Logger.getLogger(FormControlAsistenica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormControlAsistenica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormControlAsistenica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormControlAsistenica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormControlAsistenica().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BBuscar;
    private javax.swing.JButton BBuscarAsistencia;
    private javax.swing.JButton BDescargar;
    private javax.swing.JButton BGuardar;
    private javax.swing.JButton BLimpiar;
    private javax.swing.JButton BSalir;
    private javax.swing.JButton BTEliminar;
    private javax.swing.JComboBox<String> ComboBoxEstado;
    private javax.swing.JLabel FONDO;
    private javax.swing.JTextField TLApellMat;
    private javax.swing.JTextField TLApellP;
    private javax.swing.JTextField TLBuscarAlumno;
    private javax.swing.JTextField TLBuscarFecha;
    private javax.swing.JTextField TLDiasAsistencia;
    private javax.swing.JTextField TLDni;
    private javax.swing.JTextField TLEdad;
    private javax.swing.JTextField TLFecha;
    private javax.swing.JTextField TLHora;
    private javax.swing.JTextField TLNombre;
    private javax.swing.JTextField TLTel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableAlumno;
    private javax.swing.JTable jTableAsistencia;
    // End of variables declaration//GEN-END:variables
}
