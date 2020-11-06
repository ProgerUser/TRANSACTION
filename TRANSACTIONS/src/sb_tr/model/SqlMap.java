package sb_tr.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SqlMap {
	Map<String, String> sqls = new HashMap<>();

	public Map<String, String> getSqls() {
		return sqls;
	}

	public void setSqls(Map<String, String> sqls) {
		this.sqls = sqls;
	}

	public String getSql(String name) {
		return sqls.get(name);
	}

	
	public SqlMap load(String name) throws Exception {
		InputStream is = getClass().getResourceAsStream(name);
		SqlMap sqlMap = JAXB.unmarshal(is, getClass());
		return sqlMap;
	}
}