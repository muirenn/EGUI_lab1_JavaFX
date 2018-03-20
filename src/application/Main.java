package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
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
	ToggleGroup groupFilter;
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
			groupFilter = new ToggleGroup();
			rbAll = new RadioButton("All");
			rbAll.setUserData("All");
			rbAll.setToggleGroup(groupFilter);
			rbAll.setSelected(true);
			rbOverdue = new RadioButton("Overdue");
			rbOverdue.setUserData("Overdue");
			rbOverdue.setToggleGroup(groupFilter);
			rbToday = new RadioButton("Today");
			rbToday.setUserData("Today");
			rbToday.setToggleGroup(groupFilter);
			rbThisweek = new RadioButton("This week");
			rbThisweek.setUserData("This week");
			rbThisweek.setToggleGroup(groupFilter);

			cbNotCompleted = new CheckBox("Not completed");

			hbFilters.getChildren().addAll(rbAll, rbOverdue, rbToday, rbThisweek, cbNotCompleted);

			filtersTP.setContent(hbFilters);
			filtersTP.prefWidthProperty().bind(primaryStage.widthProperty());

			// Table of tasks
			tvTasks = new TableView<Task>();

			cbDone = new CheckBox();
			cbDone.setDisable(true);
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
			tvTasks.getColumns().addAll(doneCol, dateCol, titleCol, percentCol, descriptionCol);

			mainGridPane.addRow(1, filtersTP);
			mainGridPane.addRow(2, tvTasks);
			mainVBox.getChildren().addAll(mainMenuBar, mainGridPane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("ToDo List");
			primaryStage.show();
			
			
			
			groupFilter.selectedToggleProperty()
					.addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
						if (groupFilter.getSelectedToggle() != null
								&& groupFilter.getSelectedToggle().getUserData() != null)
							try {
								filter(groupFilter.getSelectedToggle().getUserData().toString());
							} catch (IOException | ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					});

			cbNotCompleted.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					try {
						filter(groupFilter.getSelectedToggle().getUserData().toString());
					} catch (IOException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			fileChooser = new FileChooser();
			chosenFile = null;
			openFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					chosenFile = fileChooser.showOpenDialog(primaryStage);
					if (chosenFile != null) {
						try {
							filter(groupFilter.getSelectedToggle().getUserData().toString());
						} catch (IOException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
			});
			
			tvTasks.setRowFactory( tv -> {
			    TableRow<Task> row = new TableRow<Task>();
			    row.setOnMouseClicked(event -> {
			        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
			        	Task rowData = row.getItem();
			        	//
			        	System.out.println(rowData.getTitle());
			        	openEditor();
			        }
			    });
			    return row ;
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void filter(String type) throws IOException, ParseException {
		// ObservableList<Task> tasks;

		if (chosenFile == null && tasks == null)
			return;

		loadFromFile();

		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Task> filteredData = new FilteredList<Task>(tasks);
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);

		switch (type) {
		case "Overdue":
			filteredData.setPredicate(task -> {
				return (today.getTime() - task.getDueDate().getTime() > 1000
						&& (task.getCompleted() || cbNotCompleted.selectedProperty().getValue()));
			});
			break;
		case "Today":
			filteredData.setPredicate(task -> {
				return (Math.abs(task.getDueDate().getTime() - today.getTime()) <= 1000
						&& (task.getCompleted() || cbNotCompleted.selectedProperty().getValue()));
			});
			break;
		case "This week":
			filteredData.setPredicate(task -> {
				return (((task.getDueDate().getTime() - today.getTime() <= 7 * 24 * 60 * 60 * 1000)
						&& (task.getDueDate().getTime() - today.getTime() > 1000))
						&& (task.getCompleted() || cbNotCompleted.selectedProperty().getValue()));
			});
			break;
		default:
			filteredData.setPredicate(task -> {
				return (task.getCompleted() || cbNotCompleted.selectedProperty().getValue());
			});
			break;
		}

		tasks = FXCollections.observableArrayList(filteredData);
		refreshLists();
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

			String delims = "[\n]";
			String[] tokens = sb.toString().split(delims);

			tasks = FXCollections.observableArrayList();

			for (int i = 0; i < tokens.length; i++) {
				tasks.add(new Task(tokens[i]));
			}

		} finally {
			br.close();
		}

	}

	public void refreshLists() {
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
	
	public void openEditor() {
	    try {
	        Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("EditWindow.fxml")), 600, 400);
	        Stage stage = new Stage();
	        stage.setTitle("Edit an Entry");
	        stage.setScene(scene);
	        
	        
	        stage.show();
	    } catch (IOException e) {
	        Logger logger = Logger.getLogger(getClass().getName());
	        logger.log(Level.SEVERE, "Failed to create new Window.", e);
	    }
	}

	public static void main(String[] args) {
		launch(args);
	}
}
