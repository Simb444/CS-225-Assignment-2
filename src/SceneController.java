/**
 * @author Christian Rudder
 * 2/25/2022
 */

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

/**
 * This class is responsible for controlling the scene elements
 */
public class SceneController implements Initializable {
	// choice box in menu
	@FXML
	private ChoiceBox<String> boardSizes;
	//
	private String[] sizes = {"3x4"};
	// clues box in during game
	@FXML
	private TextArea myCluesView;
	// start menu button
	@FXML
	private Button startButton;

	// board pane that contains the game board
	@FXML
	private BorderPane boardArea;

	// the game board
	@FXML
	private Board playerBoard;

	// hint button
	@FXML
	private Button hint;

	// clear errors button
	@FXML
	private Button clearErrors;

	// start over button
	@FXML
	private Button startOver;

	// border pane that contains the entire scene
	@FXML
	private BorderPane scenePane;

	@FXML
	private SplitPane topPane;

	@FXML
	private SplitPane leftPane;


	// String[] clues = {"There are jomba beans under 4th street","perhaps invest in a fridge","there are two animals that are egg","how many waters should you drink, yes!", "Survey says: what the dog doing?"};


	private static SceneController instance;
	private Stage stage;
	private Scene scene;
	private Parent root;


	public SceneController(){
		instance = this;
	}

	public static SceneController getInstance(){
		return instance;
	}

	/**
	 * Responsible for updates made after the initial render
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		GameLogic.newInstance(new Board(3, 4));
		if (boardSizes != null) {
			boardSizes.getItems().addAll(sizes);
			boardSizes.setOnAction(this::setSize);
		}

		if (myCluesView != null) {

			boardArea.setCenter(GameLogic.getInstance().getBoard());
			myCluesView.setText(clueCompile(GameLogic.getInstance().clues(5)));
			setHeaderCells();
		}
	}

	public void setHeaderCells(){
		/**
		 * TopPane:
		 * 		Split pane
		 * 			BorderPane (cell)
		 * 			BorderPane (cell)
		 * 				Vbox (header TOP) 
		 * 					Label (category)
		 * 				HBox (fields CENTER)
		 * 					HBox
		 * 						Label
		 * 					HBox
		 * 					HBox
		 * 					HBox
		 * 
		 * LeftPane:
		 *  	Split pane
		* 			BorderPane (cell)
		* 			BorderPane (cell)
		* 				Hbox (header LEFT)
		* 					Label (category)
		* 				VBox (fields CENTER)
		* 					VBox
								Label
		* 					VBox
		* 					VBox
		* 					VBox			
		*/

		/**
		 * Label subjects[] = new Label[labels.length - 1]; // ist of all labels to use
					Label cat = new Label(labels[0]); // creating label for category
					cat.setRotate(-90);


					VBox rows = new VBox(25); // rows to go down
					rows.setPadding(new Insets(0, 0, 0, 3));

					// adding labels to the subject list to populate the rows
					for(int i = 0; i < subjects.length; i++) {
							 Label entry = new Label(labels[i + 1]);
							 subjects[i] = entry;
					}
		 */

		String[][] boardInfo = GameLogic.getInstance().getBoard().getCollection(); //gets board information
		ArrayList<Node> Hcells = new ArrayList<>(topPane.getItems());//gets top headers
		for(int i = 0; i<Hcells.size();i++) {
			((Label)(((VBox)(((BorderPane) Hcells.get(i)).getTop())).getChildren().get(0))).setText(boardInfo[i][0]);// sets header horizontal

			ArrayList<Node> fields = new ArrayList<>(((HBox)((BorderPane) Hcells.get(i)).getCenter()).getChildren()); //gets horizontal labels
			for(int j = 0; j< fields.size();j++ ) {
				((Label)((HBox) fields.get(j)).getChildren().get(0)).setText(boardInfo[i][j+1]);
			}
			
		}

		ArrayList<Node> Vcells = new ArrayList<>(leftPane.getItems());// gets vertical headers
		int header = 2;
		for(int i = 0; i<Vcells.size();i++) {
			((Label)(((HBox)(((BorderPane) Vcells.get(i)).getLeft())).getChildren().get(0))).setText(boardInfo[header][0]);// sets header vertical

			ArrayList<Node> fields = new ArrayList<>(((VBox)((BorderPane) Vcells.get(i)).getCenter()).getChildren()); //gets vertical labels
			for(int j = 0; j< fields.size();j++ ) {
				((Label)((VBox) fields.get(j)).getChildren().get(0)).setText(boardInfo[header][j+1]);
			}
			header--;
		}
	}

	/**
	 * @return the format for which clues should be displayed
	 * formats strings to be presented in clues box
	 */
	public String clueCompile(String[] clues) {
		

			String cluesText = "\n";
			for(String clue : clues) {
					cluesText += " • " + clue + "\n\n";
			}
			return cluesText;
	}

 /**
	 *
	 * @param event action event triggered by the main menu button
	 * @throws IOException for not finding the next scene file
	 * responsible for switching to the game view scene
	 */
	public void switchToSceneGame(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("PlayArea.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Logic Game");
		stage.show();
	}

	/**
	 *
	 * @param event fired when size is picked from choice box
	 * enables the start game button upon board choice
	 */
	public void setSize(ActionEvent event){
			//could be utilized in future iterations to change size
			String chosenSize = boardSizes.getValue();
			startButton.setDisable(false);
	}

	/**
	 *
	 * @param event fires from hint button
	 * creates a hint for the player
	 */
	public void hint(ActionEvent event){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Hint"); // <----code goes here

		alert.setHeaderText("Please you deodarant <3");
		alert.setContentText("click 'ok' to close:");
		alert.showAndWait();
	}

	/**
	 *
	 * @param event fires from clear errors button
	 * clears button errors
	 */
	public void clearErrors(ActionEvent event) {
			// code goes here
	}

	/**
	 * @param event fires from start over button
	 * clears the whole board
	 */
	public void startOver(ActionEvent event) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Start Over?");
			alert.setHeaderText("Do you want to clear your board?");
			alert.setContentText("clicking 'ok' will wipe the board contents:");

			if(alert.showAndWait().get() == ButtonType.OK) {
					Board board = GameLogic.getInstance().getBoard();
					GameLogic.newInstance(new Board(board.size(), board.gridSize()));
					boardArea.setCenter(GameLogic.getInstance().getBoard());
			}
	}

	/**
	 *
	 * @param event fired from winning move
	 * alert message to tell the player they have won the game.
	 */
	public void alert() {

		// win alert
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Winner Winner Chicken Dinner ! ! ! ! ! ! !");
		alert.setHeaderText("Congradulations you've won!");
		alert.setContentText("You've completed the game (clicking 'ok' will close the game):");

		if(alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
				stage.close();
		}
	}

	/**
	 *
	 * @param event fired by the undo event
	 * undos the last move
	 */
	public void undo(ActionEvent event){

			//code goes here
	}
}