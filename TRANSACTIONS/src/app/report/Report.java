package app.report;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import com.jyloo.syntheticafx.ComparableColumnFilter;
import com.jyloo.syntheticafx.PatternColumnFilter;
import com.jyloo.syntheticafx.SyntheticaFX;
import com.jyloo.syntheticafx.TextFormatterFactory;
import com.jyloo.syntheticafx.XTableColumn;
import com.jyloo.syntheticafx.XTableView;
import com.jyloo.syntheticafx.filter.ComparableFilterModel;
import com.jyloo.syntheticafx.filter.ComparisonType;

import app.Main;
import app.model.Connect;
import app.sbalert.Msg;
import app.utils.DbUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class Report {

	
	@FXML private Button List;
	@FXML private Button Design;
	@FXML private Button Clone;
	@FXML private Button New;
	
    @FXML private CheckBox USE_CONVERTATION;
    @FXML private CheckBox LOW_REGIM;
    @FXML private CheckBox EDIT_ENABLE;
    
    @FXML private ComboBox<String> PRINTER_ID;
    @FXML private ComboBox<AP_REPORT_CAT> ComboList;
    
    @FXML private TextField FILE_NAME;
    
    @FXML private RadioButton Display;
    @FXML private RadioButton GENERATE_TYPE;
    @FXML private RadioButton ToPrinter;
    @FXML private RadioButton ToFile;
    @FXML private RadioButton DIR_MANUAL;
    @FXML private RadioButton DIR_USER_OUT;
    @FXML private RadioButton DIR_TEMP;
    
	@FXML
	void Display(ActionEvent event) {
		if (Display.isSelected()) {
			LOW_REGIM.setDisable(false);
			EDIT_ENABLE.setDisable(false);
		} else {
			LOW_REGIM.setDisable(true);
			EDIT_ENABLE.setDisable(true);
			
			LOW_REGIM.setSelected(false);
			EDIT_ENABLE.setSelected(false);
		}
	}
    
    
	@FXML
	void List(ActionEvent event) {
		ParamRep();
	}

	void onclose() {
		Stage stage = (Stage) List.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@FXML
	void ComboList(ActionEvent event) {
		try {
			AP_REPORT_CAT rep_cat = ComboList.getSelectionModel().getSelectedItem();
			if (rep_cat != null) {
			String viewer = "";
			PreparedStatement prp = conn.prepareStatement("select lower(decode(replace(CNAME, 'FRREP ', ''),\n" + 
					"                    'Отсутствует',\n" + 
					"                    '.xlt',\n" + 
					"                    'MS EXCEL',\n" + 
					"                    '.xls',\n" + 
					"                    '.' || replace(CNAME, 'FRREP ', ''))) ext,CNAME\n" + 
					"  from xap_report_cat, AP_VIEWER\n" + 
					" where xap_report_cat.REPORT_VIEWER = AP_VIEWER.ID\n" + 
					"   and xap_report_cat.REPORT_ID = ?");
			prp.setLong(1, ComboList.getSelectionModel().getSelectedItem().getREPORT_ID());
			ResultSet rs = prp.executeQuery();
			if (rs.next()) {
				viewer = rs.getString("CNAME");
				FILE_NAME.setText(rep_cat.getREPORT_TYPE_ID() + "." + rep_cat.getREPORT_ID() + ". "
						+ rep_cat.getREPORT_NAME() + "(" + java.util.UUID.randomUUID() + ")" + rs.getString("ext"));
			}
			prp.close();
			rs.close();
			if (viewer.equals("Отсутствует")) {
				GENERATE_TYPE.setText("Отсутствует");
				GENERATE_TYPE.setDisable(false);
			} else {
				GENERATE_TYPE.setText(viewer);
				GENERATE_TYPE.setDisable(true);
			}}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Cencel(ActionEvent event) {
		onclose();
	}

	@FXML
	void Run(ActionEvent event) {
		if (ComboList.getSelectionModel().getSelectedItem() != null) {
			setFrDllOptions(FRREPRunner.FRREPDllOptions.RUN);
			runfr3();
		}
	}

	@FXML
	void Clone(ActionEvent event) {
		if (ComboList.getSelectionModel().getSelectedItem() != null) {
			setFrDllOptions(FRREPRunner.FRREPDllOptions.CLONE);
			runfr3();
		}
	}
	
	@FXML
	void New(ActionEvent event) {
		if (ComboList.getSelectionModel().getSelectedItem() != null) {
			setFrDllOptions(FRREPRunner.FRREPDllOptions.NEW);
			runfr3();
		}
	}

	@FXML
	void Design(ActionEvent event) {
		if (ComboList.getSelectionModel().getSelectedItem() != null) {
			setFrDllOptions(FRREPRunner.FRREPDllOptions.DESIGN);
			runfr3();
		}
	}

	public void setFrDllOptions(FRREPRunner.FRREPDllOptions frDllOptions) {
		this.frDllOptions = frDllOptions;
	}

	public FRREPRunner.FRREPDllOptions getFrDllOptions() {
		return this.frDllOptions;
	}

	private FRREPRunner.FRREPDllOptions frDllOptions;
	String UserDir = null;
	void runfr3() {
		try {

//			if (UserDir == null || !UserDir.equals(System.getenv("MJ_PATH") + "OutReports")) {
//				PreparedStatement prp = conn.prepareStatement("UPDATE USR SET cdirOUTPBOX = ? WHERE CUSRLOGNAME = ?");
//				prp.setString(2, Connect.userID_.toUpperCase());
//				prp.setString(1, System.getenv("MJ_PATH") + "OutReports");
//				prp.executeUpdate();
//				conn.commit();
//				prp.close();
//			}
			
			FRREPRunner runner = new FRREPRunner();
			dbConnect();
			
			String port = Connect.connectionURL_.substring(Connect.connectionURL_.indexOf(":")+1,
					Connect.connectionURL_.indexOf("/"));
			//System.out.println(port);
			String sid = "";
			String host = "";
			{
				Statement statement = conn.createStatement();
				ResultSet rs = statement
						.executeQuery("select INSTANCE_NAME, HOST_NAME, userenv('SESSIONID') from SYS.V_$INSTANCE");
				if (rs.next()) {
					sid = rs.getString(1);
					runner.setSid(rs.getString(3));
				}
				statement.close();
				rs.close();
			}
			if (getRecId() != null) {
				runner.setP1(String.valueOf(getRecId()));
			}
			runner.setDllOptions(getFrDllOptions());
			runner.setUserName(Connect.userID_);
			runner.setPassw(Connect.userPassword_);
			host = Connect.connectionURL_.substring(0, Connect.connectionURL_.indexOf(":"));
			runner.setConnect_string(host + ":" + port + ":" + sid);
			runner.setServerName(sid);
			runner.setReport_file(ComboList.getSelectionModel().getSelectedItem().getREPORT_UFS());
			runner.setReport_type_id(
					String.valueOf(ComboList.getSelectionModel().getSelectedItem().getREPORT_TYPE_ID()));
		    runner.setReport_id(String.valueOf(ComboList.getSelectionModel().getSelectedItem().getREPORT_ID()));
		
		    runner.setFileName(FILE_NAME.getText());
			runner.setGenerate_type("V");
			runner.setUse_convertation("N");
			runner.setEdit_enable("N");
			runner.setDir("O");
			
			runner.run();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	public void clickItem(MouseEvent event) {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void ParamRep() {
		try {
			SyntheticaFX.init("com.jyloo.syntheticafx.SyntheticaFXModena");
			Button Update = new Button();
			Update.setText("Выбрать");
			AnchorPane secondaryLayout = new AnchorPane();
			VBox vb = new VBox();
			//ToolBar toolBar = new ToolBar(Update);
			
			ButtonBar bb = new ButtonBar();
			bb.setPrefHeight(40);
			bb.getButtons().addAll(Update);
			
			XTableView<AP_REPORT_CAT> cusllists = new XTableView<AP_REPORT_CAT>();
			XTableColumn<AP_REPORT_CAT, Long> REPORT_ID = new XTableColumn<>("Код");
			REPORT_ID.setCellValueFactory(new PropertyValueFactory<>("REPORT_ID"));
			XTableColumn<AP_REPORT_CAT, String> REPORT_NAME = new XTableColumn<>("Наименование");
			REPORT_NAME.setCellValueFactory(new PropertyValueFactory<>("REPORT_NAME"));
			cusllists.getColumns().add(REPORT_ID);
			cusllists.getColumns().add(REPORT_NAME);

			ObservableList rules = FXCollections.observableArrayList(ComparisonType.values());

			REPORT_ID.setColumnFilter(new ComparableColumnFilter(new ComparableFilterModel(rules),
					TextFormatterFactory.LONG_TEXTFORMATTER_FACTORY));
			REPORT_NAME.setColumnFilter(new PatternColumnFilter<>());

			vb.getChildren().add(cusllists);
			vb.getChildren().add(bb);

			cusllists.getStyleClass().add("mylistview");
			cusllists.getStylesheets().add("/ScrPane.css");

			vb.setPadding(new Insets(5, 5, 5, 5));
			/**/
			REPORT_ID.setCellValueFactory(cellData -> cellData.getValue().REPORT_IDProperty().asObject());
			REPORT_NAME.setCellValueFactory(cellData -> cellData.getValue().REPORT_NAMEProperty());

			/* SelData */
			PreparedStatement prepStmt = conn.prepareStatement("select * from xap_report_cat where REPORT_TYPE_ID = ?");
			prepStmt.setLong(1, getId());
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AP_REPORT_CAT> cuslist = FXCollections.observableArrayList();
			while (rs.next()) {
				AP_REPORT_CAT list = new AP_REPORT_CAT();
				//list.setAVAILABLE_SQL(rs.getString("AVAILABLE_SQL"));
				list.setOEM_DATA(rs.getString("OEM_DATA"));
				list.setEDIT_PARAM(rs.getString("EDIT_PARAM"));
				list.setREPORT_COMMENT(rs.getString("REPORT_COMMENT"));
				list.setREPORT_VIEWER(rs.getLong("REPORT_VIEWER"));
				list.setCOPIES(rs.getLong("COPIES"));
				list.setREPORT_UFS(rs.getString("REPORT_UFS"));
				list.setREPORT_NAME(rs.getString("REPORT_NAME"));
				list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
				list.setREPORT_ID(rs.getLong("REPORT_ID"));

				cuslist.add(list);
			}
			prepStmt.close();
			rs.close();

			// двойной щелчок
			cusllists.setRowFactory(tv -> {
				TableRow<AP_REPORT_CAT> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						if (cusllists.getSelectionModel().getSelectedItem() == null) {
							Msg.Message("Выберите данные из таблицы!");
						} else {
							AP_REPORT_CAT qq = cusllists.getSelectionModel().getSelectedItem();
							ComboList.getSelectionModel().select(qq);
							((Node) (event.getSource())).getScene().getWindow().hide();
						}
					}
				});
				return row;
			});

			cusllists.setItems(cuslist);

			cusllists.setPrefWidth(500);
			cusllists.setPrefHeight(350);

			REPORT_ID.setPrefWidth(100);
			REPORT_NAME.setPrefWidth(350);

			TableFilter<AP_REPORT_CAT> CUSFilter = TableFilter.forTableView(cusllists).apply();
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
					if (cusllists.getSelectionModel().getSelectedItem() == null) {
						Msg.Message("Выберите данные из таблицы!");
					} else {
						AP_REPORT_CAT qq = cusllists.getSelectionModel().getSelectedItem();
						ComboList.getSelectionModel().select(qq);
						((Node) (event.getSource())).getScene().getWindow().hide();
					}
				}

			});

			secondaryLayout.getChildren().add(vb);

			Scene secondScene = new Scene(secondaryLayout, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			Stage stage = (Stage) List.getScene().getWindow();

			Stage newWindow = new Stage();
			newWindow.setTitle("Список");
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

	@FXML
	private void initialize() {
		// ResourceManager.addBundle();
		try {
			ToggleGroup toggleGroup = new ToggleGroup();
			GENERATE_TYPE.setToggleGroup(toggleGroup);
			ToFile.setToggleGroup(toggleGroup);
			ToPrinter.setToggleGroup(toggleGroup);
			Display.setToggleGroup(toggleGroup);
			
			ToggleGroup toggleGroup2 = new ToggleGroup();
			DIR_MANUAL.setToggleGroup(toggleGroup2);
			DIR_USER_OUT.setToggleGroup(toggleGroup2);
			DIR_TEMP.setToggleGroup(toggleGroup2);
			
			DIR_TEMP.setText(System.getenv("TEMP"));
			
			DIR_TEMP.setSelected(true);
			
			
			dbConnect();
			// установить svg как иконку для кнопки
//			{
//				InputStream svgFile = getClass().getResourceAsStream("/table2.svg");
//				SvgLoader loader = new SvgLoader();
//				Group svgImage = loader.loadSvg(svgFile);
//				svgImage.setScaleX(0.060);
//				svgImage.setScaleY(0.060);
//				Group graphic = new Group(svgImage);
//				List.setGraphic(graphic);
//			}
			//OctIconView icon = new OctIconView(OctIcon.TOOLS);
			//Text icon = OctIconFactory.get().createIcon(OctIcon.TOOLS);
			
			PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

	        PrintService service = PrintServiceLookup.lookupDefaultPrintService(); 
	        for (PrintService printer : printServices) {
	            PRINTER_ID.getItems().add(printer.getName());
	        }
	        PRINTER_ID.getSelectionModel().select(service.getName());
	        
	        
			if (DbUtil.Odb_Action(163l) == 0) {
				Clone.setVisible(false);
			}
			
			if (DbUtil.Odb_Action(162l) == 0) {
				Design.setVisible(false);
			}
			if (DbUtil.Odb_Action(164l) == 0) {
				New.setVisible(false);
			}

//			{
//				InputStream svgFile = getClass().getResourceAsStream("/copy_frrep.svg");
//				SvgLoader loader = new SvgLoader();
//				Group svgImage = loader.loadSvg(svgFile);
//				svgImage.setScaleX(0.04);
//				svgImage.setScaleY(0.04);
//				Group graphic = new Group(svgImage);
//				Clone.setGraphic(graphic);
//			}

//			{
//				InputStream svgFile = getClass().getResourceAsStream("/design_frrep.svg");
//				SvgLoader loader = new SvgLoader();
//				Group svgImage = loader.loadSvg(svgFile);
//				svgImage.setScaleX(0.04);
//				svgImage.setScaleY(0.04);
//				Group graphic = new Group(svgImage);
//				Design.setGraphic(graphic);
//			}

			// отчеты
			{
				PreparedStatement stmt = conn.prepareStatement("select * from xap_report_cat where REPORT_TYPE_ID = ?");
				stmt.setLong(1, getId());
				ResultSet rs = stmt.executeQuery();
				ObservableList<AP_REPORT_CAT> rep = FXCollections.observableArrayList();
				while (rs.next()) {
					AP_REPORT_CAT list = new AP_REPORT_CAT();
					//list.setAVAILABLE_SQL(rs.getString("AVAILABLE_SQL"));
					list.setOEM_DATA(rs.getString("OEM_DATA"));
					list.setEDIT_PARAM(rs.getString("EDIT_PARAM"));
					list.setREPORT_COMMENT(rs.getString("REPORT_COMMENT"));
					list.setREPORT_VIEWER(rs.getLong("REPORT_VIEWER"));
					list.setCOPIES(rs.getLong("COPIES"));
					list.setREPORT_UFS(rs.getString("REPORT_UFS"));
					list.setREPORT_NAME(rs.getString("REPORT_NAME"));
					list.setREPORT_TYPE_ID(rs.getLong("REPORT_TYPE_ID"));
					list.setREPORT_ID(rs.getLong("REPORT_ID"));
					rep.add(list);
				}

				stmt.close();
				rs.close();

				ComboList.setItems(rep);
				ComboList.getSelectionModel().select(0);
				CombRep(ComboList);
			}
			
			//Template
			{
				AP_REPORT_CAT rep_cat = ComboList.getSelectionModel().getSelectedItem();
				if (rep_cat != null) {
					FILE_NAME.setText(rep_cat.getREPORT_TYPE_ID() + "." + rep_cat.getREPORT_ID() + ". "
							+ rep_cat.getREPORT_NAME());

					String viewer = "";
				    PreparedStatement prp = conn.prepareStatement("select lower(decode(replace(CNAME, 'FRREP ', ''),\n" + 
				    		"                    'Отсутствует',\n" + 
				    		"                    '.xlt',\n" + 
				    		"                    'MS EXCEL',\n" + 
				    		"                    '.xls',\n" + 
				    		"                    '.' || replace(CNAME, 'FRREP ', ''))) ext, CNAME\n" + 
						"  from AP_REPORT_CAT, AP_VIEWER\n" + 
						" where AP_REPORT_CAT.REPORT_VIEWER = AP_VIEWER.ID\n" + 
						"   and AP_REPORT_CAT.REPORT_ID = ?\n" + 
						"");
					prp.setLong(1, ComboList.getSelectionModel().getSelectedItem().getREPORT_ID());
					ResultSet rs = prp.executeQuery();
					if (rs.next()) {
						viewer = rs.getString("CNAME");
						FILE_NAME.setText(rep_cat.getREPORT_TYPE_ID() + "." + rep_cat.getREPORT_ID() + ". "
								+ rep_cat.getREPORT_NAME() + "(" + java.util.UUID.randomUUID() + ")"
								+ rs.getString("ext"));
					}
					prp.close();
					rs.close();
					if (viewer.equals("Отсутствует")) {
						GENERATE_TYPE.setText("Отсутствует");
						GENERATE_TYPE.setDisable(false);
					} else {
						GENERATE_TYPE.setText(viewer);
						GENERATE_TYPE.setDisable(true);
					}
				}
				CallableStatement cst = conn
						.prepareCall("{  ? = call USERS_UTILITIES.Get_OutBox_Directory(vcLOGNAME=>?)} ");
				cst.registerOutParameter(1, Types.VARCHAR);
				cst.setString(2, Connect.userID_.toUpperCase());
				cst.execute();
				UserDir = cst.getString(1);
				cst.close();
				DIR_USER_OUT.setText(UserDir);
				DIR_USER_OUT.setSelected(true);
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	private void CombRep(ComboBox<AP_REPORT_CAT> cmb) {
		cmb.setConverter(new StringConverter<AP_REPORT_CAT>() {
			@Override
			public String toString(AP_REPORT_CAT product) {
				return (product != null) ? product.getREPORT_ID()+". "+ product.getREPORT_NAME() : "";
			}

			@Override
			public AP_REPORT_CAT fromString(final String string) {
				return cmb.getItems().stream().filter(product -> product.getREPORT_NAME().equals(string)).findFirst()
						.orElse(null);
			}
		});
	}

	/**
	 * Строка соединения
	 */
	private Connection conn;

	/**
	 * Открыть сессию
	 */
	private void dbConnect() {
		try {
			Main.logger = Logger.getLogger(getClass());
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program",getClass().getName());
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Закрыть
	 */
	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

	private BooleanProperty Status;

	private LongProperty Id;

	private LongProperty RecId;

	public void setStatus(Boolean value) {
		this.Status.set(value);
	}

	public boolean getStatus() {
		return this.Status.get();
	}

	public void setRecId(Long value) {
		this.RecId.set(value);
	}

	public Long getRecId() {
		return this.RecId.get();
	}

	public void setId(Long value) {
		this.Id.set(value);
	}

	public Long getId() {
		return this.Id.get();
	}

	public Report() {
		Main.logger = Logger.getLogger(getClass());
		this.Status = new SimpleBooleanProperty();
		this.RecId = new SimpleLongProperty();
		this.Id = new SimpleLongProperty();
	}
}
