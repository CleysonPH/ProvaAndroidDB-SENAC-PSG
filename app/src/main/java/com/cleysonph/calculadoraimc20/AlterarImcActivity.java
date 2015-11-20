package com.cleysonph.calculadoraimc20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cleysonph.calculadoraimc20.domain.SQLiteHelper;

public class AlterarImcActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    private String id;
    private Cursor cursor;

    private TextView txtIdImc, txtNovoIMC;
    private EditText edtAlterarPeso, edtAlterarAltura;

    private double resultadoIMC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_imc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra("id");
        sqLiteHelper = new SQLiteHelper(this);

        txtIdImc = (TextView) findViewById(R.id.txtIdImc);
        txtNovoIMC = (TextView) findViewById(R.id.txtNovoIMC);
        edtAlterarAltura = (EditText) findViewById(R.id.edtAlterarAltura);
        edtAlterarPeso = (EditText) findViewById(R.id.edtAlterarPeso);

        cursor = carregaImc(Integer.parseInt(id));

        txtIdImc.setText(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
        edtAlterarPeso.setText(cursor.getString(cursor.getColumnIndexOrThrow("peso")));
        edtAlterarAltura.setText(cursor.getString(cursor.getColumnIndexOrThrow("altura")));
        txtNovoIMC.setText(getString(R.string.txt_resultado) + " " + cursor.getString(cursor.getColumnIndexOrThrow("resultado")));

        findViewById(R.id.btnRecalcular).setOnClickListener(recalcularIMC());
        findViewById(R.id.btnSalvar).setOnClickListener(alterarImc());
    }

    private View.OnClickListener alterarImc() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultadoIMC != 0){
                    db = sqLiteHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    String whereArgs = id;
                    values.put("peso", Double.parseDouble(edtAlterarPeso.getText().toString()));
                    values.put("altura", Double.parseDouble(edtAlterarAltura.getText().toString()));
                    values.put("resultado", resultadoIMC);
                    db.update("imc", values, "_id = " + whereArgs, null);
                    db.close();

                    Toast.makeText(AlterarImcActivity.this, getString(R.string.alterar_sucesso), Toast.LENGTH_LONG).show();

                    finish();
                    startActivity(new Intent(getApplicationContext(), HistoricoActivity.class));
                } else {
                    Toast.makeText(AlterarImcActivity.this, getString(R.string.nao_clicou_em_recalcular), Toast.LENGTH_LONG).show();
                }


            }
        };
    }

    private View.OnClickListener recalcularIMC() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                double peso = Double.parseDouble(edtAlterarPeso.getText().toString());
                double altura = Double.parseDouble(edtAlterarAltura.getText().toString());

                resultadoIMC = peso / (altura * altura);

                txtNovoIMC.setText(getString(R.string.txt_resultado) + " " + resultadoIMC);
            }
        };
    }

    private Cursor carregaImc(int id){
        db = sqLiteHelper.getWritableDatabase();
        String[] campos = {"peso", "altura", "resultado", "_id"};
        String whereArgs = String.valueOf(id);
        cursor = db.query("imc", campos, whereArgs, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

}
