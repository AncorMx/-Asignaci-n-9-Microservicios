# Actividad: Microservicios con Quarkus, Panache y Base de Datos

Repositorio con la solucion de la practica de microservicios usando Quarkus. En esta version se agrego persistencia a base de datos para ambos servicios usando Hibernate con Panache, manteniendo cada microservicio en proyectos y puertos separados.

---

## Como se resolvio la actividad

Cumpliendo con los puntos solicitados:

1. Base de datos: Se configuro una base de datos H2 para cada microservicio (productosdb y ordenesdb). De esta forma cada servicio tiene sus propios datos aislados sin depender del otro.
2. Persistencia con Panache: Las clases Producto y Orden heredan de PanacheEntity, usando el patron Active Record para guardar (persist()), buscar (findById()) y listar (listAll()) directamente.
3. Proyectos y puertos separados:
   - ms-productos: Corre en el puerto 8081 y maneja el catalogo de productos con una carga inicial en import.sql.
   - ms-ordenes: Corre en el puerto 8082, recibe una orden con 3 IDs de productos, se comunica por REST con ms-productos para validar que existan y obtener sus precios, calcula el total y guarda la orden en su base de datos.

---

## Tecnologias utilizadas

- Java 17 o 21
- Quarkus 3.15.1
- Hibernate ORM con Panache (quarkus-hibernate-orm-panache)
- Base de datos H2 (quarkus-jdbc-h2)
- Cliente REST Jackson (quarkus-rest-client-jackson)

---

## Como ejecutar el proyecto

Para ejecutar ambos microservicios, se deben abrir dos terminales:

### Terminal 1: ms-productos
```bash
cd ms-productos
mvn quarkus:dev
```
El servicio queda disponible en: http://localhost:8081

### Terminal 2: ms-ordenes
```bash
cd ms-ordenes
mvn quarkus:dev -Ddebug=5006
```
El servicio queda disponible en: http://localhost:8082
