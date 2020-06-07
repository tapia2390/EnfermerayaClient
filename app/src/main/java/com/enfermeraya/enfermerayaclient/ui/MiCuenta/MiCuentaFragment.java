package com.enfermeraya.enfermerayaclient.ui.MiCuenta;

import android.annotation.TargetApi;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Usuario;
import com.enfermeraya.enfermerayaclient.comandos.ComandoPerfilFragment;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;
import com.enfermeraya.enfermerayaclient.views.MainActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MiCuentaFragment extends Fragment implements ComandoPerfilFragment.OnPerfilChangeListener{

    Usuario usuario;

    EditText txt_nombre, txt_apellido, txt_celular, txt_correo;
    Modelo modelo = Modelo.getInstance();
    String token = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ComandoPerfilFragment comandoPerfil;

    //foto
    String userChoosenTask="";
    boolean img_cam1 = false;
    private int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    ImageView camara1;
    TextView txt_camara1;

    private ProgressDialog progressDialog;
    private static final String TAG ="AndroidBash";
    String foto ="";
    Date date;
    DateFormat hourFormat;
    int setHora = 0;


    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private final int MY_PERMISSIONS = 123;
    private final int PHOTO_CODE = 3;
    private final int SELECT_PICTURE = 300;
    private String mPath = "";
    private LinearLayout mRlView;
    Uri photoURI = null;
    String ruta1 = null;
    static final int REQUEST_TAKE_PHOTO = 3;
    String mCurrentPhotoPath = "";
    Utility utility;
    SweetAlertDialog pDialog;
    LinearLayout layut1,layut2;
    Button btnupdate;
    Space space1;
    String tipoLogin;
    private GoogleSignInClient mGoogleSignInClient;
    Button btnNext, update;


    private com.enfermeraya.enfermerayaclient.ui.MiCuenta.MiCuentaViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mi_cuenta, container, false);

        //instanciamos la clase utility
        utility = new Utility();
        usuario =  null;

        txt_nombre = (EditText)root.findViewById(R.id.txt_nombre);
        txt_apellido = (EditText)root.findViewById(R.id.txt_apellido);
        txt_celular = (EditText)root.findViewById(R.id.txt_celular);
        txt_correo = (EditText)root.findViewById(R.id.txt_correo);

        camara1 = (ImageView)root.findViewById(R.id.camara1);
        txt_camara1 = (TextView)root. findViewById(R.id.txt_camara1);
        mRlView = (LinearLayout)root. findViewById(R.id.mRlView);
        layut1 = (LinearLayout)root. findViewById(R.id.layut1);
        layut2 = (LinearLayout)root. findViewById(R.id.layut2);
        btnupdate =(Button)root. findViewById(R.id.btnupdate);
        space1 =(Space) root.findViewById(R.id.space1);
         btnNext = root.findViewById(R.id.btncerrar);
         update = root.findViewById(R.id.btnupdate);


        comandoPerfil =  new ComandoPerfilFragment(this);



        if (utility.estado(getActivity())) {


            Log.v("User" ,  ""+user.getDisplayName());

            if(modelo.tipoLogin.equals("normal")){
                loadswet("Cargando la información...");
                comandoPerfil.getUsuario();
            }else{
                layut1.setVisibility(View.GONE);
                btnupdate.setVisibility(View.GONE);
                layut2.setVisibility(View.GONE);
                space1.setVisibility(View.GONE);
                txt_camara1.setVisibility(View.GONE);
                txt_nombre.setFocusable(false);
                txt_correo.setFocusable(false);
                txt_nombre.setText(user.getDisplayName());
                txt_correo.setText(user.getEmail());
                camara1.setClickable(false);
                txt_camara1.setClickable(false);

                //Bitmap bitmap = BitmapFactory.decodeFile(user.getPhotoUrl());

                try {
                    // Uri url = user.getPhotoUrl();

                        /*Glide.with(getApplicationContext())
                                .load(user.getPhotoUrl())
                                .centerCrop()
                                .into(camara1);*/

                    Glide.with(getActivity())
                            .load(user.getPhotoUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(camara1);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                //camara1.setImageBitmap(getCircularBitmap(bitmap));

                //camara1.setBackgroundResource();
            }

        }else {
            alerta("Sin Internet","Valide la conexión a internet");
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Toast.makeText(getActivity(), modelo.tipoLogin, Toast.LENGTH_SHORT).show();

        getPreference();
        modelo.tipoLogin = tipoLogin;



        //


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cerrarSesion();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validar();

            }
        });

        camara1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                camara1();

            }
        });

        return root;
    }




    //posgres dialos sweetalert

    public void loadswet(String text){

        try {
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
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



    public void validar(){

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
        else{

            usuario = new Usuario();
            usuario.setNombre(txt_nombre.getText().toString());
            usuario.setApellido(txt_nombre.getText().toString());
            usuario.setCelular(txt_celular.getText().toString());
            usuario.setFoto(foto);



            if (utility.estado(getActivity())) {
                loadswet("Validando la información...");
                comandoPerfil.actualizarUsuario(usuario);
            }else{
                alerta("Sin Internet","Valide la conexión a internet");
            }

        }


    }


    //validar email
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    ///imagen
    public void camara1(){
        if(Utility.checkPermission(getActivity())){
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

        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = Utility.checkPermission(getActivity());
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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


                photoURI = FileProvider.getUriForFile(getActivity(),
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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




   // @Override
  /*  public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.getActivity().onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getActivity(),
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
                getActivity().getContentResolver (). openFileDescriptor ( uri , "r" );
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
                Toast.makeText(getActivity(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                camara1.setEnabled(true);

            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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


    //alerta swit alert
    //alerta
    public void alerta(String titulo,String decripcion){

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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





    @Override
    public void cargoUSuario() {

        hideDialog();
        txt_nombre.setText("" + modelo.usuario.getNombre());
        txt_apellido.setText("" + modelo.usuario.getApellido());
        txt_celular.setText("" + modelo.usuario.getCelular());
        txt_correo.setText("" + modelo.usuario.getCorreo());
        if (!modelo.usuario.getFoto().equals("")) {
            camara1.setImageBitmap(getCircularBitmap(decodeBase64(modelo.usuario.getFoto())));
        }


    }

    @Override
    public void setUsuarioListener() {
        hideDialog();
        alerta("Actualización","Datos Actualizados");
    }

    @Override
    public void errorSetUsuario() {
        hideDialog();
        alerta("Eror","Error con el regitro");
    }



    //Cerrar Session

    public void cerrarSesion(){

        if(modelo.tipoLogin.equals("normal")){
            cerrarNormal();

        }else if(modelo.tipoLogin.equals("facebook")){

            cerrarFacebook();

        }
        else if(modelo.tipoLogin.equals("gmail")){
            cerrarGmail();
        }

    }



    public void cerrarGmail(){
        mGoogleSignInClient.signOut();
        Logout();
    }


    public void cerrarNormal(){
        Logout();
    }


    public void cerrarFacebook(){
        Toast.makeText(getActivity().getApplication(),  "Cerrarndo sessión", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Logout();
    }

    public void Logout(){
        try {
            modelo.tipoLogin = "";
            user =  null;
            modelo.uid = "";
            mAuth.signOut();
            setPreference("");
            mAuth.getInstance().signOut();
            Toast.makeText(getActivity().getApplication(),  "Cerrarndo sessión", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(i);

        }
        catch (Exception e){}
    }


    public void getPreference(){
        SharedPreferences prefs = getActivity().getSharedPreferences("tipoLogin", MODE_PRIVATE);
        tipoLogin = prefs.getString("tipoLogin", "");//"No name defined" is the default value.
    }

    public void setPreference(String tipoLogin){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tipoLogin", MODE_PRIVATE).edit();
        editor.putString("tipoLogin", tipoLogin);
        editor.apply();
    }





}
