package oose.dea.mikevanegmond.spotitube_mongodb_lab;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequest) {

        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("spotitube");
        MongoCollection<Document> collection = database.getCollection("users");

        Document query = new Document("username", loginRequest.getUser());
        MongoCursor result = collection.find(query).iterator();

        if (!result.hasNext()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Password & Username combination is incorrect.")
                    .build();
        }

        Document oneResult = (Document) result.next();

        if (!oneResult.getString("password").equals(loginRequest.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Password & Username combination is incorrect.")
                    .build();
        }

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUsername("username");
        loginResponseDTO.setPassword("password");

        return Response.status(Response.Status.OK)
                .entity(loginResponseDTO)
                .build();
    }
}
