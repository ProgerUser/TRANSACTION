package su.sbra.psv.app.access.grp;

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

import com.jyloo.syntheticafx.RootPane;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.access.action.ODB_ACTION;
import su.sbra.psv.app.access.menu.ODB_MNU;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

public class GrpController {

	public GrpController() {
		Main.logger = Logger.getLogger(getClass());
	}
	
	ODB_ACTION adb_act_act;
	
	@FXML private TextField ActionID_ACT;
	@FXML private TreeView<ODB_ACTION> Actions;
	@FXML private TableColumn<USR_IN_OUT, String> out_fio;
	@FXML private TableColumn<USR_IN_OUT, String> out_login;
	@FXML private TableView<ODB_GROUP_USR> grp;
	@FXML private TableColumn<USR_IN_OUT, String> in_login;
	@FXML private TableColumn<USR_IN_OUT, String> in_fio;
	@FXML private TableColumn<ODB_GROUP_USR, String> name;
	@FXML private TableColumn<ODB_GROUP_USR, Long> id;
	@FXML private TableView<USR_IN_OUT> usrout;
	@FXML private TableView<USR_IN_OUT> usrin;
	@FXML private TreeView<ODB_MNU> MNU;
	@FXML private ContextMenu ContMenu;

	//Report_________________________________________________
    @FXML private TableView<AP_REPORT_TYPE> ap_report_type;
    @FXML private TableColumn<AP_REPORT_TYPE, Long> REPORT_TYPE_ID;
    @FXML private TableColumn<AP_REPORT_TYPE, String> REPORT_TYPE_NAME;
	//IN
	@FXML private TableView<AP_REPORT_CAT> ap_report_cat_out;
	@FXML private TableColumn<AP_REPORT_CAT, Long> REPORT_ID_OUT;
	@FXML private TableColumn<AP_REPORT_CAT, String> REPORT_NAME_OUT;
	//OUT
	@FXML private TableView<AP_REPORT_CAT> ap_report_cat_in;
	@FXML private TableColumn<AP_REPORT_CAT, Long> REPORT_ID_IN;
	@FXML private TableColumn<AP_REPORT_CAT, String> REPORT_NAME_IN;
	//________________________________________________________
	//IN
	@FXML private TableView<AP_REPORT_TYPE> ap_report_type_out;
	@FXML private TableColumn<AP_REPORT_TYPE, Long> REPORT_TYPE_ID_OUT;
	@FXML private TableColumn<AP_REPORT_TYPE, String> REPORT_TYPE_NAME_OUT;
	//OUT
    @FXML private TableView<AP_REPORT_TYPE> ap_report_type_in;
    @FXML private TableColumn<AP_REPORT_TYPE, Long> REPORT_TYPE_ID_IN;
    @FXML private TableColumn<AP_REPORT_TYPE, String> REPORT_TYPE_NAME_IN;
	//________________________________________________________
	
