package com.god.is.spark.xogame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    // game field size 3 = 3x3, 4 = 4x4, etc.
    private int fieldSize = 4;

    // - represents game field state like X,O,X,O,O...
    // - length equal squared *fieldSize*
    // - counting starts from left top tile and
    //   continuous right by horizontal and down by vertical
    private String[] gameState;

    // player turn (X or O)
    private String currentTurn = "X";

    private boolean endOfGame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        fieldSize = Character.getNumericValue(item.getTitle().charAt(0));

        return super.onOptionsItemSelected(item);
    }

    public void startRound(View view){

        initGameField();

        endOfGame = false;

        gameState = new String[fieldSize * fieldSize];

        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = "";
        }

        currentTurn = "X";

        TextView status = (TextView)findViewById(R.id.status);

        status.setText(currentTurn);
    }

    // init standard 3x3 field
    private void initGameField() {

        GridLayout gameField = (GridLayout)findViewById(R.id.gameField);

        gameField.removeAllViews();
        //gameField.setRowCount(fieldSize);
        gameField.setColumnCount(fieldSize);

        for (int i = 0; i < fieldSize * fieldSize; i++) {
            createGameTile(gameField, i);
        }
    }

    // create one field tile
    private void createGameTile(GridLayout gameField, int pos) {

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();

        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

        Button button = new Button(this);

        button.setTag(pos);
        button.setLayoutParams(layoutParams);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setMinimumHeight(0);
        button.setMinHeight(0);
        handleTileClick(button);

        gameField.addView(button);
    }

    private void handleTileClick(Button tile){

        tile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                turn((Button)v);
            }
        });
    }

    private void turn(Button tile) {
        if (endOfGame)
            return;

        int pos = (int)tile.getTag();

        if (gameState[pos] != "")
            return;

        gameState[pos] = currentTurn;

        tile.setText(currentTurn);

        nextTurn(pos);
    }

    private void nextTurn(int pos) {
        TextView status = (TextView)findViewById(R.id.status);

        boolean win = checkPlayerWin(pos);

        if (win)
        {
            status.setText(currentTurn + " IS WIN!!!!");

            endOfGame = true;
        }
        else
        {
            if (currentTurn == "X")
                currentTurn = "O";
            else
                currentTurn = "X";

            status.setText(currentTurn);
        }
    }

    private boolean checkPlayerWin(int pos) {
        return
            checkHorizontalCombination(pos) ||
            checkVerticalCombination(pos) ||
            checkFirstDiagonalCombination(pos) ||
            checkSecondDiagonalCombination(pos);
    }

    private boolean checkHorizontalCombination(int pos) {
        // player turn
        String sign = gameState[pos];

        // index of row
        int row = pos / fieldSize;

        // start pos in array
        int startPos = fieldSize * row;

        // end pos in array
        int endPos = startPos + fieldSize - 1;

        // enumerate row elements and check for equality of signs
        for (int i = startPos; i < endPos + 1; i++) {
            if (gameState[i] != sign)
                return false;
        }

        return true;
    }

    private boolean checkVerticalCombination(int pos) {
        // player turn
        String sign = gameState[pos];

        // index of column
        int column = pos % fieldSize;

        int startPos = column;

        int endPos = fieldSize * (fieldSize - 1) + column;

        for (int i = startPos; i < endPos + 1; i+= fieldSize) {
            if (gameState[i] != sign)
                return false;
        }

        return true;
    }

    private boolean checkFirstDiagonalCombination(int pos) {
        // player turn
        String sign = gameState[pos];

        // start pos in array
        int startPos = 0;

        // end pos in array
        int endPos = fieldSize * fieldSize - 1;

        for (int i = startPos; i < endPos + 1; i+= fieldSize + 1) {
            if (gameState[i] != sign)
                return false;
        }

        return true;
    }

    private boolean checkSecondDiagonalCombination(int pos) {
        // player turn
        String sign = gameState[pos];

        // start pos in array
        int startPos = fieldSize - 1;

        // end pos in array
        int endPos = fieldSize * (fieldSize - 1);

        for (int i = startPos; i < endPos + 1; i+= fieldSize - 1) {
            if (gameState[i] != sign)
                return false;
        }

        return true;
    }
}
