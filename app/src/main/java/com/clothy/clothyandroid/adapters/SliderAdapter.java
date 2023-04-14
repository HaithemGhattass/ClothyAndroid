package com.clothy.clothyandroid.adapters;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clothy.clothyandroid.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;

    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");
        String lastname = sharedPreferences.getString("lastname", "");
        String gender = sharedPreferences.getString("gander", "");
        String phone = sharedPreferences.getString("phone", "");
        String pseudo = sharedPreferences.getString("pseudo", "");
        switch (position) {
            case 0:
                viewHolder.mo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do something when the TextView is clicked
                        Log.d("MyAdapter", "TextView clicked at position " + position);
                    }
                });
                viewHolder.textViewTitle.setText(firstname);
                viewHolder.textViewDescription.setText(lastname);
                viewHolder.pseudoViewTitle.setText(pseudo);


                break;
            case 1:

                viewHolder.textViewTitle.setText(gender);
                viewHolder.textViewDescription.setText(phone);
                viewHolder.pseudoViewTitle.setVisibility(View.INVISIBLE);
                break;
            case 2:

                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_3));
                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_3));
                break;

            case 3:

                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_4));
                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_4));
                break;

            case 4:

                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_5));
                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_5));
                break;

            case 5:

                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_6));
                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_6));
                break;
        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 6;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        TextView mo;
        View itemView;
        ImageView imageViewSlider;
        EditText textViewDescription, textViewTitle,pseudoViewTitle;

        SliderAdapterVH(View itemView) {
            super(itemView);
            mo = itemView.findViewById(R.id.edit_profile);
            textViewTitle = itemView.findViewById(R.id.text_slider_title);
            textViewDescription = itemView.findViewById(R.id.text_slider_description);
            pseudoViewTitle = itemView.findViewById(R.id.text_slider_pseudo);
            this.itemView = itemView;
        }
    }

}
