package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bd_realm.R;
import com.example.bd_realm.adapters.BoardAdapter;
import com.example.bd_realm.models.Board;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener {

    private Realm realm;
    private FloatingActionButton fab;
    private View View;
    private ListView listView;
    private BoardAdapter adapter;
    private RealmResults<Board> boards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB REALM//
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        adapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);

        listView = (ListView) findViewById(R.id.listViewBoards);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertForCreatingBoards("Add new Board", "Type a name for new Board");
            }
        });

        registerForContextMenu(listView);
    }

    // CRUD action //
    private void createnameBoard(String boardName) {
        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        realm.commitTransaction();

    }

    private void deleteBoard(Board board){
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();

    }

    private void editingBoard(String newName, Board board){
        realm.beginTransaction();
        board.setTitle(newName);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();

    }

    private void deletealll( ){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

    }


    //Method ShowAlerta
    private void ShowAlertForCreatingBoards(String title, String message){

        //Create builder//
        AlertDialog.Builder builderr = new AlertDialog.Builder(this);

        if(title != null) builderr.setTitle(title);
        if(message != null) builderr.setMessage(message);

        //View Inflater//
        View viewInflater = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builderr.setView(viewInflater);

        //EditTextReference//
        final EditText input = (EditText)viewInflater.findViewById(R.id.editTexto);


        //Configurate option Button//
        builderr.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String boardName = input.getText().toString().trim();

                if(boardName.length() > 0){
                    createnameBoard(boardName);
                }

                else{
                    Toast.makeText(getApplicationContext(),"The name is required to create a new Board", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Create and show builder//
        AlertDialog dialog =  builderr.create();
        dialog.show();
    }

    //Method ShowAlerta
    private void ShowAlertForEditingBoards(String title, String message, final Board board){

        //Create builder//
        AlertDialog.Builder builderr = new AlertDialog.Builder(this);

        if(title != null) builderr.setTitle(title);
        if(message != null) builderr.setMessage(message);

        //View Inflater//
        View viewInflater = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builderr.setView(viewInflater);

        //EditTextReference//
        final EditText input = (EditText)viewInflater.findViewById(R.id.editTexto);
        input.setText(board.getTitle());

        //Configurate option Button//
        builderr.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String boardName = input.getText().toString().trim();
                if(boardName.length() == 0)
                    Toast.makeText(getApplicationContext(),"The name is required to edit the current Board", Toast.LENGTH_LONG).show();

                else if(boardName.equals(board.getTitle()))
                    Toast.makeText(getApplicationContext(),"The name is the same than it was before ", Toast.LENGTH_LONG).show();

                else
                       editingBoard(boardName, board);



            }
        });

        //Create and show builder//
        AlertDialog dialog =  builderr.create();
        dialog.show();
    }



    //Explicar mas adelante a los tigueres//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.deleteall:
                deletealll();
        return true;

            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, android.view.View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.delete_board:
               deleteBoard(boards.get(info.position));
                return true;

            case R.id.edit_board:
                ShowAlertForEditingBoards("Edit Board", "Change the name of the board", boards.get(info.position));
                return true;

            default: return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(RealmResults<Board> boards) {

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
        Intent intenet = new Intent(MainActivity.this, NotesActivity.class);
        intenet.putExtra("id", boards.get(position).getId());
        startActivity(intenet);
    }

}