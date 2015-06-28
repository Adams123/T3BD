/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconnection;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public final class historico extends JFrame
{

    String host, user, pass = null;
    Connection con;
    int tamx = 500, tamy = 100;
    JFrame j;
    JPanel panelBaixo, panelTopo;
    JButton remover, alterar, inserir, sair;
    JTable tabelaHistorico;
    JScrollPane paneHistorico;
    JTextField jtAreaDeStatus;

    public historico(String host, String user, String pass, Connection con)
    {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.con = con;

        exibeJanelaHistorico();
    }
    
    public void exibeJanelaHistorico()
    {
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
        tabelaHistorico = exibeHistorico(j, con, "HISTORICODEPAGAMENTO");
        j.pack();
        j.setVisible(true);
        j.addWindowFocusListener(new WindowFocusListener()
        {

            public void windowGainedFocus(WindowEvent we)
            {
                //atualizar tabela sempre que ganhar foco
            }

            public void windowLostFocus(WindowEvent we)
            {
            }
        });
    }

    public void eventosBotoes()
    {
        sair.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                j.dispose(); //fecha janela atual
            }
        });
        inserir.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                inserirHistorico hist;
                hist = new inserirHistorico(con);
                hist.setComboboxes(con);
                hist.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                hist.setVisible(true);
                
            }
        });
        remover.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                int row = tabelaHistorico.getSelectedRow();
                String pk1 = tabelaHistorico.getValueAt(row, 0).toString();
                String pk2 = tabelaHistorico.getValueAt(row, 2).toString();

                deletar(pk1, pk2, con);
            }
        });
        alterar.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                if (tabelaHistorico.getSelectedRow() != -1)
                {

                    JFrame alterar = new JFrame();
                    alterar.setSize(300, 200);
                    alterar.setLayout(new BorderLayout());
                    alterar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    JPanel panelEsq = new JPanel();
                    JPanel panelDir = new JPanel();
                    JPanel panelBot = new JPanel();
                    panelEsq.setLayout(new GridLayout(3, 1));
                    panelDir.setLayout(new GridLayout(3, 1));
                    alterar.add(panelEsq, BorderLayout.WEST);
                    alterar.add(panelDir, BorderLayout.EAST);
                    alterar.add(panelBot, BorderLayout.SOUTH);
                    
                    JButton btAlterar = new JButton("Alterar");
                    panelBot.add(btAlterar);
                    
                    final JComboBox boxes[] = new JComboBox[3];
                    JLabel labels[] = new JLabel[3];
                    for (int i = 0; i < 3; i++)
                    {
                        boxes[i] = new JComboBox();
                        labels[i] = new JLabel();
                    }
                    setFksBoxes(boxes);

                    labels[0].setText("Usuario");
                    labels[1].setText("Plano");
                    labels[2].setText("Data, Forma");

                    for (int i = 0; i < 3; i++)
                    {
                        panelDir.add(boxes[i]);
                        panelEsq.add(labels[i]);
                    }
                    
                    btAlterar.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent ae)
                        {
                            alterar(tabelaHistorico.getValueAt(tabelaHistorico.getSelectedRow(), 0).toString(),
                                    tabelaHistorico.getValueAt(tabelaHistorico.getSelectedRow(), 2).toString(),
                                    boxes[0].getSelectedItem().toString(),
                                    boxes[1].getSelectedItem().toString(),
                            boxes[2].getSelectedItem().toString(),
                            con);
                        }
                    });
                    
                    alterar.setVisible(true);
                } else
                {
                    jtAreaDeStatus.setText("Por favor selecione uma tupla");
                }
            }

        });
    }
    public String arrumaData(String pk2){
        
        String data[] = new String[3];
        for (int i = 0; i < 3; i++)
        {
            data[i] = new String();
        }
        data = pk2.split("-");
        pk2 = "";
        pk2 = pk2.concat(data[2] + "/" + data[1] + "/" + data[0]);
        return pk2;
    }
    public void alterar(String pk1, String pk2, String usuario, String plano, String dataForma, Connection conexao)
    {
        usuario=usuario.replace("[", "");
        usuario=usuario.replace("]", "");
        plano=plano.replace("[", "");
        plano=plano.replace("]", "");
        dataForma=dataForma.replace("[", "");
        dataForma=dataForma.replace("]", "");
        dataForma=dataForma.replace(",", "");
        String split[] = dataForma.split(" ");
        String data = split[0];
        String forma = split[1];
        pk2=arrumaData(pk2);
        
        String input = "UPDATE HISTORICODEPAGAMENTO SET USUARIO=" + usuario + " ,"
                + " PLANO = " + plano + ", DATA = '" + data + "',"
                + " FORMA = '" + forma.toUpperCase() + "'"
                + " WHERE USUARIO = " + pk1 + " AND DATA = '" + pk2 + "'";
        
        try
        {
            PreparedStatement instrucao = conexao.prepareStatement(input);
            instrucao.executeUpdate();
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, "ERRO SQL: " + e.getMessage());
        }
        
    }

    public void setFksBoxes(JComboBox boxes[])
    {
        String input1 = "SELECT U.CPF FROM USUARIO U";
        String input2 = "SELECT P.CODPLANO FROM PLANO P";
        String input3 = "SELECT to_char(P.DATA,'DD/MM/YY'), P.FORMA FROM PAGAMENTO P";

        Vector data1 = new Vector();
        Vector data2 = new Vector();
        Vector data3 = new Vector();

        try
        {

            PreparedStatement instrucao1 = con.prepareStatement(input1);
            PreparedStatement instrucao2 = con.prepareStatement(input2);
            PreparedStatement instrucao3 = con.prepareStatement(input3);
            //construção da classe PreparedStatement para passagem de parâmetros

            ResultSet result1 = instrucao1.executeQuery(); //recebe os resultados da query
            ResultSet result2 = instrucao2.executeQuery(); //recebe os resultados da query
            ResultSet result3 = instrucao3.executeQuery(); //recebe os resultados da query

            ResultSetMetaData resultados1 = result1.getMetaData(); //cria metadados dos resultados
            ResultSetMetaData resultados2 = result2.getMetaData(); //cria metadados dos resultados
            ResultSetMetaData resultados3 = result3.getMetaData(); //cria metadados dos resultados

            int colunas1 = resultados1.getColumnCount(); //pega quantidade de colunas
            int colunas2 = resultados2.getColumnCount(); //pega quantidade de colunas
            int colunas3 = resultados3.getColumnCount(); //pega quantidade de colunas

            int count = 0;
            while (result1.next() || result2.next() || result3.next())
            {
                count++;
            }
            if (count == 0)
            {
                jtAreaDeStatus.setText("Nenhum resultado encontrado");
                return;
            }
            result1 = instrucao1.executeQuery();//reposiciona ponteiro de leitura dos resultados
            result2 = instrucao2.executeQuery();//reposiciona ponteiro de leitura dos resultados
            result3 = instrucao3.executeQuery();//reposiciona ponteiro de leitura dos resultados
            while (result1.next())
            {
                Vector row = new Vector(colunas1);     //cria as tuplas com os dados para exibicao
                for (int i = 1; i <= colunas1; i++)
                {
                    row.addElement(result1.getObject(i));
                }
                data1.addElement(row); //adiciona no vetor de dados as tuplas
            }
            while (result2.next())
            {
                Vector row = new Vector(colunas2);     //cria as tuplas com os dados para exibicao
                for (int i = 1; i <= colunas2; i++)
                {
                    row.addElement(result2.getObject(i));
                }
                data2.addElement(row); //adiciona no vetor de dados as tuplas
            }
            while (result3.next())
            {
                Vector row = new Vector(colunas3);     //cria as tuplas com os dados para exibicao
                for (int i = 1; i <= colunas3; i++)
                {
                    row.addElement(result3.getObject(i));
                }
                data3.addElement(row);
            }

            result1.close();       //encerra a consulta
            result2.close();
            result3.close();

        } catch (SQLException e)
        {
            jtAreaDeStatus.setText("ERRO SQL: " + e.getMessage());
        }

        for (int i = 0; i < data1.capacity(); i++)
        {
            boxes[0].addItem(data1.elementAt(i).toString());
        }

        for (int i = 0; i < data2.capacity(); i++)
        {
            boxes[1].addItem(data2.elementAt(i).toString());
        }
        for (int i = 0; i < data3.capacity(); i++)
        {
            boxes[2].addItem(data3.get(i).toString());
        }
    }
    
    public void deletar(String pk1, String pk2, Connection conexao)
    {
        pk2 = arrumaData(pk2);
        String input = "DELETE FROM HISTORICODEPAGAMENTO WHERE USUARIO = "
                + pk1 + " AND DATA = '" + pk2 + "'";
        try
        {
            PreparedStatement instrucao = conexao.prepareStatement(input);
            instrucao.executeUpdate();
        } catch (SQLException e)
        {
            jtAreaDeStatus.setText("ERRO SQL: " + e.getMessage());
        }
    }

    public JTable exibeHistorico(JFrame principal, Connection conexao, String tablename)
    {
        String input;
        int count = 0;
        input = "SELECT * FROM " + tablename;

        Vector columnNames = new Vector();
        Vector data = new Vector();

        try
        {

            PreparedStatement instrucao = conexao.prepareStatement(input);
            //construção da classe PreparedStatement para passagem de parâmetros

            ResultSet result = instrucao.executeQuery(); //recebe os resultados da query
            ResultSetMetaData resultados = result.getMetaData(); //cria metadados dos resultados
            int colunas = resultados.getColumnCount(); //pega quantidade de colunas

            while (result.next())
            {
                count++;
            }
            if (count == 0)
            {
                jtAreaDeStatus.setText("Nenhum resultado encontrado.");
                return null;
            }
            for (int i = 1; i <= colunas; i++)
            {
                columnNames.addElement(resultados.getColumnName(i)); //adiciona os nomes das colunas ao vetor de nomes
            }
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

            result.close();       //encerra a consulta
        } catch (SQLException e)
        {
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
        jtAreaDeStatus.setText("Tabela " + tablename + "\n Tuplas: " + count); //avisa qual tabela está sendo exibida

        return tATable;

    }
}
