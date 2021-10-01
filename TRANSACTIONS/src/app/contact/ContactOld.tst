//				Date date = new Date();
//				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
//				String strDate = dateFormat.format(date);
//				DataFormatter dataFormatter = new DataFormatter();
//				Statement statement = null;
//
//				DBUtil.dbDisconnect();
//				DBUtil.dbConnect();
//
//				Connection conn = DBUtil.conn;
//				PreparedStatement sql_statement = null;
//				String jdbc_insert_sql = "INSERT INTO Z_SB_CONTACT" + "(cod,code_name,summ,purp,cardnumber,NUMBEP) "
//						+ "VALUES " + "(?,?,?,?,?,?)";
//
//				sql_statement = conn.prepareStatement(jdbc_insert_sql);
//
//				FileInputStream input_document = new FileInputStream(new File(file_.getAbsolutePath()));
//
//				HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
//				HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
//				Iterator<Row> rowIterator = my_worksheet.rowIterator();
//				while (rowIterator.hasNext()) {
//					Row row = rowIterator.next();
//					if (row.getRowNum() > 9) {
//						Iterator<Cell> cellIterator = row.cellIterator();
//						while (cellIterator.hasNext()) {
//							Cell cell = cellIterator.next();
//							if (cell.getColumnIndex() == 4) {// code
//								sql_statement.setString(1, dataFormatter.formatCellValue(cell));
//							} else if (cell.getColumnIndex() == 5) {// code_name
//								sql_statement.setString(2, dataFormatter.formatCellValue(cell));
//							} else if (cell.getColumnIndex() == 9) {// summ
//								sql_statement.setString(3, dataFormatter.formatCellValue(cell));
//							} else if (cell.getColumnIndex() == 11) {// purp
//								sql_statement.setString(4, dataFormatter.formatCellValue(cell));
//							} else if (cell.getColumnIndex() == 12) {// cardnumber
//								sql_statement.setString(5, dataFormatter.formatCellValue(cell));
//							} else if (cell.getColumnIndex() == 1) {// NUMBEP
//								sql_statement.setString(6, dataFormatter.formatCellValue(cell));
//							}
//						}
//						sql_statement.executeUpdate();
//					}
//				}
//
//				String execute_ = "begin z_sb_calc_contact.create_; end;";
//				statement = conn.createStatement();
//				statement.execute(execute_);
//
//				Statement sqlStatement = conn.createStatement();
//				String readRecordSQL = "SELECT * FROM Z_SB_CONTACT_ERROR";
//				ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
//
//				DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
//
//				String strDate_ = dateFormat_.format(date);
//				String createfolder = System.getenv("TRANSACT_PATH") + "ContactLog/" + strDate_;
//				File file = new File(createfolder);
//
//				if (!file.exists()) {
//					if (file.mkdir()) {
//						System.out.println("Directory is created!");
//					} else {
//						System.out.println("Failed to create directory!");
//					}
//				}
//
//				String path_file = createfolder + "\\" + strDate + "_ERROR.txt";
//				PrintWriter writer = new PrintWriter(path_file);
//				while (myResultSet.next()) {
//					writer.write(myResultSet.getString("ID") + "__" + myResultSet.getString("TEXT") + "__"
//							+ myResultSet.getString("SUMM") + "__" + myResultSet.getString("NUMBER_") + "\r\n");
//				}
//
//				writer.close();
//
//				ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_ERROR.txt");
//				pb.start();
//
//				myResultSet.close();
//				input_document.close();
//				sql_statement.close();