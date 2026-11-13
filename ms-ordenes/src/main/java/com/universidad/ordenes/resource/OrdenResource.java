package com.universidad.ordenes.resource;

import com.universidad.ordenes.client.ProductoDTO;
import com.universidad.ordenes.client.ProductoServiceClient;
import com.universidad.ordenes.entity.Orden;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Path("/ordenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdenResource {

    @Inject
    @RestClient
    ProductoServiceClient productClient;

    @GET
    public List<Orden> listAll() {
        return Orden.listAll();
    }

    @POST
    @Transactional
    public Response createOrden(List<Long> productoIds) {
        // Validar que la lista contenga exactamente 3 IDs
        if (productoIds == null || productoIds.size() != 3) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "error", "Solicitud inválida. La orden debe contener exactamente 3 IDs de productos.",
                            "recibidos", productoIds != null ? productoIds.size() : 0
                    ))
                    .build();
        }

        double total = 0.0;

        for (Long id : productoIds) {
            if (id == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Los IDs de los productos no pueden ser nulos."))
                        .build();
            }

            try {
                ProductoDTO producto = productClient.getById(id);
                if (producto == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity(Map.of("error", "Producto con ID " + id + " no encontrado en ms-productos."))
                            .build();
                }
                total += producto.price;
            } catch (WebApplicationException e) {
                if (e.getResponse() != null && e.getResponse().getStatus() == 404) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity(Map.of("error", "Producto con ID " + id + " no encontrado en ms-productos (HTTP 404)."))
                            .build();
                }
                return Response.status(Response.Status.BAD_GATEWAY)
                        .entity(Map.of("error", "Error al comunicarse con ms-productos para el producto " + id + ": " + e.getMessage()))
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(Map.of("error", "Servicio ms-productos no disponible: " + e.getMessage()))
                        .build();
            }
        }

        // Crear la orden con los 3 IDs validados y la suma de sus precios
        Orden orden = new Orden(LocalDateTime.now(), productoIds, total);
        orden.persist();

        return Response.created(URI.create("/ordenes/" + orden.id))
                .entity(orden)
                .build();
    }
}

