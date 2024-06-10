package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConversorDatos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConversorDatos conversor = new ConversorDatos();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=fea38674";

    public void menu(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        //Datos generales de la serie
        var serie = scanner.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + serie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);
        //Datos de las temporadas de la serie
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE+ serie.replace(" ", "+")+"&Season="+i+ API_KEY);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        //temporadas.forEach(System.out::println);

        //Mostara solo el titulo de los episodios para las temporadas
//        for (int i = 0; i < datos.totalTemporadas(); i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).episodio());
//
//            }
//        }
       // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.episodio())));


        //Convertir informacion a una lista de tipo DatosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Top 5 episodios
        System.out.println("Top 5 episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
