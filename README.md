# Editor de codigo de los cracks, also proyecto compilador para mi clase de lenguajes y autómatas
--------------------------------
# NOTAS
- El compilador acepta comentarios normales, ponga <b>//</b> y todo lo que le siga a eso en una linea será ignorado por el compilador
- El compilador no acepta expresiones lógicas
- Dado el punto anterior, el compilador no acepta if ni while, chanclas xd
- Tampoco acepta métodos ni funciones, pero no vi que fueran obligatorios, sólo vi que quería que se manejara el alcance y para eso bastan las clases y los bloques de código sin clase

### Los colores en la tabla de símbolo de hecho son una ayuda para saber el alcance en el que están declaradas las variables y clases.
![Imagen del camino feliz](/img/primerCaminoFeliz.png)

### Recomiendo dar click a F1 para ver los acortadores antes de manejar el programa, para facilitar su navegación.
![Imagen los acortadores](/img/nuevaVentanaAyuda.png)


-----------------------------------
## Lista de weas por hacer
- corregir que el / no se coloree si está solo si está junto a otro /. Actualmente se colorea como si fuera un número, es un bug
- añadir tabulador automático o como se llame
- añadir teclas ctrl+z y ctrl+y
- añadir un buscador
- hacer que el cambio de tema cambie los colores de toda la interfaz, no sólo los colores de la caja de texto
- hacer un mensaje de bienvenida tipo "¿Es tu primera vez usando Crack's Code, crack?" donde te muestre los atajos del programa y algo de info sobre éste
- hacer un mensaje más decente en el dialogo <i>Acerca de</i>
- hacer que los tooltip	sean más bonitos
- hacer un guardado de sesión automático, para recargar la última sesión una vez que se vuelva a abrir el programa
- crear un objeto para manejar un archivo abierto a la vez y hacer una lista de estos objetos, en vez de tener mil listas y montones de métodos en la clase Vista (es la clase de la ventana morro, no de toda la interfaz >:c)