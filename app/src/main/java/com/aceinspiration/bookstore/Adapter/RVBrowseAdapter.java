package com.aceinspiration.bookstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aceinspiration.bookstore.BookAddActivity;
import com.aceinspiration.bookstore.Database.DatabaseHelper;
import com.aceinspiration.bookstore.Model.Book;

import java.util.List;

import static com.aceinspiration.bookstore.R.*;

public class RVBrowseAdapter extends RecyclerView.Adapter<RVBrowseAdapter.BrowseViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private Context context;
    private List<Book> bookList;
    private int itemPosition = 0;//id


    public RVBrowseAdapter(Context context, List<Book> bookList) {
        this.bookList = bookList;
        this.context = context;
    }

    @Override
    public BrowseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout.book_card, null);
        return new BrowseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrowseViewHolder holder, int position) {
        holder.bind(getItem(position), position);

    }

    private Book getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case id.item_edit:
                updateTodo(itemPosition);
                return true;
            case id.item_delete:
                deleteBook(itemPosition);
                return true;
            default:
                return false;
        }
    }

    private void updateTodo(int itemPosition) {



        Book book = getItem(itemPosition);
        Intent intent = new Intent(context, BookAddActivity.class);
        intent.putExtra("id", book.getId());
        intent.putExtra("name", book.getBook_name());
        intent.putExtra("description", book.getBook_description());
        intent.putExtra("author", book.getAuthor_name());
        intent.putExtra("genre", book.getGenre_name());
        intent.putExtra("publisher", book.getPublisher_name());
        intent.putExtra("publishing_date", book.getPublishing_date());
        intent.putExtra("price", book.getPrice());
        intent.putExtra("quantity", book.getQuantity());
        intent.putExtra("photo", book.getImagePath());
        context.startActivity(intent);

    }

    private void deleteBook(int itemPosition) {
        Book book = getItem(itemPosition);
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteBook(book.getId());
        bookList.remove(itemPosition);
        notifyItemRemoved(itemPosition);
        notifyDataSetChanged();
    }

    public class BrowseViewHolder extends RecyclerView.ViewHolder {

        private TextView BookName, AuthorName;
        private ImageView BookPhoto;
        private ImageView btn_popup;
        private Book book;

        public BrowseViewHolder(View itemView) {
            super(itemView);
            BookName = (TextView) itemView.findViewById(id.book_cover_name);
            BookPhoto = (ImageView) itemView.findViewById(id.book_cover_image);
            AuthorName = (TextView) itemView.findViewById(id.author_name);
            btn_popup = (ImageView) itemView.findViewById(id.btn_popup);

        }

        public void bind(final Book item, final int position) {
            this.book = item;
            BookName.setText(book.getBook_name());
            AuthorName.setText(book.getAuthor_name());
            if (BookPhoto != null) {
                BookPhoto.setImageDrawable(book.getThumbnail(context));
            }

            Log.d("Book", "price " + book.getPrice());

            btn_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemPosition = position;
                    PopupMenu popupMenu = new PopupMenu(context, btn_popup);
                    popupMenu.inflate(menu.menu_action);
                    popupMenu.setOnMenuItemClickListener(RVBrowseAdapter.this);
                    popupMenu.show();
                }
            });

            BookPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Clicked Book Position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}
