package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {

	VBox mainVBox;
	Scene scene;

	MenuBar mainMenuBar;
	Menu fileMenu;
	MenuItem openFileMenuItem;
	MenuItem saveFileMenuItem;
	MenuItem closeFileMenuItem;
	Menu editMenu;
	Menu helpMenu;

	// Grid Pane (Filters and the table)
	GridPane mainGridPane;
	TitledPane filtersTP;

	// Filters section
	HBox hbFilters;
	RadioButton rbAll;
	RadioButton rbOverdue;
	RadioButton rbToday;
	RadioButton rbThisweek;
	CheckBox cbNotCompleted;

	// Table of tasks
	TableView<Task> tvTasks;
	CheckBox cbDone;
	TableColumn<Task, Boolean> doneCol;

	TableColumn<Task, String> dateCol;
	TableColumn<Task, String> titleCol;
	TableColumn<Task, Integer> percentCol;
	TableColumn<Task, String> descriptionCol;

	ObservableList<Task> tasks;

	FileChooser fileChooser;
	File chosenFile;

	@Override
	public void start(Stage primaryStage) {
		try {
			// Main VerticalBox
			mainVBox = new VBox();
			scene = new Scene(mainVBox, 1000, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Main Menu Bar settings
			mainMenuBar = new MenuBar();
			// File menu
			fileMenu = new Menu();
			fileMenu.setId("File");
			fileMenu.setText("File");
			openFileMenuItem = new MenuItem("Open");
			saveFileMenuItem = new MenuItem("Save");
			closeFileMenuItem = new MenuItem("Close");
			fileMenu.getItems().addAll(openFileMenuItem, saveFileMenuItem, closeFileMenuItem);

			fileChooser = new FileChooser();
			chosenFile = null;
			openFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					File file = fileChooser.showOpenDialog(primaryStage);
					if (file != null) {
						chosenFile = file;
						if (chosenFile != null)
							try {
								loadFromFile();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					}
				}
			});

			// Edit menu
			editMenu = new Menu();
			editMenu.setId("Edit");
			editMenu.setText("Edit");
			// Help menu
			helpMenu = new Menu();
			helpMenu.setId("Help");
			helpMenu.setText("Help");

			mainMenuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

			// Grid Pane (Filters and the table)
			mainGridPane = new GridPane();
			mainGridPane.prefWidthProperty().bind(primaryStage.widthProperty());
			filtersTP = new TitledPane();
			filtersTP.setCollapsible(true);
			filtersTP.setText("Filter");

			// Filters section
			hbFilters = new HBox(3);
			hbFilters.setSpacing(10);
			final ToggleGroup groupFilter = new ToggleGroup();
			rbAll = new RadioButton("All");
			rbAll.setToggleGroup(groupFilter);
			rbAll.setSelected(true);
			rbOverdue = new RadioButton("Overdue");
			rbOverdue.setToggleGroup(groupFilter);
			rbToday = new RadioButton("Today");
			rbToday.setToggleGroup(groupFilter);
			rbThisweek = new RadioButton("This week");
			rbThisweek.setToggleGroup(groupFilter);

			cbNotCompleted = new CheckBox("Not completed");
			hbFilters.getChildren().addAll(rbAll, rbOverdue, rbToday, rbThisweek, cbNotCompleted);

			filtersTP.setContent(hbFilters);
			filtersTP.prefWidthProperty().bind(primaryStage.widthProperty());

			// Table of tasks
			tvTasks = new TableView<Task>();

			cbDone = new CheckBox();
			doneCol = new TableColumn<Task, Boolean>("");
			doneCol.graphicProperty();
			doneCol.setGraphic(cbDone);

			dateCol = new TableColumn<Task, String>("Due Date");
			dateCol.setMinWidth(100);

			titleCol = new TableColumn<Task, String>("Title");
			titleCol.setMinWidth(150);

			percentCol = new TableColumn<Task, Integer>("% Completed");
			percentCol.setMinWidth(70);

			descriptionCol = new TableColumn<Task, String>("Description");
			descriptionCol.setMinWidth(250);
			// tvTasks.setColumnResizePolicy((param) -> true );

			groupFilter.selectedToggleProperty()
					.addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
						if (groupFilter.getSelectedToggle() != null)
							try {
								filter(groupFilter.getSelectedToggle().getUserData().toString());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						// System.out.println(groupFilter.getSelectedToggle().getUserData().toString());
						;
					});

			tvTasks.getColumns().addAll(doneCol, dateCol, titleCol, percentCol, descriptionCol);
			tvTasks.setItems(tasks);

			mainGridPane.addRow(1, filtersTP);
			mainGridPane.addRow(2, tvTasks);
			mainVBox.getChildren().addAll(mainMenuBar, mainGridPane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void filter(String type) throws IOException {
		// ObservableList<Task> tasks;
		
		if(tasks == null)
			return;
		
		switch (type) {
		case "All":
			break;
		case "Overdue":
			break;
		case "Today":
			break;
		case "This week":
			break;
		default:
			break;
		}

	}

	public void loadFromFile() throws IOException, ParseException {

		BufferedReader br = new BufferedReader(new FileReader(chosenFile.getAbsolutePath()));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything);

			String delims = "[\n]";
			String[] tokens = everything.split(delims);

			tasks = FXCollections.observableArrayList();
			
			for (int i = 0; i < tokens.length; i++) {
				tasks.add(new Task(tokens[i]));
				System.out.println(i + " " + tokens[i]);
			}

		} finally {
			br.close();
		}

		doneCol.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("completed"));
		doneCol.setCellValueFactory(new Callback<CellDataFeatures<Task, Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Task, Boolean> param) {
				return param.getValue().isCompletedProperty();
			}
		});
		doneCol.setCellFactory(CheckBoxTableCell.forTableColumn(doneCol));
		dateCol.setCellValueFactory(new PropertyValueFactory<Task, String>("dueDateString"));
		titleCol.setCellValueFactory(new PropertyValueFactory<Task, String>("title"));
		percentCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("percent"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
		
		tvTasks.setItems(tasks);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
