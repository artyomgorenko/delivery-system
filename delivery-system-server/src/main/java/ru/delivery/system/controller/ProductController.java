package ru.delivery.system.controller;

import ru.delivery.system.dao.ProductManager;
import ru.delivery.system.model.json.product.ProductListResponse;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

import static ru.delivery.system.common.utils.JsonSerializer.toJson;

@Path("product/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {

    @EJB
    private ProductManager productManager;

    @GET
    @Path("/productList")
    public Response getProductList() {
        try {
            ProductListResponse productListResponse = new ProductListResponse();

            List<ProductListResponse.Product> productList = productManager.getProductList()
                    .stream().map(productEntity -> {
                        ProductListResponse.Product product = new ProductListResponse.Product();
                        product.setProductId(productEntity.getId());
                        product.setName(productEntity.getName());
                        product.setCategory(productEntity.getCategory().getCName());
                        product.setCost(productEntity.getCost());
                        product.setWeight(productEntity.getWeight());
                        product.setCost(productEntity.getCost());
                        product.setHeight(productEntity.getHeight());
                        product.setWidth(productEntity.getWidth());
                        product.setLength(productEntity.getLength());
                        return product;
                    }).collect(Collectors.toList());

            productListResponse.setProductList(productList);
            return Response.status(Response.Status.OK).entity(toJson(productListResponse)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

}
