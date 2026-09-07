# TODO persistente — PetDog Rest Assured

Este archivo conserva el plan de trabajo si el contexto de la conversación se comprime. El proyecto se trabaja con GitHub Flow y cada punto se completa en una rama corta de tipo `feature/*`, se revisa, se publica, se integra mediante Pull Request a `main` y se elimina la rama después del merge.

## Criterio operativo para cada feature

1. Partir de `main` actualizado: `git switch main` y `git pull --ff-only`.
2. Crear una rama corta: `git switch -c feature/<nombre-corto>`.
3. Implementar únicamente el punto indicado y documentar el código que se agregue.
4. Verificar: `mvn clean test` y revisar `git diff`/`git status`.
5. Guardar el trabajo: `git add <archivos>` y `git commit -m "<cambio>"`.
6. Publicar: `git push -u origin feature/<nombre-corto>`.
7. Abrir PR contra `main`: `gh pr create --base main --head feature/<nombre-corto> --title "..." --body "..."`.
8. Revisar el diff y los checks; integrar: `gh pr merge --squash --delete-branch`.
9. Sincronizar y limpiar localmente: `git switch main`, `git pull --ff-only`, `git branch -d feature/<nombre-corto>`.
10. Marcar el punto como listo en este archivo dentro del mismo feature o en el siguiente commit de documentación, y confirmar `git status` limpio.

## Puntos de la actividad

- [x] **F0 — Base del repositorio:** inicializar Git en `Intellij/rest-assured`, usar `main` como rama principal, crear el repositorio remoto público `michael-pinzon/rest-assured-petstore` con GitHub CLI y enlazar `origin`.
- [x] **F1 — Build Maven:** completar `pom.xml` con Rest Assured, TestNG, compilación Java 21 y Surefire; comprobar que Maven descubre tests TestNG.
- [x] **F2 — Soporte reutilizable:** crear configuración de la API, cliente Rest Assured y fábricas de datos únicas para que cada prueba pueda preparar su propio estado sin depender de otra.
- [ ] **F3 — Usuarios y sesión:** automatizar crear usuario, login de un usuario creado durante la misma prueba y logout; cada operación debe tener un test distinto y aserciones de contrato.
- [ ] **F4 — Mascotas:** automatizar el listado filtrado por `available` y la consulta de una mascota concreta seleccionada de forma independiente; validar respuesta y datos.
- [ ] **F5 — Orden:** automatizar la creación de una orden para una mascota disponible seleccionada dentro de la prueba; validar la orden creada.
- [ ] **F6 — Entrega:** agregar README con introducción, estructura, ejecución, alcance y decisiones de independencia; ejecutar la verificación final y dejar `main` sincronizado con GitHub.

## Definición de terminado

- Las seis operaciones de la historia tienen un test TestNG independiente.
- Ninguna prueba consume datos creados por otro test; los datos de usuario y mascota necesarios se preparan dentro de cada prueba.
- `mvn clean test` termina correctamente desde un checkout limpio.
- El código tiene comentarios/Javadoc donde aportan contexto y el README permite ejecutar y entender la solución.
- `main` contiene todos los PRs integrados y el repositorio remoto es público.
