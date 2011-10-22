package br.com.bc.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class RepositoryImpl implements Repository {
	
	String type = "server";
	Prevayler prevayler = null;
	SnapshotTimer snapshot = null;

	public RepositoryImpl() {
		try {
			if (type.equals("server")) {
				prevayler = getPrevaylerServer();
			} else if (type.equals("client")) {
				prevayler = getPrevaylerClient();
			} else {
				prevayler = getPrevayler();
			}

			snapshot = new SnapshotTimer(prevayler);
			snapshot.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see br.com.bc.repository.Repository#addClass(br.com.bc.rest.model.ClassDefinition)
	 */
	@Override
	public void addClass(ClassDefinition classDefinition) {
		//ListaClassDefinition lista = ((ListaClassDefinition) prevayler.prevalentSystem());
		prevayler.execute(new AdicionarClassDefinition(classDefinition.getName(), classDefinition.getSource(), classDefinition.getData()));
		
		//ListaClassDefinition lista = ((ListaClassDefinition) prevayler.prevalentSystem());
		//System.out.println(lista.size());
		
	}
	
	
	@Override
	public void addService(ServiceDefinition serviceDefinition) {
		prevayler.execute(new AdicionarServiceDefinition(serviceDefinition.getName(), serviceDefinition.getRequest(), 
				serviceDefinition.getResponse(), serviceDefinition.getMainClass()));

		ListaServiceDefinition lista = ((ListaServiceDefinition) prevayler.prevalentSystem());
		System.out.println(lista.size());		
		
	}
	
	
	
	@Override
	public ServiceDefinition getServiceDefinition(String name) throws Exception {
		ServiceDefinition result = null;
		
		ListaServiceDefinition lista = ((ListaServiceDefinition) prevayler.prevalentSystem());
		
		for (int i=0; i < lista.size(); i++) {
			ServiceDefinition pd = lista.get(i);
			if (pd.getName().equals(name)) {
				result = pd;
				break;
			}
		}
		
		return result;
	}



	@Override
	public ClassDefinition getClassDefinition(String name) throws Exception {
		
		ClassDefinition result = null;
		
		ListaServiceDefinition lista = ((ListaServiceDefinition) prevayler.prevalentSystem());
		
		for (int i=0; i < lista.size(); i++) {
			ServiceDefinition pd = lista.get(i);
			
			ClassDefinition mainClass = pd.getMainClass();
			
			//TODO: esta condicao devera ser corrigida
			//if (name.contains(cd.getName())) {
			if (name.equals(mainClass.getName())) {
				result = mainClass;
				break;
			}
			
		}		
		
		return result;
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Cria um objeto Prevayler para aplicao normal - Sem replicao
	 */
	public static Prevayler getPrevayler() throws IOException, ClassNotFoundException {
		System.out.println("Instanciando Prevayler normal");
		return getPrevaylerFactory().create();
	}

	/**
	 * Cria um objeto Prevayler para ser um servidor de replicao
	 */
	public static Prevayler getPrevaylerServer() throws IOException, ClassNotFoundException {
		System.out.println("Instanciando o Servidor");
		PrevaylerFactory factory = getPrevaylerFactory();
		factory.configureReplicationServer(PrevaylerFactory.DEFAULT_REPLICATION_PORT);
		return factory.create();
	}

	/**
	 * Cria um objeto Prevayler para ser um cliente de replicao
	 */
	public static Prevayler getPrevaylerClient() throws IOException, ClassNotFoundException {
		System.out.println("Instanciando o Cliente");
		PrevaylerFactory factory = getPrevaylerFactory();
		factory.configureReplicationClient("localhost", PrevaylerFactory.DEFAULT_REPLICATION_PORT);
		factory.configurePrevalenceBase("BaseClient");
		return factory.create();
	}

	public static PrevaylerFactory getPrevaylerFactory() {
		PrevaylerFactory factory = new PrevaylerFactory();
		//factory.configurePrevalentSystem(new ListaClassDefinition());
		factory.configurePrevalentSystem(new ListaServiceDefinition());
		factory.configurePrevalenceBase("/usr/apprest");
		return factory;
	}



	@Override
	public List<ServiceDefinition> getServices() throws Exception {
		List<ServiceDefinition> result = new ArrayList<ServiceDefinition>();
		
		ListaServiceDefinition lista = ((ListaServiceDefinition) prevayler.prevalentSystem());
		
		for (int i=0; i < lista.size(); i++) {
			ServiceDefinition pd = lista.get(i);
			result.add(pd);
		}
		
		return result;

	}




}
