package com.expensemanager.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class IncomeHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView imgText, incomeParticularName, incomeType, particularDate, particularTime, particularAmount;
    public ImageView clearBtn;

    public IncomeHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        imgText = itemView.findViewById(R.id.imgText);
        incomeParticularName = itemView.findViewById(R.id.incomeParticularName);
        incomeType = itemView.findViewById(R.id.incomeType);
        particularAmount = itemView.findViewById(R.id.particularAmount);
        particularDate = itemView.findViewById(R.id.particularDate);
        particularTime = itemView.findViewById(R.id.particularTime);
        clearBtn = itemView.findViewById(R.id.clearBtn);

    }
}
