package application;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

			groupFilter.selectedToggleProperty()
					.addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
						if (groupFilter.getSelectedToggle() != null)
							// System.out.println(groupFilter.getSelectedToggle().getUserData().toString());
							;
					});

			filtersTP.setContent(hbFilters);
			filtersTP.prefWidthProperty().bind(primaryStage.widthProperty());

			// Set of tasks
			final ObservableList<Task> tasks = FXCollections.observableArrayList(
					new Task("21/02/2018|GUI Laboratory 1|15|JavaFX Laboratory"),
					new Task("05/04/2018|EDYCO Homework|100|Finish the homework"));

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

	public void loadFromFile() {
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

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
