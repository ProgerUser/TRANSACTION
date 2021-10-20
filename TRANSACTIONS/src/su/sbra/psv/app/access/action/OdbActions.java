package su.sbra.psv.app.access.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.access.menu.USR;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

public class OdbActions {

	@FXML
	private TextField ActionID;

	@FXML
	private TreeView<ODB_ACTION> Actions;

	TreeItem<ODB_ACTION> root = null;

	ODB_ACTION adb_act;
	
	@FXML
	private TableView<USR> Users;

	@FXML
	private TableColumn<USR, String> Login;

	@FXML
	private TableColumn<USR, String> Fio;

	@FXML
	private ContextMenu ContMenu;

	@FXML
	private MenuItem Add;

	@FXML
	private MenuItem Delete;

	@FXML
	private TextField ID_FIND;

	public OdbActions() {
		Main.logger = Logger.getLogger(getClass());
	}
	/**
	 * Редактировать
	 * 
	 * @param event
	 */
	@FXML
	void FIND(ActionEvent event) {
		try {
			if (!ID_FIND.getText().equals("")) {
				// Get the selected item
				// TreeItem<ODB_ACTION> selectedItem =
				// Actions.getSelectionModel().getSelectedItem();
				//fillTree();
				findNode(root, Integer.valueOf(ID_FIND.getText()));
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Только число
	 * 
	 * @param TxtFld
	 */
	void OnlyDigits(TextField TxtFld) {
		TxtFld.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					TxtFld.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	/**
	 * Редактировать
	 * 
	 * @param event
	 */
	@FXML
	void EdtAction(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(6l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			Main.logger = Logger.getLogger(getClass());
			Stage stage = new Stage();
			Stage stage_ = (Stage) Users.getScene().getWindow();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/su/sbra/psv/app/access/action/IUAction.fxml"));

			EditAction controller = new EditAction();
			loader.setController(controller);
			controller.setParantid(Actions.getSelectionModel().getSelectedItem().getValue().getACT_ID(),
					Actions.getSelectionModel().getSelectedItem().getValue().getACT_NAME());

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("Редактировать: " + Actions.getSelectionModel().getSelectedItem().getValue().getACT_NAME());
			stage.initOwner(stage_);
			stage.setResizable(false);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					if (controller.getStatus()) {
						fillTree();
					}
					controller.dbDisconnect();
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Actions.requestFocus();
							Actions.getSelectionModel().select(SelTbl);
							Actions.scrollTo(SelTbl);
						}
					});
				}
			});
			stage.show();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Удалить действие
	 * 
	 * @param event
	 */
	@FXML
	void DeleteAction(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(5l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			Main.logger = Logger.getLogger(getClass());
			Stage stage = (Stage) Users.getScene().getWindow();
			Label alert = new Label("Удалить запись?");
			alert.setLayoutX(75.0);
			alert.setLayoutY(11.0);
			alert.setPrefHeight(17.0);

			Button no = new Button();
			no.setText("Нет");
			no.setLayoutX(111.0);
			no.setLayoutY(56.0);
			no.setPrefWidth(72.0);
			no.setPrefHeight(21.0);

			Button yes = new Button();
			yes.setText("Да");
			yes.setLayoutX(14.0);
			yes.setLayoutY(56.0);
			yes.setPrefWidth(72.0);
			yes.setPrefHeight(21.0);

			AnchorPane yn = new AnchorPane();
			yn.getChildren().add(alert);
			yn.getChildren().add(no);
			yn.getChildren().add(yes);
			Scene ynScene = new Scene(yn, 250, 100);
			Stage newWindow_yn = new Stage();
			no.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					newWindow_yn.close();
				}
			});
			yes.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					try {
						PreparedStatement delete = conn.prepareStatement("declare " + "pragma autonomous_transaction;"
								+ "begin " + " delete from odb_action_sb where ACT_ID = ?;" + "commit;" + "end;");

						delete.setLong(1, Actions.getSelectionModel().getSelectedItem().getValue().getACT_ID());
						delete.executeUpdate();
						delete.close();
						fillTree();
					} catch (SQLException e) {
						try {
							conn.rollback();
						} catch (SQLException e1) {
							Msg.Message(ExceptionUtils.getStackTrace(e1));
							Main.logger.error(ExceptionUtils.getStackTrace(e1) + "~" + Thread.currentThread().getName());
						}
						DbUtil.Log_Error(e);
					}
					newWindow_yn.close();
				}
			});
			newWindow_yn.setTitle("Внимание");
			newWindow_yn.setScene(ynScene);
			newWindow_yn.initModality(Modality.WINDOW_MODAL);
			newWindow_yn.initOwner(stage);
			newWindow_yn.setResizable(false);
			newWindow_yn.getIcons().add(new Image("/icon.png"));
			newWindow_yn.show();

		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Добавить действие
	 * 
	 * @param event
	 */
	@FXML
	void AddChildAction(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(4l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			Main.logger = Logger.getLogger(getClass());
			Stage stage = new Stage();
			Stage stage_ = (Stage) Users.getScene().getWindow();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/su/sbra/psv/app/access/action/IUAction.fxml"));

			AddAction controller = new AddAction();
			loader.setController(controller);
			controller.setParantid(Actions.getSelectionModel().getSelectedItem().getValue().getACT_ID());

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("Добавить новую запись");
			stage.initOwner(stage_);
			stage.setResizable(false);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					if (controller.getStatus()) {
						fillTree();
					}
					controller.dbDisconnect();
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Actions.requestFocus();
							Actions.getSelectionModel().select(SelTbl);
							Actions.scrollTo(SelTbl);
						}
					});
					
				}
			});
			stage.show();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Add(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(2l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			Main.logger = Logger.getLogger(getClass());
			if (Users.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите пользователя");
			} else {
				if (Actions.getSelectionModel().getSelectedItem() == null) {
					Msg.Message("Выберите действие");
				} else {
					USR usr = Users.getSelectionModel().getSelectedItem();
					ODB_ACTION odb_act = Actions.getSelectionModel().getSelectedItem().getValue();
					// String acts = Actions.getSelectionModel().getSelectedItem().getValue();
					// Integer act = Long.valueOf(acts.substring(0, acts.indexOf(":")));
					CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbActUsrAdd(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, usr.getIUSRID());
					callStmt.setLong(3, odb_act.getACT_ID());
					callStmt.execute();
					String ret = callStmt.getString(1);
					callStmt.close();
					if (ret.equals("ok")) {
						conn.commit();
						fillTree();
					} else {
						conn.rollback();
						Msg.Message(ret);
					}
				}
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				DbUtil.Log_Error(e1);
			}
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Delete(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(3l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			Main.logger = Logger.getLogger(getClass());
			if (Users.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите пользователя");
			} else {
				if (Actions.getSelectionModel().getSelectedItem() == null) {
					Msg.Message("Выберите действие");
				} else {
					USR usr = Users.getSelectionModel().getSelectedItem();
					ODB_ACTION odb_act = Actions.getSelectionModel().getSelectedItem().getValue();
					// String acts = Actions.getSelectionModel().getSelectedItem().getValue();
					// Integer act = Long.valueOf(acts.substring(0, acts.indexOf(":")));

					CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbActUsrDelete(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, usr.getIUSRID());
					callStmt.setLong(3, odb_act.getACT_ID());
					callStmt.execute();
					String ret = callStmt.getString(1);
					callStmt.close();
					if (ret.equals("ok")) {
						conn.commit();
						fillTree();
					} else {
						conn.rollback();
						Msg.Message(ret);
					}
				}
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				DbUtil.Log_Error(e1);
			}
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	private void initialize() {
		try {
			Main.logger = Logger.getLogger(getClass());

			OnlyDigits(ID_FIND);

			Login.setCellValueFactory(cellData -> cellData.getValue().CUSRLOGNAMEProperty());
			Fio.setCellValueFactory(cellData -> cellData.getValue().CUSRNAMEProperty());

			// Actions.setCellFactory((TreeView<String> p) -> new TextFieldTreeCellImpl());

			Actions.setCellFactory(tv -> {
				TreeCell<ODB_ACTION> cell = new TreeCell<ODB_ACTION>() {
					@Override
					public void updateItem(ODB_ACTION item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText("");
							setGraphic(null);
						} else {
							setText(item.getACT_NAME());
							if (Users.getSelectionModel().getSelectedItem() != null) {

								USR usr = Users.getSelectionModel().getSelectedItem();

								Long act = item.getACT_ID();
								// Integer act = Long.valueOf(item.substring(0, item.indexOf(":")));
								if (DbUtil.Odb_Aaction(usr.getIUSRID(), act) == 1) {
									setStyle("-fx-text-fill: green;-fx-font-weight: bold");
								} else {
									setStyle("");
								}
							}
						}
					}
				};
				return cell;
			});

			dbConnect();

			InitUsrs();

			fillTree();

			Users.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				fillTree();
				// Clear(root);
				// USR usr = Users.getSelectionModel().getSelectedItem();
				// printChildren(root, usr.getIUSRID());
			});

			Actions.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				TreeItem<ODB_ACTION> act = Actions.getSelectionModel().getSelectedItem();
				if (act != null) {
					ActionID.setText(String.valueOf(act.getValue().getACT_ID()));
					SelTbl = Actions.getSelectionModel().getSelectedIndex();
				}
			});

			TableFilter<USR> tableFilter = TableFilter.forTableView(Users).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}
	int SelTbl;
	void InitUsrs() {
		try {
			Main.logger = Logger.getLogger(getClass());
			PreparedStatement prepStmt = conn.prepareStatement("select * from usr where usr.dusrfire is null");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<USR> usr_list = FXCollections.observableArrayList();
			while (rs.next()) {
				USR usr = new USR();
				usr.setCUSRLOGNAME(rs.getString("CUSRLOGNAME"));
				usr.setIUSRID(rs.getLong("IUSRID"));
				usr.setCUSRNAME(rs.getString("CUSRNAME"));
				usr_list.add(usr);
			}
			prepStmt.close();
			rs.close();
			Users.setItems(usr_list);
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

	private void findNode(TreeItem<ODB_ACTION> treeNode, int id) {
	       if (treeNode.getChildren().isEmpty()) {
	           // Do nothing node is empty.
	       } else {
	           // Loop through each child node.
	           for (TreeItem<ODB_ACTION> node : treeNode.getChildren()) {
	               if (node.getValue().getACT_ID() == id) {
	                   node.setExpanded(true);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Actions.requestFocus();
								Actions.getSelectionModel().select(node);
								Actions.scrollTo(Actions.getSelectionModel().getSelectedIndex());
							}
						});
						
	               } else {
	                   node.setExpanded(true);
	               }
	               // If the current node has children then check them.
	               if (!treeNode.getChildren().isEmpty()) {
	                   findNode(node, id);
	               }
	           }
	       }
	   }
	
	void fillTree() {
		Map<Long, TreeItem<ODB_ACTION>> itemById = new HashMap<>();
		Map<Long, Long> parents = new HashMap<>();
		String query = "select * from odb_action_sb ";
				//+ ((!ID_FIND.getText().equals("") ? "where ACT_ID = " + ID_FIND.getText() + "" : ""));
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				adb_act = new ODB_ACTION();
				adb_act.setACT_ID(rs.getLong("ACT_ID"));
				adb_act.setACT_NPP(rs.getLong("ACT_NPP"));
				adb_act.setACT_PARENT(rs.getLong("ACT_PARENT"));
				adb_act.setACT_NAME(rs.getString("ACT_NAME"));
				itemById.put(rs.getLong("ACT_ID"), new TreeItem<>(adb_act));
				parents.put(rs.getLong("ACT_ID"), rs.getLong("ACT_PARENT"));
			}
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Map.Entry<Long, TreeItem<ODB_ACTION>> entry : itemById.entrySet()) {
			Long key = entry.getKey();
			Long parent = parents.get(key);
			if (parent.equals(key)) {
				// in case the root item points to itself, this is it
				root = entry.getValue();
				root.setExpanded(true);
			} else {
				TreeItem<ODB_ACTION> parentItem = itemById.get(parent);
				if (parentItem == null) {
					// in case the root item has no parent in the result set, this is it
					root = entry.getValue();
				} else {
					// add to parent tree item
					parentItem.getChildren().add(entry.getValue());
				}
				if (parentItem != null /*&& parentItem.getValue().getACT_ID() == 0*/) {
					parentItem.setExpanded(true);
				}
			}
		}
		// root.setExpanded(true);
		Actions.setRoot(root);
	}

	public boolean setTreeItem(ODB_ACTION emp, TreeItem<ODB_ACTION> root_tree) {
		if (emp.equals(root_tree.getValue())) {
			root_tree.setExpanded(false);
			return true;
		} else {
			for (TreeItem<ODB_ACTION> emps : root_tree.getChildren()) {
				if (setTreeItem(emp, emps)) {
					// expand & return true, if item is a descendant of the current item
					root_tree.setExpanded(true);
					return true;
				}
			}
			return false; // set expanded to false before returning here?
		}
	}

	void Clear(TreeItem<String> root) {
		for (TreeItem<String> child : root.getChildren()) {
			child.setValue(child.getValue()
					.replace(String.valueOf(Character.toChars((int) Long.parseUnsignedLong("2713", 16))), "")
					.replace(String.valueOf(Character.toChars((int) Long.parseUnsignedLong("2718", 16))), ""));
			if (child.getChildren().isEmpty()) {

			} else {
				Clear(child);
			}
		}
	}

	void printChildren(TreeItem<String> root, Long usrid) {
		for (TreeItem<String> child : root.getChildren()) {
			Long act = Long.valueOf(child.getValue().substring(0, child.getValue().indexOf(":")));
			if (DbUtil.Odb_Aaction(usrid, act) == 1) {
				child.setValue(
						child.getValue() + String.valueOf(Character.toChars((int) Long.parseUnsignedLong("2713", 16))));
			} else {
				child.setValue(
						child.getValue() + String.valueOf(Character.toChars((int) Long.parseUnsignedLong("2718", 16))));
			}
			if (child.getChildren().isEmpty()) {

			} else {
				printChildren(child, usrid);
			}
		}
	}

	private Connection conn;

	private void dbConnect() throws UnknownHostException {
		try {
			Class.forName("oracle.jdbc.OracleDriver");

			Properties props = new Properties();
			props.setProperty("password", Connect.userPassword_);
			props.setProperty("user", Connect.userID_);
			props.put("v$session.osuser", System.getProperty("user.name").toString());
			props.put("v$session.machine", InetAddress.getLocalHost().getHostAddress());
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
			
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			DbUtil.Log_Error(e);
		}
	}

	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void AddAction(ActionEvent event) {
		try {
			Main.logger = Logger.getLogger(getClass());
			if (Actions.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите действие");
			} else {

				// TreeItem<String> selectedItem =
				// Actions.getSelectionModel().getSelectedItem();
				/*
				 * int index = selectedItem.getParent().getChildren().indexOf(selectedItem);
				 */
				// selectedItem.getChildren().add(new
				// TreeItem<>(String.valueOf(selectedItem.getChildren()) + ":"));

				// selectedItem.getParent().getChildren().add(index + 1, new
				// TreeItem<>(String.valueOf(index + 2) + ":"));

				// TreeItem newEmployee = new TreeItem<>("New Employee");
				// getTreeItem().getChildren().add(newEmployee);
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@SuppressWarnings("unused")
	private final class TextFieldTreeCellImpl extends TreeCell<String> {

		private TextField textField;

		@Override
		public void startEdit() {
			super.startEdit();

			if (textField == null) {
				createTextField();
			}
			setText(null);
			setGraphic(textField);
			textField.selectAll();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText((String) getItem());
			setGraphic(getTreeItem().getGraphic());
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (Users.getSelectionModel().getSelectedItem() != null) {
					USR usr = Users.getSelectionModel().getSelectedItem();
					Long act = Long.valueOf(item.substring(0, item.indexOf(":")));
					if (DbUtil.Odb_Aaction(usr.getIUSRID(), act) == 1) {
						setStyle("-fx-text-fill: green;-fx-font-weight: bold");
					} else {
						setStyle("");
					}
				}

				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(getTreeItem().getGraphic());
				}
			}
		}

		String NewAction(String str) {
			String ret = null;
			try {
				Main.logger = Logger.getLogger(getClass());
				TreeItem<ODB_ACTION> selectedItem = Actions.getSelectionModel().getSelectedItem();

				ODB_ACTION partext = selectedItem.getParent().getValue();

				Long pact = partext.getACT_ID();
				// Integer pact = Long.valueOf(partext.substring(0, partext.indexOf(":")));
				String text = partext.getACT_NAME();
				// str.substring(str.indexOf(":"));
				Long act = Long.valueOf(str.substring(0, str.indexOf(":")));

				String generatedColumns[] = { "ACT_ID" };
				PreparedStatement pstmt = conn.prepareStatement(
						"insert into ODB_ACTION (ACT_PARENT,ACT_NPP,ACT_NAME) values (?,?,?) ", generatedColumns);
				pstmt.setLong(1, pact);
				pstmt.setLong(2, act);
				pstmt.setString(3, text);
				pstmt.executeUpdate();
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					ret = String.valueOf(rs.getLong(1)) + ":" + text;
					conn.commit();
				}
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				DbUtil.Log_Error(e);
			}
			return ret;
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setOnKeyReleased((KeyEvent t) -> {
				if (t.getCode() == KeyCode.ENTER) {
					commitEdit(NewAction(textField.getText()));
				} else if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			});

		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}
}
