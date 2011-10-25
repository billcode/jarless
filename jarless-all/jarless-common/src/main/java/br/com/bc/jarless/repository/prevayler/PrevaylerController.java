package br.com.bc.jarless.repository.prevayler;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;


public enum PrevaylerController {
	
	INSTANCE;
	
	private final String type = "server";
	private Prevayler prevayler = null;
	private SnapshotTimer snapshot = null;
	
	public void initialize() {
		try {
			if (type.equals("server")) {
				prevayler = getPrevaylerServer();
			} else if (type.equals("client")) {
				prevayler = getPrevaylerClient();
			} else {
				prevayler = createPrevayler();
			}

			snapshot = new SnapshotTimer(prevayler);
			snapshot.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}	
	
	
	
	
	
	public void setPrevayler(Prevayler prevayler) {
		this.prevayler = prevayler;
	}
	
	public Prevayler getPrevayler() {
		return this.prevayler;
	}
	
	
	/**
	 * Cria um objeto Prevayler para aplicao normal - Sem replicao
	 */
	private static Prevayler createPrevayler() throws IOException, ClassNotFoundException {
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
		factory.configurePrevalentSystem(new ListaServiceDefinition());
		factory.configurePrevalenceBase("/usr/apprest");
		return factory;
	}	

}
