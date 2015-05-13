package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import java.io.File;
import java.util.List;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class TextFile {
    private String name;
    private String conteudoFicheiro;

    public TextFile(String nome, String conteudo){
        this.name=nome;
        this.conteudoFicheiro= conteudo;
    }

    public TextFile(String nome){
        this.name=nome;
        this.conteudoFicheiro="";
    }

    public String getNameFile(){
        return this.name;
    }

    public String getConteudoFicheiro(){
        return this.conteudoFicheiro;
    }

    public void setNameFile(String conteudo){
        this.conteudoFicheiro = conteudo;
    }

    public void setConteudoFicheiro(String conteudo){
        this.conteudoFicheiro = conteudo;
    }

    public String toString(){
        return this.getNameFile();
    }
}
