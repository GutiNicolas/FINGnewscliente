import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import Bussines.ControladorRemote;
import Bussines.dtNoticia;
import Bussines.dtPublicacion;

public class Main {
	public static void main(String[] args) {
		try {
		Properties jndiProperties =new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
		jndiProperties.put(Context.SECURITY_PRINCIPAL, "cliente");
		jndiProperties.put(Context.SECURITY_CREDENTIALS, "cliente");
		jndiProperties.put("jboss.naming.client.ejb.context", true);
		
		
		Context context = new InitialContext(jndiProperties);
		
		ControladorRemote  CR = (ControladorRemote)context.lookup("practico-ear/practico-ejb/Controlador!Bussines.ControladorRemote");
			
        
        
		int opt;
		if(CR.cargarDatosJPA())
			System.out.println("DATOS CARGADOS");
		boolean seguir=true;
		Scanner in=new java.util.Scanner(System.in);
		while(seguir) {
			System.out.println("-------------------");
			System.out.println("----  M E N U  ----");
			System.out.println("-------------------");
			System.out.println("[ 1 ] Listar Noticias");
			System.out.println("[ 2 ] Listar Publicaciones de una Noticia");
			System.out.println("[ 3 ] Agregar Publicacion a Noticia");
			System.out.println("[ 4 ] Crear Noticia");
			System.out.println("-------------------");
			System.out.println("[ 0 ] Testear Conexion");
			System.out.println("-------------------");
			System.out.println("[ 5 ] Salir");
			System.out.println("-------------------");
			System.out.println("Seleccione una para continuar");
			System.out.println("-------------------");		
			opt= in.nextInt();

			switch(opt) {
			case 0:
				System.out.println("-------------------");
				System.out.println("---  CONEXION:  ---");
				System.out.println(CR.hayDatos());
			break;
			case 1: 
				System.out.println("-- OPCION  [ 1 ] --");
				System.out.println("-------------------");
				System.out.println("--  L I S T A R  --");
				System.out.println("- N O T I C I A S -");
				System.out.println("-------------------");
				
				ArrayList<dtNoticia> adtn=new ArrayList<>();
				adtn= (ArrayList<dtNoticia>) CR.ListarNoticias();
				
				for (int i=0; i<adtn.size();i++) {
					dtNoticia dtn= adtn.get(i);
					System.out.println("["+dtn.id+"] # "+dtn.titulo+" # -> "+dtn.descripcion);
					
				}
				break;
			case 2:
				System.out.println("-- OPCION  [ 2 ] --");
				System.out.println("-------------------");
				System.out.println("--  L I S T A R  --");
				System.out.println("-- PUBLICACIONES --");
				System.out.println("-------------------");	
				ArrayList<dtNoticia> adtnn=new ArrayList<>();
				adtnn= (ArrayList<dtNoticia>) CR.ListarNoticias();
				
				for (int i=0; i<adtnn.size();i++) {
					dtNoticia dtn= adtnn.get(i);
					System.out.println("["+dtn.id+"] # "+dtn.titulo+" # -> "+dtn.descripcion);
					
				}
				System.out.println("Ingrese el id de una noticia para continuar.");
				int idn= in.nextInt();
				ArrayList<dtPublicacion> adtp= (ArrayList<dtPublicacion>) CR.ListarPublicaciones(idn);
				if(!adtp.isEmpty()) {
					for (int j=0; j<adtp.size();j++) {
						dtPublicacion dtp= adtp.get(j);
						System.out.println("["+dtp.id+"] # "+dtp.tipo+" #  "+dtp.url);
					}
				}
				else
					System.out.println("ID de Noticia invalido");
				break;
			case 3:
				System.out.println("-- OPCION  [ 3 ] --");
				System.out.println("-------------------");
				System.out.println("-- A G R E G A R --");
				System.out.println("--  PUBLICACION  --");
				System.out.println("-------------------");
				adtn= (ArrayList<dtNoticia>) CR.ListarNoticias();
				
				for (int i=0; i<adtn.size();i++) {
					dtNoticia dtn= adtn.get(i);
					System.out.println("["+dtn.id+"] # "+dtn.titulo+" # -> "+dtn.descripcion);
					
				}
				System.out.println("Ingrese el id de una noticia para crearle una publicacion.");
				idn= in.nextInt();
				System.out.println("Ingrese el tipo deseado para la publicacion.");
				in.nextLine();
				String tipo = in.nextLine();
				System.out.println(tipo);
				System.out.println("Ingrese el URL para la publicacion de tipo: "+tipo+".");
				String url = in.nextLine();
				System.out.println(url);
				System.out.println("Intentando crear la publicacion del tipo ["+tipo+"] con URL: ["+url+"] para la noticia con ID ["+idn+"]");
				System.out.println(CR.AddPublication2New(tipo, url, idn));;
				break;
			case 5:
				System.out.println("-- OPCION  [ 5 ] --");
				System.out.println("-------------------");
				System.out.println("F I N A L I Z A N D O");
				System.out.println("-------------------");
				in.close();
				seguir=false;
				break;
			case 4:
					System.out.println("-- OPCION  [ 4 ] --");
					System.out.println("-------------------");
					System.out.println("--   C R E A R   --");
					System.out.println("-- N O T I C I A --");
					System.out.println("-------------------");

					System.out.println("Ingrese el Titulo para la Noticia.");
					in.nextLine();
					String titulo = in.nextLine();
					System.out.println(titulo);
					System.out.println("Ingrese una Descripcion para la Noticia: " + titulo + ".");
					String desc = in.nextLine();
					System.out.println(desc);
					System.out.println("Creando Noticia");
					String text = titulo + "-" + desc;

					Properties jndip = new Properties();
					jndip.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
					jndip.put("jboss.naming.client.ejb.context", "true");
					jndip.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
					jndip.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
					jndiProperties.put(Context.SECURITY_PRINCIPAL, "invitado");
					jndiProperties.put(Context.SECURITY_CREDENTIALS, "invitado");

					Context ctx = new InitialContext(jndip);
					Queue queue = (Queue) ctx.lookup("jms/queue_alta_noticia");
					ConnectionFactory cf = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");				
					Connection connection = null;
					try {
						connection = cf.createConnection("invitado","invitado");
						Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						MessageProducer mp = session.createProducer((Destination) queue);
						TextMessage message = session.createTextMessage(text);
						mp.send(message);

					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						try {
							connection.close();
						} catch (JMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				break;
			default:
				System.out.println("OPCION INCORRECTA");
				System.out.println("Vuelva a intentarlo");

			}
		}
		
		
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}

}