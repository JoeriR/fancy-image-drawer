package ui;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class NumberFieldWrapper extends HBox {
	private final Label label;
	private final NumberField numberField;
	
	public NumberFieldWrapper(String labelText, String startValue) {
		label = new Label(labelText);
		numberField = new NumberField(startValue);
		
		label.setTextFill(Color.WHITE);
		
		this.getChildren().addAll(label, numberField);
	}
	
	public void setFieldText(String newText) {
		numberField.setText(newText);
	}
	
	public void setNumberFieldDisable(boolean value) {
		numberField.setDisable(value);
	}
	
	public String getStringValue() {
		return numberField.getText();
	}
	
	public long getLongValue() {
		return Long.parseLong(numberField.getText());
	}
	
	public double getDoubleValue() {
		return Double.parseDouble(numberField.getText());
	}
}
