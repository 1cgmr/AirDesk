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

    public Workspace(Boolean publico, String workspaceName, List<String> Tags){
        this.name=workspaceName;
        this.publico=publico;
        this.Tags.addAll(Tags);

    }

    public void addTag(String Tag){
        Tags.add(Tag);
    }

    public void removeTag(String Tag){
        Tags.remove(Tag);
    }

    public List<TextFile> getFiles() {
        return Files;
    }

}
