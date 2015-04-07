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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.Invite;
import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class Invitation extends ActionBarActivity {
    TagAdapter adapter=null;
    Invite helper=null;
    Cursor dataset_cursor=null;
    EditText editNameInvite=null;

    //this is how track which Tag we are working on
    String InviteId=null;
    User g = User.getInstance();
    ListView list;
    AirDesk globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_invitation);
            list=(ListView) findViewById(R.id.listUsersInvited);
            editNameInvite = (EditText) findViewById(R.id.editTextInvitation);

            // variaveis globais
            AirDesk globals = (AirDesk) getApplicationContext();
            g=globals.getLoggedUser();

            //get our helper
            helper=new Invite(this);
            //manage the cursor, recebe todos os registos da base de dados

            // variaveis globais
            globals.getActiveWorkspace();

      //      dataset_cursor=helper.getAll(, g.getUserName());

            //pass it to our adapter
            startManagingCursor(dataset_cursor);
            //set the adapter on our list
            adapter=new TagAdapter(dataset_cursor);
            list.setAdapter(adapter);

            Button btnSimples = (Button) findViewById(R.id.btnAddUserInvited);
            btnSimples.setOnClickListener(onSave);

            Button btnDelete = (Button) findViewById(R.id.btnDeleteUserInvited);
            btnDelete.setOnClickListener(onDelete);

            list.setOnItemClickListener(onListClick);
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

    private View.OnClickListener onSave=new View.OnClickListener(){
        public void onClick(View v){
            if (InviteId==null){
                //helper.insert_User_Tag(editTag.getText().toString(), g.getUserName());
                g.addTag(editNameInvite.getText().toString());
            }

            dataset_cursor.requery();
            editNameInvite.setText("");
        }
    };

    private View.OnClickListener onDelete=new View.OnClickListener(){
        public void onClick(View v){
            if (InviteId==null){
                return;
            }
            else{
                //helper.delete_User_Tag(TagId);
                g.removeTag(InviteId);
                InviteId=null;
            }
            dataset_cursor.requery();
            editNameInvite.setText("");
        }
    };

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            InviteId=String.valueOf(id);
            Cursor c=helper.getByNameUser(InviteId);
            c.moveToFirst();
            editNameInvite.setText(helper.getNameUser(c));
        }
    };

    class TagAdapter extends CursorAdapter {
        TagAdapter(Cursor c){
            super(Invitation.this, c);
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

        void populateFrom(Cursor c, Invite helper){
            TagText.setText(helper.getNameUser(c));
        }
    }

}
