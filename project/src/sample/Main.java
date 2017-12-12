package sample;

import com.sun.tools.javac.comp.Check;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.text.NumberFormat;

public class Main extends Application {

    public static final String applicationName  = "MDS CALCULATOR";
    public static final String defaultResult    = "?";
    public static TextField tfNum1;
    public static TextField tfNum2;
    public static Button btnClear;
    public static Label lblAnswer;
    public static Button btnDivide;
    public static Button btnMultiply;
    public static Button btnAddition;
    public static Button btnSubtraction;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        // -- CONTAINER
        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(50));
        // -- GRID
        HBox row1 = createHBox();
        HBox row2 = createHBox();
        HBox row3 = createHBox();

        // -- INIT BUTTON
        btnDivide               = createButton("/", true);
        btnMultiply             = createButton("*", true);
        btnAddition             = createButton("+", true);
        btnSubtraction          = createButton("-", true);
        btnClear                = createButton("Clear", false);
        btnClear.setOnMousePressed(handleOnClear());
        // -- INIT TEXT FIELD
        tfNum1                  = createTextField();
        tfNum2                  = createTextField();
        // -- INIT LABEL
        lblAnswer               = createLabel(defaultResult);

        // ------------------------- INIT -----------------------
        row1.getChildren().addAll(btnDivide, btnMultiply);
        row2.getChildren().addAll(btnAddition, btnSubtraction);
        row3.getChildren().addAll(tfNum1, tfNum2);

        mainContent.getChildren().addAll(
                row1,
                row2,
                row3,
                lblAnswer,
                btnClear
        );
        // ------------------------- .\ INIT -----------------------

        // --- Add the grid Pane to the scene
        Scene scene                 = new Scene(mainContent,300,300);

        primaryStage.setScene(scene);
        primaryStage.setTitle(applicationName);
        primaryStage.show();
    }

    /*
    * Create default HBox Object.
    *
    * @return HBox
    *
    */
    public HBox createHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(
                new Insets(0, 0,20, 0)
        );
        hbox.setSpacing(20);
        return hbox;
    }

    /*
    * Create default Button Object.
    * Call method @handleActionClick
    *
    * @param String value - Button text value.
    * @param Boolean isDisabled - Button state.
    * @return Button
    *
    */
    public Button createButton(String value, Boolean isDisabled) {
        Button btn = new Button();
        btn.setText(value);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnMousePressed(handleActionClick());
        if(isDisabled) {
            btn.setDisable(true);
        }
        // --- make btn fill 100%.
        HBox.setHgrow(btn, Priority.ALWAYS);
        return btn;
    }

    /*
    * Update the value "setDisable" on a given Boolean value
    * for all the ACTION_BUTTON.
    *
    */
    public void updateBtnState(Boolean newState) {
        btnDivide.setDisable(newState);
        btnAddition.setDisable(newState);
        btnMultiply.setDisable(newState);
        btnSubtraction.setDisable(newState);
    }

    /*
    * Create default TextField Object
    *
    * @Listener
    * Check the correct text format.
    * - Clear TextField is there an alphabetic character.
    * - Enabled ACTION_BUTTON if both TextField contains only Numbers.
    * - Disable ACTION_BUTTON if at least one TextField is empty.
    *
    * @return TextField
    */
    public TextField createTextField() {
        TextField txtF = new TextField();
        txtF.setMaxWidth(Double.MAX_VALUE);
        txtF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    updateBtnState(true);
                    txtF.setText(oldValue);
                } else if(!tfNum1.getText().equals("") && !tfNum2.getText().equals("") ) {
                    updateBtnState(false);
                } else {
                    updateBtnState(true);
                }
            }
        });
        HBox.setHgrow(txtF, Priority.ALWAYS);
        return txtF;
    }

    /*
    * Create default Label Bbject.
    *
    * @param String value - label value.
    * @return Label
    */
    public Label createLabel(String value) {
        Label lbl = new Label(value);
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setStyle("-fx-alignment:center;");
        lbl.setPadding(new Insets(0, 0, 20, 0));
        HBox.setHgrow(lbl, Priority.ALWAYS);
        return lbl;
    }

    /*
    * Handle On Mouse Pressed on Button(Clear)
    * Clear all TextField to empty value and reset label to "?".
    *
    */
    public EventHandler<javafx.scene.input.MouseEvent> handleOnClear() {
        return new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                tfNum1.setText("");
                tfNum2.setText("");
                lblAnswer.setText(defaultResult);
            }
        };
    }

    /*
    * Handle On Mouse Pressed on Button(+, -, *, /)
    * Call the @doArithmetic method.
    *
    */
    public EventHandler<javafx.scene.input.MouseEvent> handleActionClick() {
        return new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                // -- Get the Button. [ACTION_TYPE].
                Button btn = (Button) event.getSource();
                // -- Get value of TextField.
                double num1 = Double.parseDouble(tfNum1.getText());
                double num2 = Double.parseDouble(tfNum2.getText());

                String result = doArithmetic(
                        btn.getText(),
                        num1,
                        num2
                );
                lblAnswer.setText(result);
            }
        };
    }

    /*
    * Do arithmetic.
    *
    * @param String action - Action type
    * @param double num1 - First value
    * @param double num2 - Second value
    *
    * @return String - A formatted string result.
    *
    */
    public String doArithmetic(String action, double num1, double num2) {
        Double result = 0.0;
        switch (action) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "/":
                result = num1 / num2;
                break;
            case "*":
                result = num1 * num2;
                break;
        }
        return MessageFormat.format("{0} {1} {2} = {3}", num1, action, num2, result);
    }

}
