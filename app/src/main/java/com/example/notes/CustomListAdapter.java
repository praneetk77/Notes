package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Note> {

    private Context mContext;
    private int id;
    private List<Note> list;

    public CustomListAdapter(@NonNull Context context, int id, List<Note> list) {
        super(context,id,list);
        mContext = context;
        this.id = id;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View mView = convertView;
        if (mView==null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView)mView.findViewById(R.id.textView);

        if(list.get(position) != null )
        {
            text.setTextColor(Color.BLACK);
            text.setText(list.get(position).getContent());

        }

        return mView;
    }
}
