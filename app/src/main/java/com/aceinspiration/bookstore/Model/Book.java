package com.aceinspiration.bookstore.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.aceinspiration.bookstore.Utility.Constants;
import com.aceinspiration.bookstore.Utility.FileUtils;

public class Book {

    private int id;
    private String book_name;
    private String book_description;
    private String author_name;
    private String genre_name;
    private String publisher_name;
    private int quantity, price;
    private String publishing_date;
    private String imagePath;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublishing_date() {
        return publishing_date;
    }

    public void setPublishing_date(String publishing_date) {
        this.publishing_date = publishing_date;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_description() {
        return book_description;
    }

    public void setBook_description(String book_description) {
        this.book_description = book_description;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean hasImage() {
        return getImagePath() != null && !getImagePath().isEmpty();
    }

    /**
     * Get a thumbnail of this profile's picture, or a default image if the profile doesn't have a
     * Image.
     *
     * @return Thumbnail of the profile.
     */
    public Drawable getThumbnail(Context context) {
        return getScaledImage(context, 512, 512);
    }

    /**
     * Get a scaled version of this profile's Image, or a default image if the profile doesn't have
     * a Image.
     *
     * @return Image of the profile.
     */
    private Drawable getScaledImage(Context context, int reqWidth, int reqHeight) {

        // if book has an image
        if (hasImage()) {

            // Decode input stream into bitmap
            Bitmap bitmap = FileUtils.getResizedBitmap(getImagePath(), reqWidth, reqHeight);

            // if bitmap has successfully created
            if (bitmap != null) {

                // Return a drawable representation of the bitmap.
                return new BitmapDrawable(context.getResources(), bitmap);
            }
        }
        // Return the default image drawable.
        return context.getResources().getDrawable(Constants.DEFAULT_IMAGE_RESOURCE);
    }


    public Book(int id, String book_name, String book_description, String author_name, String genre_name, String publisher_name, int price, int quantity, String publishing_date, String imagePath) {

        this.id = id;
        this.book_name = book_name;
        this.book_description = book_description;
        this.author_name = author_name;
        this.genre_name = genre_name;
        this.publisher_name = publisher_name;
        this.price = price;
        this.quantity = quantity;
        this.publishing_date = publishing_date;
        this.imagePath = imagePath;

    }

    public Book() {
    }


}
