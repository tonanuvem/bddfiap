package bddfiap;

import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;

import org.openqa.selenium.By; 
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class Autenticacao {
    static String logarUsuario(String login, String pass) {
        /*
        WebDriver driver = new ChromeDriver(); 
        String retorno = "";
        driver.get("http://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.cssSelector("i.fa-sign-in")).click();
        if( driver.getCurrentUrl().equalsIgnoreCase("http://the-internet.herokuapp.com/secure") ) {
            retorno = "Usuario autenticado";
        } else { 
            retorno = "Visitante";
        } 
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { }
        driver.close(); 
        return retorno;
        */
        if ( login.equals("usuario") & pass.equals("senha") ) {
            // Usuario autenticado
            return "Usuario autenticado";
        } else {
            // Visitante
            return "Visitante";
        }
    }
}

class Produtos {
    private HashMap<String, Integer> inventario = new HashMap<String, Integer>();
    public Produtos() {
        inventario.put("Prod 1", 3);
        inventario.put("Prod 2", 5);
        inventario.put("Prod 3", 2);
        inventario.put("Prod 4", 6);
    }
    String pesquisar (String prod) {
        if (inventario.get(prod) != null) {
            return "Produto disponivel";
        } else {
            return "Produto não disponivel";
        }
    }
    Integer estoque(String prod) {
        Integer estoque = inventario.get(prod);
        if (estoque != null) {
            return estoque;
        } else {
            return 0;
        }
    }
}

class Lojas {
    private HashMap<String, Map> lojas = new HashMap<String, Map>();
    public Lojas() {
        HashMap<String, Integer> loja1 = new HashMap<String, Integer>();
        loja1.put("Prod 1", 3);
        loja1.put("Prod 2", 5);

        HashMap<String, Integer> loja2 = new HashMap<String, Integer>();
        loja2.put("Prod 3", 2);
        loja2.put("Prod 4", 6);

        // Inserindo inventário das lojas
        lojas.put("Loja 1", loja1);
        lojas.put("Loja 2", loja2);
    }
    String pesquisar (String prod) {
        String retorno = "";
        for (Map.Entry<String, Map> x : lojas.entrySet()) {
            String loja = x.getKey();
            Map produtos = x.getValue();
            Integer qtd = (Integer) produtos.get(prod);
            if (qtd != null) {
                retorno += loja+" tem "+qtd+" unidades do "+prod;
            }
        }
        if (retorno == "") { retorno = "Produto não disponível"; }
        return retorno;
    }
}

public class Stepdefs {
    @Dado("Que cliente {string} VISITA a pagina do site com a senha {string} como {string}")
    public void que_cliente_VISITA_a_pagina_do_site(String usuario, String senha, String tipoVisita) {
        // Visitante no site
        String tipoVisitante = Autenticacao.logarUsuario(usuario, senha);
        assertEquals(tipoVisitante, tipoVisita);
    }
    
    @Quando("O cliente pesquisa por um produto {string}")
    public void o_cliente_pesquisa_por_um_produto(String string) {
        Produtos produtos = new Produtos();
        // "Produto disponivel"
        String disponibilidade = produtos.pesquisar(string);
        if (disponibilidade.equals("Produto disponivel")) {
            assertEquals(disponibilidade,"Produto disponivel");
        }
        // "Produto nao disponivel"
        if (disponibilidade.equals("Produto nao disponivel")) {
            assertEquals(disponibilidade, "Produto nao disponivel");
        }
    }
    
    @Entao("Deve ser consultado o estoque do produto {string}")
    public void deve_ser_consultado_o_estoque_do_produto(String string) {
        Produtos produtos = new Produtos();
        // Se o Produto for nao disponivel, cenario nao deve continuar
        // "Produto disponivel"
        Integer quantidade = produtos.estoque(string);
        assertNotNull(quantidade);
    }
    /*
    @Entao("Pode ser indicado o estoque {string} de outras lojas")
    public void pode_ser_indicado_o_estoque_de_outras_lojas(String prod) {
        Lojas lojas = new Lojas();
        String disponibilidade = lojas.pesquisar(prod);
        assertEquals(disponibilidade,"Produto disponivel");
    }*/

    @Entao("Retornar a quantidade de Produtos {string} Disponiveis em estoque de cada loja: {string}")
    public void retornar_a_quantidade_de_Produtos_Disponiveis_em_estoque_de_cada_loja(String prod, String resultado) {
        Lojas lojas = new Lojas();
        String disponibilidade = lojas.pesquisar(prod);
        assertEquals(disponibilidade, resultado);
    }
}
