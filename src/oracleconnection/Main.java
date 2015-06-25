package oracleconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author Robson (material original editado: Prof. José Fernando Rodrigues Jr.)
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String host = "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl";
        String user = "6791943";
        String pass = "basedededos";
        String conexao = "oracle.jdbc.driver.OracleDriver";
        
        JFrame tela = new telaInicial(host, user, pass,conexao);
        tela.setVisible(true);
    }
    
}
