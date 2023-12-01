---

## Desarrollador del Proyecto

- **Desarrollador:** Luis Gustavo García Reyna
- **Nacionalidad:** Peruana
- **Correo Electrónico:** lgr.developer.07@gmail.com

## Descripción del Proyecto

- **Nombre del Proyecto:** smart-job
- **Descripción:** API Rest para SmartJob

## Detalles del Proyecto

- **Versión de Spring Boot:** 2.4.2
- **Java Versión:** 1.8
- **Versión de CXF:** 3.2.0

## Dependencias Principales

- **Spring Boot Starter Data JPA:** Interfaz de acceso a datos JPA para Spring Boot.
- **Spring Boot Starter Web:** Inicia aplicaciones web Spring Boot.
- **Spring Boot Starter Security:** Provee seguridad para aplicaciones Spring Boot.
- **Spring Boot DevTools:** Herramientas para desarrollo rápido.
- **Spring Boot Starter Test:** Dependencias para pruebas en Spring Boot.
- **H2 Database:** Base de datos en memoria.
- **Lombok:** Biblioteca para la reducción de código boilerplate.
- **RxJava:** Biblioteca para programación reactiva en Java.
- **JJWT:** JSON Web Token para autenticación y autorización.
- **Gson:** Librería para trabajar con JSON en Java.
- **SpringFox Swagger 2:** Generación de documentación y visualización de API con Swagger 2.
- **Validation API:** API para validaciones.
- **Spring Boot Starter Validation:** Soporte de validaciones para Spring Boot.

## Build

- **Plugin Maven:** `spring-boot-maven-plugin`
- **Configuración:** Ejecutable

---

Smart Job API - Guía de Pruebas en Postman

Este README proporciona instrucciones para probar la API Smart Job en Postman. Antes de probar los endpoints CRUD (Create, Read, Update, Delete), es crucial seguir estos pasos para configurar el entorno y las variables necesarias.
Configuración de Variables de Entorno
Paso 1: Obtener el Token de Acceso

    Asegúrate de que el endpoint /login sea ejecutado primero para obtener un token de acceso válido.

Paso 2: Configurar la Variable de Entorno para el Token

    En la pestaña Tests del request del endpoint /login, asegúrate de tener el siguiente script:

javascript

var json = JSON.parse(responseBody);
pm.environment.set("token", json.token);

Esto guardará el token generado en la variable de entorno token.
Configuración de Endpoints CRUD

Para cada endpoint CRUD (register, find, update y delete), sigue los pasos a continuación:
Configuración de la Pestaña Authorization

    Type: Selecciona Bearer Token.
    Token: Ingresa {{token}} en este campo. Esta variable token contiene el token de acceso generado en el paso anterior.

Probando los Endpoints CRUD

Una vez que hayas configurado correctamente las variables de entorno y la autorización para los endpoints CRUD, puedes probar cada uno de ellos:

    /register: Endpoint para registrar un nuevo elemento.
    /find: Endpoint para encontrar un elemento existente.
    /update: Endpoint para actualizar un elemento.
    /delete: Endpoint para eliminar un elemento.

Nota: Asegúrate de incluir el token de acceso válido en la autorización para cada solicitud CRUD, de lo contrario, es posible que la operación no se complete correctamente.

Instrucciones para Probar en Postman
Paso 1: Configuración de Variables de Entorno

    Antes de probar los endpoints del CRUD, es necesario obtener un token de autenticación. Asegúrate de ejecutar primero el endpoint de inicio de sesión (/login).

Paso 2: Configuración de Variables de Entorno en la Pestaña Test

    En la pestaña Tests del request del endpoint de inicio de sesión (/login), asegúrate de incluir el siguiente script para almacenar el token en una variable de entorno:

javascript

var json = JSON.parse(responseBody);
pm.environment.set("token", json.token);

Paso 3: Configuración de Autorización para los Endpoints CRUD

    Para cada uno de los endpoints del CRUD (registro, búsqueda, actualización y eliminación), sigue estos pasos:
        En la pestaña Authorization del request en Postman:
            Selecciona el tipo de autorización como Bearer Token.
            En el campo Token, establece el valor como {{token}}.

Nota: El CRUD no funcionará si no se ejecuta primero el endpoint de inicio de sesión y si la variable de entorno token no está configurada correctamente.

