package fr.ulille.iut;

import javax.ws.rs.client.Client;


import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class ProductResourceTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient();

		target = c.target(Main.BASE_URI);

		// Utiliser un attribut statique pour stocker les utilisateurs dans la ressource ProductResource
		// est une très mauvaise pratique. Cela pose problème pour les tests.
		// Du coup, je suis obligé de remettre à zéro cet attribut "à la main"...
		Field field = ProductResource.class.getDeclaredField("products");
		field.setAccessible(true);
		field.set("null", new HashMap<>());
	}

	@SuppressWarnings("deprecation")
	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	/**
	 * Test de création d'un utilisateur (retour HTTP et envoi de l'URI de la nouvelle instance)
	 */
	@Test
	public void testCreateProduct() {
		Product product = new Product(1,1,5, "Red", "Pen with Hello on it");
		// Conversion de l'instance de Product au format JSON pour l'envoi
		Entity<Product> productEntity = Entity.entity(product, MediaType.APPLICATION_JSON);

		// Envoi de la requête HTTP POST pour la création de l'utilisateur
		Response response = target.path("/products").request().post(productEntity);

		// Vérification du code de retour HTTP
		assertEquals(201, response.getStatus());

		// Vérification que la création renvoie bien l'URI de la nouvelle instance dans le header HTTP 'Location'
		// ici : http://localhost:8080/myapp/products/rpenhello
		URI uriAttendue = target.path("/products").path(Integer.toString(product.getId())).getUri();
		assertTrue(uriAttendue.equals(response.getLocation()));
	}

	/**
	 * Test de création en double d'un utilisateur. Doit renvoyer 409
	 */
	@Test
	public void testCreateSameProduct() {
		Product product = new Product(1,1,5, "Red", "Pen with Hello on it");
		Entity<Product> productEntity = Entity.entity(product, MediaType.APPLICATION_JSON);

		// Envoi de la requête HTTP POST pour la création de l'utilisateur
		int first = target.path("/products").request().post(productEntity).getStatus();

		// Vérification du code de retour HTTP
		assertEquals(201, first);

		int same = target.path("/products").request().post(productEntity).getStatus();
		assertEquals(409, same);

		Product product2 = new Product(1,1,5, "Red", "Pen with Hello on it");
		Entity<Product> productEntity2 = Entity.entity(product2, MediaType.APPLICATION_JSON);

		int same2 = target.path("/products").request().post(productEntity2).getStatus();
		assertEquals(409, same2);
	}

	/**
	 * Vérifie qu'initialement on a une liste d'utilisateurs vide
	 */
	@Test
	public void testGetEmptyListofProducts() {
		List<Product> list = target.path("/products").request().get(new GenericType<List<Product>>(){});
		assertTrue(list.isEmpty());
	}

	/**
	 * Vérifie que je renvoie bien une liste contenant tous les utilisateurs (ici 2)
	 */
	@Test
	public void testGetTwoProducts() {
		Product product1 = new Product(1,1,5, "Red", "Pen with Hello on it");
		Entity<Product> productEntity = Entity.entity(product1, MediaType.APPLICATION_JSON);
		target.path("/products").request().post(productEntity);

		Product product2 = new Product(2,2,10, "Blue", "Pen with Good bye on it");
		productEntity = Entity.entity(product2, MediaType.APPLICATION_JSON);

		target.path("/products").request().post(productEntity);

		List<Product> list = target.path("/products").request().get(new GenericType<List<Product>>(){});
		assertEquals(2, list.size());
	}
	
	/**
	 * Vérifie la récupération d'un utilisateur spécifique
	 */
	@Test
	public void testGetOneProduct() {
	  Product product = new Product(1,1,5, "Red", "Pen with Hello on it");
	  Entity<Product> productEntity = Entity.entity(product, MediaType.APPLICATION_JSON);
	  target.path("/products").request().post(productEntity);

	  Product result = target.path("/products").path("1").request().get(Product.class);
	  assertEquals(product, result);
	}

	/**
	 * Vérifie que la récupération d'un utilisateur inexistant renvoie 404
	 */
	@Test
	public void testGetInexistantProduct() {
	  int notFound = target.path("/products").path("5").request().get().getStatus();
	  assertEquals(404, notFound);
	}
	
	/**
	 *
	 * Vérifie que la suppression d'une ressource est effective
	 */
	@Test
	public void testDeleteOneProduct() {
	  Product product = new Product(1,1,5, "Red", "Pen with Hello on it");
	  Entity<Product> productEntity = Entity.entity(product, MediaType.APPLICATION_JSON);
	  target.path("/products").request().post(productEntity);

	  int code = target.path("/products").path("1").request().delete().getStatus();
	  assertEquals(204, code);

	  int notFound = target.path("/products").path("1").request().get().getStatus();
	  assertEquals(404, notFound);  
	}

	/**
	 *
	 * Vérifie que la suppression d'un utilisateur inexistant renvoie 404
	 */
	@Test
	public void testDeleteInexistantProduct() {
	  int notFound = target.path("/products").path("tking").request().delete().getStatus();
	  assertEquals(404, notFound);
	}
	
	/**
	  *
	  * Vérifie que la modification d'un utilisateur inexistant renvoie 404
	  */
	 @Test
	 public void testModifyInexistantProduct() {
	   Product inexistant = new Product(1,1,5, "Red", "Pen with Hello on it");
	   Entity<Product> productEntity = Entity.entity(inexistant, MediaType.APPLICATION_JSON);
	   int notFound = target.path("/products").path("1").request().put(productEntity).getStatus();
	   assertEquals(404, notFound);
	 }

	/**
	  *
	  * Vérifie que la modification d'une ressource est effective
	  */
	 @Test
	 public void testModifyProduct() {
	   Product product = new Product(2,2,10, "Blue", "Pen with Good bye on it");
	   Entity<Product> productEntity = Entity.entity(product, MediaType.APPLICATION_JSON);
	   target.path("/products").request().post(productEntity);

	   Product modified = new Product(2,2,10, "Blue", "Pen with GoodBy on it");
	   productEntity = Entity.entity(modified, MediaType.APPLICATION_JSON);

	   int noContent = target.path("/products").path("2").request().put(productEntity).getStatus();
	   assertEquals(204, noContent);

	   Product retrieved = target.path("/products").path("2").request().get(Product.class);
	   assertEquals(modified, retrieved);
	 }
	 
	 /**
	 *
	 * Vérifie la prise en charge de l'encodage application/x-www-form-urlencoded
	 */
	@Test
	public void testCreateUserFromForm() {
	  Form form = new Form();
	  form.param("id", "3");
	  form.param("order", "3");
	  form.param("amont", "4");
	  form.param("color", "green");
	  form.param("text", "table with yo on it");

	  Entity<Form> formEntity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
	  int code = target.path("/products").request().post(formEntity).getStatus();

	  assertEquals(201, code);
	}
	
	 /**
	  * Vérifie qu'on récupère bien un utilisateur avec le type MIME application/xml
	  */
	@Test
	 public void testGetProductAsXml() {
	   Product product = new Product(2,2,10, "Blue", "Pen with Good bye on it");
	   Entity<Product> productEntity = Entity.entity(product, MediaType.APPLICATION_JSON);
	   target.path("/products").request().post(productEntity);

	   int code = target.path("/products").path("2").request(MediaType.APPLICATION_XML).get().getStatus();
	   assertEquals(200, code);
	 }
}
