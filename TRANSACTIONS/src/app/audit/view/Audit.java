package app.audit.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import app.Main;
import app.sbalert.Msg;
import app.utils.DbUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Audit {

	public Audit() {
		Main.logger = Logger.getLogger(getClass());
	}
	
	private Executor exec;

	@FXML
	private VBox SplitPane;

	@FXML
	private Button AUFILTER;

	@FXML
	private TextField ID_ANUM;

	@FXML
	private TableView<V_AU_DATA> V_AU_DATA;

	@FXML
	private TableColumn<V_AU_DATA, String> CFIELD;

	@FXML
	private TableColumn<V_AU_DATA, String> COLDDATA;

	@FXML
	private TextField CAUDUSER;

	@FXML
	private TextField IAUDSESSION;

	@FXML
	private TextField Get_Col_Comment;

	@FXML
	private TableColumn<AU_ACTION, String> CAUDOPERATION;

	@FXML
	private TextField CAUDPROGRAM;

	@FXML
	private TableColumn<AU_ACTION, LocalDateTime> DAUDDATE;

	@FXML
	private TextField RROWID;

	@FXML
	private TableView<AU_ACTION> AU_ACTION;

	@FXML
	private TextField CTABLE;

	@FXML
	private TableColumn<V_AU_DATA, String> CNEWDATA;

	@FXML
	private TextField CAUDIP_ADDRESS;

	@FXML
	private TextField CAUDMACHINE;

	@FXML
	private TextField ID_NUM;

	private void INIT_AU_ACTION(String filter) {
		try {
			/* SelData */
			String selectStmt = "select * from AU_ACTION " + filter + " order by DAUDDATE desc";
			Main.logger.info(selectStmt);
			Connection conn = DbUtil.conn;
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AU_ACTION> au_action = FXCollections.observableArrayList();
			while (rs.next()) {
				AU_ACTION au = new AU_ACTION();
				au.setIACTION_ID(rs.getLong("IACTION_ID"));
				String DATE_LOAD = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getTimestamp("DAUDDATE"));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				au.setDAUDDATE(LocalDateTime.parse(DATE_LOAD, formatter));
				au.setCAUDUSER(rs.getString("CAUDUSER"));
				au.setCTABLE(rs.getString("CTABLE"));
				au.setCAUDMACHINE(rs.getString("CAUDMACHINE"));
				au.setCAUDPROGRAM(rs.getString("CAUDPROGRAM"));
				au.setCAUDOPERATION(rs.getString("CAUDOPERATION"));
				au.setRROWID(rs.getString("RROWID"));
				au.setCAUDACTION(rs.getString("CAUDACTION"));
				au.setCAUDMODULE(rs.getString("CAUDMODULE"));
				au.setIAUDSESSION(rs.getLong("IAUDSESSION"));
				au.setCAUDIP_ADDRESS(rs.getString("CAUDIP_ADDRESS"));
				au.setID_NUM(rs.getString("ID_NUM"));
				au.setID_ANUM(rs.getString("ID_ANUM"));
				au_action.add(au);
			}
			prepStmt.close();
			rs.close();
			AU_ACTION.setItems(au_action);
			// autoResizeColumns(AU_ACTION);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	private void initialize() {
		try {

			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			Connection conn = DbUtil.conn;
			/* AU_ACTION */
			DAUDDATE.setCellValueFactory(cellData -> cellData.getValue().DAUDDATEProperty());
			/* Format Date */
			DAUDDATE.setCellFactory(column -> {
				TableCell<AU_ACTION, LocalDateTime> cell = new TableCell<AU_ACTION, LocalDateTime>() {
					private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

					@Override
					protected void updateItem(LocalDateTime item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							setText(format.format(item));
						}
					}
				};
				return cell;
			});

			/******************/
			CAUDOPERATION.setCellValueFactory(cellData -> cellData.getValue().CAUDOPERATIONProperty());
			/* V_AU_DATA */
			CFIELD.setCellValueFactory(cellData -> cellData.getValue().CFIELDProperty());
			COLDDATA.setCellValueFactory(cellData -> cellData.getValue().COLDDATAProperty());
			CNEWDATA.setCellValueFactory(cellData -> cellData.getValue().CNEWDATAProperty());
			/* Filter */
			TableFilter<AU_ACTION> tableFilter = TableFilter.forTableView(AU_ACTION).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});

			/* Listener */
			V_AU_DATA.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					try {
						AU_ACTION act = AU_ACTION.getSelectionModel().getSelectedItem();
						V_AU_DATA data = V_AU_DATA.getSelectionModel().getSelectedItem();
						String colcomment = "select AUP_UTIL.Get_Col_Comment(Tab_Name => ?,\n"
								+ "                                      Tab_Owner => ?,\n"
								+ "                                      Field_Name => ?) from dual";
						PreparedStatement pst = conn.prepareStatement(colcomment);
						pst.setString(1, act.getCTABLE());
						pst.setString(2, "XXI");
						pst.setString(3, data.getCFIELD());
						ResultSet res = pst.executeQuery();
						if (res.next()) {
							Get_Col_Comment.setText(res.getString(1));
						}
						pst.close();
						res.close();
					} catch (Exception e) {
						DbUtil.Log_Error(e);
					}
				}
			});

			AU_ACTION.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					try {
						AU_ACTION auc = AU_ACTION.getSelectionModel().getSelectedItem();
						String seldata = "select * from V_AU_DATA where IACTION_ID = ?";
						PreparedStatement pst = conn.prepareStatement(seldata);
						pst.setLong(1, auc.getIACTION_ID());
						ResultSet res = pst.executeQuery();
						ObservableList<V_AU_DATA> au_action2 = FXCollections.observableArrayList();
						while (res.next()) {
							V_AU_DATA au = new V_AU_DATA();
							au.setIACTION_ID(res.getLong("IACTION_ID"));
							au.setCFIELD(res.getString("CFIELD"));
							au.setCNEWDATA(res.getString("CNEWDATA"));
							au.setCOLDDATA(res.getString("COLDDATA"));
							au_action2.add(au);
						}
						pst.close();
						res.close();

						CAUDUSER.setText(auc.getCAUDUSER());
						CTABLE.setText(auc.getCTABLE());
						CAUDMACHINE.setText(auc.getCAUDMACHINE());
						CAUDPROGRAM.setText(auc.getCAUDPROGRAM());
						CAUDOPERATION.setText(auc.getCAUDOPERATION());
						RROWID.setText(auc.getRROWID());
						IAUDSESSION.setText(String.valueOf(auc.getIAUDSESSION()));
						CAUDIP_ADDRESS.setText(auc.getCAUDIP_ADDRESS());
						ID_NUM.setText(auc.getID_NUM());
						ID_ANUM.setText(auc.getID_ANUM());

						V_AU_DATA.setItems(au_action2);
						autoResizeColumns(V_AU_DATA);
						/* Filter */
						TableFilter<V_AU_DATA> tableFilter2 = TableFilter.forTableView(V_AU_DATA).apply();
						tableFilter2.setSearchStrategy((input, target) -> {
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
			});
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void filter(ActionEvent event) {
		try {
			/*******/
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/app/audit/view/AuditFilter.fxml"));

			AuditFilter controller = new AuditFilter();

			loader.setController(controller);

			Parent root = loader.load();

			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("Ôèëüòð");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(false);
			stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					if (controller.getStatus()) {
						INIT_AU_ACTION(controller.getFilter());
					}
				}
			});
			stage.show();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	private ProgressIndicator PB;

	void BlockMain() {
		SplitPane.setDisable(false);
		PB.setVisible(false);
	}

	@FXML
	void Print(ActionEvent event) {
		try {
			if (AU_ACTION.getSelectionModel().getSelectedItem() != null) {
				Main.logger = Logger.getLogger(getClass());
				SplitPane.setDisable(true);
				PB.setVisible(true);
				Task<Object> task = new Task<Object>() {
					@Override
					public Object call() throws Exception {
						AU_ACTION auact = AU_ACTION.getSelectionModel().getSelectedItem();
						new PrintReport2().showReport(auact.getCTABLE(), auact.getIACTION_ID());
						return null;
					}
				};
				task.setOnFailed(e -> Msg.Message(task.getException().getMessage()));
				task.setOnSucceeded(e -> BlockMain());
				exec.execute(task);
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
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
}
