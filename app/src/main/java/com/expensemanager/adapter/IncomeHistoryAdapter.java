package com.expensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.model.Income;
import com.expensemanager.utils.IncomeHistoryListener;
import com.expensemanager.viewholder.IncomeHistoryViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IncomeHistoryAdapter extends RecyclerView.Adapter<IncomeHistoryViewHolder> {

    private Context context;
    private List<Income> incomeList;
    private IncomeHistoryListener listener;

    public IncomeHistoryAdapter(Context context, List<Income> incomeList, IncomeHistoryListener listener) {
        this.context = context;
        this.incomeList = incomeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IncomeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomeHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.income_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeHistoryViewHolder holder, int position) {

        Income income = incomeList.get(position);

        holder.incomeParticularName.setText(income.getIncome_particular());
        holder.particularAmount.setText(String.valueOf(income.getIncome_amount()));
        holder.incomeType.setText(income.getIncome_account_type());
        holder.particularDate.setText(income.getShow_date());
        holder.particularTime.setText(income.getIncome_time());

        char firstLetter = income.getProfileLetter();
        holder.imgText.setText(String.valueOf(firstLetter));

        String imgTextLetter = holder.imgText.getText().toString();

        if (imgTextLetter.equals("A") || imgTextLetter.equals("a")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_1);
        }else if (imgTextLetter.equals("B") || imgTextLetter.equals("b")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_2);
        }else if (imgTextLetter.equals("C") || imgTextLetter.equals("c")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_3);
        }else if (imgTextLetter.equals("D") || imgTextLetter.equals("d")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_1);
        }else if (imgTextLetter.equals("E") || imgTextLetter.equals("e")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_2);
        }else if (imgTextLetter.equals("F") || imgTextLetter.equals("f")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_3);
        }else if (imgTextLetter.equals("G") || imgTextLetter.equals("g")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_4);
        }else if (imgTextLetter.equals("H") || imgTextLetter.equals("h")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_5);
        }else if (imgTextLetter.equals("I") || imgTextLetter.equals("i")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_6);
        }else if (imgTextLetter.equals("J") || imgTextLetter.equals("j")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_4);
        }else if (imgTextLetter.equals("K") || imgTextLetter.equals("k")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_5);
        }else if (imgTextLetter.equals("L") || imgTextLetter.equals("l")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_6);
        }else if (imgTextLetter.equals("M") || imgTextLetter.equals("m")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_7);
        }else if (imgTextLetter.equals("N") || imgTextLetter.equals("n")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_8);
        }else if (imgTextLetter.equals("O") || imgTextLetter.equals("o")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_9);
        }else if (imgTextLetter.equals("P") || imgTextLetter.equals("p")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_7);
        }else if (imgTextLetter.equals("Q") || imgTextLetter.equals("q")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_8);
        }else if (imgTextLetter.equals("R") || imgTextLetter.equals("r")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_9);
        }else if (imgTextLetter.equals("S") || imgTextLetter.equals("s")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_10);
        }else if (imgTextLetter.equals("T") || imgTextLetter.equals("t")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_1);
        }else if (imgTextLetter.equals("U") || imgTextLetter.equals("u")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_2);
        }else if (imgTextLetter.equals("V") || imgTextLetter.equals("v")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_10);
        }else if (imgTextLetter.equals("W") || imgTextLetter.equals("w")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_1);
        }else if (imgTextLetter.equals("X") || imgTextLetter.equals("x")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_2);
        }else if (imgTextLetter.equals("Y") || imgTextLetter.equals("y")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_3);
        }else if (imgTextLetter.equals("Z") || imgTextLetter.equals("z")){
            holder.imgText.setBackgroundResource(R.drawable.random_back_4);
        }

        holder.clearBtn.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.delete_history_layout, null);
            alert.setView(mView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(false);
            mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                listener.deleteIncomeSingleHistory(income);
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }
}
