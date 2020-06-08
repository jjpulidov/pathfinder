<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pathfinder</title>
</head>
<body>
<h1>Algoritmo Pahtfinder</h1>
<h2>A través de fichero</h2>
<form action="FileManagement" method="post" enctype="multipart/form-data" target="_blank">
    <p>
        <input type="hidden" name="action" value="fichero"><br/>
        Sube un archivo:
        <input type="file" name="file"><br/>
        <input type="submit" value="Subir archivo">
    </p>
</form>

<h2>A través de teclado</h2>
<form action="FileManagement" method="post" target="_blank">
    <p>
        <input type="hidden" name="action" value="teclado"><br/>
        <label>Parámetro r:<input type="text" name='r'></label><br/>
        <label>Parámetro q:<input type="text" name='q'></label><br/>
        <label>Nombre de los nodos (separados por comas):<input type="text" name="nodos"></label><br/>
        <label>Pares de nodos (separado el par por espacios y los pares por lineas)<br>
            <textarea rows="20" cols="60" name="pares">Entrar los pares aquí...</textarea>
        </label><br>
        <input type="submit" value="Enviar parámetros">
    </p>
</form>
</body>
</html>
