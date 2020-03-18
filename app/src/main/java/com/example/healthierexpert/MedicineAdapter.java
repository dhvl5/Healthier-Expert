package com.example.healthierexpert;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder>
{
    private ArrayList<Medicine> medicineList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void OnItemClick(int position, View v, MyViewHolder holder);
    }

    void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    private OnItemLongClickListener mLongListener;

    public interface OnItemLongClickListener
    {
        void OnLongClick(int position, MyViewHolder holder);
    }

    void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        mLongListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        MaterialTextView noteTitle;
        CardView noteCard;
        MaterialTextView noteTimeText;

        MyViewHolder(final View view)
        {
            super(view);
            noteTitle = view.findViewById(R.id.note_title);
            noteCard = view.findViewById(R.id.cv);
            noteTimeText = view.findViewById(R.id.note_time_txt);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            mListener.OnItemClick(position,  noteCard, MyViewHolder.this);
                        }
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mLongListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            mLongListener.OnLongClick(position, MyViewHolder.this);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public ArrayList<Medicine> getAll()
    {
        return medicineList;
    }

    ArrayList<Medicine> getSelected()
    {
        ArrayList<Medicine> selected = new ArrayList<>();
        for(int i = 0; i < medicineList.size(); i++)
        {
            if(medicineList.get(i).isChecked())
                selected.add(medicineList.get(i));
        }
        return selected;
    }

    MedicineAdapter(Context context, ArrayList<Medicine> medicineList)
    {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Medicine note = medicineList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteTimeText.setText(note.getCheckedBoxes());

        //holder.noteTimeText.setText();

        if(getSelected().isEmpty())
            holder.noteCard.setCardBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}
