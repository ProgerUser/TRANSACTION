package sb_tr.model;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

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
		// create file writer object
        File f = new File(name);
        // create new file input stream
        FileInputStream fis = new FileInputStream(f);
        SqlMap sqlMap = JAXB.unmarshal(fis, SqlMap.class);
		return sqlMap;
	}
}