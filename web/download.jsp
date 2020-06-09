<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Download</title>
</head>
<body>
<H1>Descarga de ficheros</H1>
<a href="<%out.println(request.getAttribute("fichero_salida"));%>">Fichero de salida</a><br>
<a href="<%out.println(request.getAttribute("fichero_estadisticas"));%>">Fichero de estadisticas</a>
</body>
</html>
