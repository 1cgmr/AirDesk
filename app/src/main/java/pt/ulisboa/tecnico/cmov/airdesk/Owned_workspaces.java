package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ItemBean;
import pt.ulisboa.tecnico.cmov.airdesk.Adapters.ListViewCustomAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.User;


public class Owned_workspaces extends Activity implements OnItemClickListener {

    ListView lview3;
    ListViewCustomAdapter adapter;

    private ArrayList<Object> itemList;
    private ItemBean bean;
    User user;
    private File userDir;
    //shdbdgfdfg
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workspace_list);
        user = User.getInstance();
        Context context = this.getApplicationContext();
        userDir = context.getDir(user.getUserName(), Context.MODE_PRIVATE);
        Log.d("ABC", "teste");
        prepareArrayLists();
        lview3 = (ListView) findViewById(R.id.listView1);
        adapter = new ListViewCustomAdapter(this, itemList);
        lview3.setAdapter(adapter);
        Log.d("ABC", "teste");

        lview3.setOnItemClickListener(this);
        Log.d("ABC", "teste");

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        // TODO Auto-generated method stub
        ItemBean bean = (ItemBean) adapter.getItem(position);
        Log.d("ABC", "onItemClick");
        Toast.makeText(this, "Title => " + bean.getTitle() + " \n Description => " + bean.getDescription(), Toast.LENGTH_SHORT).show();
    }

    /* Method used to prepare the ArrayList,
     * Same way, you can also do looping and adding object into the ArrayList.
     */
    public void prepareArrayLists()
    {
        itemList = new ArrayList<>();

        for(final File fileEntry : userDir.listFiles()){
            Log.d("ABC", ""+ fileEntry.getName()+ ", length(): " + fileEntry.length() );
            AddObjectToList(fileEntry.getName(), ""+fileEntry.getTotalSpace());
        }
    }

    // Add one item into the Array List
    public void AddObjectToList(String title, String desc)
    {
        bean = new ItemBean();
        bean.setDescription(desc);
        bean.setTitle(title);
        itemList.add(bean);
    }
}
