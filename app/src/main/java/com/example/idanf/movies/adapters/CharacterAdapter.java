package com.example.idanf.movies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idanf.movies.R;
import com.example.idanf.movies.models.Character;
import com.example.idanf.movies.utils.DownloadImageTask;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private final ArrayList<Character> characters;

    public CharacterAdapter(ArrayList<Character> characters) {
        this.characters = characters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Character character = characters.get(position);
        holder.name.setText(character.name);

        if (character.bitMap == null) {
            if (character.imagePath != null && !character.imagePath.equals("null")) {
                new DownloadImageTask(holder.imageView, character).execute("https://image.tmdb.org/t/p/w185//" + character.imagePath);
            } else {
                holder.imageView.setImageResource(R.mipmap.no_image_found);
            }
        } else {
            holder.imageView.setImageBitmap(character.bitMap);
        }

    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView name;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.characterImage);
            name = view.findViewById(R.id.characterName);
        }
    }
}
