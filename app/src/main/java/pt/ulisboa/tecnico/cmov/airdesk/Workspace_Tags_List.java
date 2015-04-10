package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.List_Tags_Workspaces;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;

public class Workspace_Tags_List extends ActionBarActivity {
    TagAdapter adapter=null;
    List_Tags_Workspaces helper=null;
    Cursor dataset_cursor=null;
    EditText editTag=null;

    //this is how track which Tag we are working on
    String TagId=null;

    private pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.Workspace workspace;
    AirDesk globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_workspace_tags_list);

            globals = (AirDesk) getApplicationContext();
            //Workspace actualmente seleccionado
            workspace = globals.getActiveWorkspace();

            ListView list = (ListView) findViewById(R.id.listTags);
            list.setOnItemClickListener(onListClick);

            //Botão para adicionar uma nova tag ao workspace seleccionado
            Button btnSimples = (Button) findViewById(R.id.btnAddTagWorkspace);
            btnSimples.setOnClickListener(onSave);

            //Botão delete para eliminar uma determinada Tag pertencente ao workspace
            Button btnDelete = (Button) findViewById(R.id.btnDeleteTagWorkspace);
            btnDelete.setOnClickListener(onDelete);

            //EdiText com o valor da Tag (nome)
            editTag = (EditText) findViewById(R.id.editTextTagsWorkspace);

            //get our helper
            helper=new List_Tags_Workspaces(this);
            //manage the cursor, recebe todos os registos (tags do workspace) da base de dados, com o objectivo de popular a ListView
            dataset_cursor=helper.getAll(workspace.getName());
            //pass it to our adapter
            startManagingCursor(dataset_cursor);
            //set the adapter on our list
            adapter=new TagAdapter(dataset_cursor);
            list.setAdapter(adapter);
        }
        catch (Exception e){
            Log.e("ERROR", "ERROR IN CODE:" + e.toString());
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        helper.close();
    }

    //Função para adicionar uma nova Tag ou fazer update de um determinado valor (nome) da Tag
    private View.OnClickListener onSave=new View.OnClickListener(){
        public void onClick(View v){
            if(!editTag.getText().toString().equals("")) {
                if (TagId == null) {
                    workspace.addTag(editTag.getText().toString());
                    helper.insert_Workspace_Tag(editTag.getText().toString(), workspace.getName());
                } else {
                    helper.update_Workspace_Tag(TagId, editTag.getText().toString());
                    TagId = null;
                }
                dataset_cursor.requery();
                editTag.setText("");
            }
        }
    };

   //Função para eliminar a Tag seleccionada na ListView
    private View.OnClickListener onDelete=new View.OnClickListener(){
        public void onClick(View v){
            if (TagId==null){
                return;
            }
            else{
                workspace.removeTag(editTag.getText().toString());
                helper.delete_Workspace_Tag(TagId);
                TagId=null;
            }
            dataset_cursor.requery();
            editTag.setText("");
        }
    };

    //Quando uma determinada Tag é clicked vai ser preenchido o seu valor (nome) para o EditText
    // para elaborar operações de update ou delete da respectiva TAG
    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            TagId=String.valueOf(id);
            Cursor c=helper.getById(TagId);
            c.moveToFirst();
            editTag.setText(helper.getTag(c));
        }
    };

    class TagAdapter extends CursorAdapter{
        TagAdapter(Cursor c){
            super(Workspace_Tags_List.this, c);
        }

        public void  bindView (View row, Context ctxt, Cursor c){
            TagHolder holder=(TagHolder)row.getTag();
            holder.populateFrom(c, helper);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row_user_tag_list, parent, false);
            TagHolder holder= new TagHolder(row);
            row.setTag(holder);
            return(row);
        }
    }

    static class TagHolder{
        private TextView TagText=null;

        TagHolder(View row){
            TagText=(TextView)row.findViewById(R.id.note);
        }

        void populateFrom(Cursor c, List_Tags_Workspaces helper){
            TagText.setText(helper.getTag(c));
        }
    }

}