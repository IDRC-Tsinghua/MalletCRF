package Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import Microblog.Node;
import org.json.JSONObject;

public class Utils {

	
	public Node[] readFile(String filePath) throws IOException {

		Node[] nodes = null;
		List<String> lines = Files.readAllLines(Paths.get(filePath), 
				Charset.defaultCharset() );
		Node[] nodes;
		for(int i=0; i<lines.size(); i++) {
			
			JSONObject nodeJSON = new JSONObject(lines.get(i));
			nodes.add(Node(nodeJSON));
		}
	}
}
