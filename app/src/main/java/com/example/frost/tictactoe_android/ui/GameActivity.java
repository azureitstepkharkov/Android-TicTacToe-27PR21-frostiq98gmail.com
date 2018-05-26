package com.example.frost.tictactoe_android.ui;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.frost.tictactoe_android.R;
import com.example.frost.tictactoe_android.db.DBHelper;
import com.example.frost.tictactoe_android.game.Game;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private List<Button> buttons;

    protected DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initGameBoard();
        game = new Game(3);
        dbHelper = new DBHelper(this);
    }

    private void initGameBoard() {
        LinearLayout gameBoard = (LinearLayout) findViewById(R.id.gameBoard);
        buttons = new ArrayList<>();
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            if (gameBoard.getChildAt(i) instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) gameBoard.getChildAt(i);
                for (int j = 0; j < layout.getChildCount(); j++) {
                    Button button = (Button) layout.getChildAt(j);
                    buttons.add(button);
                    button.setOnClickListener(new GameBoardButtonOnClickListener(j, i));
                }
            }
        }
    }

    private void cleanUpBoard() {
        for (Button button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
    }

    private class GameBoardButtonOnClickListener implements View.OnClickListener {
        private int x = 0;
        private int y = 0;

        public GameBoardButtonOnClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }


        public void incrementCounter(String winnerName,FirebaseDatabase database) {
            DatabaseReference ref = database.getReference(winnerName);
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(final MutableData currentData) {
                    if (currentData.getValue() == null) {
                        currentData.setValue(1);
                    } else {
                        currentData.setValue((Long) currentData.getValue() + 1);
                    }
                    return Transaction.success(currentData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                }
            });
        }

        protected void saveWinner(String winnerName,SQLiteDatabase db){
            db.execSQL("UPDATE tictactoe SET winner = winner + 1 WHERE name=?", new String[] { winnerName });
            dbHelper.close();
        }

        @Override
        public void onClick(View view) {
            Game.Player player = game.getCurrentPlayer();
            game.makeTurn(x, y);

            Button button = (Button) view;
            button.setText(player.toString());
            if (player.equals(Game.Player.PLAYER_1)) {
                button.setTextColor(Color.BLACK);
            } else {
                button.setTextColor(Color.BLUE);
            }
            button.setEnabled(false);

            if (game.isFinished()) {
                Game.Player winner = game.getWinner();
                Toast toast;

                FirebaseDatabase database =FirebaseDatabase.getInstance();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if (winner == null) {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.no_one_won), Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.player_won, winner.toString()), Toast.LENGTH_SHORT);

                    if(winner.toString()=="X"){
                        saveWinner("X",db);
                        incrementCounter("X",database);
                    }else{
                        saveWinner("O",db);
                        incrementCounter("O",database);
                    }
                }
                toast.show();
                game.reset();
                cleanUpBoard();
            }
        }
    }
}