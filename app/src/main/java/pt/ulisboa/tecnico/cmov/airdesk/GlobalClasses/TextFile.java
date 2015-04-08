package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import java.io.File;
import java.util.List;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class TextFile {
    private String name;
    private StringBuilder conteudoFicheiro;

    public TextFile(String nome, StringBuilder conteudo){
        this.name=nome;
        this.conteudoFicheiro= conteudo;
    }
    public TextFile(String nome){
        this.name=nome;
        this.conteudoFicheiro = new StringBuilder("");
    }

    public String getNameFile(){
        return this.name;
    }

    public StringBuilder getConteudoFicheiro(){
        return this.conteudoFicheiro;
    }

    public void setNameFile(StringBuilder conteudo){
        this.conteudoFicheiro = conteudo;
    }

    public void setConteudoFicheiro(StringBuilder conteudo){
        this.conteudoFicheiro = conteudo;
    }

}
