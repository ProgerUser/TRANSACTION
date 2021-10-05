package app.audit.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import app.Main;
import app.sbalert.Msg;
import app.tr.pl.ConvConst;
import app.utils.DbUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class AuditFilter {

	@FXML
	private TextField IACTION_ID;

	@FXML
	private DatePicker DAUDDATE;
	@FXML
	private DatePicker DAUDDATE1;
	@FXML
	private TextField RROWID;
	@FXML
	private ComboBox<AU_TABLE> CTABLE;
	@FXML
	private TextField CAUDMACHINE;
	@FXML
	private TextField ID_ANUM;
	@FXML
	private TextField CAUDUSER;
	@FXML
	private TextField IAUDSESSION;
	@FXML
	private Button BDAUDDATE;
	@FXML
	private ComboBox<String> CAUDOPERATION;
	@FXML
	private TextField CAUDPROGRAM;
	@FXML
	private TextField CAUDIP_ADDRESS;
	@FXML
	private TextField datefinal;
	@FXML
	private GridPane ifonedate;
	@FXML
	private TextField ID_NUM;
	/* CHKBX */
	@FXML
	public CheckBox CHCAUDIP_ADDRESS;
	@FXML
	public CheckBox CHID_NUM;
	@FXML
	public CheckBox CHCAUDUSER;
	@FXML
	public CheckBox CHCAUDMACHINE;
	@FXML
	public CheckBox CHCAUDOPERATION;
	@FXML
	public CheckBox CHID_ANUM;
	@FXML
	public CheckBox CHIAUDSESSION;
	@FXML
	public CheckBox CHIACTION_ID;
	@FXML
	public CheckBox CHRROWID;
	@FXML
	public CheckBox CHCAUDPROGRAM;
	@FXML
	public CheckBox CHDAUDDATE;
	@FXML
	public CheckBox CHCTABLE;
	/************************/

	String AUFILTERTEXT = "\r\n where 1=2 \r\n";
	String tDAUDDATE = "";
	String tCAUDOPERATION = "";
	String tIACTION_ID = "";
	String tCTABLE = "";
	String tRROWID = "";
	String tID_ANUM = "";
	String tID_NUM = "";
	String tCAUDUSER = "";
	String tIAUDSESSION = "";
	String tCAUDPROGRAM = "";
	String tCAUDMACHINE = "";
	String tСAUDIP_ADDRESS = "";

	@FXML
	void ACTDAUDDATE(ActionEvent event) {
		try {
			if (CHDAUDDATE.isSelected()) {
				DAUDDATE.setStyle("-fx-background-color:#ff80ff");
				DAUDDATE1.setStyle("-fx-background-color:#ff80ff");
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				// ifonedate.setVisible(false);
				// datefinal.setVisible(true);
				if (DAUDDATE.getValue() == null) {
					Msg.Message("Заполните дату!");
					CHDAUDDATE.setSelected(false);
					DAUDDATE.setStyle("");
					DAUDDATE1.setStyle("");
					return;
				}
				if (DAUDDATE.getValue() == null & DAUDDATE1.getValue() != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					String DAUDDATE1s = DAUDDATE1.getValue().format(formatter);
					tDAUDDATE = "\r\n and DAUDDATE between to_date('" + DAUDDATE1s + "','dd.mm.yyyy') and to_date('"
							+ DAUDDATE1s + "','dd.mm.yyyy')\r\n";
				} else if (DAUDDATE.getValue() != null & DAUDDATE1.getValue() == null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					String DAUDDATEs = DAUDDATE.getValue().format(formatter);
					tDAUDDATE = "\r\n and DAUDDATE between to_date('" + DAUDDATEs + "','dd.mm.yyyy') and to_date('"
							+ DAUDDATEs + "','dd.mm.yyyy')\r\n";
				} else if (DAUDDATE.getValue() != null & DAUDDATE1.getValue() != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					String DAUDDATEs = DAUDDATE.getValue().format(formatter);
					String DAUDDATE1s = DAUDDATE1.getValue().format(formatter);
					tDAUDDATE = "\r\n and DAUDDATE between to_date('" + DAUDDATEs + "','dd.mm.yyyy') and to_date('"
							+ DAUDDATE1s + "','dd.mm.yyyy')\r\n";
				} else {
					Msg.Message("Ошибка заполнения даты!");
					CHDAUDDATE.setSelected(false);
					return;
				}

				// datefinal.setStyle("-fx-control-inner-background:#ff80ff");
				// datefinal.setText(tDAUDDATE);
			} else {
				DAUDDATE.setStyle("");
				DAUDDATE1.setStyle("");
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				// datefinal.setVisible(false);
				// ifonedate.setVisible(true);
				tDAUDDATE = "";
				// datefinal.setText(tDAUDDATE);
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void datebetween(ActionEvent event) {
		try {
			Button Update = new Button();
			Update.setText("Выбрать");
			Update.setLayoutX(29.0);
			Update.setLayoutY(462.0);
			AnchorPane secondaryLayout = new AnchorPane();
			/**/
			Update.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {

				}
			});

			secondaryLayout.getChildren().add(Update);

			// VBox vbox = new VBox(debtinfo);
			Scene secondScene = new Scene(secondaryLayout, 518, 500);
			Stage stage = (Stage) IACTION_ID.getScene().getWindow();

			Stage newWindow = new Stage();
			newWindow.setTitle("Диапазон дат");
			newWindow.setScene(secondScene);
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

	@FXML
	void ACTCAUDOPERATION(ActionEvent event) {
		try {
			if (CHCAUDOPERATION.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tCAUDOPERATION = "\r\n and CAUDOPERATION = '"
						+ ((CAUDOPERATION.getSelectionModel().getSelectedItem() != null) ? CAUDOPERATION.getValue()
								: "")
						+ "'\r\n";
				CAUDOPERATION.setStyle("-fx-background-color:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				CAUDOPERATION.setStyle("");
				tCAUDOPERATION = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTIACTION_ID(ActionEvent event) {
		try {
			if (CHIACTION_ID.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tIACTION_ID = "\r\n and IACTION_ID = " + IACTION_ID.getText() + "\r\n";
				IACTION_ID.setStyle("-fx-control-inner-background:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				IACTION_ID.setStyle("");
				tIACTION_ID = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTCTABLE(ActionEvent event) {
		try {
			if (CHCTABLE.isSelected()) {
				AU_TABLE au = CTABLE.getSelectionModel().getSelectedItem();
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tCTABLE = "\r\n and upper(CTABLE) = upper('"
						+ ((CTABLE.getSelectionModel().getSelectedItem() != null) ? au.getCNAME() : null) + "') \r\n";
				CTABLE.setStyle("-fx-background-color:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				CTABLE.setStyle("");
				tCTABLE = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTRROWID(ActionEvent event) {
		try {
			if (CHRROWID.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tRROWID = "\r\n and RROWID = '" + RROWID.getText() + "' \r\n";
				RROWID.setStyle("-fx-control-inner-background:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				RROWID.setStyle("");
				tRROWID = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTID_ANUM(ActionEvent event) {
		try {
			if (CHID_ANUM.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tID_ANUM = "\r\n and ID_ANUM = '" + ID_ANUM.getText() + "' \r\n";
				ID_ANUM.setStyle("-fx-control-inner-background:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				ID_ANUM.setStyle("");
				tID_ANUM = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTID_NUM(ActionEvent event) {
		try {
			if (CHID_NUM.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tID_NUM = "\r\n and ID_NUM = '" + ID_NUM.getText() + "' \r\n";
				ID_NUM.setStyle("-fx-control-inner-background:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				ID_NUM.setStyle("");
				tID_NUM = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTCAUDUSER(ActionEvent event) {
		try {
			if (CHCAUDUSER.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tCAUDUSER = "\r\n and CAUDUSER = '" + CAUDUSER.getText() + "' \r\n";
				CAUDUSER.setStyle("-fx-control-inner-background:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				CAUDUSER.setStyle("");
				tCAUDUSER = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTCAUDIP_ADDRESS(ActionEvent event) {
		try {
			if (CHCAUDIP_ADDRESS.isSelected()) {
				AUFILTERTEXT = "\r\n where 1=1 \r\n";
				tСAUDIP_ADDRESS = "\r\n and AUDIP_ADDRESS = '" + CAUDIP_ADDRESS.getText() + "' \r\n";
				CAUDIP_ADDRESS.setStyle("-fx-control-inner-background:#ff80ff");
			} else {
				AUFILTERTEXT = "\r\n where 1=2 \r\n";
				CAUDIP_ADDRESS.setStyle("");
				tСAUDIP_ADDRESS = "";
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void ACTIAUDSESSION(ActionEvent event) {
		if (CHIAUDSESSION.isSelected()) {
			AUFILTERTEXT = "\r\n where 1=1 \r\n";
			tIAUDSESSION = "\r\n and IAUDSESSION = '" + IAUDSESSION.getText() + "' \r\n";
			IAUDSESSION.setStyle("-fx-control-inner-background:#ff80ff");
		} else {
			AUFILTERTEXT = "\r\n where 1=2 \r\n";
			IAUDSESSION.setStyle("");
			tIAUDSESSION = "";
		}
	}

	@FXML
	void ACTCAUDPROGRAM(ActionEvent event) {
		if (CHCAUDPROGRAM.isSelected()) {
			AUFILTERTEXT = "\r\n where 1=1 \r\n";
			tCAUDPROGRAM = "\r\n and CAUDPROGRAM = '" + CAUDPROGRAM.getText() + "' \r\n";
			CAUDPROGRAM.setStyle("-fx-control-inner-background:#ff80ff");
		} else {
			AUFILTERTEXT = "\r\n where 1=2 \r\n";
			CAUDPROGRAM.setStyle("");
			tCAUDPROGRAM = "";
		}
	}

	@FXML
	void ACTCAUDMACHINE(ActionEvent event) {
		if (CHCAUDMACHINE.isSelected()) {
			AUFILTERTEXT = "\r\n where 1=1 \r\n";
			tCAUDMACHINE = "\r\n and CAUDMACHINE = '" + CAUDMACHINE.getText() + "' \r\n";
			CAUDMACHINE.setStyle("-fx-control-inner-background:#ff80ff");
		} else {
			AUFILTERTEXT = "\r\n where 1=2 \r\n";
			CAUDMACHINE.setStyle("");
			tCAUDMACHINE = "";
		}
	}

	@FXML
	void EXECFILTER(ActionEvent event) {
		setStatus(true);
		AUFILTERTEXT = AUFILTERTEXT + tDAUDDATE + tCAUDOPERATION + tIACTION_ID + tCTABLE + tRROWID + tID_ANUM + tID_NUM
				+ tCAUDUSER + tIAUDSESSION + tCAUDPROGRAM + tCAUDMACHINE + tСAUDIP_ADDRESS;
		setFilter(AUFILTERTEXT);
		Stage stage = (Stage) ID_NUM.getScene().getWindow();
		// do what you have to do
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@FXML
	void cencel(ActionEvent event) {
		setStatus(false);
		AUFILTERTEXT = "\r\n where 1=2 \r\n";
		Stage stage = (Stage) ID_NUM.getScene().getWindow();
		// do what you have to do
		// stage.close();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	private BooleanProperty Status;

	public void setStatus(Boolean value) {
		this.Status.set(value);
	}

	public boolean getStatus() {
		return this.Status.get();
	}

	/**
	 * Для типа документов
	 */
	private void convertComboDisplayList() {
		CTABLE.setConverter(new StringConverter<AU_TABLE>() {
			@Override
			public String toString(AU_TABLE object) {
				return object != null ? object.getCNAME() + "=" + object.getTABLENAME() : "";
			}

			@Override
			public AU_TABLE fromString(final String string) {
				return CTABLE.getItems().stream()
						.filter(product -> (product.getCNAME() + "=" + product.getTABLENAME()).equals(string))
						.findFirst().orElse(null);
			}
		});
	}

	@FXML
	private void initialize() {
		try {
			String pattern = "dd.MM.yyyy";

			CAUDOPERATION.getItems().addAll("I", "U", "D");
			{
				PreparedStatement sqlStatement = DbUtil.conn.prepareStatement("select * from AU_TABLE ");
				ResultSet rs = sqlStatement.executeQuery();
				ObservableList<AU_TABLE> areas = FXCollections.observableArrayList();
				AU_TABLE list = null;
				while (rs.next()) {
					list = new AU_TABLE();
					list.setTABLENAME(rs.getString("TABLENAME"));
					list.setCNAME(rs.getString("CNAME"));
					areas.add(list);
				}

				sqlStatement.close();
				rs.close();
				// 1. Сначала фильтр !
//				FilteredList<AU_TABLE> filterednationals = new FilteredList<AU_TABLE>(areas);
//				CTABLE.getEditor().textProperty()
//						.addListener(new InputFilter<AU_TABLE>(CTABLE, filterednationals, false));
				// 2. Потом данные
				CTABLE.setItems(areas);
				
				FxUtilTest.getComboBoxValue(CTABLE);
				FxUtilTest.autoCompleteComboBoxPlus(CTABLE, (typedText, itemToCompare) -> (itemToCompare.CNAMEProperty()
						+ "=" + itemToCompare.getTABLENAME()).toLowerCase().contains(typedText.toLowerCase()));
				
				convertComboDisplayList();
				// CTABLE.getSelectionModel().select(0);
				rs.close();
			}
			/*
			 * CAUDOPERATION.setTextFormatter(new TextFormatter<>((change) -> {
			 * change.setText(change.getText().toUpperCase()); return change; }));
			 */
			/*
			 * CTABLE.setTextFormatter(new TextFormatter<>((change) -> {
			 * change.setText(change.getText().toUpperCase()); return change; }));
			 */
			RROWID.setTextFormatter(new TextFormatter<>((change) -> {
				change.setText(change.getText().toUpperCase());
				return change;
			}));
			CAUDUSER.setTextFormatter(new TextFormatter<>((change) -> {
				change.setText(change.getText().toUpperCase());
				return change;
			}));
			CAUDPROGRAM.setTextFormatter(new TextFormatter<>((change) -> {
				change.setText(change.getText().toUpperCase());
				return change;
			}));
			CAUDMACHINE.setTextFormatter(new TextFormatter<>((change) -> {
				change.setText(change.getText().toUpperCase());
				return change;
			}));

			DAUDDATE.setConverter(new StringConverter<LocalDate>() {
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

				@Override
				public String toString(LocalDate date) {
					if (date != null) {
						return dateFormatter.format(date);
					} else {
						return "";
					}
				}

				@Override
				public LocalDate fromString(String string) {
					if (string != null && !string.isEmpty()) {
						return LocalDate.parse(string, dateFormatter);
					} else {
						return null;
					}
				}
			});
			DAUDDATE1.setConverter(new StringConverter<LocalDate>() {
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

				@Override
				public String toString(LocalDate date) {
					if (date != null) {
						return dateFormatter.format(date);
					} else {
						return "";
					}
				}

				@Override
				public LocalDate fromString(String string) {
					if (string != null && !string.isEmpty()) {
						return LocalDate.parse(string, dateFormatter);
					} else {
						return null;
					}
				}
			});
			new ConvConst().FormatDatePiker(DAUDDATE);
			new ConvConst().FormatDatePiker(DAUDDATE1);
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

	private StringProperty Filter;

	public void setFilter(String value) {
		this.Filter.set(value);
	}

	public String getFilter() {
		return this.Filter.get();
	}

	public AuditFilter() {
		this.Status = new SimpleBooleanProperty();
		Main.logger = Logger.getLogger(getClass());
		this.Filter = new SimpleStringProperty();
	}
}
