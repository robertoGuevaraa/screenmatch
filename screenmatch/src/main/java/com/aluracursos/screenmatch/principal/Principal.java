package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConversorDatos;

import javax.sound.midi.Soundbank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConversorDatos conversor = new ConversorDatos();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=fea38674";
    private List<DatosSerie> datosSeries = new ArrayList<>();


    public void mostrarMenu(){
        var opcion = -1;
        while (opcion!=0){
            var menu = """
                    1. Buscar series
                    2. Buscar episodios
                    3. Mostrar series buscadas
                    
                    0. Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Cerrando aplicaion...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }
    }

    private DatosSerie getDatosSerie(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = scanner.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie(){
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i <= datosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obtenerDatos(URL_BASE+ datosSerie.titulo().replace(" ", "+")+ API_KEY);
            DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);

    }

    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        List<Serie> series = new ArrayList<>();
        series = datosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }



//    public static Categoria fromString(String text) {
//        for (Categoria categoria : Categoria.values()) {
//            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
//                return categoria;
//            }
//        }
//        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
//    }
//    public void menu(){
//        System.out.println("Escribe el nombre de la serie que deseas buscar");
//        //Datos generales de la serie
//        var serie = scanner.nextLine();
//        var json = consumoApi.obtenerDatos(URL_BASE + serie.replace(" ", "+") + API_KEY);
//        var datos = conversor.obtenerDatos(json, DatosSerie.class);
//        System.out.println(datos);
//        //Datos de las temporadas de la serie
//        List<DatosTemporadas> temporadas = new ArrayList<>();
//        for (int i = 1; i <= datos.totalTemporadas(); i++) {
//            json = consumoApi.obtenerDatos(URL_BASE+ serie.replace(" ", "+")+"&Season="+i+ API_KEY);
//            var datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
//            temporadas.add(datosTemporada);
//        }
        //temporadas.forEach(System.out::println);

        //-----------------------Mostara solo el titulo de los episodios para las temporadas--------------------------
//        for (int i = 0; i < datos.totalTemporadas(); i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).episodio());
//
//            }
//        }
       // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.episodio())));


        //-------------------------Convertir informacion a una lista de tipo DatosEpisodio----------------------------
//        List<DatosEpisodio> datosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());

        //--------------------------------------------Top 5 episodios-------------------------------------------------
//        System.out.println("Top 5 episodios");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("1. Filtro (N/A) " + e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("2. Ordenar de Mayor a Menor " + e))
//                .map(e -> e.episodio().toUpperCase())
//                .peek(e -> System.out.println("3. Mayusculas "+ e))
//                .limit(5)
//                .forEach(System.out::println);

        //-----------------------Convirtiendo los datos a una lista de tipo Episodio----------------------------------
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream().
//                        map(d -> new Episodio(t.numeroTemporada(),d)))
//                .collect(Collectors.toList());
        //episodios.forEach(System.out::println);


        //--------------------------Busqueda de episodios a partir de año especifico----------------------------------
//        System.out.println("Indica el año a partir del cual deseas ver los episodios");
//        var fecha = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1,1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getFechaLanzamiento() != null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada()+
//                                " Titulo: " + e.getTitulo()+
//                                " Fecha de Lanzamiento: " + e.getFechaLanzamiento().format(dtf)
//                ));

        //------------------------Buscar episodios por considencia del titulo------------------------------------------
//        System.out.println("Escribe el titulo del episodio que deseas buscar");
//        var tituloParcial = scanner.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(tituloParcial.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado");
//            System.out.println("Los datos son: " + episodioBuscado.get().getTitulo());
//        }else {
//            System.out.println("Episodio no encontrado");
//        }


//        Map<Integer, Double> evaluacionesTemporada = episodios.stream()
//                .filter(e -> e.getEvaluacion() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getEvaluacion)));
//        System.out.println(evaluacionesTemporada);
//
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getEvaluacion() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
//        System.out.println("Media de evaluaciones: " + est.getAverage());
//        System.out.println("Episodio mejor valorado: " + est.getMax());
//        System.out.println("Peor evaluacion de episodio: " + est.getMin());
    //}
}
