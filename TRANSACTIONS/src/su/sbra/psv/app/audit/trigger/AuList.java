package su.sbra.psv.app.audit.trigger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

public class AuList {

	public AuList() {
		Main.logger = Logger.getLogger(getClass());
	}
	
	@FXML
	private TableColumn<AU_TABLE, Boolean> CMODE;
	@FXML
	private TableColumn<AU_TABLE, String> STATUS;
	@FXML
	private ProgressIndicator PB;
	@FXML
	private VBox ROOT;
	@FXML
	private TableColumn<AU_TABLE, String> TABLENAME;
	@FXML
	private TableColumn<AU_TABLE, String> CNAME;
	@FXML
	private TableView<AU_TABLE> AU_TABLE;

	@FXML
	void CreateTrigger(ActionEvent event) {
		try {
			
//			if (DbUtil.Odb_Action(154l) == 0) {
//				Msg.Message("Нет доступа!");
//				return;
//			}
			
			if (AU_TABLE.getSelectionModel().getSelectedItem() != null) {
				Main.logger = Logger.getLogger(getClass());
				Stage stage = new Stage();
				Stage stage_ = (Stage) ROOT.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/su/sbra/psv/app/audit/trigger/IUAu.fxml"));

				Trigger controller = new Trigger();
				loader.setController(controller);

				controller.SetTableName(AU_TABLE.getSelectionModel().getSelectedItem().getCNAME());

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Исходный текст триггера");
				stage.initOwner(stage_);
				stage.setResizable(false);

				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						Refresh();
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void FindTriggers(ActionEvent event) {

	}

	void TableList(String type) {
		try {
			Main.logger = Logger.getLogger(getClass());
			Button Update = new Button();
			Update.setText("Выбрать");
			// Update.setLayoutX(30.0);
			// Update.setLayoutY(450.0);
			AnchorPane secondaryLayout = new AnchorPane();

			VBox vb = new VBox();
			
			ButtonBar buttonBar = new ButtonBar();
		      //Adding the buttons to the button bar
		    ButtonBar.setButtonData(Update, ButtonData.APPLY);
		    buttonBar.getButtons().addAll(Update);
			
						
			TableView<ALL_TABLE> ALL_TABLE = new TableView<ALL_TABLE>();
			TableColumn<ALL_TABLE, String> TABLE_NAME = new TableColumn<>("Название таблицы");
			TABLE_NAME.setCellValueFactory(new PropertyValueFactory<>("TABLE_NAME"));
			TableColumn<ALL_TABLE, String> TABLECOMMENT = new TableColumn<>("Комментарий к таблице");
			TABLECOMMENT.setCellValueFactory(new PropertyValueFactory<>("TABLECOMMENT"));
			ALL_TABLE.getColumns().add(TABLE_NAME);
			ALL_TABLE.getColumns().add(TABLECOMMENT);

			vb.getChildren().add(ALL_TABLE);
			vb.getChildren().add(buttonBar);

			ALL_TABLE.getStyleClass().add("mylistview");
			ALL_TABLE.getStylesheets().add("/ScrPane.css");

			vb.setPadding(new Insets(5, 5, 5, 5));
			vb.setSpacing(5);
			/**/
			TABLE_NAME.setCellValueFactory(cellData -> cellData.getValue().TABLE_NAMEProperty());
			TABLECOMMENT.setCellValueFactory(cellData -> cellData.getValue().TABLECOMMENTProperty());

			/* SelData */
			String selectStmt = "select * from ALL_TABLE where TABLE_NAME not in (select g.cname from au_tables g)";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<ALL_TABLE> cuslist = FXCollections.observableArrayList();
			while (rs.next()) {
				ALL_TABLE list = new ALL_TABLE();
				list.setTABLE_NAME(rs.getString("TABLE_NAME"));
				list.setTABLECOMMENT(rs.getString("TABLECOMMENT"));
				cuslist.add(list);
			}
			prepStmt.close();
			rs.close();

			ALL_TABLE.setItems(cuslist);

			// Main.autoResizeColumns(ALL_TABLE);

			ALL_TABLE.setPrefWidth(500);
			ALL_TABLE.setPrefHeight(350);

			TABLE_NAME.setPrefWidth(150);
			TABLECOMMENT.setPrefWidth(350);

			TableFilter<ALL_TABLE> CUSFilter = TableFilter.forTableView(ALL_TABLE).apply();
			CUSFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			/**/
			Update.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (ALL_TABLE.getSelectionModel().getSelectedItem() == null) {
						Msg.Message("Выберите данные из таблицы!");
					} else {
						ALL_TABLE all_tbl = ALL_TABLE.getSelectionModel().getSelectedItem();

						if (type.equals("add")) {
							try {
								PreparedStatement delete = conn.prepareStatement(
										"declare "+ 
								        "pragma autonomous_transaction;" + 
										"begin "+ 
										" insert into  AU_TABLES(CNAME,CMODE) values (?,?);" + 
										"commit;" + 
										"end;");
								delete.setString(1, all_tbl.getTABLE_NAME());
								delete.setString(2, "Y");
								delete.executeUpdate();
								delete.close();
								Refresh();
							} catch (SQLException e) {
								try {
									conn.rollback();
								} catch (SQLException e1) {
									e.printStackTrace();
									Msg.Message(ExceptionUtils.getStackTrace(e1));
									Main.logger.error(ExceptionUtils.getStackTrace(e1) + "~" + Thread.currentThread().getName());
								}
								DbUtil.Log_Error(e);
							}
						}
						((Node) (event.getSource())).getScene().getWindow().hide();
					}
				}
			});

			secondaryLayout.getChildren().add(vb);
			// secondaryLayout.getChildren().add(cusllists);

			// VBox vbox = new VBox(debtinfo);
			Scene secondScene = new Scene(secondaryLayout, Control.USE_COMPUTED_SIZE,
					Control.USE_COMPUTED_SIZE);/* Control.USE_COMPUTED_SIZE */
			Stage stage = (Stage) ROOT.getScene().getWindow();

			Stage newWindow = new Stage();
			newWindow.setTitle("Список таблиц");
			newWindow.setScene(secondScene);
			newWindow.setResizable(false);
			// Specifies the modality for new window.
			newWindow.initModality(Modality.WINDOW_MODAL);
			// Specifies the owner Window (parent) for new window
			newWindow.initOwner(stage);
			newWindow.getIcons().add(new Image("/icon.png"));
			newWindow.show();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * добавить
	 */
	void Add() {
		try {
			if (DbUtil.Odb_Action(152l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			TableList("add");
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * удалить
	 */
	void Delete() {
		try {
			if (DbUtil.Odb_Action(153l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (AU_TABLE.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите строку!");
			} else {
				Main.logger = Logger.getLogger(getClass());

				Stage stage = (Stage) AU_TABLE.getScene().getWindow();
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
							PreparedStatement delete = conn
									.prepareStatement("declare " + "pragma autonomous_transaction;" + "begin "
											+ " delete from AU_TABLES where CNAME = ?;" + "commit;" + "end;");
							AU_TABLE cl = AU_TABLE.getSelectionModel().getSelectedItem();
							delete.setString(1, cl.getCNAME());
							delete.executeUpdate();
							delete.close();
							Refresh();
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
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Открыта ли форма редактирования <br>
	 * Возможен только один экземпляр формы
	 */
	boolean isopen = false;

	/**
	 * Печать
	 */
	void Print() {
		try {

		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Обновить таблицу
	 */
	void Refresh() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main.logger = Logger.getLogger(getClass());
					String selectStmt = " select * from AU_TABLE t";
					PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
					ResultSet rs = prepStmt.executeQuery();
					ObservableList<AU_TABLE> dlist = FXCollections.observableArrayList();
					while (rs.next()) {
						AU_TABLE list = new AU_TABLE();
						list.setCMODE((rs.getString("CMODE").equals("Y") ? true : false));
						list.setTABLENAME(rs.getString("TABLENAME"));
						list.setSTATUS(rs.getString("STATUS"));
						list.setCNAME(rs.getString("CNAME"));
						dlist.add(list);
					}
					prepStmt.close();
					rs.close();

					AU_TABLE.setItems(dlist);

					TableFilter<AU_TABLE> tableFilter = TableFilter.forTableView(AU_TABLE).apply();
					tableFilter.setSearchStrategy((input, target) -> {
						try {
							return target.toLowerCase().contains(input.toLowerCase());
						} catch (Exception e) {
							return false;
						}
					});
					Label firstNameLabel = new Label("В");
					firstNameLabel.setTooltip(new Tooltip("Включить/Выключить аудирование таблицы"));
					CMODE.setGraphic(firstNameLabel);

					// ==== SINGLE? (CHECH BOX) ===
					CMODE.setCellValueFactory(
							new Callback<CellDataFeatures<AU_TABLE, Boolean>, ObservableValue<Boolean>>() {

								@Override
								public ObservableValue<Boolean> call(CellDataFeatures<AU_TABLE, Boolean> param) {
									AU_TABLE person = param.getValue();

									SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(person.getCMODE());

									// Note: singleCol.setOnEditCommit(): Not work for
									// CheckBoxTableCell.

									// When "Single?" column change.
									booleanProp.addListener(new ChangeListener<Boolean>() {

										@Override
										public void changed(ObservableValue<? extends Boolean> observable,
												Boolean oldValue, Boolean newValue) {
											person.setCMODE(newValue);

											AU_TABLE.getSelectionModel().select(person);
											System.out
													.println(AU_TABLE.getSelectionModel().getSelectedItem().getCMODE());
											try {
												PreparedStatement delete = conn.prepareStatement(
														"declare " + "pragma autonomous_transaction;" + "begin "
																+ " update  AU_TABLES set CMODE = ? where CNAME = ?;"
																+ "commit;" + "end;");
												AU_TABLE cl = AU_TABLE.getSelectionModel().getSelectedItem();
												delete.setString(1, (newValue) ? "Y" : "N");
												delete.setString(2, cl.getCNAME());
												delete.executeUpdate();
												delete.close();
												// Refresh();
											} catch (SQLException e) {
												try {
													conn.rollback();
												} catch (SQLException e1) {
													e.printStackTrace();
													Msg.Message(ExceptionUtils.getStackTrace(e1));
													Main.logger.error(
															ExceptionUtils.getStackTrace(e1) + "~" + Thread.currentThread().getName());
												}
												DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
												Main.logger
														.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
											}
										}
									});
									return booleanProp;
								}
							});

					CMODE.setCellFactory(new Callback<TableColumn<AU_TABLE, Boolean>, //
							TableCell<AU_TABLE, Boolean>>() {
						@Override
						public TableCell<AU_TABLE, Boolean> call(TableColumn<AU_TABLE, Boolean> p) {
							CheckBoxTableCell<AU_TABLE, Boolean> cell = new CheckBoxTableCell<AU_TABLE, Boolean>();
							cell.setAlignment(Pos.CENTER);
							return cell;
						}
					});

					STATUS.setCellFactory(col -> new TextFieldTableCell<AU_TABLE, String>() {
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setText(null);
								setGraphic(null);
								setStyle("");
							} else {
								setText(item.toString());
								if (item.equals("VALID")) {
									setStyle(
											"-fx-background-color: rgb(162, 189, 48);-fx-border-color:black;-fx-border-width :  0.5 0.5 0.5 0.5 ");
								} else {
									setStyle("-fx-background-color: #D24141;");
								}
							}
						}
					});

				} catch (Exception e) {
					DbUtil.Log_Error(e);
				}
			}
		});
	}

	/**
	 * км обновить
	 * 
	 * @param event
	 */
	@FXML
	void CmRefresh(ActionEvent event) {
		Refresh();
	}

	/**
	 * кс добавить
	 * 
	 * @param event
	 */
	@FXML
	void CmAdd(ActionEvent event) {
		Add();
	}

	/**
	 * км печать
	 * 
	 * @param event
	 */
	@FXML
	void CmPrint(ActionEvent event) {
		Print();
	}

	/**
	 * км удалить
	 * 
	 * @param event
	 */
	@FXML
	void CmDelete(ActionEvent event) {
		Delete();
	}

	/**
	 * добавть
	 * 
	 * @param event
	 */
	@FXML
	void BtAdd(ActionEvent event) {
		Add();
	}

	/**
	 * Кн удалить
	 * 
	 * @param event
	 */
	@FXML
	void BtDelete(ActionEvent event) {
		Delete();
	}

	/**
	 * Кнопка печать
	 * 
	 * @param event
	 */
	@FXML
	void BtPrint(ActionEvent event) {
		Print();
	}

	/**
	 * Подключение к базе
	 */
	private Connection conn;

	/**
	 * Открыть сессию
	 * @throws UnknownHostException 
	 */
	private void dbConnect() throws UnknownHostException {
		try {
			Main.logger = Logger.getLogger(getClass());
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
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Закрыть сессию
	 */
	public void dbDisconnect() {
		try {
			Main.logger = Logger.getLogger(getClass());
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Форматирование даты и времени
	 * 
	 * @param tc
	 */
	void CellDateFormatDT(TableColumn<AU_TABLE, LocalDateTime> tc) {
		tc.setCellFactory(column -> {
			TableCell<AU_TABLE, LocalDateTime> cell = new TableCell<AU_TABLE, LocalDateTime>() {
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
	}

	/**
	 * Форматирование даты
	 * 
	 * @param tc
	 */
	void CellDateFormatD(TableColumn<AU_TABLE, LocalDate> tc) {
		tc.setCellFactory(column -> {
			TableCell<AU_TABLE, LocalDate> cell = new TableCell<AU_TABLE, LocalDate>() {
				private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null) {
							setText(format.format(item));
						}
					}
				}
			};
			return cell;
		});
	}

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			Main.logger = Logger.getLogger(getClass());

			dbConnect();
			
			Refresh();

			/**
			 * Столбцы таблицы
			 */
			{
				CMODE.setCellValueFactory(cellData -> cellData.getValue().CMODEProperty());
				STATUS.setCellValueFactory(cellData -> cellData.getValue().STATUSProperty());
				TABLENAME.setCellValueFactory(cellData -> cellData.getValue().TABLENAMEProperty());
				CNAME.setCellValueFactory(cellData -> cellData.getValue().CNAMEProperty());
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

}
