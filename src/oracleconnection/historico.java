package oracleconnection;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        //definindo áreas de botões e tabelas
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
        //inserindo componentes em seus devidos lugares
        panelBaixo.add(inserir);
        panelBaixo.add(remover);
        panelBaixo.add(alterar);
        panelBaixo.add(sair);
        panelBaixo.add(jtAreaDeStatus);
        //inicializando detecção de eventos
        eventosBotoes();
        //cria a tabela dos históricos
        tabelaHistorico = exibeHistorico(panelTopo, con, "HISTORICODEPAGAMENTO");
        
        j.pack();
        j.setVisible(true);
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
                hist = new inserirHistorico(con); //inserindo no historico
                hist.setComboboxes(con); //pegando as fks da tabela historico
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
                //ao clicar em alterar, é verificado se tem alguma tupla selecionada
                if (tabelaHistorico.getSelectedRow() != -1)
                {
                    //criando janela principal
                    JFrame alterar = new JFrame();
                    alterar.setSize(300, 200);
                    alterar.setLayout(new BorderLayout());
                    alterar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    //criando áreas na janela para botoes e campos
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
                    //criando caixas de diálogos e de opções (para fks)
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
                    //adicionando as comboboxes e campos nos lugares certos
                    for (int i = 0; i < 3; i++)
                    {
                        panelDir.add(boxes[i]);
                        panelEsq.add(labels[i]);
                    }
                    //ação de clicar em Alterar na interface final
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
                    alterar.pack();
                    alterar.setVisible(true);
                } else //caso não tenha sido selecionado nenhuma tupla para alteração
                {
                    jtAreaDeStatus.setText("Por favor selecione uma tupla");
                }
            }

        });
    }

    public String arrumaData(String pk2) //deixa a data no formato de inserção 
            //correto para o banco de dados (dd/mm/yy)
    {

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

    public void alterar(String pk1, String pk2, String usuario,
            String plano, String dataForma, Connection conexao)
    {
        //removendo caracteres das strings e pks
        usuario = usuario.replace("[", "");
        usuario = usuario.replace("]", "");
        plano = plano.replace("[", "");
        plano = plano.replace("]", "");
        dataForma = dataForma.replace("[", "");
        dataForma = dataForma.replace("]", "");
        dataForma = dataForma.replace(",", "");
        String split[] = dataForma.split(" ");
        String data = split[0];
        String forma = split[1];
        pk2 = arrumaData(pk2);

        String input = "UPDATE HISTORICODEPAGAMENTO SET USUARIO=" + usuario + " ,"
                + " PLANO = " + plano + ", DATA = '" + data + "',"
                + " FORMA = '" + forma.toUpperCase() + "'"
                + " WHERE USUARIO = " + pk1 + " AND DATA = '" + pk2 + "'";

        //realizando a atualizacao
        try
        {
            PreparedStatement instrucao = conexao.prepareStatement(input);
            instrucao.executeUpdate();
        } catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "ERRO SQL: " + e.getMessage());
        }

    }

    public void setFksBoxes(JComboBox boxes[])
            //metodo para montar comboboxes necessárias contendo as fks da tabela historicodepagamento
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
            while (result1.next() || result2.next() || result3.next()) //verifica se algum resultado retornou nulo
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
                data3.addElement(row);//adiciona no vetor de dados as tuplas
            }

            result1.close();       //encerra as consultas
            result2.close();
            result3.close();

        } catch (SQLException e)
        {
            jtAreaDeStatus.setText("ERRO SQL: " + e.getMessage());
        }

        for (int i = 0; i < data1.size(); i++) //insere os resultados nas comboboxes
        {
            boxes[0].addItem(data1.elementAt(i).toString());
        }

        for (int i = 0; i < data2.size(); i++)
        {
            boxes[1].addItem(data2.elementAt(i).toString());
        }
        for (int i = 0; i < data3.size(); i++)
        {
            boxes[2].addItem(data3.get(i).toString());
        }
    }

    public void deletar(String pk1, String pk2, Connection conexao)
            //deleta da tabela historicodepagamento dado as duas pks da tabela
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

    public JTable exibeHistorico(JPanel principal, Connection conexao, String tablename)
    {
        //insere no panel da janela principal a tabela historicodepagamento
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
        DefaultTableModel d = new DefaultTableModel(data, columnNames); //cria um modelo de tabela
        JTable tATable = new JTable(d); //cria a tabela baseado no modelo de tabela
        tATable.setCellSelectionEnabled(true); //permite seleção de células
        ListSelectionModel cellSelectionModel = tATable.getSelectionModel(); 
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tATable); //cria o painel para colocar a tabela
        principal.add(scrollPane);
        jtAreaDeStatus.setText("Tabela " + tablename + "\n Tuplas: " + count); //avisa qual tabela está sendo exibida

        return tATable;

    }
}
