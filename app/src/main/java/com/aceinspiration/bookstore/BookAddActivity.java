package com.aceinspiration.bookstore;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aceinspiration.bookstore.Database.DatabaseHelper;
import com.aceinspiration.bookstore.Model.Book;
import com.aceinspiration.bookstore.Utility.Constants;
import com.aceinspiration.bookstore.Utility.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText book_name, book_description, author_name, genre_name, publisher_name, qty, book_price;
    private Button btn_add, btn_cancel, btnSelectPhoto, btn_asset_data;
    private ImageView preview;
    public static Button btn_publishing_date;
    private Spinner spinner_genre;
    public static List<Book> books = new ArrayList<>();
    DatabaseHelper dbHelper;
    static boolean isUpdate = false;
    private String mCurrentImagePath = null;
    private Uri mCapturedImageURI = null;
    private EditText editText;
    static int year, month, day;
    int id = 0;
    private String genreName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dbHelper = new DatabaseHelper(this);
        book_name = (EditText) findViewById(R.id.txt_book_name);
        book_description = (EditText) findViewById(R.id.txt_book_description);
        author_name = (EditText) findViewById(R.id.txt_author_name);
        publisher_name = (EditText) findViewById(R.id.txt_publisher_name);
        btn_publishing_date = (Button) findViewById(R.id.btn_publishing_date);
        btn_add = (Button) findViewById(R.id.btn_book_add);
        btn_cancel = (Button) findViewById(R.id.btn_book_cancel);
        qty = (EditText) findViewById(R.id.txt_book_qty);
        book_price = (EditText) findViewById(R.id.txt_book_price);
        spinner_genre = (Spinner) findViewById(R.id.genre_spinner);
        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        preview = (ImageView) findViewById(R.id.preview);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBookOnClick(view);
            }
        });

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        // Ensure there is a saved instance state.
        if (savedInstanceState != null) {

            // Get the saved Image uri string.
            String ImageUriString = savedInstanceState.getString(Constants.KEY_IMAGE_URI);

            // Restore the Image uri from the Image uri string.
            if (ImageUriString != null) {
                mCapturedImageURI = Uri.parse(ImageUriString);
            }
            mCurrentImagePath = savedInstanceState.getString(Constants.KEY_IMAGE_URI);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            id = extras.getInt("id");
            book_name.setText(extras.getString("name"));
            book_description.setText(extras.getString("description"));
            author_name.setText(extras.getString("author"));
            //genre_name.setText(extras.getString("genre"));
            genreName = extras.getString("genre");
            genre_name.setText(genreName);
            publisher_name.setText(extras.getString("publisher"));
            btn_publishing_date.setText(extras.getString("publishing_date"));
            book_price.setText(extras.getInt("price") + "");
            qty.setText(String.valueOf(extras.getInt("quantity")));
            preview.setImageDrawable(Drawable.createFromPath(extras.getString("photo")));
            mCurrentImagePath = extras.getString("photo");
            btn_add.setText("Update");
            isUpdate = true;


        }

        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.genre_array, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_genre.setAdapter(spinner_adapter);
        spinner_genre.setOnItemSelectedListener(this);

        if (!genreName.isEmpty()) {
            int spinnerPosition = spinner_adapter.getPosition(genreName);
            spinner_genre.setSelection(spinnerPosition);
        }

    }


    private void chooseImage() {

        if (book_name.getText() != null && !book_name.getText().toString().isEmpty()) {
            // Determine Uri of camera image to save.
            final File rootDir = new File(Constants.PICTURE_DIRECTORY);

            //noinspection ResultOfMethodCallIgnored
            rootDir.mkdir();

            //Get the book's name
            String bookName = book_name.getText().toString();

            //Remove all whitespace in bookname
            bookName.replaceAll("\\s+", "");

            //Use the book name to create the file name of the image that will be captured
            File file = new File(rootDir, FileUtils.generateImageName(bookName));
            mCapturedImageURI = Uri.fromFile(file);

            //Initialize a list to hold any camera application intents
            final List<Intent> cameraIntents = new ArrayList<Intent>();

            //Get the default captured camera intent
            final Intent capturedIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Get the package manager
            final PackageManager packageManger = getPackageManager();

            //Ensure that package manager is exists
            if (packageManger != null) {

                // Get all available image capture app activities.
                final List<ResolveInfo> listCam = packageManger.queryIntentActivities(capturedIntent, 0);

                //Create camera intent for all image capture app activities
                for (ResolveInfo res : listCam) {

                    //Ensure the activity info exists
                    if (res.activityInfo != null) {

                        //Get the activity's package name
                        final String PackageName = res.activityInfo.packageName;

                        // Create a new camera intent based on android's default capture intent.
                        final Intent intent = new Intent(capturedIntent);

                        //Set the intent data for the current image caputre app
                        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        intent.setPackage(getPackageName());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                        //At the intent to available camera intents.
                        cameraIntents.add(intent);

                    }
                }
            }

            //Create an intent to get pictures from the filesystem
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            //Chooser of filesystem options
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Picture");

            //Add the camera options
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

            //Start activity to choose or take the picture
            startActivityForResult(chooserIntent, Constants.ACTION_REQUEST_IMAGE);

        } else {
            book_name.setError("Please Enter Book Name");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            //Get the resultant image url
            final Uri selectedImageUri = (data == null) ? mCapturedImageURI : data.getData();

            //Ensure the image exists
            if (selectedImageUri != null) {

                //Add the image to gallery if this is an image captured with the camera
                //Otherwist no need to re-save to gallery if image already exists
                if (requestCode == Constants.ACTION_REQUEST_IMAGE) {

                    final Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(selectedImageUri);
                    sendBroadcast(mediaScanIntent);
                }

                mCurrentImagePath = FileUtils.getPath(getApplicationContext(), selectedImageUri);

                //Update Books picture
                if (mCurrentImagePath != null && !mCurrentImagePath.isEmpty()) {
                    preview.setImageDrawable(new BitmapDrawable(getResources(), FileUtils.getResizedBitmap(mCurrentImagePath, 512, 512)));
                }
            }
        }

    }

    public void showDatePickerDialog(View view) {

        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), "Date Picker");

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public Dialog onCreateDialog(Bundle saveInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if(isUpdate){

                String updated_date = btn_publishing_date.getText().toString();
                String[] dateparts = updated_date.split("/");
                year = Integer.parseInt(dateparts[0]);
                month = Integer.parseInt(dateparts[1])- 1;
                day = Integer.parseInt(dateparts[2]);

            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            btn_publishing_date.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    public void AddBookOnClick(View view) {
        if (!isValidate()) {
           return;
        }

        String name = book_name.getText().toString();
        String description = book_description.getText().toString();
        String auther = author_name.getText().toString();
        String publisher = publisher_name.getText().toString();
        String genre = String.valueOf(spinner_genre.getSelectedItem());
        int quantity = Integer.parseInt(String.valueOf(qty.getText()));
        int price = Integer.parseInt(String.valueOf(book_price.getText()));
        String date = btn_publishing_date.getText().toString();


        Book book = new Book();
        book.setBook_name(name);
        book.setBook_description(description);
        book.setAuthor_name(auther);
        book.setGenre_name(genre);
        book.setPublisher_name(publisher);
        book.setPublishing_date(date);
        book.setPrice(price);
        book.setQuantity(quantity);


        if (mCurrentImagePath != null && !mCurrentImagePath.isEmpty()) {
            book.setImagePath(mCurrentImagePath);
        }

        books.add(book);

        if (isUpdate) {
            book.setId(id);

            if (dbHelper.updateTodo(book) > 0) {
                Toast.makeText(this, "A New Book has been updated.", Toast.LENGTH_SHORT).show();
                finish();
            } else {

                Log.d("BookAddActivity", "No update");
            }
        } else {
            if (dbHelper.addTodo(book) > 0) {
                clearFormatData();
                Toast.makeText(this, "A New Book has been added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void clearFormatData() {
        book_name.setText("");
        book_description.setText("");
        author_name.setText("");
        publisher_name.setText("");
        book_price.setText("");
        qty.setText("");
        setCurrentDate();
    }


    private boolean isValidate() {
        boolean validate = true;
        if (TextUtils.isEmpty(book_name.getText().toString())) {
            validate = false;
            book_name.setError("Error");
        }
        if (TextUtils.isEmpty(book_description.getText().toString())) {
            validate = false;
            book_description.setError("Error");
        }
        if (TextUtils.isEmpty(author_name.getText().toString())) {
            validate = false;
            author_name.setError("Error");
        }
        if (TextUtils.isEmpty(publisher_name.getText().toString())) {
            validate = false;
            publisher_name.setError("Error");
        }
        if (TextUtils.isEmpty(book_price.getText().toString())) {
            validate = false;
            book_price.setError("Error");
        }
        if (TextUtils.isEmpty(qty.getText().toString())) {
            validate = false;
            qty.setError("Error");
        }
        if(mCurrentImagePath.isEmpty()){
            validate = false;
        }
        return validate;
    }


    private void setCurrentDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        btn_publishing_date.setText(year + "/" + (month + 1) + "/" + day);
    }

}


