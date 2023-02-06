package jp.ac.jec.cm0145.flashcardmaker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {


    TextView edtWord;
    TextView edtDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button btnAdd = findViewById(R.id.btnAdd);
        edtWord = findViewById(R.id.edtWord);
        edtDefinition = findViewById(R.id.edtDefinition);

        CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(this);
        btnAdd.setOnClickListener(view -> {
            if(edtWord.getText().toString().equals("") || edtDefinition.getText().toString().equals("")){
                errorAlertDialog("Please enter words and definitions");
            } else {
                if (helper.isExistWord(edtWord.getText().toString())){
                    errorAlertDialog("Word already exists");
                } else {
                    Card card = new Card(edtDefinition.getText().toString(), edtWord.getText().toString());
                    helper.insertCard(card);
                    toast("New card added");
                    edtWord.setText("");
                    edtDefinition.setText("");
                }
            }

        });

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> finish());

        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        btnDeleteAll.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete all cards?");
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                helper.deleteAll();
                dialogInterface.dismiss();
                toast("All cards has been deleted");
            });
            builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        });

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(view -> {
            if(helper.isExistWord(edtWord.getText().toString())){
                helper.updateCard(edtDefinition.getText().toString(),edtWord.getText().toString());
                toast("Definition is updated");
                edtWord.setText("");
                edtDefinition.setText("");
            } else if(edtWord.getText().toString().isEmpty()) {
                errorAlertDialog("Please enter a word");
            } else {
                errorAlertDialog("Word does not exist");
            }
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(view -> {
            EditText edtDelete = findViewById(R.id.edtDeleteText);
            if(helper.isExistWord(edtDelete.getText().toString())){
                helper.delete(edtDelete.getText().toString());
                toast("Word is deleted");
                edtDelete.setText("");

            } else if(edtDelete.getText().toString().isEmpty()) {
                errorAlertDialog("Please enter a word");
            } else {
                errorAlertDialog("Word does not exist");
            }
        });
    }

    private void toast(String str){
        Toast.makeText(EditActivity.this,str, Toast.LENGTH_LONG).show();
    }

    private void errorAlertDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Error");
        builder.setMessage(str);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }
}