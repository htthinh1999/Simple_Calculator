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
    boolean equalClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        init();
    }

    /**
     * Initial all views
     */
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

    /**
     * Get input number from tvInputResult
     * @return double from tvInputResult
     */
    private double getInputNumber(){
        String inputText = getInput().replace("–", "-");
        if(inputText.equals("-")){
            return -1;
        }
        if(inputText.contains("√")){
            int sqrtPos = inputText.indexOf("√");

            // √3 -> previousSqrtNum = 1, -√3 -> previousSqrtNum = -1
            // √ -> afterSqrtNum = 1
            double previousSqrtNum = (sqrtPos == 0) ? 1:((inputText.substring(0, sqrtPos).equals("-"))? -1:Double.parseDouble(inputText.substring(0, sqrtPos)));
            double afterSqrtNum = (sqrtPos == inputText.length()-1) ? 1:Math.sqrt(Double.parseDouble(inputText.substring(sqrtPos + 1)));
            return previousSqrtNum * afterSqrtNum;
        }
        return Double.parseDouble(inputText);
    }

    /**
     * Get input String from tvInputResult
     * @return String from tvInputResult
     */
    private String getInput(){
        return tvInputResult.getText().toString();
    }

    /**
     * Get text from button
     * @param view: Button clicked
     * @return: String from button text
     */
    private String getButtonText(View view){
        return ((Button)view).getText().toString();
    }

    /**
     * Remove last character from tvInputResult
     */
    private void removeLastInputChar(){
        String input = getInput();
        if(input.length() == 1){
            tvInputResult.setText("0");
        }else{
            tvInputResult.setText(input.substring(0, input.length()-1));
        }
    }

    /**
     * Add number [0-9] from button to show on tvInputResult
     * @param number: Number on button
     * @param inputResultText: Result to show on tvInputResult
     */
    private void inputNumber(String number, String inputResultText){

        if(equalClicked){
            if(getInputNumber()!=0){
                tvInputResult.setText(getInput() + number);
            }else{
                tvInputResult.setText(number);
            }
            equalClicked = false;
        }else{
            if(!inputResultText.contains("√")){
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
            }else{
                int num = Integer.valueOf(number);
                if(num == 0){
                    if(getInput().contains(".") || inputResultText.charAt(inputResultText.length()-2) != '√'){
                        tvInputResult.setText(inputResultText);
                    }
                }else{
                    tvInputResult.setText(inputResultText);
                }
            }
        }
    }

    /**
     * Click event for button with operator (+, –, ×, ÷)
     * @param buttonText: String from button text
     */
    private void inputOperator(String buttonText){
        if (getInputNumber() != 0) {
            if (firstNum != 0) {
                calculate();
            }
            if(getInput().equals("√")){
                tvTemp.setText("√1" + buttonText);
            }else{
                tvTemp.setText(getInput() + buttonText); // get new input
            }
            firstNum = getInputNumber();
            operator = buttonText.charAt(0);
            tvInputResult.setText("0");
            if(firstNum == 0){tvTemp.setText("");}
        } else if (buttonText.equals("–")) {
            tvInputResult.setText(buttonText);
        } else if (firstNum != 0) {
            operator = buttonText.charAt(0);
            outputFix(tvTemp, firstNum, buttonText);
        }
    }

    /**
     * Click event for button (., √, %)
     * @param buttonText: String from button text
     * @param inputText: String from tvInputResult
     * @param inputResultText: Result to show on tvInputResult
     */
    private void inputSymbol(String buttonText, String inputText, String inputResultText){
        switch (buttonText){
            case ".":
                if(!getInput().contains(".")){
                    tvInputResult.setText(inputResultText);
                }
                break;
            case "√":
                if(!inputText.contains(buttonText)){
                    if(getInputNumber() == 0){
                        tvInputResult.setText(buttonText);
                    }else{
                        tvInputResult.setText(inputResultText);
                    }
                }
                break;
            case "%":
                double numerPercent = getInputNumber()/100;
                outputFix(tvInputResult, numerPercent, "");
                break;
        }
    }

    /**
     * Calculate operation
     */
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
        outputFix(tvInputResult, result, "");
        firstNum = 0;
        operator = '+';
    }

    /**
     * Fix output = 0.9999999999999 -> 1 or -1.0 -> -1
     * @param tv: TextView to show (tvInputResult, tvTemp)
     * @param number: Number to show
     * @param buttonText: Add operator (+, –, ×, ÷) after number for tvTemp, empty for tvInputResult
     */
    private void outputFix(TextView tv, double number, String buttonText){
        if(number >= 0){
            if(Math.round((number - (long) (number + 0.000000000001)) * 10000000000.0)/10000000000.0 == 0){
                tv.setText(String.valueOf((long) (number + 0.0000000001)) + buttonText);
            }else{
                tv.setText(String.valueOf(number));
            }
        }else{
            if(Math.round((number - (long) (number - 0.000000000001)) * 10000000000.0)/10000000000.0 == 0){
                tv.setText(String.valueOf((long) (number - 0.0000000001)) + buttonText);
            }else{
                tv.setText(String.valueOf(number));
            }
        }

    }

    @Override
    public void onClick(View view) {
        String inputText = getInput();
        String buttonText = getButtonText(view);
        String inputResultText = inputText + buttonText;

        switch (view.getId()){
            case R.id.btn0: case R.id.btn1: case R.id.btn2: case R.id.btn3: case R.id.btn4:
            case R.id.btn5: case R.id.btn6: case R.id.btn7: case R.id.btn8: case R.id.btn9:
                inputNumber(buttonText, inputResultText);
                break;
            case R.id.btnAdd: case R.id.btnSub: case R.id.btnMul: case R.id.btnDiv:
                inputOperator(buttonText);
                break;
            case R.id.btnDot: case R.id.btnSqrt: case R.id.btnPercent:
                inputSymbol(buttonText, inputText, inputResultText);
                break;
            case R.id.btnClear:
                removeLastInputChar();
                break;
            case R.id.btnResult:
                calculate();
                equalClicked = true;
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