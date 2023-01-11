package id.kawahedukasi.controller;

import id.kawahedukasi.model.Item;

import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController{

    @POST
    @Transactional
    public Response create(JsonObject request) {

        Item item = new Item();
        item.name = request.getString("name");
        item.count = request.getInt("count");
        item.price = request.getInt("price");
        item.type = request.getString("type");
        item.description = request.getString("description");


        item.persist();
        return Response.ok().entity(new HashMap<>()).build();
    }

    //get semua data di database
    @GET
    public Response getList() {
        return Response.ok().entity(Item.listAll()).build();
    }

    //get by type
    @GET
    @Path("/type/{type}")
    public Response getByType(@PathParam("type") String type) {
        if (!type.equals("Jacket") || !type.equals("Hoodie") || !type.equals("Shirt")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "type_not_found"))
                    .build();
        }
        List<Item> itemList = Item.find("UPPER(type) = ?1", type.toUpperCase(Locale.ROOT)).list();
        return Response.ok().entity(itemList).build();
    }

    //update data by id
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, JsonObject request) {
        Item item = Item.findById(id);
        item.name = request.getString("name");
        item.count = request.getInt("count");
        item.price = request.getInt("price");
        item.type = request.getString("type");
        item.description = request.getString("description");

        // save data
        item.persist();

        return Response.ok().entity(new HashMap<>()).build();
    }

    //delete by id
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        Item.deleteById(id);

        return Response.ok().entity(new HashMap<>()).build();
    }
}
