package tr.pl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import app.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import sbalert.Msg;

public class ConvConst {

	public ConvConst() {
		Main.logger = Logger.getLogger(getClass());
	}

	/**
	 * Формат <br>
	 * dd.MM.yyyy
	 */
	public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	/**
	 * Формат <br>
	 * dd.MM.yyyy <br>
	 * HH:mm:ss
	 */
	public static final DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	/**
	 * Формат <br>
	 * dd.MM.yyyy
	 */
	public static final DateFormat DateFrm = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 * Формат ввода даты <br>
	 * dd.MM.yyyy <br>
	 * DatePicker
	 * 
	 * @param DP
	 */
	public void FormatDatePiker(DatePicker DP) {
		try {
			if (DP != null) {
				DateFrm.setLenient(false);
				DateStringConverter converter = new DateStringConverter(DateFrm);

				TextFormatter<Date> formatter = new TextFormatter<>(converter, null, c -> {

					if (c.isContentChange()) {
						// auto parse
						if (c.getControlNewText().length() == 10) {
							try {
								DateFrm.parse(c.getControlNewText());
							} catch (ParseException ex) {
								c.getControl().setStyle("-fx-background-color: red;");
							}
						} else {
							c.getControl().setStyle(null);
						}
					}
					if (c.isAdded()) {
						// length restriction
						if (c.getControlNewText().length() > 10) {
							return null;
						}

						// auto mask
						int caretPosition = c.getCaretPosition();
						if (caretPosition == 2 || caretPosition == 5) {
							c.setText(c.getText() + ".");
							c.setCaretPosition(c.getControlNewText().length());
							c.setAnchor(c.getControlNewText().length());
						}
					}
					return c;
				});
				DP.getEditor().setTextFormatter(formatter);

				// DP.setOnAction(event -> {
				// LocalDate date = DP.getValue();
				// System.out.println("Selected date: " + date);
				// });

			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Clob в строку
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public String ClobToString(Clob clob) throws SQLException, IOException {
		BufferedReader stringReader = new BufferedReader(clob.getCharacterStream());
		String singleLine = null;
		StringBuffer strBuff = new StringBuffer();
		while ((singleLine = stringReader.readLine()) != null) {
			strBuff.append(singleLine + "\r\n");
		}
		return strBuff.toString();
	}

	/**
	 * Форматирование столбцов <br>
	 * dd.MM.yyyy
	 * 
	 * @param TC
	 */
	public void TableColumnDate(TableColumn<Object, LocalDate> TC) {
		try {
			TC.setCellFactory(column -> {
				TableCell<Object, LocalDate> cell = new TableCell<Object, LocalDate>() {
					@Override
					protected void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateFormat.format(item));
							}
						}
					}
				};
				return cell;
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Форматирование столбцов <br>
	 * dd.MM.yyyy HH:mm:ss
	 * 
	 * @param TC
	 */
	public void TableColumnDateTime(TableColumn<Object, LocalDateTime> TC) {
		try {
			TC.setCellFactory(column -> {
				TableCell<Object, LocalDateTime> cell = new TableCell<Object, LocalDateTime>() {

					@Override
					protected void updateItem(LocalDateTime item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateTimeFormat.format(item));
							}
						}
					}
				};
				return cell;
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Первая буква заглавная
	 * 
	 * @param value
	 * @return
	 */
	private String upperCaseAllFirst(String value) {
		char[] array = value.toCharArray();
		// Uppercase first letter.
		array[0] = Character.toUpperCase(array[0]);
		// Uppercase all letters that follow a whitespace character.
		for (int i = 1; i < array.length; i++) {
			if (Character.isWhitespace(array[i - 1])) {
				array[i] = Character.toUpperCase(array[i]);
			}
		}
		return new String(array);
	}

	/**
	 * Форматирование DatePiker
	 * 
	 * @param DP
	 */
	@Deprecated
	public void DateFormatPiker(DatePicker DP) {
		try {
			DP.setConverter(new StringConverter<LocalDate>() {

				@Override
				public String toString(LocalDate date) {
					if (date != null) {
						return DateFormat.format(date);
					} else {
						return "";
					}
				}

				@Override
				public LocalDate fromString(String string) {
					if (string != null && !string.isEmpty()) {
						return LocalDate.parse(string, DateFormat);
					} else {
						return null;
					}
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Первая буква заглавная
	 * 
	 * @param TxtFld
	 */
	public void FirstWUpp(TextField TxtFld) {
		try {
			TxtFld.textProperty().addListener((ov, oldValue, newValue) -> {
				if (newValue != null && newValue.length() > 0) {
					TxtFld.setText(upperCaseAllFirst(newValue));
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Быстрый способ заполнения даты... Устарело
	 * 
	 * @param dp
	 */
	@Deprecated
	public void DateAutoComma(DatePicker dp) {
		try {
			DateTimeFormatter fastFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			dp.setConverter(new StringConverter<LocalDate>() {

				@Override
				public String toString(LocalDate object) {
					return (object != null) ? object.format(defaultFormatter) : null;
				}

				@Override
				public LocalDate fromString(String string) {
					try {
						return LocalDate.parse(string, fastFormatter);
					} catch (DateTimeParseException dtp) {

					}

					return LocalDate.parse(string, defaultFormatter);
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Только число
	 * 
	 * @param TxtFld
	 */
	public void OnlyAlpha(TextField TxtFld) {
		try {
			TxtFld.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\sа-яА-Я*")) {
						TxtFld.setText(newValue.replaceAll("[^\\sа-яА-Я]", ""));
					}
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	public void TxtFldDeleteListener(TextField TxtFld) {
		try {
			ChangeListener<String> changeListener = new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					TxtFld.textProperty().removeListener(this);
				}
			};
			TxtFld.textProperty().addListener(changeListener);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Только число
	 * 
	 * @param TxtFld
	 */
	public void OnlyNumber(TextField TxtFld) {
		try {
			TxtFld.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*")) {
						TxtFld.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * В верхнем регистре
	 * 
	 * @param tf
	 */
	public void UpperCase(TextField tf) {
		try {
			tf.setTextFormatter(new TextFormatter<>((change) -> {
				change.setText(change.getText().toUpperCase());
				return change;
			}));
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}
}
