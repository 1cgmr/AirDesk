package pt.ulisboa.tecnico.cmov.airdesk.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.GlobalClasses.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.R;

public class GridviewAdapter extends BaseAdapter
{
    private ArrayList<TextFile> listFiles;
    private Activity activity;

    public GridviewAdapter(Activity activity, List<TextFile> listFiles) {
        super();
        this.listFiles = (ArrayList<TextFile>)listFiles;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listFiles.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return listFiles.get(position).getNameFile();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {
        public ImageView imgViewFlag;
        public TextView txtViewTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);

            view.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.imageView1);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtViewTitle.setText(listFiles.get(position).getNameFile());
        view.imgViewFlag.setImageResource(R.drawable.icon);

        return convertView;
    }

}
