package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    
    private EditText txtNote;
    private Button btnAdd;
    private ListView listView;

    private int p;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
        showNotesOnListView(dataBaseHelper);

        //Adds note to list view
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toCreate = txtNote.getText().toString();
                if(toCreate.equals("")){
                    Toast.makeText(MainActivity.this, "Please enter some text first.", Toast.LENGTH_SHORT).show();
                }else{
                    dataBaseHelper = new DataBaseHelper(MainActivity.this);
                    dataBaseHelper.addOne(toCreate);

                    showNotesOnListView(dataBaseHelper);

                }
                txtNote.setText("");
            }
        });

        //Deletes single note
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(note);
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                showNotesOnListView(dataBaseHelper);
                return false;
            }
        });

        //Edits note
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                p = position;
                openDialog();
            }
        });
    }


    private void initialise() {
        txtNote = findViewById(R.id.txtNote);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        dataBaseHelper = new DataBaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Button on menu to delete all items in list
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.btnDeleteAll){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dataBaseHelper.deleteAll();
                    showNotesOnListView(dataBaseHelper);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setMessage("Are you sure you want to delete all the notes?");
            builder.create().show();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Updates list view
    private void showNotesOnListView(DataBaseHelper dataBaseHelper) {
        List<Note> list = new ArrayList<>();
        for(int i=dataBaseHelper.getAll().size()-1 ; i>=0 ; i--){
            list.add(dataBaseHelper.getAll().get(i));
        }

        CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, R.layout.list_item, list);
        listView.setAdapter(adapter);
    }

    private void openDialog(){
        CustomDialog dialog = new CustomDialog();
        dialog.show(getSupportFragmentManager(), "edit");
    }

    @Override
    public void applyText(String newNote) {
        dataBaseHelper.update((Note) listView.getItemAtPosition(p), newNote);
        showNotesOnListView(dataBaseHelper);
    }
}