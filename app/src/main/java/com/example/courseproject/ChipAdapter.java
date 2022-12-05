package com.example.courseproject;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ViewHolder> {

    List<ChipModel> chipModelList;
    Context context;

    public ChipAdapter(Context context, List<ChipModel> chipModelList, ChipAdapter.ItemClickListener clickListener) {
        this.context=context;
        this.chipModelList = chipModelList;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ChipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.guest_chip_recyclerview, parent, false);

        return new ChipAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipAdapter.ViewHolder holder, int position) {

        holder.guestChip.setText(chipModelList.get(position).getTitle());

        holder.guestChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }



    @Override
    public int getItemCount() {
        return chipModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public Chip guestChip;

        public ViewHolder(View itemView) {
            super(itemView);

            guestChip=itemView.findViewById(R.id.chip);

        }
    }


    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onItemClick(int position);
    }
}
