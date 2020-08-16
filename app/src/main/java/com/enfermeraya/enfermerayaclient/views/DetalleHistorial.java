package com.enfermeraya.enfermerayaclient.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Historial;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.comandos.ComandoHistorial;
import com.enfermeraya.enfermerayaclient.comandos.ComandoPerfil;
import com.enfermeraya.enfermerayaclient.comandos.ComandoSercicio;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.enfermeraya.enfermerayaclient.views.Registrarse.decodeBase64;

public class DetalleHistorial extends Activity implements ComandoPerfil.OnPerfilChangeListener , ComandoHistorial.OnHistorialChangeListener {

    TextView txtfecha;
    TextView txtservicio;
    TextView txtnombre;
    TextView text_medical_center;
    Button btnestado;
    Button btntitulo;
    Utility utility;
    Modelo modelo = Modelo.getInstance();
    ComandoPerfil comandoPerfil;
    ComandoHistorial comandoHistorial;
    EditText observacionesenferm;
    EditText medicamentos;
    SweetAlertDialog pDialog;
    Button btnaceptar;
    Servicios servicios;
    Historial historial;

    String dato = "";


    //foto
    String userChoosenTask="";
    boolean img_cam1 = false;
    private int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    ImageView camara1;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detalle_historial);

        txtfecha = (TextView) findViewById(R.id.txtfecha);
        txtservicio = (TextView) findViewById(R.id.txtservicio);
        txtnombre = (TextView) findViewById(R.id.txtnombre);
        text_medical_center = (TextView) findViewById(R.id.text_medical_center);
        btnestado = (Button) findViewById(R.id.btnestado);
        observacionesenferm = (EditText) findViewById(R.id.observacionesenferm);
        medicamentos = (EditText) findViewById(R.id.medicamentos);
        btnaceptar = (Button) findViewById(R.id.btnaceptar);
        camara1 = (ImageView)findViewById(R.id.camara1);
        btntitulo = (Button)findViewById(R.id.btntitulo);


        comandoPerfil =  new ComandoPerfil(this);
        comandoHistorial =  new ComandoHistorial(this);
        utility =  new Utility();

        boolean result = Utility.checkPermission(DetalleHistorial.this);

        if(utility.estado(getApplicationContext())){


            Bundle parametros = this.getIntent().getExtras();
            if(parametros !=null){
                dato = parametros.getString("historial");
                historial =  modelo.historial;
                txtfecha.setText(modelo.historial.getFecha());
                txtservicio.setText(modelo.historial.getTipoServicio());
                txtnombre.setText(modelo.historial.getNombre());
                text_medical_center.setText(modelo.historial.getDireccion());
                btnestado.setText(modelo.historial.getEstado());
                observacionesenferm.setText(modelo.historial.getObservacionesEnfermero());
                medicamentos.setText(modelo.historial.getMedicamentosAsignados());
                btntitulo.setText("Detalle Historico");

                if (!modelo.historial.getFoto().equals("")) {
                    camara1.setImageBitmap(getCircularBitmap(decodeBase64(modelo.historial.getFoto())));
                }

                observacionesenferm.setEnabled(false);
                medicamentos.setEnabled(false);
                btnestado.setText("Finalizado");
                btnaceptar.setVisibility(View.GONE);

            }else{
                servicios =  modelo.servicios;
                txtfecha.setText(modelo.servicios.getFecha());
                txtservicio.setText(modelo.servicios.getTipoServicio());
                txtnombre.setText(modelo.servicios.getNombre());
                text_medical_center.setText(modelo.servicios.getDireccion());
                btnestado.setText(modelo.servicios.getEstado());
                btntitulo.setText("Detalle Servicio");

            }




        }else {
            alerta("Sin Internet", "Valide la conexión a internet");
        }

    }


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       if(dato.equals("")){
           Intent i = new Intent(getApplicationContext(), MenuLateral.class);
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(i);
           overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
           finish();
       }
    }

    @Override
    public void cargoUSuario() {
        txtnombre.setText(modelo.servicios.getNombre());
    }

    @Override
    public void setUsuarioListener() {

    }

    @Override
    public void errorSetUsuario() {

    }

    public void finalizar(View v){

        if (utility.estado(getApplicationContext())){
            String foto = this.foto;
            String observaciones  = observacionesenferm.getText().toString();
            String medicamento  = medicamentos.getText().toString();
            if(observaciones.equals("") || medicamento.equals("")  || foto.equals("")){
                alerta("Campos Hobligtorios", "Todos los campos son hobligatorios");
            }else{
                loadswet("Validando la información...");

                comandoHistorial.updateServico(observaciones,medicamento,modelo.servicios.getKey(),foto);
            }

        }
        else {
            alerta("Sin Internet", "Valide la conexión a internet");
        }

    }

    @Override
    public void errorHistorial() {
        Log.v("Error","error al mover el servicio al historico");

    }

    @Override
    public void okHistorial() {

        hideDialog();
        observacionesenferm.setEnabled(false);
        medicamentos.setEnabled(false);
        btnestado.setText("Finalizado");
        btnaceptar.setVisibility(View.GONE);

        modelo.listServicios.remove(servicios);


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
        final AlertDialog.Builder builder = new AlertDialog.Builder(DetalleHistorial.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleHistorial.this);
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

}
