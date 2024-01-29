package com.expensemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.expensemanager.model.User;

@Dao
public interface UserDao {
    @Insert
    void registerUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users WHERE user_email = :email AND user_password = :password")
    LiveData<User> loginUser(String email, String password);

    @Query("SELECT * FROM users WHERE security_question = :question AND security_answer = :answer")
    User getUserForRetrievePassword(String question, String answer);

    @Query("SELECT * FROM Users")
    User getUser();
}
