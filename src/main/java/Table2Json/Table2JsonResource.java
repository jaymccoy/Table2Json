package Table2Json;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Path("/table2json")
public class Table2JsonResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        String message = "";
        final Optional<String> filePath;

        filePath = environment("filePath");
        String fileName = String.format("%s%s", filePath.get(), "Table2Json.html");

        File file = new File(fileName);
        if (file.exists() && file.isFile()) {

            try {
                message = new String(Files.readAllBytes(Paths.get(fileName)));
            } catch (IOException e) {
                e.printStackTrace();
                //TODO:add more error handling
            }//end try

            return Response
                    .status(200)
                    .entity(message.toString())
                    .build();
        } else {
            return Response
                    .status(404)
                    .build();
        }//end if
    }//end function

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setTestWorld(String requestBody
            , @QueryParam("database") String database
            , @QueryParam("schema") String schema
            , @QueryParam("table") String table) {

        Gson g = new Gson();

        ConnectionConfig config = g.fromJson(requestBody, ConnectionConfig.class);

        JSONObject json = new JSONObject();
        JSONObject form = new JSONObject();
        JSONObject query = new JSONObject();

        form.put("pass", "************");
        form.put("url", config.getUrl());
        form.put("user", config.getUser());

        query.put("database", database);
        query.put("schema", schema);
        query.put("table", table);

        json.put("query", query);
        json.put("form", form);

        String message = table2json(config.url, config.user, config.pass, table, schema, database);

        return Response
                .status(200)
                .entity(message.toString())
                .build();
    }//end function

    private String table2json(String url, String user, String pass, String table) {
        return table2json(url, user, pass, table, null, null);
    }//end function

    private String table2json(String url, String user, String pass, String table, String schema) {
        return table2json(url, user, pass, table, schema, null);
    }//end function

    private String table2json(ConnectionConfig config, String table, String schema, String database) {
        return table2json(config.url, config.user, config.pass, table, schema, database);
    }//end function

    private String table2json(String url, String user, String pass, String table, String schema, String database) {

        JSONArray message = new JSONArray();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);

            Statement stat = (Statement) conn.createStatement();

            String t1 = "";
            String t2 = "";
            if (database != null) {
                t1 = database + ".";
            }
            if (schema != null) {
                t2 = schema + ".";
            }

            String query = "SELECT * FROM " + t1 + t2 + table;
            ResultSet result = stat.executeQuery(query);
            ResultSetMetaData meta = result.getMetaData();

            while (result.next()) {
                JSONObject current = new JSONObject();
                for (int column = 1; column <= meta.getColumnCount(); ++column) {
                    String column_name = meta.getColumnName(column);
                    final Object value = result.getObject(column);

                    switch (meta.getColumnType(column)) {
                        case Types.INTEGER:
                        case Types.NUMERIC:
                            if (value != null) {
                                current.put(column_name, Integer.valueOf((String.valueOf(value))));
                            } else {
                                current.put(column_name, JSONObject.NULL);
                            }//end if
                            break;
                        case Types.DOUBLE:
                        case Types.FLOAT:
                            if (value != null) {
                                current.put(column_name, Float.valueOf((String.valueOf(value))));
                            } else {
                                current.put(column_name, JSONObject.NULL);
                            }//end if
                            break;
                        case Types.BOOLEAN:
                            current.put(column_name, value);
                            break;
                        default:
                            if (value != null) {
                                current.put(column_name, String.valueOf(value));
                            } else {
                                current.put(column_name, JSONObject.NULL);
                            }//end if
                            break;
                    }//end switch

                }//end for
                message.put(current);
            }//end while
        } catch (Exception e) {
            //TODO add more error handling
            e.printStackTrace();
        }//end try

        return message.toString();
    }//end function


    private static Optional<String> environment(String environmentOrSystemProperty) {
        String value = System.getenv(environmentOrSystemProperty);
        if (value == null) {
            value = System.getProperty(environmentOrSystemProperty);
        }//end if
        return Optional.ofNullable(value);
    }//end function

}//end class
