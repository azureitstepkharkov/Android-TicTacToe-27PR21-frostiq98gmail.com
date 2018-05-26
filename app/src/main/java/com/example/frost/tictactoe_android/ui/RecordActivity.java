package com.example.frost.tictactoe_android.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.frost.tictactoe_android.R;
import com.example.frost.tictactoe_android.db.DBHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecordActivity extends AppCompatActivity {
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        final TextView txCrossesAll = (TextView) findViewById(R.id.txCrossesAll);
        final TextView txZeroAll = (TextView) findViewById(R.id.txZeroAll);
        final TextView txCrosses = (TextView) findViewById(R.id.txCrosses);
        final TextView txZero = (TextView) findViewById(R.id.txZero);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ShowData("X",txCrosses,db);
        ShowData("O",txZero,db);
        dbHelper.close();



        FirebaseDatabase database =FirebaseDatabase.getInstance();
        ShowDataFierBase(database,txCrossesAll,"X");
        ShowDataFierBase(database,txZeroAll,"O");


    }
    public void ShowDataFierBase(FirebaseDatabase database, final TextView txView,String ref){
        DatabaseReference Ref = database.getReference(ref);
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer winner = dataSnapshot.getValue(Integer.class);
                txView.setText(winner.toString());
                Log.d( "winner :" , winner.toString());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }


    public void ShowData(String str,TextView txView,SQLiteDatabase db){
        Cursor o =db.rawQuery("SELECT winner FROM tictactoe  WHERE name= ?", new String[] { str });
        o.moveToNext();
        String winner = o.getString( o.getColumnIndex("winner") );
        txView.setText(winner);

    }
}
