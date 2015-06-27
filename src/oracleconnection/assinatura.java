/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconnection;

import java.sql.Connection;
import javax.swing.JFrame;

/**
 *
 * @author Adams
 */
class assinatura extends JFrame {
String host, user, pass = null;
Connection con;
    public assinatura(String host, String user, String pass, Connection con){
        this.host=host;
        this.user=user;
        this.pass=pass;
        this.con = con;
    }
    
}
