package com.example.crowddatacollection;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowddatacollection.Adapter.CourseRVAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;

public class Activity_Details extends AppCompatActivity {


    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView courseRV;
    private TextView ID;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Button csv;
    WritableWorkbook workbook;
    private ArrayList<Details> coursesArrayList;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseFirestore db;
    private boolean getFile;
    ProgressBar loadingPB;
    private File filePath ;

    private static String EXCEL_SHEET_NAME = "Sheet1";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        workbook=null;
        getFile=false;

        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        String str = intent.getStringExtra("message_key");
        filePath= new File(Environment.getExternalStorageDirectory() + "/"+str+".csv");
        ID = findViewById(R.id.ID);
        ID.setText(str);
        // initializing our variables.
        courseRV = findViewById(R.id.idRVCourses);
        loadingPB = findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        coursesArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        courseRVAdapter = new CourseRVAdapter(coursesArrayList, this);

        // setting adapter to our recycler view.
        courseRV.setAdapter(courseRVAdapter);

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection(str).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            getFile=true;
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            csv.setText("Get CSV");
                            loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Details c = d.toObject(Details.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                coursesArrayList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            courseRVAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(Activity_Details.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(Activity_Details.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });

        csv=(Button)findViewById(R.id.getFile);
        csv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(getFile==true)
                {
                if (ContextCompat.checkSelfPermission(Activity_Details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                }else{
                    HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                    HSSFSheet hssfSheet = hssfWorkbook.createSheet(str);

                    HSSFRow hssfRow = hssfSheet.createRow(0);
                    HSSFCell hssfCell = hssfRow.createCell(0);
                    hssfCell.setCellValue("Longitude");
                    hssfCell = hssfRow.createCell(1);
                    hssfCell.setCellValue("Latitude");
                    hssfCell = hssfRow.createCell(2);
                    hssfCell.setCellValue("Date and Time");



                    for (int j=1;j<coursesArrayList.size();j++)
                {
                    hssfRow = hssfSheet.createRow(j);
                    hssfCell = hssfRow.createCell(0);
                    hssfCell.setCellValue(coursesArrayList.get(j).longitude);
                    hssfCell = hssfRow.createCell(1);
                    hssfCell.setCellValue(coursesArrayList.get(j).latitude);
                    hssfCell = hssfRow.createCell(2);
                    hssfCell.setCellValue(coursesArrayList.get(j).dateAndTime);


                }






                try {
                    if (!filePath.exists()){
                        filePath.createNewFile();

                    }


                    FileOutputStream fileOutputStream= new FileOutputStream(filePath);
                    hssfWorkbook.write(fileOutputStream);

                    if (fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            }
                else Toast.makeText(Activity_Details.this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Activity_Details.this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(Activity_Details.this, "not granted", Toast.LENGTH_SHORT).show();
            // Requesting the permission
            ActivityCompat.requestPermissions(Activity_Details.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(Activity_Details.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}
