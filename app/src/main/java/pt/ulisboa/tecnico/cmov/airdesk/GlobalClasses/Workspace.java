package pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist168635 on 25-03-2015.
 */
public class Workspace {

    private String name;
    private List<TextFile> Files = new ArrayList<TextFile>();
    private List<String> Tags = new ArrayList<String>();
    private boolean publico = false;
    private int max_quota = 0;

    public Workspace(Boolean publico, String workspaceName, List<String> Tags, int max_quota){
        this.name=workspaceName;
        this.publico=publico;
        this.Tags.addAll(Tags);
        this.max_quota=max_quota;
    }

    public String getName(){
        return name;
    }

    public void addTag(String Tag){
        Tags.add(Tag);
    }

    public void removeTag(String Tag){
        Tags.remove(Tag);
    }

    public String toString(){
        return name;
    }
}
