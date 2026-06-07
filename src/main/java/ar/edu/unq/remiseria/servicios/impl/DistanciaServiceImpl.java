package ar.edu.unq.remiseria.servicios.impl;

import ar.edu.unq.remiseria.servicios.interfaces.DistanciaService;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DistanciaServiceImpl implements DistanciaService {

    private final String apiKey = "eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6ImQyNDQyOGU3MDhhZjRiZWY4ODZkOWIyMjliZTE3YTUxIiwiaCI6Im11cm11cjY0In0=";

    @Override
    public Double calcularDistanciaKm(String origen, String destino) {
        try {
            double[] coordOrigen = geocodificar(origen);
            double[] coordDestino = geocodificar(destino);

            return calcularDistancia(coordOrigen, coordDestino);

        } catch (Exception e) {
            throw new RuntimeException("Error calculando distancia", e);
        }
    }

    private double[] geocodificar(String direccion) throws Exception {
        String url = "https://api.openrouteservice.org/geocode/search?api_key=" + apiKey +
                "&text=" + java.net.URLEncoder.encode(direccion, "UTF-8");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Usamos Jackson
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        JsonNode coords = root.path("features").get(0).path("geometry").path("coordinates");

        double lon = coords.get(0).asDouble();
        double lat = coords.get(1).asDouble();
        return new double[]{lon, lat};
    }

    private double calcularDistancia(double[] origen, double[] destino) throws Exception {
        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + apiKey +
                "&start=" + origen[0] + "," + origen[1] +
                "&end=" + destino[0] + "," + destino[1];

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        double distanciaMetros = root.path("features").get(0)
                .path("properties")
                .path("segments").get(0)
                .path("distance").asDouble();

        return distanciaMetros / 1000.0; // km
    }
}

