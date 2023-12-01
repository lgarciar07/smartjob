---

# SmartJob API - Guía de Pruebas en Postman

Este repositorio contiene una API Restful llamada SmartJob, la cual proporciona operaciones CRUD (Crear, Leer, Actualizar y Eliminar) para gestionar datos específicos. Asegúrate de seguir estos pasos para probar correctamente la API en Postman.

## Configuración de Variables de Entorno

1. **Variable de Entorno `token`:** Esta variable almacenará el token de autenticación requerido para acceder a los endpoints protegidos.

    - Para obtener el token, asegúrate de ejecutar primero el endpoint de login.
    - En la pestaña "Tests" de la solicitud de login, asegúrate de tener la siguiente configuración para establecer la variable de entorno `token`:

        ```javascript
        var json = JSON.parse(responseBody);
        pm.environment.set("token", json.token);
        ```

## Configuración de Autorización para Endpoints CRUD

2. **Endpoints CRUD (Crear, Leer, Actualizar, Eliminar):** Estos endpoints requieren autorización mediante un token válido en la cabecera de la solicitud.

    - Para cada solicitud CRUD (register, find, update, delete):
        - En la pestaña "Authorization" de Postman, configura lo siguiente:
            - **Type:** Bearer Token
            - **Token:** `{{token}}`

    - **Nota:** Asegúrate de que el endpoint de login se ejecute primero para obtener el token antes de acceder a los endpoints CRUD. Sin un token válido, estos endpoints no funcionarán.

## Ejemplo de Uso

1. **Login (Obtener Token):**

    - Método: POST
    - URL: `localhost:8080/bci/login?username=admin&password=Insert1991$`
    - Cuerpo (si es necesario): `{ "usuario": "ejemplo", "contraseña": "123456" }`
    - Descripción: Inicia sesión y obtén el token de autenticación.

2. **Registro de Datos:**

   - Método: POST
   - URL: `localhost:8080/bci/api/users/register`
   - Cuerpo:
       ```json
       {
           "name": "Luis Gustavo Garcia Reyna",
           "email": "lgr.developer.07@gmail.com",
           "password": "smartJob",
           "phones": [
               {
                   "number": "951399508",
                   "cityCode": "01",
                   "countryCode": "51"
               },
               {
                   "number": "991661322",
                   "cityCode": "044",
                   "countryCode": "51"
               }
           ]
       }
       ```
   - Descripción: Registra un nuevo usuario con la información proporcionada.


3. **Buscar Datos:**

    - Método: GET
    - URL: `localhost:8080/bci/api/users/find/{id}`
    - {id}: Valor obtenido en el response del Registro de Datos (endPoint register)
    - Descripción: Obtiene los datos almacenados.

4. **Actualizar Datos:**

    - Método: PUT
    - URL: `localhost:8080/bci/api/users/update/{id}`
    - {id}: Valor obtenido en el response del Registro de Datos (endPoint register)
    - Cuerpo:
       ```json
       {
           "name": "Luis Garcia Reyna",
           "email": "lgarcia.reyna.07@gmail.com",
           "password": "bci",
           "phones": [
               {
                   "number": "951399508",
                   "cityCode": "04",
                   "countryCode": "01"
               },
               {
                   "number": "991661322",
                   "cityCode": "044",
                   "countryCode": "51"
               }
           ]
       }
       ```
   - Descripción: Registra un nuevo usuario con la información proporcionada.

5. **Eliminar Datos:**

    - Método: DELETE
    - URL: `localhost:8080/bci/api/users/delete/{id}`
    - {id}: Valor obtenido en el response del Registro de Datos (endPoint register)
    - Descripción: Elimina los datos según el ID proporcionado.

---

# SWAGGER

http://localhost:8080/bci/swagger-ui.html

---

Asegúrate de ajustar las URL y los datos de solicitud según la configuración específica de tu API. Esta guía te ayudará a realizar pruebas utilizando Postman y asegurarte de configurar adecuadamente la autorización con el token obtenido del endpoint de login.