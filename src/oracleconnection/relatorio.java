/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconnection;

import javax.swing.JFrame;

/**
 *
 * @author Adams
 */
class relatorio extends JFrame {
String host, user, pass = null;
    public relatorio(String host, String user, String pass){
        this.host=host;
        this.user=user;
        this.pass=pass;
    }
    
}
