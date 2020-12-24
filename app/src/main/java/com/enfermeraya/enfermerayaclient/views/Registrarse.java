package com.enfermeraya.enfermerayaclient.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.Splash;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Usuario;
import com.enfermeraya.enfermerayaclient.comandos.ComandoValidarCorreoFirebase;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class Registrarse extends Activity implements ComandoValidarCorreoFirebase.OnValidarCorreoFirebaseChangeListener {

    Usuario usuario;

    EditText txt_nombre, txt_apellido, txt_celular, txt_correo, txt_passwor,txt_passwor1;
    Modelo modelo = Modelo.getInstance();
    String token = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth.AuthStateListener mAuthListener;
    ComandoValidarCorreoFirebase comandoValidarCorreoFirebase;

    //foto
    String userChoosenTask="";
    boolean img_cam1 = false;
    private int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    ImageView camara1;
    TextView txt_camara1;
    private static final String TAG ="AndroidBash";
    String foto ="";


    private final int MY_PERMISSIONS = 123;
    private final int PHOTO_CODE = 3;
    private final int SELECT_PICTURE = 300;
    private String mPath = "";
    private RelativeLayout mRlView;
    Uri photoURI = null;
    String ruta1 = null;
    static final int REQUEST_TAKE_PHOTO = 3;

    Utility utility;
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registrarse);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        utility = new Utility();
        usuario =  null;
        boolean result = Utility.checkPermission(Registrarse.this);

        txt_nombre = (EditText)findViewById(R.id.txt_nombre);
        txt_apellido = (EditText)findViewById(R.id.txt_apellido);
        txt_celular = (EditText)findViewById(R.id.txt_celular);
        txt_correo = (EditText)findViewById(R.id.txt_correo);
        txt_passwor = (EditText)findViewById(R.id.txt_passwor);
        txt_passwor1 = (EditText)findViewById(R.id.txt_passwor1);

        camara1 = (ImageView)findViewById(R.id.camara1);
        txt_camara1 = (TextView) findViewById(R.id.txt_camara1);
        mRlView = (RelativeLayout) findViewById(R.id.mRlView);


        token = FirebaseInstanceId.getInstance().getToken();
        comandoValidarCorreoFirebase = new ComandoValidarCorreoFirebase(this);


    }


    public void validar(View v){

        if(txt_nombre.getText().toString().length() < 3){
            txt_nombre.setError("Información muy corta");
        }
        else if(txt_apellido.getText().toString().length() < 3){
            txt_apellido.setError("Información muy corta");
        }

        else if(txt_celular.getText().toString().length() < 10){
            txt_celular.setError("Información muy corta");
        }

        else if(txt_correo.getText().toString().length() < 5){
            txt_correo.setError("Email muy corta");
        }

        else if (!validarEmail(txt_correo.getText().toString())) {
            txt_correo.setError("Email no válido");
        }
        else if(txt_passwor.getText().toString().length() < 8){
            txt_passwor.setError("Contraseña muy corta");
        }
        else if(txt_passwor1.getText().toString().length() < 8){
            txt_passwor1.setError("Contraseña muy corta");
        }

        else if(!txt_passwor.getText().toString().equals(txt_passwor.getText().toString())){
            txt_passwor.setError("Las contraseña no coinciden.");
        }
        else{


            if (utility.estado(getApplicationContext())) {
                loadswet("Validando la información...");
                comandoValidarCorreoFirebase.checkAccountEmailExistInFirebase(txt_correo.getText().toString());

            }else{
                alerta("Sin Internet","Valide la conexión a internet");
            }
        }
    }


    //posgres dialos sweetalert

    public void loadswet(String text){

        try {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(text);
            pDialog.setCancelable(false);
            pDialog.show();

        }catch (Exception e){

        }

    }


    //oculatomos el dialog
    private void hideDialog() {
        if (pDialog != null)
            pDialog.dismiss();
    }


    //validar email
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    ///imagen
    public void camara1(View v){
        if(Utility.checkPermission(getApplicationContext())){
            camara1.setEnabled(true);
            showOptions();
        }

        else{
            camara1.setEnabled(false);

        }

    }

    //manejo camara y galeria
    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
        builder.setTitle("Seleccione una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = Utility.checkPermission(getApplicationContext());
                if(option[which] == "Tomar foto"){

                    if (result) {
                        openCamera();
                    }
                }else if(option[which] == "Elegir de galería"){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {

        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ruta1 = photoFile.getAbsolutePath();


                photoURI = FileProvider.getUriForFile(this,
                        //"com.tactoapps.android.fileprovider",
                        //otener ruta fileprovidder   getApplicationContext().getPackageName()
                        getString(R.string.authorities),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix //
                ".jpg",         // suffix //
                storageDir      // directory //
        );

        // Save a file: path for use with ACTION_VIEW intents
        mPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    camara1.setImageBitmap(getCircularBitmap(bitmap));

                    txt_camara1.setText("Cambiar Foto");
                    foto = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);

                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();

                    try {
                        Bitmap imagen = getBitmapFromUri (path);
                        camara1.setImageBitmap(getCircularBitmap(imagen));
                        foto = encodeToBase64(imagen, Bitmap.CompressFormat.JPEG, 100);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // camara1.setImageURI(path);
                    txt_camara1.setText("Cambiar Foto");

                    break;

            }
        }
        else {
            Log.v("Code",""+requestCode);
        }
    }


    //convertir uri en bitmap
    private Bitmap getBitmapFromUri ( Uri uri ) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver (). openFileDescriptor ( uri , "r" );
        FileDescriptor fileDescriptor = parcelFileDescriptor . getFileDescriptor ();
        Bitmap image = BitmapFactory . decodeFileDescriptor ( fileDescriptor );
        parcelFileDescriptor . close ();
        return image ;
    }

    //convertir bitmap a base64
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                camara1.setEnabled(true);

            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }
    //fin manejo de galeria y camara




    //imagen circular
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    @Override
    public void cargoValidarCorreoFirebase() {
        String nombre = txt_nombre.getText().toString();
        String apellido = txt_apellido.getText().toString();
        String celular = txt_celular.getText().toString();
        String correo = txt_correo.getText().toString();
        String password = txt_passwor.getText().toString();
        String foto = this.foto;
        String token = this.token;
        String cod = this.codigo();

        usuario = new Usuario("",nombre,apellido,celular,correo,password,foto,token, "",modelo.latitud,modelo.longitud,0,0,true,"", cod);

        comandoValidarCorreoFirebase.registroUsuario(usuario);
    }

    @Override
    public void cargoValidarCorreoFirebaseEroor() {
        txt_correo.setError("Email ya registrado");
        hideDialog();

    }

    @Override
    public void setUsuarioListener() {

        hideDialog();
        alertaOK("Registro exitoso", "Inicie sesion");
    }

    @Override
    public void errorSetUsuario() {
        hideDialog();
        alerta("Error Resgisto","Algo salió malcon el registro verifique su conexión.");
    }

    @Override
    public void errorCreacionUsuario() {
        hideDialog();

        alerta("Error Resgisto","Algo salió malcon el registro verifique su conexión");
    }

    //alerta swit alert
    //alerta
    public void alerta(String titulo,String decripcion){

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(decripcion)
                .setConfirmText("Aceptar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismissWithAnimation();

                    }
                })

                .show();
    }

    //alerta
    public void alertaOK(String titulo,String decripcion){

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(decripcion)
                .setConfirmText("Aceptar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismissWithAnimation();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

                    }
                })

                .show();
    }


    public void terminos(View v){
        modelo.tipoLogin = "normal";
        setPreference("normal");
        Intent i = new Intent(getApplicationContext(), Teminos.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }


    public void setPreference(String tipoLogin){
        SharedPreferences.Editor editor = getSharedPreferences("tipoLogin", MODE_PRIVATE).edit();
        editor.putString("tipoLogin", tipoLogin);
        editor.apply();
    }


    //codigo
    public  String codigo(){
        // Inicializamos la variable que almacenará la codigo.
        String codigoaleatorio = "";
// Inicializamos la instancia de la clase Random con la que
// generaremos el valor aleatorio.
        Random rnd = new Random();

// Creamos un ciclo que se ejecute 7 veces, que corresponden al
// texto de la codigo.
        for (int i = 0; i < 4; i++)
        {
            // Con este condicional verificamos si estamos en la parte
            // numérica o alfabética de la codigo.
            // Solo debe entrar al condicional si estamos generando los
            // números de la codigo.
            if(i < 2)
            {
                // Con esta instrucción se genera un número aleatorio entre
                // 0 y 9, no se incluye el 10.
                codigoaleatorio += rnd.nextInt(10);
            }
            // Entrará en esta parte del condicional cuando estemos generando
            // las letras de la codigo.
            else
            {
                // Con esta instrucción se genera un número aleatorio entre
                // 65 y 90, no se incluye el 91. Luego se convierte a un
                // caracter ASCII.
                codigoaleatorio += (char)(rnd.nextInt(91) + 65);
            }
        }

// Por último imprimimos la codigo en la consola.
        System.out.println("el código es: " + codigoaleatorio);

        return  codigoaleatorio.replace(" ","");
    }
}
