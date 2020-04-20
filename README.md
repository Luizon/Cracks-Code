# Editor de codigo de los cracks, also proyecto compilador para mi clase de lenguajes y autómatas
--------------------------------
# NOTAS
- El compilador no acepta modificadores, no intente añadir public o private
- El compilador acepta comentarios normales, ponga <b>//</b> y todo lo que le siga a eso en una linea será ignorado por el compilador
- El compilador no acepta expresiones aritméticas ni expresiones lógicas, no supe hacerlo y no me di el tiempo suficiente para aprender xd
- El modo oscuro del compilador puede no funcionar perfectamente siempre, se recomienda el claro si le da un problema, personalmente prefiero el oscuro de todos modos

### Los colores en la tabla de símbolo de hecho son una ayuda para saber el alcance en el que están declaradas las variables y clases.
![Imagen del camino feliz](/img/caminoFeliz.png)


---------------------------------------
disculpe usted los comentarios informales profe, no esperaba que lo pidiera por GitHub cuando había hecho el repositorio hace meses jaja


-----------------------------------
## Lista de weas por hacer
### IMPORTANTES
- hacer más compleja la forma en que se detectan strings, en el compilador semántico.
- añadir un contador de lineas o como se llame a la izquierda del texto
### Secundaros
- añadir notas del compilador
- añadir tabulador automático o como se llame
- añadir teclas ctrl+z y ctrl+y
- añadir un buscador
- hacer que el cambio de tema cambie los colores de toda la interfaz, no sólo los colores de la caja de texto
- hacer un mensaje de bienvenida tipo "¿Es tu primera vez usando Crack's Code, crack?" donde te muestre los atajos del programa y algo de info sobre éste
- hacer un mensaje más decente en el dialogo <i>Acerca de</i>
- hacer que los tooltip	sean más bonitos
- añadir la opción de maximizar y restaurar el panel de pestaña de textos, o de minimizar y maximizar el panel de pestañas de <i>Consola</i> y <i>Datos</i>
- hacer un guardado de sesión automático, para recargar la última sesión una vez que se vuelva a abrir el programa
- crear un objeto para manejar un archivo abierto a la vez y hacer una lista de estos objetos, en vez de tener mil listas y montones de métodos en la clase Vista (es la clase de la ventana morro, no de toda la interfaz >:c)