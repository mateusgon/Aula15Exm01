package br.ufjf.dcc171;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aula15Exm01 {

    public static void main(String[] args) {
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String driverURL = "jdbc:derby://localhost:1527/2017-3-dcc171";
            Connection conexao = DriverManager.getConnection(driverURL, "USUARIO", "SENHA");
            Scanner entrada = new Scanner(System.in);
            Integer cmd = -1;
            do {
                System.out.println("Escolha uma opção (q p/ sair)");
                System.out.println("\t 1 - Insere registro aleatório");
                System.out.println("\t 2 - Diminui estoques");
                System.out.println("\t 3 - Limpa estoques");
                System.out.println("\t 4 - Lista estoques");
                cmd = entrada.nextInt();
                switch(cmd)
                {
                    case 1:
                        insereRegistroAleatorio(conexao);
                        break;
                    case 2:
                        diminuiEstoques(conexao);
                        break;
                    case 3:
                        limpaEstoque(conexao);
                        break;
                    default:
                        listaEstoque(conexao);
                }
            }
            while(cmd!=0);
        } catch (Exception ex) {
            Logger.getLogger(Aula15Exm01.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private static void insereRegistroAleatorio(Connection conexao) throws SQLException {
        Random rnd = new Random();
        String sql = String.format("insert into produto (nome, qtd, atualizado) values ('Produto %d', %d, current_timestamp)", rnd.nextInt(100), rnd.nextInt(10)+1);
        Statement operacao = conexao.createStatement();
        operacao.executeUpdate(sql);
    }

    private static void diminuiEstoques(Connection conexao) throws SQLException {
        Statement operacao = conexao.createStatement();
        int n = operacao.executeUpdate("update produto set qtd=qtd-1 where qtd>0");
        System.out.println(n+ " Registros atualizados!");
    }
    
    private static void limpaEstoque(Connection conexao) throws SQLException {
        Statement operacao = conexao.createStatement();
        int n = operacao.executeUpdate("delete from produto where qtd<=0");
        System.out.println(n+ " Registros atualizados!");
    }

    private static void listaEstoque(Connection conexao) throws SQLException {
        Statement operacao = conexao.createStatement();
        ResultSet resultado = operacao.executeQuery("select nome, qtd, atualizado from produto");
        System.out.println("+Produto\tQTD+");
        while (resultado.next())
        {
            String nome = resultado.getString("nome");
            Integer qtd = resultado.getInt("qtd");
            System.out.println(String.format(" %s\t%d", nome, qtd));
        }
        System.out.println("+\t  +");
    }
    
}
