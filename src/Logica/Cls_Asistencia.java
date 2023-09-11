
package Logica;

import Conexion.cConexionMS;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class Cls_Asistencia {
    
    private final String sql_Insert = "INSERT INTO TAlumno (DNI,Nombre, Apellido_pat,Apellido_Mat, DiasAsistencia, Edad, Telefono) VALUES (?, ?, ?, ?, ?, ?)";
    private final String sql_Select = "SELECT * From TAlumno";
    
    // Supongamos que ya tienes los valores capturados en las variables dni, fecha, estado y hora
    private final String sql_InsertAsistencia = "INSERT INTO TAsistencia (DNI ,Nombre, Fecha, Estado, Hora) VALUES (?, ?, ?, ?, ?)";
    private final String sql_SelecAsistencia = "SELECT * From TAsistencia";
    
    private PreparedStatement PS;
    private DefaultTableModel DT;
    private DefaultTableModel DTT;
    private JTextField TF;
    private ResultSet RS;
    private final cConexionMS CN;     
    
    public Cls_Asistencia(){
        PS = null;
        CN = new cConexionMS();
    }
    private DefaultTableModel setTitulos(){
        DT = new DefaultTableModel();
        DT.addColumn("Dni");
        DT.addColumn("Nombre");
        DT.addColumn("ApellidoP");
        DT.addColumn("ApellidoM");
        DT.addColumn("DiasAsistencia");
        DT.addColumn("Edad");
        DT.addColumn("Telefono");
        return DT; 
    }
    
    private DefaultTableModel setTituloss(){
        DTT = new DefaultTableModel();
        DTT.addColumn("ID");
        DTT.addColumn("DNI");
        DTT.addColumn("NOMBRE");
        DTT.addColumn("FECHA");
        DTT.addColumn("ESTADO");
        DTT.addColumn("HORA");
        return DTT; 
    }
    
    
    public int insertDatos(String cod, String Nom, String Ap, String Am, String DA, String Edad, String Tel){
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(sql_Insert);
            PS.setString(1, cod);
            PS.setString(2, Nom);
            PS.setString(3, Ap);
            PS.setString(4, Am);
            PS.setString(5, DA);
            PS.setString(6, Edad);
            PS.setString(7, Tel);
            
            res = PS.executeUpdate();
            if(res >0){
                JOptionPane.showMessageDialog(null,"Registro Guardado...");
            }
        } catch (SQLException ex){
            System.err.println("Error en Guardar los Datos en la BD"+ex.getMessage());
        } finally{
            PS = null;
            CN.close();
        }
        return res; 
    }
    
    public int insertDatosAsistencia(String DNI_Alumno,String Nombre, String Fecha, String Estado, String Hora){
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(sql_InsertAsistencia);
            PS.setString(1, DNI_Alumno);
            PS.setString(2, Nombre);
            PS.setString(3, Fecha);
            PS.setString(4, Estado);
            PS.setString(5, Hora);
            
            res = PS.executeUpdate();
            if(res >0){
                JOptionPane.showMessageDialog(null,"Registro Guardado..");
            }
        } catch (SQLException ex){
            System.err.println("Error en Guardar los Datos en la BD"+ex.getMessage());
        } finally{
            PS = null;
            CN.close();
        }
        return res; 
    }
    
    public DefaultTableModel getDatos(){
        try{
            setTitulos();
            PS = CN.getConnection().prepareStatement(sql_Select);
            RS = PS.executeQuery();
            Object[] fila = new Object[7];
            while(RS.next()){
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);  
                fila[2] = RS.getString(3);  
                fila[3] = RS.getString(4);  
                fila[4] = RS.getString(5);  
                fila[5] = RS.getString(6);
                fila[6] = RS.getString(7);
                DT.addRow(fila);
                
            }
        } catch (SQLException e){
            System.out.println("Error al Listar los Datos:"+e.getMessage());       
        } finally{
            PS = null;
            RS = null;
            CN.close();
        
        }
        return DT;    
    }     
    
    public DefaultTableModel getDatoss(){
        try{
            setTituloss();
            PS = CN.getConnection().prepareStatement(sql_SelecAsistencia);
            RS = PS.executeQuery();
            Object[] fila = new Object[6];
            while(RS.next()){
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);  
                fila[2] = RS.getString(3);  
                fila[3] = RS.getString(4);
                fila[4] = RS.getString(5);
                fila[5] = RS.getString(6);
                DTT.addRow(fila);
                
            }
        } catch (SQLException e){
            System.out.println("Error al Listar los Datos:"+e.getMessage());       
        } finally{
            PS = null;
            RS = null;
            CN.close();
        
        }
        return DTT;    
    }     
    
    public int deleteDatos(int ID_Asistencia) {
    int res = 0;
    String sql = "DELETE FROM TAsistencia WHERE ID = ?";
    
    try {
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de Eliminar este Registro?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            PS = CN.getConnection().prepareStatement(sql);
            PS.setInt(1, ID_Asistencia); // Establecer el valor del ID_Asistencia como parámetro
            res = PS.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Eliminado...");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Eliminación Cancelada.");
        }
    } catch (SQLException ex) {
        System.err.println("Error al Eliminar los Datos en la BD " + ex.getMessage());
    } finally {
        PS = null;
        CN.close();
    }
    return res;
}
}