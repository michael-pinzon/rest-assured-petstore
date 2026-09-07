# Automatización de la API PetDog

## Introducción

Esta solución automatiza con Rest Assured la historia de usuario de la tienda PetDog sobre la API pública [Swagger Petstore](https://petstore.swagger.io/). Se cubren las seis funcionalidades solicitadas: creación de usuario, login, consulta de mascotas disponibles, consulta de una mascota por id, creación de una orden y logout.

Los tests están escritos con TestNG, se ejecutan mediante Maven y utilizan la API v2 en `https://petstore.swagger.io/v2` por defecto.

## Tecnologías

- Java 21.
- Maven 3.9 o superior.
- Rest Assured 6.0.1.
- TestNG 7.12.0.
- Jackson Databind 2.22.2 para serialización JSON.
- Maven Surefire 3.6.0.

## Estructura

```text
src/test/java/com/petdog/api/
├── client/
│   └── PetStoreClient.java       # Endpoints y especificaciones Rest Assured
├── config/
│   └── ApiConfig.java             # URI y base path configurables
├── data/
│   ├── TestDataFactory.java       # Usuarios y órdenes únicos
│   └── TestUser.java              # Datos inmutables de usuario
└── tests/
    ├── OrderApiTest.java          # Creación de orden
    ├── PetApiTest.java            # Listado y detalle de mascotas
    └── UserApiTest.java           # Crear usuario, login y logout
```

## Casos automatizados

| Funcionalidad | Clase y método | Endpoint principal | Validaciones |
| --- | --- | --- | --- |
| Crear usuario | `UserApiTest.createUser_returnsSuccessResponse` | `POST /user` | HTTP 200, `code=200` e id devuelto |
| Login | `UserApiTest.login_withNewlyCreatedUser_returnsSessionMessage` | `GET /user/login` | Usuario creado en el mismo test, sesión devuelta |
| Mascotas disponibles | `PetApiTest.findPetsByStatus_available_returnsAvailablePets` | `GET /pet/findByStatus?status=available` | HTTP 200, colección no vacía e items con status `available` |
| Detalle de mascota | `PetApiTest.getPetById_forAvailablePet_returnsPetDetails` | `GET /pet/{petId}` | Id, nombre, fotos y status documentado |
| Crear orden | `OrderApiTest.placeOrder_forAvailablePet_returnsPlacedOrder` | `POST /store/order` | Id, mascota, cantidad, fecha, estado `placed` y `complete` |
| Logout | `UserApiTest.logout_afterLogin_returnsSuccessResponse` | `GET /user/logout` | HTTP 200, `code=200` y `message=ok` |

## Independencia de los tests

Cada método puede ejecutarse solo o en cualquier orden:

- Los usuarios se crean con un id y username únicos usando timestamp y UUID.
- Los tests de login y logout crean y autentican su propio usuario durante su ejecución.
- Los tests de detalle y orden consultan una mascota disponible dentro del mismo test; no dependen del listado producido por otro test.
- No se comparten variables, credenciales, ids ni respuestas entre métodos de prueba.

## Ejecución

Desde la raíz del proyecto:

```bash
mvn clean test
```

Para ejecutar una clase concreta:

```bash
mvn clean -Dtest=UserApiTest test
mvn clean -Dtest=PetApiTest test
mvn clean -Dtest=OrderApiTest test
```

La URL puede cambiarse sin modificar el código:

```bash
mvn clean test -Dpetstore.baseUri=https://petstore.swagger.io -Dpetstore.basePath=/v2
```

## Consideraciones de la API pública

La ejecución requiere conexión a internet y depende de la disponibilidad del entorno compartido de Swagger Petstore. Las pruebas de usuario y orden escriben datos en ese entorno; los datos se generan únicos para evitar colisiones. La API pública puede presentar límites de tráfico, latencia o cambios de datos externos.
