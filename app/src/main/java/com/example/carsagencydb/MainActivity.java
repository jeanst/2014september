package com.example.carsagencydb;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class MainActivity extends ActionBarActivity implements Serializable {
    Button btncar1, btncar2, btncar3, btncar4;
    Button btnown1;
    Button btnbid2;
    Button getcarnr;
    Button btnyes;
    Button btnno;
    Button save;
    static EditText enteredcarnumber;
    public static CarSales agency;
    public static Car[] cars;
    DBAdapter dbadapter;
    static int NoOfCars = 3;
    static int ActNoOfCars = 0;

    private static final long serialVersionUID = 1L;
    private static final int REQUEST_CODE = 7;
    static String License = "00-000-00";
    public static int position;

    Boolean FirstTime = true;
    Boolean NeedSave = false;
    Boolean SafeRemove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFirstTime();
        if (FirstTime) {
            Toast.makeText(this,"Cars entered manually",Toast.LENGTH_LONG).show();
            Owner o1 = new Owner("Jean Steinberg", "050-5886905");
            Owner o2 = new Owner("Jimmy Weiss", "050-4830036");
            Owner o3 = new Owner("Yona Marcovici", "054-4827142");

            Car c1 = new Car(o1, "60-724-73", "Skoda/Fabia", true, false, 2011, 5, 82000, 77000, 80000);
            Car c2 = new Car(o2, "21-123-99", "Ford/Focus", true, false, 2013, 5, 26000, 88000, 85000);
            Car c3 = new Car(o3, "99-666-44", "Mitsubishi", true, false, 2007, 5, 170000, 30000, 32000);

            c1.makeBid(new Bid("", 0));
            c2.makeBid(new Bid("", 0));
            c3.makeBid(new Bid("", 0));

            agency = new CarSales(NoOfCars);
            cars = new Car[NoOfCars];
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            if (agency.addCar(c1))
                //System.out.println(" added car " + c1.getManufacturer());
                dbAdapter.insertCar(c1);
            if (agency.addCar(c2))
                //System.out.println(" added car " + c2.getManufacturer());
                dbAdapter.insertCar(c2);
            if (agency.addCar(c3))
                //System.out.println(" added car " + c3.getManufacturer());
                dbAdapter.insertCar(c3);
            ActNoOfCars = agency.getNoOfCars();
            dbAdapter.close();

        } else {

            //Load data from DB
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            Cursor cursor = dbAdapter.getAllCars();
            ActNoOfCars = cursor.getCount();
            NoOfCars = ActNoOfCars +10;
            if (ActNoOfCars == 0) {
                Toast.makeText(this,"Loading from file",Toast.LENGTH_LONG).show();
                loadData();
                Car[] cars = new Car[agency.getNoOfCars()];
                for (int i = 0; i < agency.getNoOfCars(); i++) {
                    dbAdapter.insertCar(agency.getCars()[i]);
                }
            } else {
                Toast.makeText(this,"Loading from DB",Toast.LENGTH_LONG).show();
                agency = new CarSales(NoOfCars);
                Car[] cars = new Car[ActNoOfCars];
                int count = 0;
                while (cursor.moveToNext()) {
                    String Model = cursor.getString(0);
                    String License = cursor.getString(1);
                    String OwnerName = cursor.getString(2);
                    String OwnerTel = cursor.getString(3);
                    int Year = cursor.getInt(4);
                    int Km = cursor.getInt(5);
                    int Seats = cursor.getInt(6);
                    boolean Airbag = (cursor.getInt(7) == 1 ? true : false);
                    boolean Leasing = (cursor.getInt(8) == 1 ? true : false);
                    int Price = cursor.getInt(9);
                    int LevyPrice = cursor.getInt(10);
                    int MaxBid = cursor.getInt(11);
                    String BidTel = cursor.getString(12);
                    Owner o1 = new Owner(OwnerName, OwnerTel);
                    Bid b1 = new Bid(BidTel, MaxBid);
                    cars[count] = new Car(o1, License, Model, Airbag, Leasing, Year, Seats, Km,
                            Price, LevyPrice, b1);
                    if (agency.addCar(cars[count])) ;
                    count++;
                }
            }
            cursor.close();
            dbAdapter.close();
        }

        View view = (View) findViewById(R.id.mainmenu);

        btncar1 = (Button) findViewById(R.id.btncar1);
        btncar2 = (Button) findViewById(R.id.btncar2);
        btncar3 = (Button) findViewById(R.id.btncar3);
        btncar4 = (Button) findViewById(R.id.btncar4);
        btnown1 = (Button) findViewById(R.id.btnown1);
        btnbid2 = (Button) findViewById(R.id.btnbid2);

        enteredcarnumber = (EditText) findViewById(R.id.enteredcarnumber);
        save = (Button) findViewById(R.id.save);
        getcarnr = (Button) findViewById(R.id.getcarnr);
        getcarnr.setOnClickListener(btnListener);


        btncar1.setOnClickListener(btnListener);
        btncar2.setOnClickListener(btnListener);
        btncar3.setOnClickListener(btnListener);
        btncar4.setOnClickListener(btnListener);
        btnown1.setOnClickListener(btnListener);
        btnbid2.setOnClickListener(btnListener);
        save.setOnClickListener(btnListener);
        btnyes = (RadioButton) findViewById(R.id.btnyes);
        btnno = (RadioButton) findViewById(R.id.btnno);
        btnyes.setOnClickListener(btnListener1);
        btnno.setOnClickListener(btnListener1);

        // Save the data

    }

    public void checkFirstTime() {
        File f = new File((Environment.getExternalStorageDirectory()
                + File.separator + "CarAgencyDB"), "NoOfCar.txt");
        if (f.exists())
            FirstTime = false;
    }

    // Load in.
    public void loadData() {
        try {
            String s1;
            String imageName;
            File sdCardDir = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "CarAgencyDB");
            imageName = "NoOfCar.txt";
            BufferedReader in = new BufferedReader(new FileReader(new File(sdCardDir, imageName)));
            s1 = in.readLine();
            NoOfCars = Integer.valueOf(s1);
            s1 = in.readLine();
            ActNoOfCars = Integer.valueOf(s1);
            in.close();
            agency = new CarSales(NoOfCars);
            imageName = "caragency.bin";
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(sdCardDir, imageName)));
            Object o = ois.readObject(); // read the class as an 'object'
            ois.close();// close the stream
            agency = (CarSales) o;
        } catch (Exception ex) {
            Log.d("Address Book", ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Save out.
    public void saveData() {
        try {
            String imageName;
            File sdCardDir = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "CarAgencyDB");
            if(sdCardDir.mkdirs()) {
                imageName = "NoOfCar.txt";
                BufferedWriter out = new BufferedWriter(new FileWriter(new File(sdCardDir, imageName)));
                out.write(String.valueOf(NoOfCars) + "\n");
                out.flush();
                out.write(String.valueOf(agency.getNoOfCars()));
                out.flush();
                out.close();
                imageName = "caragency.bin";
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(sdCardDir, imageName)));
                Object o = (CarSales) agency;
                oos.writeObject(o); // write the class as an 'object'
                oos.flush(); // flush the stream to insure all of the information was written to 'caragency.bin'
                oos.close();// close the stream
            }
        } catch (Exception ex) {
            Log.d("File on disk: Error.", ex.getMessage());
            Toast.makeText(this,"Problem with file on disk",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener btnListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = "";
            boolean isclicked = ((RadioButton) v).isChecked();
            // Check which radio button was clicked
            switch (v.getId()) {
                case R.id.btnyes:
                    if (isclicked)
                        SafeRemove = true;
                    break;
                case R.id.btnno:
                    if (isclicked)
                        SafeRemove = false;
                    break;
            }
        }
    };


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s = "";
            boolean isclicked = ((Button) view).isPressed();
            //Check which  button was clicked
            switch (view.getId()) {
                case R.id.getcarnr:
                    if (isclicked)
                        checkLicense();
                    break;
                case R.id.btncar1:
                    if (isclicked) {
                        s = "CARS  LIST";
                        prepareCarList4Display();
                    }
                    break;
                case R.id.btnown1:
                    if (isclicked) {
                        s = "OWNERS  LIST";
                        prepareOwnwerList4Display();
                    }
                    break;
                case R.id.btncar2:
                    if (isclicked) {
                        s = "CAR  ADD";
                       if ((ActNoOfCars - agency.getNoOfCars()) <= 0) {
                           Toast.makeText(getBaseContext(), "There is NO ROOM in AGENCY for a new car",
                                   Toast.LENGTH_LONG).show();
                          makeRoom();
                            break;
                       }
                        prepareAddCarMenu();
                    }
                    break;
                case R.id.btncar3:
                    if (isclicked) {
                        s = "CAR  DISPLAY";
                        prepareDisplayOneCar();
                    }
                    break;
                case R.id.btnbid2:
                    if (isclicked) {
                        s = "BID  ADD";
                        add1Bid2aCar();
                    }
                    break;
                case R.id.btncar4:
                    if (isclicked) {
                        s = "CAR  REMOVE";
                        removeCar();
                    }
                    break;
                case R.id.save:
                    if (isclicked)
                        s = "SAVE!";
                    saveData();
                    break;
            }
            //Toast.makeText(getBaseContext(), " Menu: " + s, Toast.LENGTH_LONG).show();
        }
    };

    public void prepareCarList4Display() {
        Intent i = new Intent(this, ListCars.class);
        startActivity(i);
    }

    public void prepareOwnwerList4Display() {
        Intent i = new Intent(this, ListOwners.class);
        startActivity(i);
    }

    public void prepareAddCarMenu() {
        Intent intent = new Intent(this, AddCarDialog.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Car c = new Car((Car) extras.getSerializable("AddedCar"));
            if(agency.addCar(c))
               Toast.makeText(this," Car " + c.getLicense() + " has been added!", Toast.LENGTH_LONG).show();
            DBAdapter dbadapter = new DBAdapter(this);
            dbadapter.open();
            Cursor cursor = dbadapter.getAllCars();
            cursor.moveToLast();
            dbadapter.insertCar(c);
            cursor.close();
            dbadapter.close();
            NeedSave = true;
            saveData();
        }
    }

    public void makeRoom() {
        int oldNoOfCars = ActNoOfCars;
        Car[] temp = new Car[oldNoOfCars];
        for (int i = 0; i < oldNoOfCars; i++) {
            temp[i] = agency.getCars()[i];
        }
        ActNoOfCars += 10;
        agency = new CarSales(ActNoOfCars);
        Car[] cars = new Car[oldNoOfCars];
        for (int i = 0; i < oldNoOfCars; i++) {
            if (agency.addCar(temp[i]))
                cars[i] = agency.getCars()[i];
        }
        Toast.makeText(this, "OldNoOfCars: " + oldNoOfCars + " NewNoOfCars: " + ActNoOfCars, Toast.LENGTH_LONG).show();
    }

    public void checkLicense() {
        License = enteredcarnumber.getText().toString();
        if ((License.indexOf('-') != 2) || (License.lastIndexOf('-') != 6)) {
            Toast.makeText(getBaseContext(), "License number not legal  "
                    + License, Toast.LENGTH_LONG).show();
            enteredcarnumber.setText("00-000-00");
        }
    }

    public Boolean getPosition() {
        position = -1;
        for (int i = 0; i < agency.getNoOfCars(); i++) {
            if (License.equals(agency.getCars()[i].getLicense()))
                position = i;
        }
        if (position < 0 || position > agency.getNoOfCars()) {
            Toast.makeText(getBaseContext(), "No car with this license number:" + License, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void prepareDisplayOneCar() {
        //get position of the car with license License
        if (!getPosition())
            return;
        Intent intent = new Intent(this, DisplayOneCar.class);
        Bundle extras = new Bundle();
        extras.putSerializable("OneCar", (Serializable) agency.getCars()[position]);
        extras.putSerializable("OneOwner", (Serializable) agency.getCars()[position].getOwner());
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void removeFile(String License) {
        String imageName;
        File sdCardDir = new File(Environment.getExternalStorageDirectory() +
                File.separator + "CarAgency");
        imageName = License + ".jpg";
        File imageFile = new File(sdCardDir, imageName);
        if (imageFile.exists()) {
            imageFile.delete();
        } else {
            Toast.makeText(this, "File NOT exists : " + imageFile.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void removeCar() {
        if (!getPosition())
            return;
        int number = agency.getNoOfCars();
        int count = 0;
        DBAdapter dbadapter = new DBAdapter(this);
        dbadapter.open();
        Car[] temp1 = new Car[number];
        for (int i = 0; i < number; i++) {
            if (License.equals(agency.getCars()[i].getLicense())) {
                if (SafeRemove) {
                    Toast.makeText(this, "This car: " + License + " will be removed", Toast.LENGTH_LONG).show();
                    removeFile(License);
                    dbadapter.deleteCar(agency.getCars()[i]);
                } else {
                    temp1[count] = agency.getCars()[i];
                    count++;
                }
            } else {
                temp1[count] = agency.getCars()[i];
                count++;
            }
            //Toast.makeText(this, "Count " + count, Toast.LENGTH_LONG).show();
        }
        dbadapter.close();
        agency = new CarSales(count);
        Car[] cars = new Car[count];
        for (int i = 0; i < count; i++) {
            if (agency.addCar(temp1[i]))
                cars[i] = agency.getCars()[i];
        }
        NeedSave=true;
    }

    public void add1Bid2aCar() {
        if (!getPosition())
            return;
        Intent intent = new Intent(this, Add1Bid.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        if (NeedSave) {
            saveData();
            NeedSave = false;
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (NeedSave) {
            saveData();
            NeedSave = false;
        }
        super.onStop();
    }


}
