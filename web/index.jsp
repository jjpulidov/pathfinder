<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pathfinder</title>
    <link rel="stylesheet" href="./index.css" type="text/css" media="all">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js" type="text/javascript"></script>
    <script src="index.js"></script>
</head>
<body>
<h1>Algoritmo Pahtfinder</h1>
<form action="FileManagement" method="post" enctype="multipart/form-data" target="_blank">
    <h1>Parámetros comunes</h1>
    <label>Parámetro r:<input type="text" name='r'></label><br/>
    <label>Parámetro q:<input type="text" name='q'></label><br/>
    <label>Nombre de los nodos (separados por comas):<input type="text" name="nodos"></label><br/>
    <input type="radio" id="fichero" name="seleccion_entrada" value="fichero" checked="checked">
    <label for="fichero">Fichero</label><br>
    <input type="radio" id="teclado" name="seleccion_entrada" value="teclado">
    <label for="teclado">Teclado</label><br>
    <div id="params_fichero">
        <h2>A través de fichero</h2>
        <p>
            Sube un archivo: <input type="file" name="file"><br/>
        </p>
    </div>
    <div id="params_teclado">
        <h2>A través de teclado</h2>
        <p>
            <label>Pares de nodos (separado el par por espacios y los pares por lineas)<br>
                <textarea rows="20" cols="60" name="pares">Entrar los pares aquí...</textarea>
            </label><br>
        </p>
    </div>
    <input type="submit" value="Enviar parámetros">
</form>
</body>
</html>
