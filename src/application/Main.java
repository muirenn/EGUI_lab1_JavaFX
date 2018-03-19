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
	@Override
	public void start(Stage primaryStage) {
		try {
			// Main VerticalBox
			VBox mainVBox = new VBox();
			Scene scene = new Scene(mainVBox, 1000, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Main Menu Bar settings
			MenuBar mainMenuBar = new MenuBar();
			// File menu
			Menu fileMenu = new Menu();
			fileMenu.setId("File");
			fileMenu.setText("File");
			MenuItem openFileMenuItem = new MenuItem("Open");
			MenuItem saveFileMenuItem = new MenuItem("Save");
			MenuItem closeFileMenuItem = new MenuItem("Close");
			fileMenu.getItems().addAll(openFileMenuItem, saveFileMenuItem, closeFileMenuItem);
			// Edit menu
			Menu editMenu = new Menu();
			editMenu.setId("Edit");
			editMenu.setText("Edit");
			// Help menu
			Menu helpMenu = new Menu();
			helpMenu.setId("Help");
			helpMenu.setText("Help");

			mainMenuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

			// Grid Pane (Filters and the table)
			GridPane mainGridPane = new GridPane();
			mainGridPane.prefWidthProperty().bind(primaryStage.widthProperty());
			TitledPane filtersTP = new TitledPane();
			filtersTP.setCollapsible(true);
			filtersTP.setText("Filter");

			// Filters section
			HBox hbFilters = new HBox(3);
			final ToggleGroup groupFilter = new ToggleGroup();
			RadioButton rbAll = new RadioButton("All");
			rbAll.setToggleGroup(groupFilter);
			rbAll.setSelected(true);
			RadioButton rbOverdue = new RadioButton("Overdue");
			rbOverdue.setToggleGroup(groupFilter);
			RadioButton rbToday = new RadioButton("Today");
			rbToday.setToggleGroup(groupFilter);
			RadioButton rbThisweek = new RadioButton("This week");
			rbThisweek.setToggleGroup(groupFilter);
			CheckBox cbNotCompleted = new CheckBox("Not completed");
			hbFilters.setSpacing(10);
			hbFilters.getChildren().addAll(rbAll, rbOverdue, rbToday, rbThisweek, cbNotCompleted);

			groupFilter.selectedToggleProperty()
					.addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
						if (groupFilter.getSelectedToggle() != null)
							// System.out.println(groupFilter.getSelectedToggle().getUserData().toString());
							;
					});

			filtersTP.setContent(hbFilters);
			filtersTP.prefWidthProperty().bind(primaryStage.widthProperty());

			mainGridPane.addRow(1, filtersTP);

			// Set of tasks
			final ObservableList<Task> tasks = FXCollections.observableArrayList(
					new Task("21/02/2018|GUI Laboratory 1|15|JavaFX Laboratory"),
					new Task("05/04/2018|EDYCO Homework|100|Finish the homework"));

			// Table of tasks
			TableView<Task> tvTasks = new TableView<Task>();
			CheckBox cbDone = new CheckBox();
			TableColumn<Task, Boolean> doneCol = new TableColumn<Task, Boolean>("");
			doneCol.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("completed"));

			doneCol.setCellValueFactory(new Callback<CellDataFeatures<Task, Boolean>, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(CellDataFeatures<Task, Boolean> param) {
					return param.getValue().isCompletedProperty();
				}
			});

			doneCol.setCellFactory(CheckBoxTableCell.forTableColumn(doneCol));

			doneCol.graphicProperty();
			doneCol.setGraphic(cbDone);

			TableColumn<Task, String> dateCol = new TableColumn<Task, String>("Due Date");
			dateCol.setMinWidth(100);
			dateCol.setCellValueFactory(new PropertyValueFactory<Task, String>("dueDateString"));

			TableColumn<Task, String> titleCol = new TableColumn<Task, String>("Title");
			titleCol.setMinWidth(150);
			titleCol.setCellValueFactory(new PropertyValueFactory<Task, String>("title"));

			TableColumn<Task, Integer> percentCol = new TableColumn<Task, Integer>("% Completed");
			percentCol.setMinWidth(70);
			percentCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("percent"));

			TableColumn<Task, String> descriptionCol = new TableColumn<Task, String>("Description");
			descriptionCol.setMinWidth(250);
			descriptionCol.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
			//tvTasks.setColumnResizePolicy((param) -> true );			
			tvTasks.getColumns().addAll(doneCol, dateCol, titleCol, percentCol, descriptionCol);
			tvTasks.setItems(tasks);

			mainGridPane.addRow(2, tvTasks);
			mainVBox.getChildren().addAll(mainMenuBar, mainGridPane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
