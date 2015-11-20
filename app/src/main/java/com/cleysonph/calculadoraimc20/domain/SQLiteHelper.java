package com.cleysonph.calculadoraimc20.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aluno on 19/11/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "calculadora_imc.sqlite";
    private static final int VERSAO_BANCO = 1;

    public SQLiteHelper(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS imc (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "peso REAL," +
                "altura REAL," +
                "resultado REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public long inserir(IMC imc){
        long id = imc.getId();
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("peso", imc.getPeso());
            values.put("altura", imc.getAltura());
            values.put("resultado", imc.getResultado());

            if(id != 0){
                String _id = String.valueOf(imc.getId());
                String[] whereArgs = new String[] {_id};
                int count = db.update("imc", values, "_id=?", whereArgs);
                return count;
            } else {
                id = db.insert("imc", "", values);
                return id;
            }
        } finally {
            db.close();
        }
    }

    public int deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("imc", null, null);
            return count;
        } finally {
            db.close();
        }
    }

    public int deleteOne(IMC imc){
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("imc", "_id=?", new String[]{String.valueOf(imc.getId())});
            return count;
        } finally {
            db.close();
        }
    }
}
