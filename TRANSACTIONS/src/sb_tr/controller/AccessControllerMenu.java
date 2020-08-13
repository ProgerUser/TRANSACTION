package sb_tr.controller;

import org.controlsfx.control.table.TableFilter;

import javafx.collections.ObservableList;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sb_tr.model.Forms;
import sb_tr.model.MenuItems;
import sb_tr.model.TerminalDAO;
import sb_tr.model.User_in;
import sb_tr.model.User_out;
import sb_tr.util.DBUtil;

/**
 * Пачулия Саид 04.06.2020.
 */
public class AccessControllerMenu {

	@FXML
	private TableView<MenuItems> MENU;
	
	@FXML
	private TableColumn<MenuItems, Integer> ID_MENU;
	
	@FXML
	private TableColumn<MenuItems, Integer> MENU_POS;
	
	@FXML
	private TableColumn<MenuItems, Integer> MENU_I;
	
	@FXML
	private TableColumn<MenuItems, String> MENU_NAME;
	
	@FXML
	private TableColumn<MenuItems, String> MENU_DESC;

	@FXML
	private TableView<User_out> USER_OUT;
	
	@FXML
	private TableColumn<User_out, String> USR_ID_O;
	
	@FXML
	private TableColumn<User_out, String> FIO_O;

	@FXML
	private TableView<User_in> USERS_IN;
	
	@FXML
	private TableColumn<User_in, String> USR_ID_I;
	
	@FXML
	private TableColumn<User_in, String> FIO_I;
	
	@FXML
	private TableColumn<User_in, String> TYPE_ACCESS_I;

	@FXML
	private void change_acc_type() {
		if (USERS_IN.getSelectionModel().getSelectedItem() == null) {
			this.altert("Выберите сначала данные из таблицы!");
		} else {
			int form_id = MENU.getSelectionModel().getSelectedItem().getID_MENU();
			String user_name = USERS_IN.getSelectionModel().getSelectedItem().get_USR_ID_I();

			String updateStmt = "update z_sb_access_gr_menu_amra\n" + "   set GR_ID = decode((select j.gr_id\n"
					+ "                        from z_sb_access_gr_menu_amra j\n"
					+ "                       where MENU_ID = " + form_id + "\n"
					+ "                         and USR_ID =\n" + "                             (select f.iusrid\n"
					+ "                                from usr f\n"
					+ "                               where lower(f.cusrlogname) = lower('" + user_name + "'))),\n"
					+ "                      1,\n" + "                      2,\n" + "                      1)\n"
					+ " where MENU_ID = " + form_id + "\n" + "   and USR_ID = (select f.iusrid\n"
					+ "                   from usr f\n" + "                  where lower(f.cusrlogname) = lower('"
					+ user_name + "'))\n";

			DBUtil.dbExecuteUpdate(updateStmt);
			upd_usr_in();
		}
	}

	public void upd_usr_in() {
		MenuItems forms = MENU.getSelectionModel().getSelectedItem();
		ObservableList<User_in> empData_2 = TerminalDAO.User_in_menu(forms.getID_MENU());
		populate_user_in(empData_2);
		autoResizeColumns(USERS_IN);
		TableFilter.forTableView(USERS_IN).apply();
	}

	public void upd_usr_out() {
		int form_id = MENU.getSelectionModel().getSelectedItem().getID_MENU();

		ObservableList<User_out> empData_2 = TerminalDAO.User_out_menu(form_id);
		populate_user_o(empData_2);

		autoResizeColumns(USER_OUT);
		TableFilter.forTableView(USER_OUT).apply();
	}

