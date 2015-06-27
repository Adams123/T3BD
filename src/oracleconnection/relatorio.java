/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconnection;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Adams
 */
final class relatorio extends JFrame {
String host, user, pass = null;
    Connection con;
    JFrame j;
    JPanel panelBaixo, panelTopo;
    JButton salvar, sair;
    JTable tabelaRelatorio;
    JScrollPane paneRelatorio;

    public relatorio(String host, String user, String pass, Connection con){
        this.host=host;
        this.user=user;
        this.pass=pass;
        this.con=con;
        
        exibeJanelaRelatorio();
    }
    
    public void exibeJanelaRelatorio(){
        j = new JFrame("ICMC-USP - SCC0240 - Projeto 3");
        j.setSize(700, 600);
        j.setLayout(new BorderLayout());
        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //definindo áreas de botões
        panelBaixo = new JPanel();
        j.add(panelBaixo, BorderLayout.SOUTH);
        panelTopo = new JPanel();
        j.add(panelTopo, BorderLayout.NORTH);
        //criando os botões
        salvar = new JButton();
        salvar.setText("Salvar");
        sair = new JButton();
        sair.setText("Fechar");
        //área de status
        panelBaixo.add(salvar);
        panelBaixo.add(sair);

        eventosBotoes();
        tabelaRelatorio = exibeRelatorio(j, con);

        j.setVisible(true);
    }
    
    public void eventosBotoes(){
        sair.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                j.dispose(); //fecha janela atual
            }
        });
        salvar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                j.dispose(); //fecha janela atual
            }
        });
    }
    
    public JTable exibeRelatorio(JFrame principal, Connection conexao){
        String input;
        int foi = 0;
        int count = 0;
        input = "SELECT I.PERFIL,I.LINGUA,F.TITULO FROM FILME F, AUDIOFILME A, LEGENDAFILME L, " + 
                "IDIOMAPERFIL I, ASSISTE S, FICAAMIGO FC, AVALIACAO V\n" +
                "WHERE F.TITULO = A.FILME AND\n" +
                "L.FILME = F.TITULO AND\n" +
                "(I.LINGUA = A.AUDIO OR I.LINGUA = L.LEGENDA)\n" +
                "AND FC.PERFILA = I.PERFIL\n" +
                "AND ((S.PERFIL = FC.PERFILB\n" +
                "AND S.FILME = F.TITULO)\n" +
                "OR (V.FILME = F.TITULO \n" +
                "AND V.PERFIL = FC.PERFILB))\n" +
                "GROUP BY I.PERFIL,I.LINGUA,F.TITULO\n" +
                "ORDER BY I.PERFIL";

        Vector columnNames = new Vector();
        Vector data = new Vector();
        System.out.println(foi++);
        try
        {

            PreparedStatement instrucao = conexao.prepareStatement(input);
            //construção da classe PreparedStatement para passagem de parâmetros
            System.out.println(foi++);
            ResultSet result = instrucao.executeQuery(); //recebe os resultados da query
            ResultSetMetaData resultados = result.getMetaData(); //cria metadados dos resultados
            int colunas = resultados.getColumnCount(); //pega quantidade de colunas
            
            while (result.next())
            {
                count++;
            }
            if (count == 0)
            {
                return null;
            }
            for (int i = 1; i <= colunas; i++)
            {
                columnNames.addElement(resultados.getColumnName(i)); //adiciona os nomes das colunas ao vetor de nomes
            }
            System.out.println(foi++);
            result = instrucao.executeQuery();//reposiciona ponteiro de leitura dos resultados
            while (result.next())
            {
                Vector row = new Vector(colunas);     //cria as tuplas com os dados para exibicao
                for (int i = 1; i <= colunas; i++)
                {
                    row.addElement(result.getObject(i));
                }
                data.addElement(row); //adiciona no vetor de dados as tuplas
            }
            System.out.println(foi++);
            result.close();       //encerra a consulta
        } catch (SQLException e)
        {
        }
        System.out.println(foi++);
        //cria tabela e painel novo para exibir as consultas, retornando a mesma
        DefaultTableModel d = new DefaultTableModel(data, columnNames);
        JTable tATable = new JTable(d);
        tATable.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = tATable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tATable); //cria o painel para colocar a tabela
        principal.add(scrollPane);

        return tATable;
    }
}
