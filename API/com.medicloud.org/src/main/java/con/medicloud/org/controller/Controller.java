package con.medicloud.org.controller;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.medicloud.org.model.Model;

@RestController
public class Controller {
	private static int id=1;
	private static Map<Integer, Model> firstname;
	private static Model save(Model model){
		if(firstname == null){
			firstname = new HashMap<Integer, Model>();
			id =  id++;
		}
		model.setID(id);
		id = id++;
		firstname.put(model.getId(), model);
		return model;
	}
	
	static {
		Model m1 = new Model();
		m1.setName("Test1");
		save(m1);
		
		Model m2 = new Model();
		m2.setName("HelloWorld!");
		save(m2);
	}
	
	@RequestMapping(value="/api/controll", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Model>> getModel(){
		
		Collection<Model> returns = firstname.values();
		return new ResponseEntity<Collection<Model>>(returns, HttpStatus.OK);
	}
	
	
}