    /**
     * разрешить действие
     * @param event
     */
	@FXML
	void AddAct(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(22l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			if (grp.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите пользователя");
			} else {
				if (Actions.getSelectionModel().getSelectedItem() == null) {
					Msg.Message("Выберите действие");
				} else {
					ODB_GROUP_USR grp_ = grp.getSelectionModel().getSelectedItem();
					ODB_ACTION odb_act = Actions.getSelectionModel().getSelectedItem().getValue();
					// String acts = Actions.getSelectionModel().getSelectedItem().getValue();
					// Integer act = Long.valueOf(acts.substring(0, acts.indexOf(":")));
					CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.ODB_ACT_GRP_ADD(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, grp_.getGRP_ID());
					callStmt.setLong(3, odb_act.getACT_ID());
					callStmt.execute();
					String ret = callStmt.getString(1);
					callStmt.close();
					if (ret.equals("ok")) {
						conn.commit();
						fillTreeAct();
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
				Msg.Message(ExceptionUtils.getStackTrace(e));
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * отменить действие
	 * @param event
	 */
	@FXML
	void DeleteAct(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(23l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			if (grp.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите пользователя");
			} else {
				if (Actions.getSelectionModel().getSelectedItem() == null) {
					Msg.Message("Выберите действие");
				} else {
					ODB_GROUP_USR grp_ = grp.getSelectionModel().getSelectedItem();
					ODB_ACTION odb_act = Actions.getSelectionModel().getSelectedItem().getValue();
					// String acts = Actions.getSelectionModel().getSelectedItem().getValue();
					// Integer act = Long.valueOf(acts.substring(0, acts.indexOf(":")));

					CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.ODB_ACT_GRP_DEL(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, grp_.getGRP_ID());
					callStmt.setLong(3, odb_act.getACT_ID());
					callStmt.execute();
					String ret = callStmt.getString(1);
					callStmt.close();
					if (ret.equals("ok")) {
						conn.commit();
						fillTreeAct();
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
				Msg.Message(ExceptionUtils.getStackTrace(e));
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
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
			props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
			
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	@FXML
	void AddMnu(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(20l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			if (grp.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите пользователя");
			} else {
				if (MNU.getSelectionModel().getSelectedItem() == null) {
					Msg.Message("Выберите действие");
				} else {
					ODB_GROUP_USR grp_ = grp.getSelectionModel().getSelectedItem();
					ODB_MNU odb_act = MNU.getSelectionModel().getSelectedItem().getValue();
					// String acts = MNU.getSelectionModel().getSelectedItem().getValue();
					// Integer act = Long.valueOf(acts.substring(0, acts.indexOf(":")));
					CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbMnuGrpAdd(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, grp_.getGRP_ID());
					callStmt.setLong(3, odb_act.getMNU_ID());
					callStmt.execute();
					String ret = callStmt.getString(1);
					callStmt.close();
					if (ret.equals("ok")) {
						conn.commit();
						fillTreeMnu();
					} else {
						conn.rollback();
						Msg.Message(ret);
					}
				}
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				DbUtil.Log_Error(e1);
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void DeleteMnu(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(21l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			if (grp.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите пользователя");
			} else {
				if (MNU.getSelectionModel().getSelectedItem() == null) {
					Msg.Message("Выберите действие");
				} else {
					ODB_GROUP_USR grp_ = grp.getSelectionModel().getSelectedItem();
					ODB_MNU odb_act = MNU.getSelectionModel().getSelectedItem().getValue();
					// String acts = MNU.getSelectionModel().getSelectedItem().getValue();
					// Integer act = Long.valueOf(acts.substring(0, acts.indexOf(":")));

					CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbMnuGrpDelete(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, grp_.getGRP_ID());
					callStmt.setLong(3, odb_act.getMNU_ID());
					callStmt.execute();
					String ret = callStmt.getString(1);
					callStmt.close();
					if (ret.equals("ok")) {
						conn.commit();
						fillTreeMnu();
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * добавить пользователя
	 * @param event
	 */
	@FXML
	void addusr(ActionEvent event) {
		try {
			if (grp.getSelectionModel().getSelectedItem() != null
					& usrout.getSelectionModel().getSelectedItem() != null) {
				
				if (DbUtil.Odb_Action(24l) == 0) {
					Msg.Message("Нет доступа!");
					return;
				}
				
				ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
				USR_IN_OUT usr = usrout.getSelectionModel().getSelectedItem();
				PreparedStatement prp = conn
						.prepareStatement(
								"declare\n" + 
								"  pragma autonomous_transaction;\n" + 
								"  usr_id number;\n" + 
								"begin\n" + 
								"  select usr.iusrid into usr_id from usr where usr.cusrlogname = ?;\n" + 
								"  insert into ODB_GRP_MEMBER (GRP_ID, IUSRID) values (?, usr_id);\n" + 
								"  commit;\n" + 
								"end;\n" + 
								"");
				prp.setString(1, usr.getLOGIN());
				prp.setLong(2, grp_act.getGRP_ID());
				prp.executeUpdate();
				prp.close();
				// Обновить
				InitUsrIn(grp_act.getGRP_ID());
				InitUsrOut(grp_act.getGRP_ID());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * удалить пользователя
	 * @param event
	 */
	@FXML
	void deleteusr(ActionEvent event) {
		try {
			if (grp.getSelectionModel().getSelectedItem() != null
					& usrin.getSelectionModel().getSelectedItem() != null) {
				
				if (DbUtil.Odb_Action(25l) == 0) {
					Msg.Message("Нет доступа!");
					return;
				}
				
				ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
				USR_IN_OUT usr = usrin.getSelectionModel().getSelectedItem();
				PreparedStatement prp = conn
						.prepareStatement("declare\n" + "  usr_id number;\n" + "  pragma autonomous_transaction;\n"
								+ "begin\n" + "  select usr.iusrid into usr_id from usr where usr.cusrlogname = ?;\n"
								+ "  delete from  ODB_GRP_MEMBER where GRP_ID = ? and IUSRID = usr_id;\n"
								+ "  commit;\n" + "end;\n" + "");
				prp.setString(1, usr.getLOGIN());
				prp.setLong(2, grp_act.getGRP_ID());
				prp.executeUpdate();
				prp.close();
				// Обновить
				InitUsrIn(grp_act.getGRP_ID());
				InitUsrOut(grp_act.getGRP_ID());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * добавить группу
	 * @param event
	 */
	@FXML
	void add(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(28l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/access/grp/IUGrp.fxml"));
			AddGrp controller = new AddGrp();
			loader.setController(controller);
			Parent root = loader.load();
			// stage.setScene(new Scene(root));
			RootPane rp = new RootPane(stage, root, true, true);
			stage.setScene(new Scene(rp));
			// stage.initStyle(StageStyle.DECORATED);
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("Добавить группу");
			stage.setResizable(false);
			stage.setIconified(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner((Stage) MNU.getScene().getWindow());
			// stage.initModality(Modality.WINDOW_MODAL);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					controller.dbDisconnect();
					InitGrp();
				}
			});
			stage.showAndWait();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * редактировать группу
	 * @param event
	 */
	@FXML
	void adit(ActionEvent event) {
		try {
			if (grp.getSelectionModel().getSelectedItem() != null) {
				
				if (DbUtil.Odb_Action(30l) == 0) {
					Msg.Message("Нет доступа!");
					return;
				}
				
				ODB_GROUP_USR group = InitGrp2(grp.getSelectionModel().getSelectedItem().getGRP_ID());
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				
				loader.setLocation(getClass().getResource("/su/sbra/psv/app/access/grp/IUGrp.fxml"));
				EditGrp controller = new EditGrp();
				controller.setConn(conn, group);
				
				loader.setController(controller);
				Parent root = loader.load();
				// stage.setScene(new Scene(root));
				RootPane rp = new RootPane(stage, root, true, true);
				stage.setScene(new Scene(rp));
				// stage.initStyle(StageStyle.DECORATED);
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Добавить группу");
				stage.setResizable(false);
				stage.setIconified(false);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner((Stage) MNU.getScene().getWindow());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						InitGrp();
					}
				});
				stage.showAndWait();
			} 
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * удалить группу
	 * @param event
	 */
	@FXML
	void delete(ActionEvent event) {
		try {
			if (grp.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите документ!");
			} else {
				
				if (DbUtil.Odb_Action(29l) == 0) {
					Msg.Message("Нет доступа!");
					return;
				}
				
				Stage stage = (Stage) grp.getScene().getWindow();
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
							ODB_GROUP_USR id = grp.getSelectionModel().getSelectedItem();
							PreparedStatement prst = conn.prepareStatement("delete from ODB_GROUP_USR where GRP_ID =?");
							prst.setLong(1, id.getGRP_ID());
							prst.executeUpdate();
							prst.close();
							conn.commit();
							InitGrp();
						} catch (Exception e) {
							try {
								conn.rollback();
							} catch (SQLException e1) {
								DbUtil.Log_Error(e1);
							}
							Msg.Message(ExceptionUtils.getStackTrace(e));
						}
						newWindow_yn.close();
					}
				});
				newWindow_yn.setTitle("Внимание");
				newWindow_yn.setScene(ynScene);
				// Specifies the modality for new window.
				newWindow_yn.initModality(Modality.WINDOW_MODAL);
				// Specifies the owner Window (parent) for new window
				newWindow_yn.initOwner(stage);
				newWindow_yn.setResizable(false);
				newWindow_yn.getIcons().add(new Image("/icon.png"));
				newWindow_yn.show();

			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@FXML
	private TextField ActionID;
	
	int SelAct;
	int SelMnu;
	
	@FXML
	private void initialize() {
		try {
			dbConnect();

			fillTreeMnu();
			fillTreeAct();
			
			MNU.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				TreeItem<ODB_MNU> act = MNU.getSelectionModel().getSelectedItem();
				if (act != null) {
					ActionID.setText(String.valueOf(act.getValue().getMNU_ID()));
					SelMnu = MNU.getSelectionModel().getSelectedIndex();
				}
			});
			Actions.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				TreeItem<ODB_ACTION> act = Actions.getSelectionModel().getSelectedItem();
				if (act != null) {
					ActionID_ACT.setText(String.valueOf(act.getValue().getACT_ID()));
					SelAct = Actions.getSelectionModel().getSelectedIndex();
				}
			});
			
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
							if (grp.getSelectionModel().getSelectedItem() != null) {
								ODB_GROUP_USR grp_ = grp.getSelectionModel().getSelectedItem();
								Long act = item.getACT_ID();
								if (DbUtil.Odb_Act_Grp(grp_.getGRP_ID(), act) == 1) {
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
			
			// Меню_________________________--
			MNU.setCellFactory(tv -> {
				TreeCell<ODB_MNU> cell = new TreeCell<ODB_MNU>() {
					@Override
					public void updateItem(ODB_MNU item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText("");
							setGraphic(null);
						} else {
							setText(item.getMNU_NAME());
							if (grp.getSelectionModel().getSelectedItem() != null) {
								ODB_GROUP_USR grp_ = grp.getSelectionModel().getSelectedItem();
								Long act = item.getMNU_ID();
								if (DbUtil.Odb_Mnu_Grp(grp_.getGRP_ID(), act) == 1) {
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

			// _____________________________

			name.setCellValueFactory(cellData -> cellData.getValue().GRP_NAMEProperty());
			in_fio.setCellValueFactory(cellData -> cellData.getValue().NAMEProperty());
			out_login.setCellValueFactory(cellData -> cellData.getValue().LOGINProperty());
			out_fio.setCellValueFactory(cellData -> cellData.getValue().NAMEProperty());
			in_login.setCellValueFactory(cellData -> cellData.getValue().LOGINProperty());
			id.setCellValueFactory(cellData -> cellData.getValue().GRP_IDProperty().asObject());
			InitGrp();

			grp.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				// Заполним меню
				fillTreeMnu();
				// заполним функции
				fillTreeAct();
				ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
				// Заполнить таблицы
				if (grp_act != null) {
					InitUsrIn(grp_act.getGRP_ID());
					InitUsrOut(grp_act.getGRP_ID());
					
					//Печать
					InitRepTpIn(grp_act.getGRP_ID());
					InitRepTpOut(grp_act.getGRP_ID());
				}
				ap_report_cat_out.setItems(null);
				ap_report_cat_in.setItems(null);
			});
			
			ap_report_type_in.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				AP_REPORT_TYPE rep_tp = ap_report_type_in.getSelectionModel().getSelectedItem();
				ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
				if (rep_tp != null & grp_act != null) {
					InitRepIn(rep_tp.getREPORT_TYPE_ID(), grp_act.getGRP_ID());
					InitRepOut(rep_tp.getREPORT_TYPE_ID(), grp_act.getGRP_ID());
				}
			});

			//Печать
			REPORT_TYPE_ID.setCellValueFactory(cellData -> cellData.getValue().REPORT_TYPE_IDProperty().asObject());
			REPORT_TYPE_NAME.setCellValueFactory(cellData -> cellData.getValue().REPORT_TYPE_NAMEProperty());
			
			REPORT_TYPE_ID_IN.setCellValueFactory(cellData -> cellData.getValue().REPORT_TYPE_IDProperty().asObject());
			REPORT_TYPE_NAME_IN.setCellValueFactory(cellData -> cellData.getValue().REPORT_TYPE_NAMEProperty());
			REPORT_TYPE_ID_OUT.setCellValueFactory(cellData -> cellData.getValue().REPORT_TYPE_IDProperty().asObject());
			REPORT_TYPE_NAME_OUT.setCellValueFactory(cellData -> cellData.getValue().REPORT_TYPE_NAMEProperty());
			//____Column
			REPORT_ID_OUT.setCellValueFactory(cellData -> cellData.getValue().REPORT_IDProperty().asObject());
			REPORT_NAME_OUT.setCellValueFactory(cellData -> cellData.getValue().REPORT_NAMEProperty());
			//____Column
			REPORT_ID_IN.setCellValueFactory(cellData -> cellData.getValue().REPORT_IDProperty().asObject());
			REPORT_NAME_IN.setCellValueFactory(cellData -> cellData.getValue().REPORT_NAMEProperty());
			InitRep();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	TreeItem<ODB_MNU> root = null;
	TreeItem<ODB_ACTION> root_root = null;
	ODB_MNU adb_act;

	/**
	 * Разрешить отчет группе
	 * @param event
	 */
	@FXML
    void AddRep(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(26l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			AP_REPORT_TYPE rep_tp = ap_report_type_in.getSelectionModel().getSelectedItem();
			ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
			AP_REPORT_CAT rep_out = ap_report_cat_out.getSelectionModel().getSelectedItem();
			if (rep_tp != null & grp_act != null & rep_out != null) {
				PreparedStatement prp = conn.prepareStatement("insert into AP_GROUP_REPORT_CAT_ROLE\n" + 
						"  (GROUP_ID, REPORT_TYPE_ID, REPORT_ID)\n" + 
						"values\n" + 
						"  (?,?,?)\n" + 
						" ");
				prp.setLong(1, grp_act.getGRP_ID());
				prp.setLong(2, rep_tp.getREPORT_TYPE_ID());
				prp.setLong(3, rep_out.getREPORT_ID());
				prp.executeUpdate();
				prp.close();
				conn.commit();
				InitRepIn(rep_tp.getREPORT_TYPE_ID(), grp_act.getGRP_ID());
				InitRepOut(rep_tp.getREPORT_TYPE_ID(), grp_act.getGRP_ID());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
    }
	
	/**
	 * Отобрать тип отчета у группы
	 * @param event
	 */
    @FXML
    void DeleteRepTp(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(27l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			AP_REPORT_TYPE rep_tp = ap_report_type_in.getSelectionModel().getSelectedItem();
			ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
			if (rep_tp != null & grp_act != null) {
				PreparedStatement prp = conn.prepareStatement("delete from AP_GROUP_REPORT_ROLE\n" + 
						" where GROUP_ID = ?\n" + 
						"   and REPORT_TYPE_ID = ?\n" + 
						"");
				prp.setLong(1, grp_act.getGRP_ID());
				prp.setLong(2, rep_tp.getREPORT_TYPE_ID());
				prp.executeUpdate();
				prp.close();
				conn.commit();
				InitRepTpIn(grp_act.getGRP_ID());
				InitRepTpOut( grp_act.getGRP_ID());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
    }
    
	/**
	 * Разрешить тип отчета группе
	 * @param event
	 */
	@FXML
    void AddRepTp(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(26l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			AP_REPORT_TYPE rep_tp = ap_report_type_out.getSelectionModel().getSelectedItem();
			ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
			if (rep_tp != null & grp_act != null) {
				PreparedStatement prp = conn.prepareStatement("insert into ap_group_report_role\n" + 
						"  (GROUP_ID, REPORT_TYPE_ID)\n" + 
						"values\n" + 
						"  (?,?)\n" + 
						" ");
				prp.setLong(1, grp_act.getGRP_ID());
				prp.setLong(2, rep_tp.getREPORT_TYPE_ID());
				prp.executeUpdate();
				prp.close();
				conn.commit();
				InitRepTpIn(grp_act.getGRP_ID());
				InitRepTpOut( grp_act.getGRP_ID());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
    }
	
	/**
	 * Отобрать отчет
	 * @param event
	 */
    @FXML
    void DeleteRep(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(27l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			AP_REPORT_TYPE rep_tp = ap_report_type_in.getSelectionModel().getSelectedItem();
			ODB_GROUP_USR grp_act = grp.getSelectionModel().getSelectedItem();
			AP_REPORT_CAT rep_out = ap_report_cat_in.getSelectionModel().getSelectedItem();
			if (rep_tp != null & grp_act != null & rep_out != null) {
				PreparedStatement prp = conn.prepareStatement("delete from AP_GROUP_REPORT_CAT_ROLE\n" + 
						" where GROUP_ID = ?\n" + 
						"   and REPORT_TYPE_ID = ?\n" + 
						"   and REPORT_ID = ?\n" + 
						"");
				prp.setLong(1, grp_act.getGRP_ID());
				prp.setLong(2, rep_tp.getREPORT_TYPE_ID());
				prp.setLong(3, rep_out.getREPORT_ID());
				prp.executeUpdate();
				prp.close();
				conn.commit();
				InitRepIn(rep_tp.getREPORT_TYPE_ID(), grp_act.getGRP_ID());
				InitRepOut(rep_tp.getREPORT_TYPE_ID(), grp_act.getGRP_ID());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
    }
    
	void fillTreeMnu() {
		Map<Long, TreeItem<ODB_MNU>> itemById = new HashMap<>();
		Map<Long, Long> parents = new HashMap<>();
		String query = "select * from ODB_MNU";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				adb_act = new ODB_MNU();
				adb_act.setMNU_ID(rs.getLong("MNU_ID"));
				adb_act.setMNU_NPP(rs.getLong("MNU_NPP"));
				adb_act.setMNU_PARENT(rs.getLong("MNU_PARENT"));
				adb_act.setMNU_NAME(rs.getString("MNU_NAME"));
				itemById.put(rs.getLong("MNU_ID"), new TreeItem<>(adb_act));
				parents.put(rs.getLong("MNU_ID"), rs.getLong("MNU_PARENT"));
			}
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Map.Entry<Long, TreeItem<ODB_MNU>> entry : itemById.entrySet()) {
			Long key = entry.getKey();
			Long parent = parents.get(key);
			if (parent.equals(key)) {
				// in case the root item points to itself, this is it
				root = entry.getValue();
				root.setExpanded(true);
			} else {
				TreeItem<ODB_MNU> parentItem = itemById.get(parent);
				if (parentItem == null) {
					// in case the root item has no parent in the result set, this is it
					root = entry.getValue();
				} else {
					// add to parent tree item
					parentItem.getChildren().add(entry.getValue());
				}
				parentItem.setExpanded(true);
			}
		}
		// root.setExpanded(true);
		MNU.setRoot(root);
	}

	
	void fillTreeAct() {
		Map<Long, TreeItem<ODB_ACTION>> itemById = new HashMap<>();
		Map<Long, Long> parents = new HashMap<>();
		String query = "select * from odb_action_sb";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				adb_act_act = new ODB_ACTION();
				adb_act_act.setACT_ID(rs.getLong("ACT_ID"));
				adb_act_act.setACT_NPP(rs.getLong("ACT_NPP"));
				adb_act_act.setACT_PARENT(rs.getLong("ACT_PARENT"));
				adb_act_act.setACT_NAME(rs.getString("ACT_NAME"));
				itemById.put(rs.getLong("ACT_ID"), new TreeItem<>(adb_act_act));
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
				root_root = entry.getValue();
				root_root.setExpanded(true);
			} else {
				TreeItem<ODB_ACTION> parentItem = itemById.get(parent);
				if (parentItem == null) {
					// in case the root item has no parent in the result set, this is it
					root_root = entry.getValue();
				} else {
					// add to parent tree item
					parentItem.getChildren().add(entry.getValue());
				}
				parentItem.setExpanded(true);
			}
		}
		// root.setExpanded(true);
		Actions.setRoot(root_root);
	}
	
	/**
	 * Пользователи вне группы
	 */
	void InitUsrOut(Long grp_id) {
		try {
			PreparedStatement prepStmt = conn.prepareStatement("select usr.cusrlogname, usr.cusrname\n"
					+ "  from usr\n" + " where not exists (select null\n" + "          from ODB_GRP_MEMBER mem\n"
					+ "         where mem.iusrid = usr.iusrid\n" + "           and mem.grp_id = ?)"
							+ " and usr.dusrfire is null\n");
			prepStmt.setLong(1, grp_id);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<USR_IN_OUT> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				USR_IN_OUT list = new USR_IN_OUT();
				list.setLOGIN(rs.getString("cusrlogname"));
				list.setNAME(rs.getString("cusrname"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();
			usrout.setItems(dlist);
			TableFilter<USR_IN_OUT> tableFilter = TableFilter.forTableView(usrout).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Пользователи в группе
	 */
	void InitUsrIn(Long grp_id) {
		try {
			PreparedStatement prepStmt = conn.prepareStatement("select usr.cusrlogname, usr.cusrname\n"
					+ "  from usr\n" + " where exists (select null\n" + "          from ODB_GRP_MEMBER mem\n"
					+ "         where mem.iusrid = usr.iusrid\n" + "           and mem.grp_id = ?)\n");
			prepStmt.setLong(1, grp_id);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<USR_IN_OUT> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				USR_IN_OUT list = new USR_IN_OUT();
				list.setLOGIN(rs.getString("cusrlogname"));
				list.setNAME(rs.getString("cusrname"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();
			usrin.setItems(dlist);
			TableFilter<USR_IN_OUT> tableFilter = TableFilter.forTableView(usrin).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Обновить таблицу с группами
	 */
	void InitGrp() {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("select grp_id, grp_name, NAME from ODB_GROUP_USR t order by grp_id desc\n" + "");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<ODB_GROUP_USR> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				ODB_GROUP_USR list = new ODB_GROUP_USR();
				list.setGRP_ID(rs.getLong("GRP_ID"));
				list.setGRP_NAME(rs.getString("GRP_NAME"));
				list.setNAME(rs.getString("NAME"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			grp.setItems(dlist);

			TableFilter<ODB_GROUP_USR> tableFilter = TableFilter.forTableView(grp).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Обновить таблицу с типами отчетов
	 */
	void InitRepTpIn(Long grp_id) {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("SELECT GROUP_ID, REPORT_TYPE_ID, REPORT_TYPE_NAME\n" + 
							"  FROM (select AP_Group_Report_Role.*, AP_Report_Type.report_type_name\n" + 
							"          from AP_Group_Report_Role, AP_Report_Type\n" + 
							"         where AP_Report_Type.report_type_id =\n" + 
							"               AP_Group_Report_Role.report_type_id)\n" + 
							" WHERE (GROUP_ID = ?)\n" + 
							" order by report_type_id\n" + 
							"");
			prepStmt.setLong(1, grp_id);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_TYPE> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_TYPE list = new AP_REPORT_TYPE();
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_TYPE_NAME(rs.getString("REPORT_TYPE_NAME"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ap_report_type_in.setItems(dlist);

			TableFilter<AP_REPORT_TYPE> tableFilter = TableFilter.forTableView(ap_report_type_in).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	void InitRep() {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("select * from ap_report_type");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_TYPE> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_TYPE list = new AP_REPORT_TYPE();
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_TYPE_NAME(rs.getString("REPORT_TYPE_NAME"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ap_report_type.setItems(dlist);

			TableFilter<AP_REPORT_TYPE> tableFilter = TableFilter.forTableView(ap_report_type).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	void InitRepTpOut(Long grp_id) {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("SELECT GROUP_ID, REPORT_TYPE_ID, REPORT_TYPE_NAME\n" + 
							"  FROM (select ODB_Group_USR.grp_id group_id, AP_Report_Type.*\n" + 
							"          from ODB_Group_USR, AP_Report_Type\n" + 
							"         where not exists\n" + 
							"         (select null\n" + 
							"                  from AP_Group_Report_Role\n" + 
							"                 where AP_Group_Report_Role.group_id = ODB_Group_USR.grp_id\n" + 
							"                   and AP_Group_Report_Role.report_type_id =\n" + 
							"                       AP_Report_Type.report_type_id))\n" + 
							" WHERE (GROUP_ID = ?)\n" + 
							" order by report_type_id\n" + 
							"");
			prepStmt.setLong(1, grp_id);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_TYPE> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_TYPE list = new AP_REPORT_TYPE();
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_TYPE_NAME(rs.getString("REPORT_TYPE_NAME"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ap_report_type_out.setItems(dlist);

			TableFilter<AP_REPORT_TYPE> tableFilter = TableFilter.forTableView(ap_report_type_out).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	void InitRepIn(Long rep_tp_id, Long grp_id) {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("SELECT GROUP_ID, REPORT_TYPE_ID, REPORT_ID, REPORT_NAME\n" + 
							"  FROM (select AP_Group_Report_Cat_Role.*, AP_Report_Cat.report_name\n" + 
							"          from AP_Group_Report_Cat_Role, AP_Report_Cat\n" + 
							"         where AP_Report_Cat.report_type_id =\n" + 
							"               AP_Group_Report_Cat_Role.report_type_id\n" + 
							"           and AP_Report_Cat.report_id = AP_Group_Report_Cat_Role.report_id)\n" + 
							" WHERE (GROUP_ID = ?)\n" + 
							"   and (REPORT_TYPE_ID = ?)\n" + 
							" order by report_id\n" + 
							"");
			prepStmt.setLong(1, grp_id);
			prepStmt.setLong(2, rep_tp_id);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_CAT> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_CAT list = new AP_REPORT_CAT();
				list.setREPORT_ID(rs.getLong("REPORT_ID"));
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_NAME(rs.getString("REPORT_NAME"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ap_report_cat_in.setItems(dlist);

			TableFilter<AP_REPORT_CAT> tableFilter = TableFilter.forTableView(ap_report_cat_in).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	void InitRepOut(Long rep_tp_id,Long grp_id) {
		try {
//			String sql = DbUtil.getResource("/mj/access/grp/InitRepOut");
			PreparedStatement prepStmt = conn
					.prepareStatement("SELECT GROUP_ID, REPORT_TYPE_ID, REPORT_ID, REPORT_NAME\n"
							+ "  FROM (select AP_Group_Report_Role.*,\n"
							+ "               AP_Report_Cat.report_id,\n"
							+ "               AP_Report_Cat.report_name\n"
							+ "          from AP_Group_Report_Role, AP_Report_Cat\n"
							+ "         where AP_Report_Cat.report_type_id =\n"
							+ "               AP_Group_Report_Role.report_type_id\n"
							+ "           and not exists (select null\n"
							+ "                  from AP_Group_Report_Cat_Role\n"
							+ "                 where AP_Group_Report_Cat_Role.group_id =\n"
							+ "                       AP_Group_Report_Role.group_id\n"
							+ "                   and AP_Group_Report_Cat_Role.report_type_id =\n"
							+ "                       AP_Report_Cat.report_type_id\n"
							+ "                   and AP_Group_Report_Cat_Role.report_id =\n"
							+ "                       AP_Report_Cat.report_id))\n"
							+ " WHERE (GROUP_ID = ?)\n"
							+ "   and (REPORT_TYPE_ID = ?)\n"
							+ " order by report_id");
			prepStmt.setLong(1, grp_id);
			prepStmt.setLong(2, rep_tp_id);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_CAT> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_CAT list = new AP_REPORT_CAT();
				list.setREPORT_ID(rs.getLong("REPORT_ID"));
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_NAME(rs.getString("REPORT_NAME"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ap_report_cat_out.setItems(dlist);

			TableFilter<AP_REPORT_CAT> tableFilter = TableFilter.forTableView(ap_report_cat_out).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	void RepIn() {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("select * from ap_report_cat t");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_CAT> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_CAT list = new AP_REPORT_CAT();
				list.setREPORT_ID(rs.getLong("REPORT_ID"));
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_NAME(rs.getString("REPORT_NAME"));
				list.setREPORT_UFS(rs.getString("REPORT_UFS"));
				list.setCOPIES(rs.getLong("COPIES"));
				list.setREPORT_VIEWER(rs.getLong("REPORT_VIEWER"));
				list.setREPORT_COMMENT(rs.getString("REPORT_COMMENT"));
				list.setEDIT_PARAM(rs.getString("EDIT_PARAM"));
				list.setOEM_DATA(rs.getString("OEM_DATA"));
				list.setAVAILABLE_SQL(rs.getString("AVAILABLE_SQL"));

				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ap_report_cat_out.setItems(dlist);

			TableFilter<AP_REPORT_CAT> tableFilter = TableFilter.forTableView(ap_report_cat_out).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	ODB_GROUP_USR InitGrp2(Long grp_id) {
		ODB_GROUP_USR list = null;
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("select grp_id, grp_name, NAME from ODB_GROUP_USR where grp_id = ?");
			prepStmt.setLong(1, grp_id);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				list = new ODB_GROUP_USR();
				list.setGRP_ID(rs.getLong("GRP_ID"));
				list.setGRP_NAME(rs.getString("grp_name"));
				list.setNAME(rs.getString("NAME"));
			}
			prepStmt.close();
			rs.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
}
