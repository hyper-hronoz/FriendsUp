package com.example.friendsup.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.friendsup.R;

import java.util.regex.Pattern;

public class FormValidator {

    private final Context context;
    public int min;
    public int max;
    public boolean isUpperCase;
    public boolean isSpecialSymbols;
    public boolean isNumber;
    public boolean isLetters;

    public String lengthMessages;
    public String lettersMessage;
    private String formValue;
    private String formName;
    private EditText editText;

    private boolean isValid = true;
    private String musntContainsNumbers;
    private String emptyMessage;
    private String incorrectEmail;
    private String mustContainsNumbers;
    private boolean isShowIncorrect = true;

    public void setColor(String color) {
        this.color = color;
    }

    public String color;

    public FormValidator(Context context, EditText editText, String formName) {
        this.formValue = editText.getText().toString().trim();
        this.context = context;
        this.editText = editText;
        this.min = 8;
        this.max = 99;
        this.isUpperCase = false;
        this.isSpecialSymbols = false;
        this.isNumber = false;
        this.isLetters = false;
        this.formName = formName;
        updateMessages();
        isEmpty();
    }


    public FormValidator(Context context, String formName) {
        this.formValue = editText.getText().toString().trim();
        this.context = context;
        this.min = 8;
        this.max = 99;
        this.isUpperCase = false;
        this.isSpecialSymbols = false;
        this.isNumber = false;
        this.isLetters = false;
        this.formName = formName;
        updateMessages();
        isEmpty();
    }

    public FormValidator isEmpty() {
        if (this.formValue.equals("")) {
            this.showError(editText, formName, this.emptyMessage);
            this.isValid = false;
        }
        return this;
    }

    public void updateMessages() {
        this.lengthMessages = "incorrect string length min: " + this.min + (this.max > this.min && this.max != 0 ? " max: " + this.max : "");
        this.lettersMessage = "field must includes letter characters";
        this.musntContainsNumbers = "field must not contains numbers";
        this.incorrectEmail = "field is not contains email";
        this.emptyMessage = "field is empty";
        this.mustContainsNumbers = "field must contains numbers";
    }

    public FormValidator isStrokeIncorrect(boolean v) {
        this.isShowIncorrect = v;
        return this;
    }

    public FormValidator isValidLength() {
        System.out.println(this.formValue);
        if (!(this.formValue.length() >= min && this.formValue.length() <= max)) {
            this.showError(editText, formName, this.lengthMessages);
            this.isValid = false;
        }
        return this;
    }

    public void validateNumber() {
        if (!Pattern.matches(".*[1-9]*.", this.formValue)) {
            this.showError(editText, formName, this.lettersMessage);
            this.isValid = false;
        }
    }

    public FormValidator isLetters() {
        if (!(this.formValue.matches(".*[a-zA-ZА-Яа-я].*"))) {
            this.showError(editText, formName, this.lettersMessage);
            this.isValid = false;
        }
        return this;
    }

    public FormValidator isContainsNumbers() {
        if (!this.formValue.matches(".*[0-9].*")) {
            this.showError(editText, formName, this.mustContainsNumbers);
            this.isValid = false;
        }
        return this;
    }

    public FormValidator isNotContainsNumbers() {
        if (this.formValue.matches(".*[0-9].*")) {
            this.showError(editText, formName, this.musntContainsNumbers);
            this.isValid = false;
        }
        return this;
    }

    public FormValidator isValidEmail() {
        String regex = "^(.+)@(.+)$";
        if (!this.formValue.matches(regex)) {
            this.showError(editText, formName, this.incorrectEmail);
            this.isValid = false;
        }
        return this;
    }

    public boolean commit() {
        if (isValid) {
            this.editText.setBackgroundResource(R.drawable.edit_text_login_sign_up);
        }
        return isValid;
    }

    public boolean isSpecialSymbols() {
        return Pattern.matches("[!@#$%^&*(),.?\":{}|<>]", this.formValue);
    }

    public void showError(EditText editText, String formName, String message) {
        Toast.makeText(this.context, "Incorrect field " + formName + " " + message, Toast.LENGTH_LONG).show();
        if (isShowIncorrect) {
            editText.setBackgroundResource(R.drawable.edit_text_error);
        }
    }

    public void showError(String formName, String message) {
        editText.setBackgroundResource(R.drawable.edit_text_error);
        Toast.makeText(this.context, "Incorrect field " + formName + " " + message, Toast.LENGTH_LONG).show();
    }

    public void setMax(int max) {
        this.max = max;
        this.updateMessages();
    }

    public void setLengthMessages(String lengthMessages) {
        this.lengthMessages = lengthMessages;
    }

    public FormValidator setMin(int min) {
        this.min = min;
        this.updateMessages();
        return this;
    }

    public void setUpperCase(boolean upperCase) {
        this.isUpperCase = upperCase;
    }

    public void setSpecialSymbols(boolean specialSymbols) {
        this.isSpecialSymbols = specialSymbols;
    }

    public void setNumber(boolean number) {
        this.isNumber = number;
    }

    public void setLetters(boolean letters) {
        this.isLetters = letters;
    }
}
