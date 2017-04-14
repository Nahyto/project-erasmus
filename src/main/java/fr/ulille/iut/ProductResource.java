package fr.ulille.iut;

import javax.ws.rs.*;


import javax.ws.rs.core.*;

import java.net.URI;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Ressource Product (accessible avec le chemin "/products")
 */
@Path("products")
public class ProductResource {
	// Pour l'instant, on se contentera d'une variable statique pour conserver l'état
	private static Map<String, Product> products = new HashMap<>();

	// L'annotation @Context permet de récupérer des informations sur le contexte d'exécution de la ressource.
	// Ici, on récupère les informations concernant l'URI de la requête HTTP, ce qui nous permettra de manipuler
	// les URI de manière générique.
	@Context
	public UriInfo uriInfo;

	/**
	 * Une ressource doit avoir un contructeur (éventuellement sans arguments)
	 */
	public ProductResource() {
	}

	/**
	 * Méthode de création d'un utilisateur qui prend en charge les requêtes HTTP POST
	 * La méthode renvoie l'URI de la nouvelle instance en cas de succès
	 *
	 * @param  product Instance d'utilisateur à créer
	 * @return Response le corps de la réponse est vide, le code de retour HTTP est fixé à 201 si la création est faite
	 *         L'en-tête contient un champs Location avec l'URI de la nouvelle ressource
	 */
	@POST
	public Response createProduct(Product product) {
		// Si l'utilisateur existe déjà, renvoyer 409
		if ( products.containsKey(Integer.toString(product.getId())) ) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		else {
			products.put(Integer.toString(product.getId()), product);

			// On renvoie 201 et l'instance de la ressource dans le Header HTTP 'Location'
			URI instanceURI = uriInfo.getAbsolutePathBuilder().path(Integer.toString(product.getId())).build();
			return Response.created(instanceURI).build();
		}
	}

	/**
	 * Method prenant en charge les requêtes HTTP GET.
	 *
	 * @return Une liste d'utilisateurs
	 */
	@GET
	public List<Product> getProducts() {
		return new ArrayList<Product>(products.values());
	}

	/** 
	  * Méthode prenant en charge les requêtes HTTP GET sur /products/{login}
	  *
	  * @return Une instance de Product
	  */
	 @GET
	 @Path("{id}")
	 @Produces({"application/json", "application/xml"})
	 public Product getProduct(@PathParam("id") String id) {
	   // Si l'utilisateur est inconnu, on renvoie 404
	   if (  ! products.containsKey(id) ) {
	           throw new NotFoundException();
	         }
	         else {
	           return products.get(id);
	         }
	 }

	@DELETE
	@Path("{id}")
	public Response deleteProduct(@PathParam("id") String id) {
		// Si l'utilisateur est inconnu, on renvoie 404
		if (  ! products.containsKey(id) ) {
			throw new NotFoundException();
		}
		else {
			products.remove(id);
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}

	/** 
	 * Méthode prenant en charge les requêtes HTTP DELETE sur /products{id}
	 *
	 * @param id le id de l'utilisateur à modifier
	 * @param product l'entité correspondant à la nouvelle instance
	 * @return Un code de retour HTTP dans un objet Response
	 */
	@PUT
	@Path("{id}")
	public Response modifyProduct(@PathParam("id") String id, Product product) {
		// Si l'utilisateur est inconnu, on renvoie 404
		if (  !products.containsKey(Integer.toString(product.getId())) ) {
			throw new NotFoundException();
		}
		else {
			products.put(Integer.toString(product.getId()), product);
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}

	/**
	 * Méthode de création d'un utilisateur qui prend en charge les requêtes HTTP POST au format application/x-www-form-urlencoded
	 * La méthode renvoie l'URI de la nouvelle instance en cas de succès
	 *
	 * @param id id de l'utilisateur
	 * @param color nom de l'utilisateur
	 * @param text le text de l'utilisateur
	 * @return Response le corps de la réponse est vide, le code de retour HTTP est fixé à 201 si la création est faite
	 *         L'en-tête contient un champs Location avec l'URI de la nouvelle ressource
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response createProduct(@FormParam("id") int id, @FormParam("color") String color,@FormParam("order") int order,@FormParam("amont") int amont, @FormParam("test") String text) {
		// Si l'utilisateur existe déjà, renvoyer 409
		if ( products.containsKey(id) ) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		else {
			products.put(Integer.toString(id), new Product(id,order,amont,color,text));

			// On renvoie 201 et l'instance de la ressource dans le Header HTTP 'Location'
			URI instanceURI = uriInfo.getAbsolutePathBuilder().path(Integer.toString(id)).build();
			return Response.created(instanceURI).build();
		}
	}
}
