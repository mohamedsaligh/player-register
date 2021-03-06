package com.scb.cic;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by saligh on 4/4/17.
 */
@Path("/home")
public class Resource {

    private String DATAFILE = "/tmp/data.txt";

    @GET
    @Path("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@PathParam("name") String name) {
        File file = new File(".");
        String path = file.getAbsolutePath();
        return "**I am bold!**! " + name + "\nFull Path: " + path;
    }

    @GET
    @Path("/players/getAll")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllPlayers(@QueryParam("_") String jsonCallback) {
        String data = getAllData();
        return "receive" + "(" + data + ")";
    }

    @POST
    @Path("/player/register")
    @Produces(MediaType.TEXT_HTML)
    public void postMethod(@FormParam("name") String name,
                           @FormParam("role") String role) {
        System.out.println("REGISTRATION: name = " + name + ", role = " + role);
        name = name.replace(",", " ");
        addPlayer(name, role);
    }


    private String getAllData() {
        return getAllDataFromFile();
        //return getAllDataFromDB();
    }

    private void addPlayer(String name, String role) {
        try {
            addPlayerToFile(name, role);
            //addPlayerToDB(name, role);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean init() throws IOException {
        File datafile = new File(DATAFILE);
        return datafile.createNewFile();
    }


    private void addPlayerToFile(String name, String role) throws Exception {
        if (init()) {
            //do nothing...
        } else {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(DATAFILE, true), "UTF-8"));
            role = role.replace(",", " ");
            writer.append(name + "," + role + "\n");
            writer.flush();
            writer.close();
        }

    }

    private String getAllDataFromFile() {
        String line = "";
        String cvsSplitBy = ",";
        String data = new String("");
        try {
            if (init()) {
                //do nothing...
            } else {
                int counter = 1;
                BufferedReader br = new BufferedReader(new FileReader(DATAFILE));
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] read = line.split(cvsSplitBy);
                    if ("".equalsIgnoreCase(data)) {
                        data = data + "{" +
                                "\"id\":" + "\"" + counter++ + "\"," +
                                "\"name\":" + "\"" + read[0] + "\"," +
                                "\"role\":" + "\"" + read[1] + "\"," +
                                "\"team\":" + "\"" + " " + "\"}";
                    } else {
                        data = data + ",{" +
                                "\"id\":" + "\"" + counter++ + "\"," +
                                "\"name\":" + "\"" + read[0] + "\"," +
                                "\"role\":" + "\"" + read[1] + "\"," +
                                "\"team\":" + "\"" + " " + "\"}";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Response: " + data);
        return "[" + data + "]";
    }

    private void addPlayerToDB(String name, String role) throws Exception {
        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.insertPlayer(name, role, " ");

    }

    private String getAllDataFromDB() {
        StringBuilder data = new StringBuilder("");
        int counter = 1;
        PlayerDAO playerDAO = new PlayerDAO();
        List<Map<String, String>> playersList = playerDAO.getAllPlayers();
        for (Map<String, String> playerDetailsMap : playersList) {

            String name = playerDetailsMap.get("NAME");
            String role = playerDetailsMap.get("SKILL");
            String team = playerDetailsMap.get("TEAM");

            if ((data.length() <= 0)) {
                data = data.append("{" +
                        "\"id\":" + "\"" + counter++ + "\"," +
                        "\"name\":" + "\"" + name + "\"," +
                        "\"role\":" + "\"" + role + "\"," +
                        "\"team\":" + "\"" + team + "\"}");
            } else {
                data = data.append(",{" +
                        "\"id\":" + "\"" + counter++ + "\"," +
                        "\"name\":" + "\"" + name + "\"," +
                        "\"role\":" + "\"" + role + "\"," +
                        "\"team\":" + "\"" + team + "\"}");
            }
        }

        System.out.println("Response: " + data);
        return "[" + data + "]";
    }
}
