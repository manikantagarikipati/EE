package com.easyexpense.android.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easyexpense.android.R;
import com.easyexpense.commons.utils.StringUtils;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

/**
 * Created by Mani on 24/03/17.
 */

public class WidgetCalculator extends RelativeLayout implements View.OnClickListener{

    private double valueOne = Double.NaN;
    private double valueTwo;

    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';

    private static final char NONE='N';

    private char CURRENT_ACTION;

    private CalculatorInf calculatorInf;


    private ImageView ivComment;

    public WidgetCalculator(Context context) {
        super(context);
        initViews(context);
    }

    public WidgetCalculator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public WidgetCalculator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WidgetCalculator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    public void setCommentBackgroundColor(int color){
            ivComment.setColorFilter(color);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_calculator,this);
        ivComment = (ImageView) view.findViewById(R.id.btn_cal_comment);
        setCalcButtonClickListener(view);
    }

    private void setCalcButtonClickListener(View view) {

        view.findViewById(R.id.btn_cal_clear).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_comment).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_divide).setOnClickListener(this);

        view.findViewById(R.id.btn_cal_dot).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_eight).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_five).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_four).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_minus).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_camera).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_submit).setOnClickListener(this);

        view.findViewById(R.id.btn_cal_multiple).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_nine).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_one).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_plus).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_seven).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_six).setOnClickListener(this);

        view.findViewById(R.id.btn_cal_three).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_two).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_zero).setOnClickListener(this);
        view.findViewById(R.id.btn_cal_equals).setOnClickListener(this);

        view.findViewById(R.id.btn_cal_clear).setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetAllInfo();
                return false;
            }
        });
    }




    private void resetAllInfo() {
        calculatorInf.resetAllInfo();
        valueOne = 0.0d;
        valueTwo = 0.0d;
        CURRENT_ACTION = NONE;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_cal_clear:
                clearTextFromTv();
                break;
            case R.id.btn_cal_comment:
                calculatorInf.onCommentSelected();
                break;
            case R.id.btn_cal_camera:
                calculatorInf.onCameraSelected();
                break;
            case R.id.btn_cal_submit:
                calculatorInf.onSubmitSelected();
                break;
            case R.id.btn_cal_dot:

                String intermediateInfString = calculatorInf.getAmountText();
                if(!intermediateInfString.contains(".")){
                    setCalculatorInfo(".");
                }

                break;
            case R.id.btn_cal_eight:
                setCalculatorInfo("8");
                break;
            case R.id.btn_cal_five:
                setCalculatorInfo("5");
                break;
            case R.id.btn_cal_four:
                setCalculatorInfo("4");
                break;

            case R.id.btn_cal_nine:
                setCalculatorInfo("9");
                break;
            case R.id.btn_cal_one:
                setCalculatorInfo("1");
                break;

            case R.id.btn_cal_seven:
                setCalculatorInfo("7");
                break;
            case R.id.btn_cal_six:
                setCalculatorInfo("6");
                break;
            case R.id.btn_cal_three:
                setCalculatorInfo("3");
                break;
            case R.id.btn_cal_two:
                setCalculatorInfo("2");
                break;
            case R.id.btn_cal_zero:
                setCalculatorInfo("0");
                break;
            case R.id.btn_cal_divide:
                calculatorInf.setCurrentOperation("/");
                if(!Double.isNaN(valueOne) && CURRENT_ACTION != NONE){
                    computeSecondaryCalculation(DIVISION);
                }else{
                    CURRENT_ACTION = DIVISION;
                    setFirstDigit();
                }

                break;
            case R.id.btn_cal_minus:
                calculatorInf.setCurrentOperation("-");
                if(!Double.isNaN(valueOne) && CURRENT_ACTION != NONE){
                    computeSecondaryCalculation(SUBTRACTION);
                }else{
                    CURRENT_ACTION = SUBTRACTION;

                    setFirstDigit();
                }
                break;

            case R.id.btn_cal_multiple:
                calculatorInf.setCurrentOperation("*");

                if(!Double.isNaN(valueOne) && CURRENT_ACTION != NONE){
                    computeSecondaryCalculation(MULTIPLICATION);
                }else{
                    CURRENT_ACTION = MULTIPLICATION;

                    setFirstDigit();
                }


                break;
            case R.id.btn_cal_plus:
                calculatorInf.setCurrentOperation("+");

                if(!Double.isNaN(valueOne) && CURRENT_ACTION != NONE){
                    computeSecondaryCalculation(ADDITION);
                }else{
                    CURRENT_ACTION = ADDITION;
                    setFirstDigit();
                }

                break;

            case R.id.btn_cal_equals:
                calculatorInf.setCurrentOperation("");
                computeCalculation();
                break;
        }
    }

    private void computeSecondaryCalculation(char postOperation) {
        if(StringUtils.isNotEmpty(calculatorInf.getAmountText())){
            if(CURRENT_ACTION != NONE){
                double valueTwo = Double.parseDouble(calculatorInf.getAmountText());
                calculatorInf.clearAmount();

                switch (CURRENT_ACTION){
                    case ADDITION:
                        valueOne = this.valueOne + valueTwo;
                        setIntermediateInf();
                        break;
                    case SUBTRACTION:
                        valueOne = this.valueOne - valueTwo;
                        setIntermediateInf();
                        break;
                    case MULTIPLICATION:
                        valueOne = this.valueOne * valueTwo;
                        setIntermediateInf();
                        break;
                    case DIVISION:
                        valueOne = this.valueOne / valueTwo;
                        setIntermediateInf();
                        break;
                }

                CURRENT_ACTION = postOperation;

            }
        }
    }

    private void setIntermediateInf() {
        if(Double.isInfinite(valueOne)){
            resetAllInfo();
            Toast.makeText(getContext(),"Cannot perform Operation with Infinite",Toast.LENGTH_LONG).show();

        }else{
            String resultInf = new DecimalFormat("#.##").format(valueOne);
            calculatorInf.setExpression1(resultInf);
            calculatorInf.clearAmount();
        }
    }

    private void clearTextFromTv() {
        final Editable formulaText = calculatorInf.getAmountEditText().getEditableText();
        final int formulaLength = formulaText.length();
        if (formulaLength > 0) {
            formulaText.delete(formulaLength - 1, formulaLength);
        }
    }


    private void setCalculatorInfo(String text){
        calculatorInf.setAmount(calculatorInf.getAmountText()+text);

        calculatorInf.getAmountEditText().setSelection(calculatorInf.getAmountText().length());
    }

    private void setResultInf(){
        CURRENT_ACTION = NONE;
        if(Double.isInfinite(valueOne)){
            resetAllInfo();
            Toast.makeText(getContext(),"Cannot perform Operation with Infinite",Toast.LENGTH_LONG).show();

        }else{
            String resultInf = new DecimalFormat("#.##").format(valueOne);
            calculatorInf.setExpression1("");
            calculatorInf.setAmount(resultInf);
            calculatorInf.getAmountEditText().setSelection(resultInf.length());
        }
    }

    private void setFirstDigit(){
        try {
            valueOne = Double.parseDouble(calculatorInf.getAmountText());
            calculatorInf.setExpression1(calculatorInf.getAmountText());
            calculatorInf.clearAmount();
        }
        catch (Exception e){}
    }
    private void computeCalculation() {
        try{
            if(!Double.isNaN(valueOne)) {
                if(StringUtils.isNotEmpty(calculatorInf.getAmountText())){
                    if(CURRENT_ACTION != NONE){
                        valueTwo = Double.parseDouble(calculatorInf.getAmountText());
                        calculatorInf.clearAmount();

                        switch (CURRENT_ACTION){
                            case ADDITION:
                                valueOne = this.valueOne + valueTwo;
                                setResultInf();
                                break;
                            case SUBTRACTION:
                                valueOne = this.valueOne - valueTwo;
                                setResultInf();
                                break;
                            case MULTIPLICATION:
                                valueOne = this.valueOne * valueTwo;
                                setResultInf();
                                break;
                            case DIVISION:
                                valueOne = this.valueOne / valueTwo;
                                setResultInf();
                                break;
                        }
                    }
                }else{
                    setResultInf();
                }

            }
            else {
                valueOne = Double.parseDouble(calculatorInf.getAmountText());
                calculatorInf.setExpression1(calculatorInf.getAmountText());
            }
        }catch (Exception e){
            Log.e(TAG,"Cannot Perform Operation");
        }
    }

    public void setCalcInf(CalculatorInf calcInf){
        this.calculatorInf = calcInf;
    }

    public interface CalculatorInf {

        void setCurrentOperation(String currentOperation);

        void setExpression1(String expression1);

        void setAmount(String amount);

        void resetAllInfo();

        EditText getAmountEditText();

        String getAmountText();

        void clearAmount();

        void onCommentSelected();

        void onSubmitSelected();

        void onCameraSelected();
    }
}
