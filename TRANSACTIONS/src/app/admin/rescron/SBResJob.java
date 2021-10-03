package app.admin.rescron;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;

import app.sbalert.Msg;
import app.tr.pl.ConvConst;
import app.util.DBUtil;
import app.utils.DbUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SBResJob {
	// tbl
	@FXML
	private TableView<VSB_CERT_EXP> SB_CERT_EXP;
	@FXML
	private TableColumn<VSB_CERT_EXP, Long> CRTID;
	@FXML
	private TableColumn<VSB_CERT_EXP, String> CERTNAME;
	@FXML
	private TableColumn<Object, LocalDate> CERTBEG;
	@FXML
	private TableColumn<Object, LocalDate> CERTEND;
	@FXML
	private TableColumn<VSB_CERT_EXP, String> CERTRES;
	@FXML
	private TableColumn<VSB_CERT_EXP, String> CERTGRP;
	@FXML
	private TableColumn<VSB_CERT_EXP, String> CERTSTAT;
	// grp
	@FXML
	private TableView<SB_CERT_EXP_GRP> SB_CERT_EXP_GRP;
	@FXML
	private TableColumn<SB_CERT_EXP_GRP, Long> GRP_ID;
	@FXML
	private TableColumn<SB_CERT_EXP_GRP, String> GRP_NAME;
	// prm
	@FXML
	private TableView<SB_CERT_EXP_PRM> SB_CERT_EXP_PRM;
	@FXML
	private TableColumn<SB_CERT_EXP_PRM, String> PRMNAME;
	@FXML
	private TableColumn<SB_CERT_EXP_PRM, String> PRMVAL;

	@FXML
	private void initialize() {
		try {
			CRTID.setCellValueFactory(cellData -> cellData.getValue().CRTIDProperty().asObject());
			CERTNAME.setCellValueFactory(cellData -> cellData.getValue().CERTNAMEProperty());
			CERTBEG.setCellValueFactory(cellData -> ((VSB_CERT_EXP) cellData.getValue()).CERTBEGProperty());
			CERTEND.setCellValueFactory(cellData -> ((VSB_CERT_EXP) cellData.getValue()).CERTENDProperty());
			CERTRES.setCellValueFactory(cellData -> cellData.getValue().CERTRESProperty());
			CERTGRP.setCellValueFactory(cellData -> cellData.getValue().CERTGRPProperty());
			CERTSTAT.setCellValueFactory(cellData -> cellData.getValue().CERTSTATProperty());
			// _______________________________________
			GRP_ID.setCellValueFactory(cellData -> cellData.getValue().GRP_IDProperty().asObject());
			GRP_NAME.setCellValueFactory(cellData -> cellData.getValue().GRP_NAMEProperty());
			// _______________________________________
			PRMNAME.setCellValueFactory(cellData -> cellData.getValue().PRMNAMEProperty());
			PRMVAL.setCellValueFactory(cellData -> cellData.getValue().PRMVALProperty());
			// _______________________________________
			Res_LoadTable();
			Prm_LoadTable();
			Grp_LoadTable();
			// _____________
			SB_CERT_EXP.setRowFactory(tv -> {
				TableRow<VSB_CERT_EXP> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Res_Edit(null);
					}
				});
				return row;
			});
			// _____________
			SB_CERT_EXP_GRP.setRowFactory(tv -> {
				TableRow<SB_CERT_EXP_GRP> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Grp_Edit(null);
					}
				});
				return row;
			});
			// _____________
			SB_CERT_EXP_PRM.setRowFactory(tv -> {
				TableRow<SB_CERT_EXP_PRM> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Prm_Edit(null);
					}
				});
				return row;
			});
			// _______________________________________
			new ConvConst().TableColumnDate(CERTBEG);
			new ConvConst().TableColumnDate(CERTEND);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Initialize table
	 */
	void Res_LoadTable() {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			String selectStmt = "select * from VSB_CERT_EXP t order by crtid";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<VSB_CERT_EXP> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				VSB_CERT_EXP list = new VSB_CERT_EXP();
				list.setCRTID(rs.getLong("CRTID"));
				list.setCERTNAME(rs.getString("CERTNAME"));
				list.setCERTBEG((rs.getDate("CERTBEG") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("CERTBEG")), formatter)
						: null);
				list.setCERTEND((rs.getDate("CERTEND") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("CERTEND")), formatter)
						: null);
				list.setCERTRES(rs.getString("CERTRES"));
				list.setCERTGRP(rs.getString("CERTGRP"));
				list.setCERTSTAT(rs.getString("CERTSTAT"));
				cus_list.add(list);
			}
			// add data
			SB_CERT_EXP.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<VSB_CERT_EXP> tableFilter = TableFilter.forTableView(SB_CERT_EXP).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SB_CERT_EXP);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void Grp_LoadTable() {
		try {
			String selectStmt = "select * from SB_CERT_EXP_GRP t order by GRP_ID";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SB_CERT_EXP_GRP> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				SB_CERT_EXP_GRP list = new SB_CERT_EXP_GRP();
				list.setGRP_NAME(rs.getString("GRP_NAME"));
				list.setGRP_ID(rs.getLong("GRP_ID"));
				cus_list.add(list);
			}
			// add data
			SB_CERT_EXP_GRP.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<SB_CERT_EXP_GRP> tableFilter = TableFilter.forTableView(SB_CERT_EXP_GRP).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SB_CERT_EXP_GRP);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void Prm_LoadTable() {
		try {
			String selectStmt = "select * from SB_CERT_EXP_PRM t order by PRMNAME";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SB_CERT_EXP_PRM> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				SB_CERT_EXP_PRM list = new SB_CERT_EXP_PRM();
				list.setPRMVAL(rs.getString("PRMVAL"));
				list.setPRMNAME(rs.getString("PRMNAME"));
				cus_list.add(list);
			}
			// add data
			SB_CERT_EXP_PRM.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<SB_CERT_EXP_PRM> tableFilter = TableFilter.forTableView(SB_CERT_EXP_PRM).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SB_CERT_EXP_PRM);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Авто расширение
	 * 
	 * @param table
	 */
	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("Статус")) {

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
				column.setPrefWidth(max + 20.0d);
			}
		});
	}

	// act
	@FXML
	void Res_Add(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(143l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			Stage stage = new Stage();
			Stage stage_ = (Stage) SB_CERT_EXP.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/app/admin/rescron/IU_Res.fxml"));

			Add_Res controller = new Add_Res();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Добавить ресурс");
			stage.initOwner(stage_);
			stage.setResizable(true);
			// stage.initModality(Modality.WINDOW_MODAL);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					Res_LoadTable();
				}
			});
			stage.show();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Res_Del(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(145l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (SB_CERT_EXP.getSelectionModel().getSelectedItem() != null) {
				VSB_CERT_EXP sel = SB_CERT_EXP.getSelectionModel().getSelectedItem();
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить \"" + sel.getCERTNAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = DBUtil.conn.prepareStatement(" delete from SB_CERT_EXP where crtid = ?");
					prp.setLong(1, sel.getCRTID());
					prp.executeUpdate();
					prp.close();
				}
			}
			Res_LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Res_Edit(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(144l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (SB_CERT_EXP.getSelectionModel().getSelectedItem() != null) {

				VSB_CERT_EXP sel = SB_CERT_EXP.getSelectionModel().getSelectedItem();

				Stage stage = new Stage();
				Stage stage_ = (Stage) SB_CERT_EXP.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/app/admin/rescron/IU_Res.fxml"));

				Edit_Res controller = new Edit_Res();
				controller.SetClass(sel);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Редактировать: " + sel.getCERTNAME());
				stage.initOwner(stage_);
				stage.setResizable(true);
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						Res_LoadTable();
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Res_Refresh(ActionEvent event) {
		try {
			Res_LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Prm_Add(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(147l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			Stage stage = new Stage();
			Stage stage_ = (Stage) SB_CERT_EXP.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/app/admin/rescron/IU_Prm.fxml"));

			Add_Prm controller = new Add_Prm();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Добавить параметр");
			stage.initOwner(stage_);
			stage.setResizable(true);
			// stage.initModality(Modality.WINDOW_MODAL);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					Prm_LoadTable();
				}
			});
			stage.show();
			
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Prm_Delete(ActionEvent event) {
		try {

			if (DbUtil.Odb_Action(153l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			if (SB_CERT_EXP_PRM.getSelectionModel().getSelectedItem() != null) {
				SB_CERT_EXP_PRM sel = SB_CERT_EXP_PRM.getSelectionModel().getSelectedItem();
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить \"" + sel.getPRMNAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = DBUtil.conn
							.prepareStatement(" delete from SB_CERT_EXP_PRM where PRMNAME = ?");
					prp.setString(1, sel.getPRMNAME());
					prp.executeUpdate();
					prp.close();
				}
			}
			Prm_LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Grp_Add(ActionEvent event) {
		try {
			
			if (DbUtil.Odb_Action(151l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			Stage stage = new Stage();
			Stage stage_ = (Stage) SB_CERT_EXP.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/app/admin/rescron/IU_Grp.fxml"));

			Add_Grp controller = new Add_Grp();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Добавить группу");
			stage.initOwner(stage_);
			stage.setResizable(true);
			// stage.initModality(Modality.WINDOW_MODAL);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					Grp_LoadTable();
				}
			});
			stage.show();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Grp_Delete(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(153l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (SB_CERT_EXP_GRP.getSelectionModel().getSelectedItem() != null) {
				SB_CERT_EXP_GRP sel = SB_CERT_EXP_GRP.getSelectionModel().getSelectedItem();
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить \"" + sel.getGRP_NAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = DBUtil.conn
							.prepareStatement(" delete from SB_CERT_EXP_GRP where GRP_ID = ?");
					prp.setLong(1, sel.getGRP_ID());
					prp.executeUpdate();
					prp.close();
				}
			}
			Grp_LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Grp_Edit(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(152l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (SB_CERT_EXP_GRP.getSelectionModel().getSelectedItem() != null) {

				SB_CERT_EXP_GRP sel = SB_CERT_EXP_GRP.getSelectionModel().getSelectedItem();

				Stage stage = new Stage();
				Stage stage_ = (Stage) SB_CERT_EXP_GRP.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/app/admin/rescron/IU_Grp.fxml"));

				Edit_Grp controller = new Edit_Grp();
				controller.SetClass(sel);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Редактировать: " + sel.getGRP_NAME());
				stage.initOwner(stage_);
				stage.setResizable(true);
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						Grp_LoadTable();
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Grp_Refresh(ActionEvent event) {
		try {
			Grp_LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Prm_Edit(ActionEvent event) {
		try {
			if (DbUtil.Odb_Action(148l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			
			if (SB_CERT_EXP_PRM.getSelectionModel().getSelectedItem() != null) {

				SB_CERT_EXP_PRM sel = SB_CERT_EXP_PRM.getSelectionModel().getSelectedItem();

				Stage stage = new Stage();
				Stage stage_ = (Stage) SB_CERT_EXP_PRM.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/app/admin/rescron/IU_Prm.fxml"));

				Edit_Prm controller = new Edit_Prm();
				controller.SetClass(sel);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Редактировать: " + sel.getPRMNAME());
				stage.initOwner(stage_);
				stage.setResizable(true);
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						Prm_LoadTable();
					}
				});
				stage.show();
			}
			
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Prm_Refresh(ActionEvent event) {
		try {
			Prm_LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

}
