package jp.ac.jec.cm0145.flashcardmaker;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CardSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_Name = "FLASHCARD_DB";
    public static final int version = 1;
    public static final String TABLE_NAME = "CARD";

    public CardSQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT, definition TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Card> getAllCard() {

        ArrayList<Card> ary = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if(db == null) {
            return null;
        }

        try {
            String[] column = new String[]{"definition", "word", "_id"};
            Cursor cur = db.query(TABLE_NAME, column, null, null, null, null, null);

            while (cur.moveToNext()){
                Card tmp = new Card(cur.getString(0), cur.getString(1), cur.getInt(2));
                ary.add(tmp);
            }
            cur.close();
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ary;

    }

    public boolean insertCard(Card newCard){
        long ret;
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("word", newCard.getword());
            values.put("definition", newCard.getdefinition());
            ret = db.insert(TABLE_NAME, null, values);
        }

        return ret != -1;
    }

    public void deleteAll(){
        try (SQLiteDatabase db = getWritableDatabase()) {
            db.delete(TABLE_NAME, null, null);
        }
    }

    public boolean checkIfEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        String count = "SELECT count(*) FROM CARD";
        Cursor cur = db.rawQuery(count, null);
        cur.moveToFirst();
        int icount = cur.getInt(0);
        if(icount>0) {
            return false;
        } else {
            return true;
        }
    }

    public void delete(String word) {

        try (SQLiteDatabase db = getWritableDatabase()) {
            db.delete(TABLE_NAME, "word=?", new String[]{word});
        }
    }

    public boolean updateCard(String definition, String word){
        long ret ;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("definition", definition);
        try {
            ret = db.update(TABLE_NAME, values,"word=?",new String[]{word}) ;
        }finally {
            db.close();
        }
        return ret !=-1;
    }



    public boolean isExistWord (String str){

        boolean ret;
        SQLiteDatabase db = getReadableDatabase();
        String [] selectionStr = new String[]{str};
        Cursor cur = db.query(TABLE_NAME, null, "word=?", selectionStr, null, null, null);

        if (cur.getCount() > 0){
            ret = true;
        } else {
            ret = false;
        }

        return ret;
    }
}
