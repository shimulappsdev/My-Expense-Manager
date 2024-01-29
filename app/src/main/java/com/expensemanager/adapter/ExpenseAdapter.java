package com.expensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.model.Expense;
import com.expensemanager.utils.ExpenseUpdateListener;
import com.expensemanager.viewholder.ExpenseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private Context context;
    private List<Expense> expenseList;
    private ExpenseUpdateListener listener;

    public ExpenseAdapter(Context context, List<Expense> expenseList, ExpenseUpdateListener listener) {
        this.context = context;
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenseViewHolder(LayoutInflater.from(context).inflate(R.layout.expense_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        Expense expense = expenseList.get(position);

        holder.particularName.setText(expense.getExpense_particular());
        holder.particularDate.setText(expense.getShow_date());
        holder.particularTime.setText(expense.getExpense_time());

        int particularAmount = expense.getExpense_amount();
        holder.particularAmount.setText(String.valueOf(particularAmount));

        char firstLetter = expense.getProfileLetter();
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

        holder.optionBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.optionEdit:
                            listener.ExpenseUpdate(expense);
                            break;

                        case R.id.optionDelete:
                            deleteIncome();
                            break;
                    }
                    return true;
                }

                private void deleteIncome() {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.delete_layout, null);
                    alert.setView(mView);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                        alertDialog.dismiss();
                        popupMenu.dismiss();
                    });
                    mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                        listener.ExpenseDelete(expense);
                        alertDialog.dismiss();
                        popupMenu.dismiss();
                    });
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
