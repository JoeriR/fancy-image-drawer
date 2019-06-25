package logic;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.GuiStage;

public class FancyDrawerMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new GuiStage();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
