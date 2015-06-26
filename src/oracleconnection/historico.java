/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconnection;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
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
public final class historico extends JFrame{
    String host, user, pass = null;
    Connection con;
    int tamx=500, tamy=100;
    JFrame j;
    JPanel panelBaixo, panelTopo;
    JButton remover, alterar, inserir, sair;
    JTable tabelaHistorico;
    JScrollPane paneHistorico;
    JTextField jtAreaDeStatus;
    
    public historico(String host, String user, String pass, Connection con){
        this.host=host;
        this.user=user;
        this.pass=pass;
        this.con = con;
        
        exibeJanelaHistorico();
    };
    
    public void exibeJanelaHistorico(){
        //janela principal
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
        inserir = new JButton();
        inserir.setText("Inserir");
        remover = new JButton();
        remover.setText("Remover");
        alterar = new JButton();
        alterar.setText("Alterar");
        sair = new JButton();
        sair.setText("Fechar");
        //área de status
        jtAreaDeStatus = new JTextField();
        
        panelBaixo.add(inserir);
        panelBaixo.add(remover);
        panelBaixo.add(alterar);
        panelBaixo.add(sair);
        panelBaixo.add(jtAreaDeStatus);
        
        eventosBotoes();
        tabelaHistorico = exibeHistorico(j,con,"HISTORICODEPAGAMENTO");
        
        j.setVisible(true);
        j.addWindowFocusListener(new WindowFocusListener(){

            public void windowGainedFocus(WindowEvent we) {
                //pra faze rum dia ae, se clicar na janela atualizar a tabela historico
            }

            public void windowLostFocus(WindowEvent we) {
            }
            
        });

        
    }
    
    public void eventosBotoes(){
        sair.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                j.dispose(); //fecha janela atual
            }
        });
        inserir.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae) {
                inserirHistorico hist;
                hist = new inserirHistorico(con);
                hist.setComboboxes(con);
                hist.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                hist.setVisible(true);
            }
        });
    }
    
    public JTable exibeHistorico(JFrame principal, Connection conexao, String tablename){
        String input;
        
        input = "SELECT * FROM " + tablename;
        
        Vector columnNames = new Vector();
        Vector data = new Vector();
        
        try {
            
            PreparedStatement instrucao = conexao.prepareStatement(input);
            //construção da classe PreparedStatement para passagem de parâmetros

            ResultSet result = instrucao.executeQuery(); //recebe os resultados da query
            ResultSetMetaData resultados = result.getMetaData(); //cria metadados dos resultados
            int colunas = resultados.getColumnCount(); //pega quantidade de colunas
            int count = 0;
            while (result.next()) {
                count++;
            }
            if (count == 0) {
                jtAreaDeStatus.setText("Nenhum resultado encontrado.");
                return null;
            }
            for (int i = 1; i <= colunas; i++) {
                columnNames.addElement(resultados.getColumnName(i)); //adiciona os nomes das colunas ao vetor de nomes
            }
            result = instrucao.executeQuery();//reposiciona ponteiro de leitura dos resultados
            while (result.next()) {
                Vector row = new Vector(colunas);     //cria as tuplas com os dados para exibicao
                for (int i = 1; i <= colunas; i++) {
                    row.addElement(result.getObject(i));
                }
                data.addElement(row); //adiciona no vetor de dados as tuplas
            }
            
            result.close();       //encerra a consulta
        } catch (SQLException e) {
            jtAreaDeStatus.setText("ERRO SQL: " + e.getMessage());
        }
        //cria tabela e painel novo para exibir as consultas, retornando a mesma
        DefaultTableModel d = new DefaultTableModel(data, columnNames);
        JTable tATable = new JTable(d);
        tATable.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = tATable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tATable); //cria o painel para colocar a tabela
        principal.add(scrollPane);
        jtAreaDeStatus.setText("Tabela " + tablename); //avisa qual tabela está sendo exibida
        
        return tATable;

    }
}
