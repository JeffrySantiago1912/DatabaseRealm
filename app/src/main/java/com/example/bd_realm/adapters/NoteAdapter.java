package com.example.bd_realm.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bd_realm.R;
import com.example.bd_realm.models.Board;
import com.example.bd_realm.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> list;
    private int layout;

    public NoteAdapter(Context context, List<Note> notes, int layout){
        this.context = context;
        this.list = notes;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.description = (TextView) convertView.findViewById(R.id.textViewNoteDiscription);
            vh.description = (TextView) convertView.findViewById(R.id.textViewNoteCreatedAt);
            convertView.setTag(vh);

        }

        else{
            vh = (ViewHolder) convertView.getTag();
        }

        Note note = list.get(position);
        vh.description.setText(note.getDescription());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(note.getCreatedAt());
        vh.createdAt.setText(date);


        return convertView;
    }

    //Create Adapter// //Create a class ViewHolderNote//
    public class ViewHolder{
        TextView description;
        TextView createdAt;
    }

}
