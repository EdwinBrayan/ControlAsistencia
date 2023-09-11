//MENU PRINCIPAL DEL ESTUDIANATE CONECATADO A SQLSERVER
package Control;

import Formulario.FormControlAsistenica;// BASE DE DATOS MYSQL


public class Cls_Main {
    public static void main(String[] args){
        FormControlAsistenica FP = new FormControlAsistenica(); // BASE DE DATOS MYSQL

        FP.setVisible(true);
    }   
}
