package com.expensemanager.utils;

import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.expensemanager.R;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.model.Expense;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyWorker extends Worker {
    private Context context;
    private String today;

    public MyWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @Override
    public Result doWork() {
        Date currentDateAndTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        today = dateFormat.format(currentDateAndTime);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Expense> expenseList = ExpenseDatabase.getInstance(context).expenseDao().getAllExpenseOfToday(today);
            int expenseAmount = calculateTotalExpenses(expenseList);
            String notificationTitle = "Today's Expense";
            String notificationText = "Today (" + today + ") you expended total \u09F3" + expenseAmount;
            createAndShowNotification(notificationTitle, notificationText);
        });

        return Result.success();
    }

    private int calculateTotalExpenses(List<Expense> expenses) {
        int totalAmount = 0;
        for (Expense expense : expenses) {
            totalAmount += expense.getExpense_amount();
        }
        return totalAmount;
    }

    private void createAndShowNotification(String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }
}
