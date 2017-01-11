package com.aceinspiration.bookstore.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aceinspiration.bookstore.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstoreDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BOOKSTORE = "bookstore";
    private static final String ID = "id";
    private static final String BOOK_NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String AUTHOR = "author";
    private static final String GENRE = "genre";
    private static final String PUBLISHER = "publisher";
    private static final String PUBLISHING_DATE = "publishing_date";
    private static final String CREATED_DATE = "created_date";
    private static final String PRICE = "price";
    private static final String QUANTITY = "quantity";
    private static final String PHOTO = "photo";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_BOOKSTORE_TABLE = "CREATE TABLE " + TABLE_BOOKSTORE + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL UNIQUE , " +
                BOOK_NAME + " TEXT NOT NULL , " +
                DESCRIPTION + " TEXT NOT NULL , " +
                AUTHOR + " TEXT NOT NULL , " +
                GENRE + " TEXT NOT NULL , " +
                PUBLISHER + " TEXT NOT NULL , " +
                PRICE + " INTEGER NOT NULL , " +
                QUANTITY + " INTEGER NOT NULL , " +
                PUBLISHING_DATE + " DATETIME NOT NULL , " +
                PHOTO + " TEXT NOT NULL , " +
                CREATED_DATE + " DATETIME DEFAULT CURRENT_DATE)";

        sqLiteDatabase.execSQL(CREATE_BOOKSTORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKSTORE);
            onCreate(sqLiteDatabase);
        }
    }

    public long addTodo(Book books) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOK_NAME, books.getBook_name());
        values.put(DESCRIPTION, books.getBook_description());
        values.put(AUTHOR, books.getAuthor_name());
        values.put(GENRE, books.getGenre_name());
        values.put(PUBLISHER, books.getPublisher_name());
        values.put(String.valueOf(PRICE), books.getPrice());
        values.put(String.valueOf(QUANTITY), books.getQuantity());
        values.put(PUBLISHING_DATE, books.getPublishing_date());
        values.put(PHOTO, books.getImagePath());
        long rowID = db.insert(TABLE_BOOKSTORE, null, values);
        Log.i("row_id ", "" + rowID);

        return rowID;

    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BOOKSTORE + " ORDER BY id DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex(BOOK_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                String author = cursor.getString(cursor.getColumnIndex(AUTHOR));
                String genre = cursor.getString(cursor.getColumnIndex(GENRE));
                String publisher = cursor.getString(cursor.getColumnIndex(PUBLISHER));
                int price = cursor.getInt(cursor.getColumnIndex(PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                String publishing_date = cursor.getString(cursor.getColumnIndex(PUBLISHING_DATE));
                String image_path = cursor.getString(cursor.getColumnIndex(PHOTO));


                Book book = new Book(id, name, description, author, genre, publisher,price, quantity,publishing_date,image_path);
                books.add(book);

            } while (cursor.moveToNext());
        }
        return books;
    }

    public void deleteBook(int id) {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_BOOKSTORE, "id = ?", new String[]{
                String.valueOf(id)
        });
    }

    public int updateTodo(Book book) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOK_NAME, book.getBook_name());
        values.put(DESCRIPTION, book.getBook_description());
        values.put(AUTHOR, book.getAuthor_name());
        values.put(GENRE, book.getBook_name());
        values.put(PUBLISHER, book.getPublisher_name());
        values.put(String.valueOf(PRICE), book.getPrice());
        values.put(String.valueOf(QUANTITY), book.getQuantity());
        values.put(PUBLISHING_DATE, book.getPublishing_date());
        values.put(PHOTO, book.getImagePath());

        int count = db.update(TABLE_BOOKSTORE, values, "id = ?", new String[]{
                String.valueOf(book.getId())
        });

        return count;
    }
}
