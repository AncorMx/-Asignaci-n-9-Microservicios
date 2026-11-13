package com.universidad.productos.resource;

import com.universidad.productos.entity.Producto;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {

    @GET
    public List<Producto> listAll() {
        return Producto.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Producto producto = Producto.findById(id);
        if (producto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Producto con ID " + id + " no encontrado"))
                    .build();
        }
        return Response.ok(producto).build();
    }

    @POST
    @Transactional
    public Response create(Producto producto) {
        if (producto == null || producto.name == null || producto.name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El nombre del producto es obligatorio"))
                    .build();
        }
        producto.id = null;
        producto.persist();
        return Response.created(URI.create("/productos/" + producto.id))
                .entity(producto)
                .build();
    }
}

