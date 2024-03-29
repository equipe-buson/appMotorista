package com.buson.appmotorista;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    private FirebaseAuth mAuth;
    private InterstitialAd mInterstitialAd;
    Dados dados = new Dados();

    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pedindo a permissão de utilizar o GPS para o usuário
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){

            e.printStackTrace();

        }

        // Instanciando o Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // Pegando dados do button
        final Button login = (Button) findViewById(R.id.btn_login);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Ação de click do button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pegando dados dos campos E-mail e Senha
                final EditText et_email = (EditText) findViewById(R.id.main_email);
                final TextInputEditText et_senha = (TextInputEditText) findViewById(R.id.main_senha);
                final String email = et_email.getText().toString().trim();
                final String senha = et_senha.getText().toString().trim();

                // Verificando há possibilidade de erros
                // Se ambos os campos forem vazios
                if (et_email.getText().toString().length()<=0 && et_senha.getText().toString().length()<=0) {

                    et_email.setError("Campo nescessário");
                    et_senha.setError("Campo nescessário");

                // Se o campo "E-mail" for vazio
                }else  if (et_email.getText().toString().length()<=0){

                    et_email.setError("Campo nescessário");

                // Se o campo "Senha" for vazio
                }else if (et_senha.getText().toString().length()<=0){

                    et_senha.setError("Campo nescessário");

                // Se o campo "Senha" conter menos que 8 caracteres
                }else if (et_senha.getText().toString().length()<8){

                    et_senha.setError("Senha deve conter 8 ou mais caracteres");

                // Se o Campo "E-mail" e "Senha" atender todos os requesitos esperados
                }else if (et_email.getText().toString().length()>0 && et_senha.getText().toString().length()>8) {

                    login.setEnabled(true);

                    mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // Se task for Sucessful o usuário pode acessar a sua conta
                            if (task.isSuccessful()) {

                                Intent go_to_Dados = new Intent(MainActivity.this, Dados.class);
                                startActivity(go_to_Dados);

                                Toast.makeText(MainActivity.this, "Acessando conta", Toast.LENGTH_LONG).show();

                                
                                et_senha.setText("");

                                // Se task for diferente de Sucessful
                            }else if (!task.isSuccessful()){

                                Toast.makeText(MainActivity.this,"Email ou Senha incorretas", Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
