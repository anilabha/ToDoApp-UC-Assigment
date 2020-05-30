package com.anilabha.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
 * Created by Anilabha Baral. Uc Assigment 30.05.2020
 */
public class MainActivity extends AppCompatActivity {

    Dbhelper dbhelper;
    ArrayAdapter<String> mAdapter;
    ListView listView;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        dbhelper = new Dbhelper(this);

        loadTask();


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MainActivity.this.mAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }









    //Add icon to menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Load ALl tasks

    private void loadTask(){
        ArrayList<String> taskList = dbhelper.getTaskList();

        if (mAdapter == null){
            mAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.task_title, taskList);
            listView.setAdapter(mAdapter);
        } else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addTask:
                final EditText editText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("Whats your task ?")
                        .setView(editText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(editText.getText());
                                dbhelper.insertNewTask(task);

                                loadTask();
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .create();
                dialog.show();
                return true;


        }


        return super.onOptionsItemSelected(item);
    }


    public void deleteTask(View view){
        try{
            int index = listView.getPositionForView(view);
            String task = mAdapter.getItem(index++);
            dbhelper.deleteTask(task);
            loadTask();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}