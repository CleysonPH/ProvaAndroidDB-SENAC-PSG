package com.cleysonph.calculadoraimc20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cleysonph.calculadoraimc20.domain.IMC;
import com.cleysonph.calculadoraimc20.domain.SQLiteHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CalculoImcActivity extends AppCompatActivity {

    private TextView txtResultado;
    private TextView txtSituacao;
    private EditText edtPeso;
    private EditText edtAltura;

    private double peso, altura, resultadoIMC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_imc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtResultado = (TextView) findViewById(R.id.txtResultado);
        txtSituacao = (TextView) findViewById(R.id.txtSituacao);
        edtPeso = (EditText) findViewById(R.id.edtPeso);
        edtAltura = (EditText) findViewById(R.id.edtAltura);

        findViewById(R.id.btnCalcular).setOnClickListener(calcularIMC());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculo_imc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.list_settings) {
            Intent intent = new Intent(getApplicationContext(), HistoricoActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener calcularIMC(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NumberFormat decimal = new DecimalFormat(".#");

                peso = Double.parseDouble(edtPeso.getText().toString());
                altura = Double.parseDouble(edtAltura.getText().toString());

                resultadoIMC = peso / (altura * altura);

                txtResultado.setText(getString(R.string.txt_resultado) + " " + decimal.format(resultadoIMC));

                String situacao = "";
                String toast = "";

                if(resultadoIMC <= 18.5){
                    situacao = getString(R.string.abaixo_peso);
                    toast = getString(R.string.toast_anormal);
                } else if(resultadoIMC <= 24.9){
                    situacao = getString(R.string.peso_ideal);
                    toast = getString(R.string.toast_ideal);
                } else if(resultadoIMC <= 29.9){
                    situacao = getString(R.string.acima_peso);
                    toast = getString(R.string.toast_anormal);
                } else if(resultadoIMC <= 34.9){
                    situacao = getString(R.string.obesidade_I);
                    toast = getString(R.string.toast_anormal);
                } else if(resultadoIMC <= 39.9){
                    situacao = getString(R.string.obesidade_II);
                    toast = getString(R.string.toast_anormal);
                } else {
                    situacao = getString(R.string.obesidade_III);
                    toast = getString(R.string.toast_anormal);
                }

                txtSituacao.setText(situacao);
                Toast.makeText(CalculoImcActivity.this, toast, Toast.LENGTH_LONG).show();

                inserirDados();

                edtPeso.setText("");
                edtAltura.setText("");

                edtPeso.requestFocus();
            }
        };
    }

    private void inserirDados(){
        IMC imc = new IMC();
        imc.setPeso(peso);
        imc.setAltura(altura);
        imc.setResultado(resultadoIMC);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(CalculoImcActivity.this);
        sqLiteHelper.inserir(imc);
    }
}
