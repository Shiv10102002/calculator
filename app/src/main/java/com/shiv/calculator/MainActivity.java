package com.shiv.calculator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.shiv.calculator.databinding.ActivityMainBinding;
import org.mariuszgromada.math.mxparser.Expression;
public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;
    String number = null;
    int countOpenpar =0,countClosepar =0;
    boolean operator = false,dotControl = false,buttonEqualControl = false,isDarkMode = false;

    String result ="";

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding  = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        sharedPreferences = getSharedPreferences("Theme", MODE_PRIVATE);
        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        getDelegate().setLocalNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        mainBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.dark_mode) {
                isDarkMode = !isDarkMode; // Toggle dark mode
                sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply();
                getDelegate().setLocalNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                // Update menu icon
                updateMenuIcon();
                return true;
            }
            return false;
        });
        mainBinding.textViewResult.setText("0");
        mainBinding.btn0.setOnClickListener(view -> {
            onNumberClicked("0");
        });
        mainBinding.btn1.setOnClickListener(view -> {
            onNumberClicked("1");
        });
        mainBinding.btn2.setOnClickListener(view -> {
            onNumberClicked("2");
        });
        mainBinding.btn3.setOnClickListener(view -> {
            onNumberClicked("3");
        });
        mainBinding.btn4.setOnClickListener(view -> {
            onNumberClicked("4");
        });
        mainBinding.btn5.setOnClickListener(view -> {
            onNumberClicked("5");
        });
        mainBinding.btn6.setOnClickListener(view -> {
            onNumberClicked("6");
        });
        mainBinding.btn7.setOnClickListener(view -> {
            onNumberClicked("7");
        });
        mainBinding.btn8.setOnClickListener(view -> {
            onNumberClicked("8");
        });
        mainBinding.btn9.setOnClickListener(view -> {
            onNumberClicked("9");
        });
        mainBinding.btnOpen.setOnClickListener(view -> {
           if( !dotControl){
               onParClicked("(");
               countOpenpar++;
           }
        });
        mainBinding.btnClose.setOnClickListener(view -> {
           if(!number.equals("(")){
               if(countOpenpar>countClosepar){
                   onParClicked(")");
                   countClosepar++;
               }
           }
        });
        mainBinding.btnPlus.setOnClickListener(view -> {
            if(!operator && !dotControl){
                if(number.isEmpty()){
                    number = "0+";

                }else if(buttonEqualControl){
                    number = result+ "+";
                }
                else {
                    number+= "+";
                }
                operator = true;
                mainBinding.textViewResult.setText(number);
                dotControl=true;
                buttonEqualControl=false;
            }
        });
        mainBinding.btnMinus.setOnClickListener(view -> {
            if(!operator && !dotControl){
                if(number.isEmpty()){
                    number = "0-";

                }else if(buttonEqualControl){
                    number = result+ "-";
                }
                else {
                    number+= "-";
                }
                operator = true;
                mainBinding.textViewResult.setText(number);
                dotControl=true;
                buttonEqualControl = false;
            }
        });

        mainBinding.btnDevide.setOnClickListener(view -> {
            if (!operator && !dotControl) {
                if (number.isEmpty()) {
                    number = "0/";

                }else if(buttonEqualControl){
                    number = result+ "/";
                }
                else {
                    number += "/";
                }
                operator = true;
                mainBinding.textViewResult.setText(number);
                dotControl=true;
                buttonEqualControl =false;
            }
        });

        mainBinding.btnMultie.setOnClickListener(view -> {
            if (!operator && !dotControl) {
                if (number.isEmpty()) {
                    number = "0*";

                } else if(buttonEqualControl){
                    number = result+ "*";
                }
                else {
                    number += "*";
                }
                operator = true;
                mainBinding.textViewResult.setText(number);
                dotControl=true;
                buttonEqualControl = false;
            }
        });
        mainBinding.btnDot.setOnClickListener(view -> {
            if(!dotControl && !operator){

              if(buttonEqualControl){
                  if(!result.contains(".")){
                      number = result+".";
                      mainBinding.textViewResult.setText(number);
                      dotControl = true;
                      operator=true;
                      buttonEqualControl = false;
                  }
              }else{
                  if(number == null){
                      number = "0.";
                      mainBinding.textViewResult.setText(number);
                      dotControl = true;
                      operator=true;
                  }
                  else {
                      // ✅ Check if the last character is ')'
                      if (number.endsWith(")")) {
                          return; // Prevent adding '.' after ')'
                      }
                      String expressionAfterlastOperator = "";
                      String lastCharactor ;
                      dotloop: for(int i= number.length()-1; i>=0;i--){
                          lastCharactor =String.valueOf( number.charAt(i));
                          if(lastCharactor.equals("+") || lastCharactor.equals("-") || lastCharactor.equals("*") || lastCharactor.equals("/")){
                              break dotloop;
                          }
                          expressionAfterlastOperator = lastCharactor + expressionAfterlastOperator;

                      }
                      if(!expressionAfterlastOperator.contains(".")){
                          number += ".";
                          mainBinding.textViewResult.setText(number);
                          dotControl = true;
                          operator=true;
                      }


                  }
              }

            }

        });

         mainBinding.btnAC.setOnClickListener(view -> {
              onButtonACClicked();
         });
         mainBinding.btnDel.setOnClickListener(view->{
             if(number == null|| number.length()==1){
                 onButtonACClicked();
             }else{
                 if(number.length()>1){
                     String lastCharactor = String.valueOf(number.charAt(number.length()-1));
                     if(lastCharactor.equals("+") || lastCharactor.equals("-") || lastCharactor.equals("*") || lastCharactor.equals("/")||lastCharactor.equals(".")){
                         operator = false;
                         dotControl = false;
                     }else if (lastCharactor.equals("(")){
                         countOpenpar--;
                     }else if (lastCharactor.equals(")")){
                         countClosepar--;
                     }
                     number = number.substring(0,number.length()-1);
                     mainBinding.textViewResult.setText(number);
                     lastCharactor= String.valueOf(number.charAt(number.length()-1));
                     if(lastCharactor.equals("+") || lastCharactor.equals("-") || lastCharactor.equals("*") || lastCharactor.equals("/") || lastCharactor.equals(".") ){
                         operator = true;
                         dotControl = true;
                     }
                 }
             }
         });
         mainBinding.btnEqual.setOnClickListener(view->{
             String expressionForCalculate = mainBinding.textViewResult.getText().toString();
             int parenthesisDiff = countOpenpar-countClosepar;

             if(parenthesisDiff>0){
                 for(int i=0;i<parenthesisDiff;i++){
                     expressionForCalculate+=")";
                 }
             }

             Expression expression = new Expression(expressionForCalculate);
             result= String.valueOf(expression.calculate());
             if(result.equals("NaN")){
                 checkDivisor(expressionForCalculate);
             }else{
                 int indexOfDot = result.indexOf(".");
                 String expressionAfterDot = result.substring(indexOfDot+1);
                 if(expressionAfterDot.equals("0")){
                     result = result.substring(0,indexOfDot);
                 }

                 mainBinding.textViewHistory.setText(expressionForCalculate.concat("=").concat(result));
                 mainBinding.textViewResult.setText(result);
                 buttonEqualControl=true;
                 operator=false;
                 dotControl=false;
                 countOpenpar=0;
                 countClosepar=0;
             }


         });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = this.getSharedPreferences("Theme", Context.MODE_PRIVATE);
        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);

        // Apply the theme
        getDelegate().setLocalNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Update menu icon
        updateMenuIcon();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String resultTextSave  = mainBinding.textViewResult.getText().toString();
        String historyTextToSave = mainBinding.textViewHistory.getText().toString();
        String resultToSave = result;
        String numberToSave = number;
        boolean equalToSave  = buttonEqualControl;
        boolean operatorToSave = operator;
        boolean dotControlToSave = dotControl;
        int countOpenParToSave = countOpenpar;
        int countCloseParToSave = countClosepar;
        editor.putString("resultText",resultToSave);
        editor.putString("historyText",historyTextToSave);
        editor.putString("number",numberToSave);
        editor.putBoolean("operator",operatorToSave);
        editor.putBoolean("dot",dotControlToSave);
        editor.putInt("countOpenPar",countOpenParToSave);
        editor.putInt("countClosePar",countCloseParToSave);
        editor.putBoolean("equal",equalToSave);
        editor.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainBinding.textViewResult.setText(sharedPreferences.getString("number","0"));
        mainBinding.textViewHistory.setText(sharedPreferences.getString("historyText","0"));
        result = sharedPreferences.getString("resultText","0");
        number = sharedPreferences.getString("number",null);
        operator = sharedPreferences.getBoolean("operator",false);
        dotControl = sharedPreferences.getBoolean("dot",false);
        buttonEqualControl = sharedPreferences.getBoolean("equal",false);
        countOpenpar = sharedPreferences.getInt("countOpenPar",0);
        countClosepar = sharedPreferences.getInt("countClosePar",0);
    }

    public void onNumberClicked(String clickedNum){
        if(number == null || buttonEqualControl){
            number = clickedNum;
        }else {
            number = number + clickedNum;
        }
        mainBinding.textViewResult.setText(number);
        operator=false;
        dotControl=false;
        buttonEqualControl=false;
    }
    public void onParClicked(String par){
        if(number == null || buttonEqualControl){
            number = par;
        }else {
            if(number.endsWith(".") && par.equals(")")){
                number = number + "0)";
            }else if(number.endsWith(".") && par.equals("(")){
                number = number;
            }else{
                number = number + par;
            }

        }
        mainBinding.textViewResult.setText(number);
        buttonEqualControl=false;
    }

    public void onButtonACClicked(){
        number = null;
        mainBinding.textViewResult.setText("0");
        mainBinding.textViewHistory.setText("0");
        operator=false;
        dotControl=false;
        countOpenpar=0;
        countClosepar=0;
        buttonEqualControl = false;
        result = "";
    }

    // Method to update the menu icon based on the theme
    private void updateMenuIcon() {
        MenuItem item = mainBinding.toolbar.getMenu().findItem(R.id.dark_mode);
        if (item != null) {
            item.setIcon(isDarkMode ? R.drawable.baseline__light_dark_mode_24 : R.drawable.baseline_dark_mode_24);
        }
    }

    public void checkDivisor(String expressionForCalculate){
        if(expressionForCalculate.contains("/")){
            int indexOfSlash = expressionForCalculate.indexOf("/");
            String expressionAfterSlash = expressionForCalculate.substring(indexOfSlash+1);
            if(expressionAfterSlash.contains(")")){
                int closingpar = 0,openingpar = 0;
                for(int i=0; i<expressionAfterSlash.length();i++){
                    String ispar = String.valueOf(expressionAfterSlash.charAt(i));
                    if(ispar.equals(")")){
                        closingpar++;
                    }else if(ispar.equals("(")){
                        openingpar++;
                    }

                }
                int pardif = closingpar-openingpar;
                if(pardif>0){
                    for(int i=0;i<pardif;i++){
                        expressionAfterSlash= "(".concat(expressionAfterSlash);
                    }

                }
            }
            Expression expression = new Expression(expressionAfterSlash);
            String newRes  = String.valueOf(expression.calculate());
            if(newRes.equals("0.0")){
                mainBinding.textViewHistory.setText("The divisor cannot be zero");
                buttonEqualControl = true;
            }else{
                checkDivisor(expressionAfterSlash);
            }

        }else{
            mainBinding.textViewHistory.setText("Syntex error");
            buttonEqualControl = true;
        }
    }
}