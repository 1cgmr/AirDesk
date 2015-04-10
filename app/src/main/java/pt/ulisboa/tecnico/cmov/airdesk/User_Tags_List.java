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

import pt.ulisboa.tecnico.cmov.airdesk.DataBase.User_Tag;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.AirDesk;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;

public class User_Tags_List extends ActionBarActivity {
    TagAdapter adapter=null;
    User_Tag helper=null;
    Cursor dataset_cursor=null;
    EditText editTag=null;

    //this is how track which Tag we are working on
    String TagId=null;
    User g = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_user__tags__list);
            ListView list=(ListView) findViewById(R.id.listTags);
            editTag = (EditText) findViewById(R.id.editTextTags);

            // variaveis globais
            AirDesk globals = (AirDesk) getApplicationContext();
            g=globals.getLoggedUser();

            //get our helper
            helper=new User_Tag(this);
            //manage the cursor, recebe todos os registos da base de dados
            dataset_cursor=helper.getAll(g.getUserName());
            //pass it to our adapter
            startManagingCursor(dataset_cursor);
            //set the adapter on our list
            adapter=new TagAdapter(dataset_cursor);
            list.setAdapter(adapter);

            Button btnSimples = (Button) findViewById(R.id.btnAddTag);
            btnSimples.setOnClickListener(onSave);

            Button btnDelete = (Button) findViewById(R.id.btnDeleteTag);
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
            if(!editTag.getText().toString().equals("")){
                if (TagId == null) {
                    //helper.insert_User_Tag(editTag.getText().toString(), g.getUserName());
                    g.addTag(editTag.getText().toString());
                } else {
                    helper.update_User_Tag(TagId, editTag.getText().toString());
                    TagId = null;
                }
                dataset_cursor.requery();
                editTag.setText("");
            }
        }
    };

    private View.OnClickListener onDelete=new View.OnClickListener(){
        public void onClick(View v){
            if (TagId==null){
                return;
            }
            else{
                //helper.delete_User_Tag(TagId);
                g.removeTag(TagId);
                TagId=null;
            }
            dataset_cursor.requery();
            editTag.setText("");
        }
    };

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
            super(User_Tags_List.this, c);
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

        void populateFrom(Cursor c, User_Tag helper){
            TagText.setText(helper.getTag(c));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}