	public void altert(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	@FXML
	private void initialize() {
		ID_MENU.setCellValueFactory(cellData -> cellData.getValue().ID_MENUProperty().asObject());
		MENU_NAME.setCellValueFactory(cellData -> cellData.getValue().MENU_NAMEProperty());
		MENU_DESC.setCellValueFactory(cellData -> cellData.getValue().MENU_DESCProperty());
		MENU_POS.setCellValueFactory(cellData -> cellData.getValue().MENU_POSProperty().asObject());
		MENU_I.setCellValueFactory(cellData -> cellData.getValue().MENU_IProperty().asObject());

		USR_ID_I.setCellValueFactory(cellData -> cellData.getValue().USR_ID_I_Property());
		FIO_I.setCellValueFactory(cellData -> cellData.getValue().FIO_I_Property());
		TYPE_ACCESS_I.setCellValueFactory(cellData -> cellData.getValue().TYPE_ACCESS_I_Property());

		USR_ID_O.setCellValueFactory(cellData -> cellData.getValue().USR_ID_O_Property());
		FIO_O.setCellValueFactory(cellData -> cellData.getValue().FIO_O_Property());

		ObservableList<MenuItems> empData = TerminalDAO.User_Menu();
		populate_forms(empData);
		autoResizeColumns(MENU);
		TableFilter.forTableView(MENU).apply();

		/* Listener */
		MENU.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				upd_usr_in();
				upd_usr_out();
			}
		});
		
		MENU.setRowFactory(row -> new TableRow<MenuItems>(){
		    @Override
		    public void updateItem(MenuItems item, boolean empty){
		        super.updateItem(item, empty);

		        if (item == null || empty) {
		            setStyle("");
		        } else {
		            //Now 'item' has all the info of the Person in this row
		            if (item.getMENU_POS() == 0) {
		            	setStyle("-fx-background-color: #b8b894;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");                      
		            } else {
		            	setStyle("-fx-background-color: #c2d6d6;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");    
		            }
		        }
		    }
		});
	}

	@FXML
	private void insert() {
		if (MENU.getSelectionModel().getSelectedItem() == null
				| USER_OUT.getSelectionModel().getSelectedItem() == null) {
			this.altert("Выберите сначала данные из таблицы!");
		} else {

			int form_id = MENU.getSelectionModel().getSelectedItem().getID_MENU();
			String user_name = USER_OUT.getSelectionModel().getSelectedItem().get_USR_ID_O();

			String updateStmt = "insert into Z_SB_ACCESS_GR_MENU_AMRA (menu_id, \n" + "usr_id, \n" + "gr_id) \n"
					+ " values (" + form_id + ",(select IUSRID from usr h\n" + "where h.CUSRLOGNAME = '" + user_name
					+ "'),1)";
			DBUtil.dbExecuteUpdate(updateStmt);
			upd_usr_out();
			upd_usr_in();
		}
	}

	@FXML
	private void delete() {
		if (USERS_IN.getSelectionModel().getSelectedItem() == null) {
			this.altert("Выберите сначала данные из таблицы!");
		} else {
			int form_id = MENU.getSelectionModel().getSelectedItem().getID_MENU();
			String user_name = USERS_IN.getSelectionModel().getSelectedItem().get_USR_ID_I();
			String type = USERS_IN.getSelectionModel().getSelectedItem().get_TYPE_ACCESS_I();

			String updateStmt = "delete from Z_SB_ACCESS_GR_MENU_AMRA j\n" + " where j.menu_id = '" + form_id + "'\n"
					+ "   and j.usr_id =\n" + "       (select IUSRID from usr h where h.CUSRLOGNAME = '" + user_name
					+ "')\n" + "   and j.gr_id = decode('" + type + "', 'Y', 1, 'N', 2)\n";
			DBUtil.dbExecuteUpdate(updateStmt);
			upd_usr_out();
			upd_usr_in();
		}
	}

	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("sess_id")) {

			} else {
				// Minimal width = columnheader
				Text t = new Text(column.getText());
				double max = t.getLayoutBounds().getWidth();
				for (int i = 0; i < table.getItems().size(); i++) {
					// cell must not be empty
					if (column.getCellData(i) != null) {
						t = new Text(column.getCellData(i).toString());
						double calcwidth = t.getLayoutBounds().getWidth();
						// remember new max-width
						if (calcwidth > max) {
							max = calcwidth;
						}
					}
				}
				// set the new max-widht with some extra space
				column.setPrefWidth(max + 10.0d);
			}
		});
	}

	private void populate_forms(ObservableList<MenuItems> trData) {
		MENU.setItems(trData);
	}

	private void populate_user_in(ObservableList<User_in> trData) {
		USERS_IN.setItems(trData);
	}

	private void populate_user_o(ObservableList<User_out> trData) {
		USER_OUT.setItems(trData);
	}
}
