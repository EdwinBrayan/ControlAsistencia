// CLASE DE CONEXION CONECTADO A MYSQL
package Conexion;
package com.mycompany.mysqljava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class cConexionMS {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver" ;
    private static final String USER  = "root";
    private static final String PASSWORD = "543210";
    private static final String SERVIDOR = "127.0.0.1:3306";
    private static final String BaseDatos = "BD_Asistencia";
    private static final String URL = "jdbc:mysql://" + SERVIDOR + "/" + BaseDatos;
    private Connection CN;
    
    public cConexionMS(){
        CN = null;
    
    }
    
    public Connection getConnection(){
        try {
            Class.forName(DRIVER);
            CN = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return CN;   
    }
    
    public void close(){
        try{
            CN.close();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }  
    }    
}