package fr.ulille.iut;


import javax.ws.rs.*;



import javax.ws.rs.core.*;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Ressource Product (accessible avec le chemin "/products")
 */
@Path("products")
public class ProductResource {
	// Pour l'instant, on se contentera d'une variable statique pour conserver l'état

	// L'annotation @Context permet de récupérer des informations sur le contexte d'exécution de la ressource.
	// Ici, on récupère les informations concernant l'URI de la requête HTTP, ce qui nous permettra de manipuler
	// les URI de manière générique.
	@Context
	public UriInfo uriInfo;

	/**
	 * Une ressource doit avoir un contructeur (éventuellement sans arguments)
	 */
	public ProductResource() {}

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
		Connection c = Launch.getInstance();
		try { 
			PreparedStatement pstmt = c.prepareStatement("INSERT INTO Product (priority, amont, color, description) VALUES (?, ?, ?, ?);");
			pstmt.setInt(1, product.getPriority());
			pstmt.setInt(2, product.getAmont());
			pstmt.setString(3, product.getColor());
			pstmt.setString(4, product.getDescription());
			pstmt.executeUpdate();
			pstmt.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}finally {
			try {
				if (c != null)
					c.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// On renvoie 201 et l'instance de la ressource dans le Header HTTP 'Location'
		URI instanceURI = uriInfo.getAbsolutePathBuilder().path(Integer.toString(product.getId())).build();
		return Response.created(instanceURI).build();
	}

	/**
	 * Method prenant en charge les requêtes HTTP GET.
	 *
	 * @return Une liste d'utilisateurs
	 */
	@GET
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		Connection c = Launch.getInstance();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM Product;" );
			while ( rs.next() ) {
				int id = rs.getInt("id");
				int  priority = rs.getInt("priority");
				int amont  = rs.getInt("amont");
				String  color = rs.getString("color");
				String description = rs.getString("description");
				products.add(new Product(id,priority,amont,color,description));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}finally {
			try {
				if (c != null)
					c.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return products;
	}

	/** 
	 * Méthode prenant en charge les requêtes HTTP GET sur /products/{login}
	 *
	 * @return Une instance de Product
	 */
	@GET
	@Path("{id}")
	@Produces({"application/json", "application/xml"})
	public Product getProduct(@PathParam("id") int id) {
		Product p = null;
		//boolean in = false;
		Connection c = Launch.getInstance();
		try {
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM Product WHERE id = ?" );
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery( );
			if (!rs.isBeforeFirst())
				return null;
			else {
				while ( rs.next() ) {
					int  priority = rs.getInt("priority");
					int amont  = rs.getInt("amont");
					String  color = rs.getString("color");
					String description = rs.getString("description");
					p = new Product(id,priority,amont,color,description);
				}
				rs.close();
				stmt.close();
			}
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}finally {
			try {
				if (c != null)
					c.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return p;
	}

	@DELETE
	@Path("{id}")
	public Response deleteProduct(@PathParam("id") int id) {
		// Si l'utilisateur est inconnu, on renvoie 404
		Product p = getProduct(id);
		if (p == null)
			return null;

		Connection c = Launch.getInstance();

		try {
			PreparedStatement stmt = c.prepareStatement("DELETE FROM Product WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (c != null)
					c.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return Response.ok().build();
	}

	/** 
	 * Méthode prenant en charge les requêtes HTTP DELETE sur /products{id}
	 *
	 * @param id le id de l'utilisateur à modifier
	 * @param product l'entité correspondant à la nouvelle instance
	 * @return Un code de retour HTTP dans un objet Response
	 **/
	@PUT
	@Path("{id}")
	public Response modifyProduct(@PathParam("id") int id, Product product) {
		// Si l'utilisateur est inconnu, on renvoie 404
		Product p = getProduct(id);
		if (p == null)
			return null;
		
		Connection c = Launch.getInstance();
		try {
			PreparedStatement stmt = c.prepareStatement("UPDATE Product SET priority = ?, amont = ?, color = ?, description = ? WHERE id = ?");
			stmt.setInt(1, product.getPriority() == 0 ? p.getPriority(): product.getPriority());
			stmt.setInt(2, product.getAmont() == 0 ? p.getAmont(): product.getAmont());
			stmt.setString(3, product.getColor() == null? p.getColor() : product.getColor());
			stmt.setString(4, product.getDescription() == null ? p.getDescription() : product.getDescription());
			stmt.setInt(5, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (c != null)
					c.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return Response.ok().build();
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
	 **/
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response createProduct(@FormParam("id") int id, @FormParam("color") String color,@FormParam("priority") int priority,@FormParam("amont") int amont, @FormParam("description") String description) {
		return createProduct(new Product(id, priority, amont, color, description));
	}
	
    public int getColumnCount() {
        return new Product().getColumnCount();
    }
}
