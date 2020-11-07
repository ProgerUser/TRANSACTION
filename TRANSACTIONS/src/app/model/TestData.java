package app.model;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlType(propOrder = { "inser_clob", "testData" })
public class TestData {
	String inser_clob;

	List<TestData> testData;

	public String getinser_clob() {
		return inser_clob;
	}

	public void setinser_clob(String inser_clob) {
		this.inser_clob = inser_clob;
	}

	@XmlElement(name = "test-data")
	public List<TestData> getTestData() {
		return testData;
	}

	public void setTestData(List<TestData> testData) {
		this.testData = testData;
	}
}