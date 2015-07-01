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

public final class Generalizacao extends JFrame
{

    String host, user, pass = null;
    Connection con;

    int tamx = 500, tamy = 100;
    JFrame j;
    JPanel panelBaixo, panelTopo;
    JButton remover, alterar, inserir, sair;
    JTable tabelaGeneralizacao;
    JScrollPane paneGeneralizacao;
    JTextField jtAreaDeStatus;
    JComboBox jcSelecao;

    public Generalizacao(String host, String user, String pass, Connection con)
    {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.con = con;

        exibeJanelaGeneralizacao();

    }

    public void exibeJanelaGeneralizacao()
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

        jcSelecao = new JComboBox();
        jcSelecao.addItem("Pessoa");
        jcSelecao.addItem("Ator");
        jcSelecao.addItem("Diretor");
        jcSelecao.addItem("Profissao");
        //área de status
        jtAreaDeStatus = new JTextField();

        panelBaixo.add(inserir);
        panelBaixo.add(remover);
        panelBaixo.add(alterar);
        panelBaixo.add(jcSelecao);
        panelBaixo.add(sair);
        panelBaixo.add(jtAreaDeStatus);

        eventosBotoes();

        tabelaGeneralizacao = exibeTable(panelTopo, con, "PESSOA");
        j.pack();
        j.setVisible(true);
        
        
    }

    public JTable exibeTable(JPanel principal, Connection conexao, String tablename)
            //exibe tudo de uma dada tabela
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

    public void eventosBotoes()
    {
        sair.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                j.dispose();
            }

        });
        inserir.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                JComboBox fks = new JComboBox();
                setFksBoxes(fks, jcSelecao.getSelectedItem().toString().toUpperCase()); //cria a combobox de fks da tabela selecionada
                InserirGeneralizacao gen = new InserirGeneralizacao(con,
                        jcSelecao.getSelectedItem().toString().toUpperCase(), fks);
                //cria a interface de insercao de tuplas na generalizacao
                gen.layoutInsercao();
            }

        });
        alterar.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                if (tabelaGeneralizacao.getSelectedRow() != -1) //verifica se selecionou uma tupla
                {
                    final JTextField texts[]; //campos para digitar valores novos
                    int opcoes = 2; //quantidade de opcoes disponíveis, 2 no caso de Ator ou Diretor
                    if (jcSelecao.getSelectedItem().toString().toUpperCase().compareTo("PESSOA") == 0)
                    {
                        opcoes = 3;
                    }
                    if (jcSelecao.getSelectedItem().toString().toUpperCase().compareTo("PROFISSAO") == 0)
                    {
                        opcoes = 1;
                    }
                    texts = new JTextField[3];
                    for (int i = 0; i < 3; i++)
                    {
                        texts[i] = new JTextField();
                        texts[i].setColumns(20);
                    }
                    //cria a janela principal
                    JFrame alterar = new JFrame();
                    alterar.setSize(300, 200);
                    alterar.setLayout(new BorderLayout());
                    alterar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    //cria panels para insercao dos componentes
                    JPanel panelEsq = new JPanel();
                    JPanel panelDir = new JPanel();
                    JPanel panelBot = new JPanel();
                    panelEsq.setLayout(new GridLayout(opcoes, 1));
                    panelDir.setLayout(new GridLayout(opcoes, 1));
                    alterar.add(panelEsq, BorderLayout.WEST);
                    alterar.add(panelDir, BorderLayout.EAST);
                    alterar.add(panelBot, BorderLayout.SOUTH);
                    
                    JButton btAlterar = new JButton("Alterar");
                    panelBot.add(btAlterar);
                    //cria uma combobox com as fks da tabela correspondente
                    final JComboBox box = new JComboBox();
                    JLabel labels[] = new JLabel[opcoes];
                    
                    for (int i = 0; i < opcoes; i++)
                    {
                        labels[i] = new JLabel();
                    }
                    
                    if (opcoes != 1) //pega fk no caso de nao ser tabela profissao
                    {
                        setFksBoxes(box, jcSelecao.getSelectedItem().toString().toUpperCase());
                    }

                    if (opcoes == 1) //monta para o caso da tabela profissão
                    {
                        panelDir.add(texts[0]);
                        labels[0].setText("Profissão");
                    } else if (opcoes == 2) //monta para o caso da tabela Ator ou Diretor
                    {
                        panelDir.add(texts[0]);
                        panelDir.add(box);
                        labels[0].setText("Idade");
                        labels[1].setText("Nome");
                    } else//monta para o caso da tabela Pessoa
                    {
                        panelDir.add(texts[0]);
                        panelDir.add(box);
                        panelDir.add(texts[1]);
                        labels[0].setText("Nome");
                        labels[1].setText("Profissão");
                        labels[2].setText("Idade");
                    }
                    //adiciona as labels nas posicoes corretas
                    for (int i = 0; i < opcoes; i++)
                    {
                        panelEsq.add(labels[i]);
                    }

                    alterar.pack();

                    btAlterar.addActionListener(new ActionListener() //ao clicar em Alterar, realiza operações no banco
                    {
                        public void actionPerformed(ActionEvent ae)
                        {
                            if (jcSelecao.getSelectedItem().toString().toUpperCase().compareTo("PROFISSAO") == 0)
                            {
                                alterar(texts[0].getText(), null, null,
                                        jcSelecao.getSelectedItem().toString().toUpperCase(),
                                        tabelaGeneralizacao.getValueAt(tabelaGeneralizacao.getSelectedRow(), 0).toString(), con); //envia a PK de profissao, que é uma só
                            } else if (jcSelecao.getSelectedItem().toString().toUpperCase().compareTo("PESSOA") == 0)
                            {
                                alterar(texts[0].getText(), box.getSelectedItem().toString(), texts[1].getText(),
                                        jcSelecao.getSelectedItem().toString().toUpperCase(),
                                        tabelaGeneralizacao.getValueAt(tabelaGeneralizacao.getSelectedRow(), 0).toString(), con);
                            } else
                            {
                                alterar(texts[0].getText(), box.getSelectedItem().toString(), null,
                                        jcSelecao.getSelectedItem().toString().toUpperCase(),
                                        tabelaGeneralizacao.getValueAt(tabelaGeneralizacao.getSelectedRow(), 1).toString(), con);
                            }
                        }
                    });

                    alterar.setVisible(true);
                } else
                {
                    jtAreaDeStatus.setText("Por favor selecione uma tupla");
                }
            }

        });
        remover.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ae)
            {
                int row = tabelaGeneralizacao.getSelectedRow();
                String pk;
                if (jcSelecao.getSelectedIndex() == 0 || jcSelecao.getSelectedIndex() == 3) 
                //verifica de qual tabela está removendo. Caso profissão ou pessoa, a pk se encontra na coluna 0
                //caso ator ou diretor a pk se encontra na coluna 1
                {
                    pk = tabelaGeneralizacao.getValueAt(row, 0).toString();
                } else
                {
                    pk = tabelaGeneralizacao.getValueAt(row, 1).toString();
                }

                deletar(pk.toUpperCase(), con, jcSelecao.getSelectedItem().toString());
            }

        });
        jcSelecao.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                tabelaGeneralizacao.removeAll();
                panelTopo.removeAll();
                panelTopo.repaint();
                panelTopo.revalidate();
                tabelaGeneralizacao = exibeTable(panelTopo, con, jcSelecao.getSelectedItem().toString());
            }
        });

    }

    public void alterar(String op1, String op2, String op3, String tablename, String pk, Connection con)
    {
        op1 = op1.replace("[", "");
        op1 = op1.replace("]", "");
        pk = pk.replace("[", "");
        pk = pk.replace("]", "");
        String input;
        input = "UPDATE " + tablename + " SET PROFISSAO='" + op1.toUpperCase()
                + "' WHERE PROFISSAO = '" + pk.toUpperCase() + "'";
        if (op2 != null)
        {
            op2 = op2.replace("]", "");
            op2 = op2.replace("[", "");
            input = "UPDATE " + tablename.toUpperCase() + " SET IDADE = " + op1.toUpperCase() + ", NOME = '" + op2.toUpperCase() + "'"
                    + " WHERE NOME = '" + pk.toUpperCase() + "'";

        }
        if (op3 != null)
        {
            op3 = op3.replace("]", "");
            op3 = op3.replace("[", "");
            input = "UPDATE " + tablename.toUpperCase() + " SET NOME = '" + op1.toUpperCase() + "', "
                    + "PROFISSAO = '" + op2.toUpperCase() + "', IDADE = " + op3.toUpperCase()
                    + " WHERE NOME = '" + pk.toUpperCase() + "'";
        }
        System.out.println(input);
        try
        {
            PreparedStatement instrucao = con.prepareStatement(input);
            instrucao.executeUpdate();
        } catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "ERRO SQL: " + e.getMessage());
        }
    }

    public void setFksBoxes(JComboBox box, String tablename)
    {
        String input;
        tablename = tablename.toUpperCase();
        if (tablename.compareTo("PESSOA") == 0)
        {
            input = "SELECT PROFISSAO FROM PROFISSAO";
        } else
        {
            input = "SELECT NOME FROM PESSOA";
        }

        Vector data = new Vector();

        try
        {

            PreparedStatement instrucao = con.prepareStatement(input);
            ResultSet result = instrucao.executeQuery(); //recebe os resultados da query
            ResultSetMetaData resultados = result.getMetaData(); //cria metadados dos resultados

            int colunas = resultados.getColumnCount(); //pega quantidade de colunas
            int count = 0;
            while (result.next())
            {
                count++;
            }
            if (count == 0)
            {
                jtAreaDeStatus.setText("Nenhum resultado encontrado");
                return;
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

        for (int i = 0; i < data.size(); i++)
        {
            box.addItem(data.elementAt(i).toString());
        }
    }

    public void deletar(String pk, Connection conexao, String tablename)
    {
        String input;
        if (tablename.toUpperCase().compareTo("PROFISSAO") == 0)
        {
            input = "DELETE FROM " + tablename.toUpperCase() + " WHERE PROFISSAO = '" + pk.toUpperCase() + "'";
        } else
        {
            input = "DELETE FROM " + tablename.toUpperCase() + " WHERE NOME = '" + pk.toUpperCase() + "'";
        }
        System.out.println(input);
        try
        {
            PreparedStatement instrucao = conexao.prepareStatement(input);
            instrucao.executeUpdate();
        } catch (SQLException e)
        {
            jtAreaDeStatus.setText("ERRO SQL: " + e.getMessage());
        }
    }
}
