package app.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Tests {

	public TestData testData;

	@XmlElement(name = "test-data")
	public TestData getTestData() {
		return testData;
	}

	public void setTestData(TestData testData) {
		this.testData = testData;
	}

}
