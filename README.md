# Taller 1 - Computacion en Internet II

Sistema de gestion de discografia de artistas musicales con Spring Core y Jakarta Servlets.

## Rama actual: `ArchivoConfiguracion` (Version 3 - Archivo de configuracion Java)

La inyeccion de dependencias se declara en `com.example.config.AppConfig`, una clase
`@Configuration` con un metodo `@Bean` por cada repositorio y servicio.

En esta rama **no existe `applicationContext.xml`** y **no se usa `@ComponentScan`**:
el contexto se levanta con `new AnnotationConfigApplicationContext(AppConfig.class)`
desde `AppContextListener`.

Las clases de modelo, repositorio, servicio y servlets son identicas a las de `main`.
Lo unico que cambia entre las tres versiones es el cableado de los beans.

## Las tres versiones del taller

| Rama | Version | Como se declaran los beans |
|---|---|---|
| `main` | XML | `src/main/resources/applicationContext.xml` con `<bean>` y `<constructor-arg>` |
| `Annotations` | Annotations | `@Repository` / `@Service` / `@Component` / `@Autowired` + `<context:component-scan>` |
| `ArchivoConfiguracion` | Archivo de configuracion Java | `config/AppConfig.java` con `@Configuration` y `@Bean` |

## Como ejecutar

```bash
cd taller
mvn clean package
```

Desplegar `target/taller.war` en un Tomcat 10 o superior y entrar a
`http://localhost:8080/taller/`.

## Funcionalidad

La aplicacion arranca con 10 artistas y 50 canciones (5 por artista) cargados por
`util/DataInitializer`.

| Ruta | Descripcion |
|---|---|
| `/home` | Dashboard con totales |
| `/artists` | Listado de todos los artistas |
| `/artists/create` | Formulario para crear un artista |
| `/artists/search` | Buscar artista por nombre con todas sus canciones |
| `/artists/delete` | Eliminar un artista por id |
| `/tracks` | Listado de canciones con sus artistas asociados |
| `/tracks/create` | Crear una cancion asignando uno o varios artistas |
| `/tracks/delete` | Eliminar una cancion por id |
