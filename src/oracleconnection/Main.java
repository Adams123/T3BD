package oracleconnection;

import javax.swing.JFrame;

public class Main
{
    public static void main(String[] args)
    {

        String host = "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl"; //conexao com lab grad
        String user = "6791943"; //usuario
        String pass = "basedededos"; //senha 
        String conexao = "oracle.jdbc.driver.OracleDriver"; //driver de conexao

        JFrame tela = new telaInicial(host, user, pass, conexao);
        tela.setVisible(true);
    }

}
