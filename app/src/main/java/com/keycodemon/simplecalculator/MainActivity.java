package com.keycodemon.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    TextView tvTemp, tvInputResult;
    int[] buttonIDs = new int[]{R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                                    R.id.btnClear, R.id.btnSqrt, R.id.btnPercent, R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv, R.id.btnDot, R.id.btnResult};

    double firstNum = 0;
    char operator = '+';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        init();
    }


    private void init(){

        tvTemp = findViewById(R.id.tvTemp);
        tvInputResult = findViewById(R.id.tvInputResult);

        for(int i=0; i<buttonIDs.length; i++){
            Button btn = findViewById(buttonIDs[i]);
            btn.setOnClickListener(this);
            if(buttonIDs[i] == R.id.btnClear){
                btn.setOnLongClickListener(this);
            }
        }
    }

    private double getInputNumber(){
        return Double.parseDouble(getInput());
    }

    private String getInput(){
        return tvInputResult.getText().toString();
    }

    private String getButtonText(View view){
        return ((Button)view).getText().toString();
    }

    private void removeLastInputChar(){
        String input = getInput();
        if(input.length() == 1){
            tvInputResult.setText("0");
        }else{
            tvInputResult.setText(input.substring(0, input.length()-1));
        }
    }

    private void inputNumber(String number, String inputResultText){

        int num = Integer.valueOf(number);
        if(num == 0){
            if(getInput().contains(".") || getInputNumber() != 0){
                tvInputResult.setText(inputResultText);
            }
        }else{
            if(!getInput().contains(".") && getInputNumber() == 0){
                tvInputResult.setText(number);
            }else {
                tvInputResult.setText(inputResultText);
            }
        }
    }

    private void inputOperator(String buttonText){
        if(getInputNumber()!=0){
            if(firstNum!=0){
                calculate();
            }
            tvTemp.setText(getInput() + buttonText); // get new input
            firstNum = getInputNumber();
            operator = buttonText.charAt(0);
            tvInputResult.setText("0");
        }else if(firstNum != 0){
            operator = buttonText.charAt(0);
            if(firstNum - (long) firstNum == 0){
                tvTemp.setText((long) firstNum + buttonText);
            }else{
                tvTemp.setText(firstNum + buttonText);
            }
        }
    }

    private void calculate(){
        double result = 0;
        switch (operator){
            case '+':
                result = firstNum + getInputNumber();
                break;
            case '–':
                result = firstNum - getInputNumber();
                break;
            case '×':
                result = firstNum * getInputNumber();
                break;
            case '÷':
                result = firstNum / getInputNumber();
                break;
            default:
                break;
        }

        tvTemp.setText("");
        if(result - (long) result == 0){
            tvInputResult.setText(String.valueOf((long)result));
        }else{
            tvInputResult.setText(String.valueOf(result));
        }
        firstNum = 0;
        operator = '+';
    }

    @Override
    public void onClick(View view) {
        String inputText = getInput();
        String buttonText = getButtonText(view);
        String inputResultText = inputText + buttonText;

        switch (view.getId()){
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
                inputNumber(buttonText, inputResultText);
                break;
            case R.id.btnDot:
                if(!getInput().contains(".")){
                    tvInputResult.setText(inputResultText);
                }
                break;
            case R.id.btnAdd:
            case R.id.btnSub:
            case R.id.btnMul:
            case R.id.btnDiv:
                inputOperator(buttonText);
                break;
            case R.id.btnClear:
                removeLastInputChar();
                break;
            case R.id.btnSqrt:
                if(!inputText.contains(buttonText)){
                    if(getInputNumber() == 0){
                        tvInputResult.setText(buttonText);
                    }else{
                        tvInputResult.setText(inputResultText);
                    }
                }
                break;
            case R.id.btnPercent:

                break;
            case R.id.btnResult:
                calculate();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.btnClear:
                firstNum = 0;
                operator = '+';
                tvTemp.setText("");
                tvInputResult.setText("0");
                break;
            default:
                break;
        }
        return false;
    }
}