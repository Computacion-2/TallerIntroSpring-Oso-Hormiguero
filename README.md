# Taller 1 - Computacion en Internet II

Sistema de gestion de discografia de artistas musicales con Spring Core y Jakarta Servlets.

## Rama actual: `Annotations` (Version 2 - Annotations en las clases)

La inyeccion de dependencias se declara con anotaciones en las propias clases:
`@Repository` en los repositorios, `@Service` en los servicios, `@Component` en
`DataInitializer`, `@Autowired` en los constructores y `@PostConstruct` / `@PreDestroy`
para el ciclo de vida de los beans.

### Importante: el `applicationContext.xml` de esta rama NO es la version XML

En esta rama `src/main/resources/applicationContext.xml` **no declara ningun `<bean>`**.
Su unica funcion es activar el escaneo de anotaciones:

```xml
<context:annotation-config/>
<context:component-scan base-package="com.example"/>
```

La version XML pura (con `<bean>`, `<constructor-arg>` y `<property>`) vive en la rama
`main`, en un archivo con **el mismo nombre** pero contenido totalmente distinto. No los
confundas al comparar las ramas.

## Las tres versiones del taller

| Rama | Version | Como se declaran los beans |
|---|---|---|
| `main` | XML | `src/main/resources/applicationContext.xml` con `<bean>` y `<constructor-arg>` |
| `Annotations` | Annotations | `@Repository` / `@Service` / `@Component` / `@Autowired` + `<context:component-scan>` |
| `ArchivoConfiguracion` | Archivo de configuracion Java | `config/AppConfig.java` con `@Configuration` y `@Bean` |

Las clases de modelo, repositorio, servicio y servlets son las mismas en las tres ramas.
Lo unico que cambia entre versiones es el cableado de los beans.

## Como ejecutar

```bash
cd taller
mvn clean package
```

Desplegar `target/taller.war` en un Tomcat 10 o superior y entrar a
`http://localhost:8080/taller/`.

## Funcionalidad

La aplicacion arranca con 10 artistas y 50 canciones (5 por artista) cargados por
`util/DataInitializer` mediante `@PostConstruct`.

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
