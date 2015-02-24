package com.example.carsagencydb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by Jean on 29/01/2015.
 */
public class DisplayOneCar extends ActionBarActivity implements Serializable {
    private static final long serialVersionUID = 1L;
    Car c1;
    Owner o1;
    TextView carmodel;
    TextView caryear;
    TextView carnumber;
    TextView ownername;
    TextView ownertel;

    TextView km;
    TextView price;
    TextView levyprice;
    TextView leasing;
    TextView airbag;
    TextView highestbid;
    ImageView carimage;
    ImageView back;
    RadioButton btnyes1, btnyes2, btnno1, btnno2;

    String Model;
    String License;
    String OwnerName;
    String OwnerTel;

    int Year;
    int Km;
    int Seats;
    int Price;
    int LevyPrice;
    int posit;

    Boolean AirBags = false;
    Boolean LeasingRental = false;

    GregorianCalendar gcalendar;
    File imageFile;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_car);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        c1 = (Car) extras.getSerializable("OneCar");
        o1 = (Owner) extras.getSerializable("OneOwner");

        gcalendar = new GregorianCalendar();

        carmodel = (TextView) findViewById(R.id.carmodel);
        carnumber = (TextView) findViewById(R.id.carnumber);
        ownername = (TextView) findViewById(R.id.ownername);
        ownertel = (TextView) findViewById(R.id.ownertel);
        caryear = (TextView) findViewById(R.id.caryear);

        km = (TextView) findViewById(R.id.km);
        price = (TextView) findViewById(R.id.price);
        levyprice = (TextView) findViewById(R.id.levyprice);
        leasing = (TextView) findViewById(R.id.leasing);
        airbag = (TextView) findViewById(R.id.airbag);
        highestbid = (TextView) findViewById(R.id.highestbid);
        carimage = (ImageView) findViewById(R.id.carimage);

        imageFile = checkFile(c1.getLicense());
        if (imageFile != null) {
            carimage.setImageBitmap(getPicture(imageFile));
        } else {
            carimage.setImageResource(R.drawable.car0);
        }
        carmodel.setText(c1.getManufacturer());
        carnumber.setText(c1.getLicense());
        ownername.setText(o1.getName());
        ownertel.setText(o1.getTelNumber());
        String syear = "";
        syear = syear + c1.getYear();
        caryear.setText(syear);

        String skm = "";
        skm = skm + c1.getKm();
        km.setText(skm);
        String sprice = "";
        sprice = sprice + c1.getPrice();
        price.setText(sprice);
        String slevy = "";
        slevy = slevy + c1.getLevyPrice();
        levyprice.setText(slevy);
        if (c1.hasAirbags())
            airbag.setText("YES");
        else
            airbag.setText("NO ");
        if (c1.isLeasingOrRental())
            leasing.setText("Leasing");
        else
            leasing.setText("Private");
        String sbid = "";
        sbid += c1.getHighestBid().getBidPrice();
        highestbid.setText(sbid);
        back = (ImageView) findViewById(R.id.back);
    }

    public File checkFile(String License) {
        String imageName;
        File sdCardDir = new File(Environment.getExternalStorageDirectory() +
                File.separator + "CarAgencyDB");
        imageName = License + ".jpg";
        File imageFile = new File(sdCardDir, imageName);
        if (imageFile.exists())
            return imageFile;
        else
            return null;
    }

    public Bitmap getPicture(File imageFile) {
        String imageName;
        Bitmap bm;
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "SDCARD is not OK !", Toast.LENGTH_LONG).show();
            return null;
        }
        try {
            if (imageFile.exists()) {
                FileInputStream streamIn = new FileInputStream(imageFile);
                bm = BitmapFactory.decodeStream(streamIn); //This gets the image
                streamIn.close();
            } else {
                Toast.makeText(this, "File Problem: " + imageFile.toString(), Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return bm;
    }


    public void getBitmap(View view) {
        int RESULT_LOAD_IMAGE = 1;
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Toast.makeText(getApplicationContext(),
                    "ImageView: " + carimage.getWidth() + " x " + carimage.getHeight(),
                    Toast.LENGTH_LONG).show();
            Bitmap bitmap;
            bitmap = decodeSampledBitmapFromUri(
                    targetUri,
                    carimage.getWidth(), carimage.getHeight());
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(),
                        "the image data could not be decoded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Decoded Bitmap: " + bitmap.getWidth() + " x " + bitmap.getHeight(),
                        Toast.LENGTH_LONG).show();
                carimage.setImageBitmap(bitmap);
                // Save bitmap to a file carnumber.jpg on disk
                savePict(bitmap, c1.getLicense());
            }
        }
    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        Bitmap bm = null;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return bm;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void savePict(Bitmap bm, String License) {
        String imageName;
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "SDCARD is not OK !", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            File sdCardDir = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "CarAgencyDB");
            imageName = License + ".jpg";
            new File(String.valueOf(sdCardDir)).mkdirs();
            File filename = new File(String.valueOf(sdCardDir), imageName);
            FileOutputStream outStream;
            outStream = new FileOutputStream(filename);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
             /* 100 to keep full quality of the image */
            outStream.flush();
            outStream.close();

            //Refreshing SD card
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    filename.getAbsolutePath(), filename.getName(), filename.getName());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Image could not be saved : " +
                    "Please ensure you have SD card installed " +
                    "properly", Toast.LENGTH_LONG).show();
        }
    }

    public void btnReturn(View view) {
        //MainActivity.enteredcarnumber.setText("");
        finish();
    }

    public void getOneBID(View view) {
        for (int i = 0; i < MainActivity.agency.getNoOfCars(); i++) {
            if (MainActivity.agency.getCars()[i].getLicense().equals(c1.getLicense()))
                MainActivity.position = i;
        }
        Intent intent = new Intent(this, Add1Bid.class);
        startActivity(intent);
    }

}
