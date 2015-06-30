/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Adams
 */
public final class telaInicial extends javax.swing.JFrame {

    /**
     * Creates new form telaInicial
     */
    String host, user, pass, conexao = null;
    Connection connection;
    
    public telaInicial(String host, String user, String pass, String conexao) {
        initComponents();
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.conexao = conexao;
        
        if(!conectar())
        {
            JOptionPane.showMessageDialog(null, "ERRO: verifique seu usuário e senha");
        }
    }
    
    public boolean conectar() {
        try {
            Class.forName(conexao);
            connection = DriverManager.getConnection(
                    host,
                    user,
                    pass);
            jtAreaDeStatus.setText("Conectado com sucesso!");
            jtAreaDeStatus2.setText("usuário: " + user);
        } catch (ClassNotFoundException ex) {
            jtAreaDeStatus.setText("Problema: verifique o driver do banco de dados");
        } catch (SQLException ex) {
            jtAreaDeStatus.setText("Problema: verifique seu usuário e senha");
        }
        return true;
    }

    private telaInicial() {}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botaoHistorico = new javax.swing.JButton();
        botaoAssinaturas = new javax.swing.JButton();
        botaoRelatorio = new javax.swing.JButton();
        jtAreaDeStatus = new javax.swing.JLabel();
        jtAreaDeStatus2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        botaoHistorico.setText("Editar Histórico");
        botaoHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoHistoricoActionPerformed(evt);
            }
        });

        botaoAssinaturas.setText("Editar Assinaturas");
        botaoAssinaturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAssinaturasActionPerformed(evt);
            }
        });

        botaoRelatorio.setText("Imprimir Relatório");
        botaoRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRelatorioActionPerformed(evt);
            }
        });

        jtAreaDeStatus.setText("jLabel1");

        jtAreaDeStatus2.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botaoRelatorio)
                    .addComponent(botaoAssinaturas)
                    .addComponent(botaoHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtAreaDeStatus)
                    .addComponent(jtAreaDeStatus2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {botaoAssinaturas, botaoHistorico, botaoRelatorio});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botaoHistorico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botaoAssinaturas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botaoRelatorio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtAreaDeStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtAreaDeStatus2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botaoAssinaturas, botaoHistorico, botaoRelatorio});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoHistoricoActionPerformed
        historico historico = new historico(host, user, pass,connection);
    }//GEN-LAST:event_botaoHistoricoActionPerformed

    private void botaoAssinaturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAssinaturasActionPerformed
        assinatura assin = new assinatura(host, user, pass,connection);
    }//GEN-LAST:event_botaoAssinaturasActionPerformed

    private void botaoRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRelatorioActionPerformed
        relatorio rel = new relatorio(host, user, pass,connection);
    }//GEN-LAST:event_botaoRelatorioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(telaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(telaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(telaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(telaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new telaInicial().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoAssinaturas;
    private javax.swing.JButton botaoHistorico;
    private javax.swing.JButton botaoRelatorio;
    private javax.swing.JLabel jtAreaDeStatus;
    private javax.swing.JLabel jtAreaDeStatus2;
    // End of variables declaration//GEN-END:variables
}
