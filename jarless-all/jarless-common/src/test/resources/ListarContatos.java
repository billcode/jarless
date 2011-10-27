import java.util.ArrayList;
import java.util.List;

import br.com.bc.jarless.Execution;

public class ListarContatos implements Execution {

	@Override
	public Object execute(Object request) {
		
		List<String> contatos = new ArrayList<String>();
		
		contatos.add("Roger Waters");
		contatos.add("Nick Mason");
		contatos.add("Richard Wright");
		contatos.add("David Gilmour");
		
		contatos.add("Joey Ramone");
		contatos.add("Johnny Ramone");
		contatos.add("Dee Dee Ramone");
		
		return contatos;
	}
	

}
