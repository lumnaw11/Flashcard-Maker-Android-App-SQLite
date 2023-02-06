package jp.ac.jec.cm0145.flashcardmaker;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

public class CardActivity extends AppCompatActivity {

    private ArrayList<Card> ary = new ArrayList<>();
    private int pos;
    private boolean isShowing = false;
    int [] ranArray; //random array
    private boolean swap = false;


    TextView txtNumber;
    TextView txtWord;
    TextView txtDefinition;
    Button btnPrev;
    Button btnNext;
    Button btnStartCard;
    Button btnBack;
    Button btnShowAns;
    Switch btnRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(this);
        ary = helper.getAllCard();
        pos = 0;

        txtNumber = findViewById(R.id.txtNumber);
        txtNumber.setText("");
        txtWord = findViewById(R.id.txtWord);
        txtDefinition = findViewById(R.id.txtDefinition);
        txtWord.setVisibility(View.INVISIBLE);
        txtDefinition.setVisibility(View.INVISIBLE);
        btnRandom = findViewById(R.id.btnRandom);

        btnStartCard = findViewById(R.id.btnStartCard);
        btnStartCard.setOnClickListener(view -> {
            if(helper.checkIfEmpty()){
                Toast.makeText(CardActivity.this,"Cards are empty",Toast.LENGTH_LONG).show();
            } else {
                ranArray = getRandom();
                pos = 0;
                dispOneCard();
                checkPos();
                btnShowAns.setEnabled(true);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {
            pos++;
            dispOneCard();
            checkPos();
        });

        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(view -> {
            if (pos > 0 ){
                pos--;
                dispOneCard();
                checkPos();
            }

        });

        btnShowAns = findViewById(R.id.btnAnswer);
        btnShowAns.setOnClickListener(view -> {
            if (isShowing){
                txtWord.setVisibility(View.VISIBLE);
                txtDefinition.setVisibility(View.INVISIBLE);
                isShowing = false;
            } else {
                txtWord.setVisibility(View.INVISIBLE);
                txtDefinition.setVisibility(View.VISIBLE);
                isShowing = true;
            }

        });
        btnShowAns.setEnabled(false);
        btnNext.setEnabled(false);
        btnPrev.setEnabled(false);
    }

    private void checkPos() {
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        if (pos == 0){
            btnPrev.setEnabled(false);
        } else {
            btnPrev.setEnabled(true);
        }
        if (pos == ary.size()-1 || ary.size()==1) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }
    }
    private int[] getRandom(){
        int[] array = new int[ary.size()];

        for (int i = 0; i< array.length; i++){
            array[i] = i;
        }
        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
        return array;
    }

    private void dispOneCard() {
        Card temp;
        if (btnRandom.isChecked()){
            temp = ary.get(ranArray[pos]);
        } else {
            temp = ary.get(pos);
        }
        txtNumber.setText((pos+1) + "/" + ary.size() + "" );
        txtWord.setText(temp.getword());
        txtDefinition.setText(temp.getdefinition());
        txtWord.setVisibility(View.VISIBLE);
        isShowing = false;
    }
}