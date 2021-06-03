package activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bd_realm.R;
import com.example.bd_realm.adapters.NoteAdapter;
import com.example.bd_realm.models.Board;
import com.example.bd_realm.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NotesActivity extends AppCompatActivity implements RealmChangeListener<Board> {

    private ListView listView;
    private FloatingActionButton fab;
    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;
    private int boardID;
    private Board board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        realm = Realm.getDefaultInstance();
        if(getIntent().getExtras() != null)
           boardID = getIntent().getExtras().getInt("id");

        board = realm.where(Board.class).equalTo("id", boardID).findFirst();
        board.addChangeListener(this);
        notes = board.getNotes();

        this.setTitle(board.getTitle());

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtoNotes);
        listView = (ListView) findViewById(R.id.listViewNotes);
        adapter = new NoteAdapter(this, notes, R.layout.list_view_notes_items);

        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowAlertForCreatingNotes("Add new Note", "Type a Note for " + board.getTitle()+ ".");
            }
        });

    }

    //Method ShowAlerta
    private void ShowAlertForCreatingNotes(String title, String message){

        //Create builder//
        AlertDialog.Builder builderr = new AlertDialog.Builder(this);

        if(title != null) builderr.setTitle(title);
        if(message != null) builderr.setMessage(message);

        //View Inflater//
        View viewInflater = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builderr.setView(viewInflater);

        //EditTextReference//
        final EditText input = (EditText)viewInflater.findViewById(R.id.editTextnewNote);

        //Configurate option Button//
        builderr.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String note = input.getText().toString().trim();

                if(note.length() > 0){
                    createnewNote(note);
                }

                else{
                    Toast.makeText(getApplicationContext(),"The Note can't be empty", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Create and show builder//
        AlertDialog dialog =  builderr.create();
        dialog.show();
    }

    private void createnewNote(String note){
        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);
        board.getNotes().add(_note); //Relation
        realm.commitTransaction();

    }


    @Override
    public void onChange(Board board) {
    adapter.notifyDataSetChanged();
    }

}