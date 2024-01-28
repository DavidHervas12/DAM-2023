<?php
// Compruebo si es una petición get o post.
if ($_SERVER['REQUEST_METHOD'] === "POST") {
    if (isset($_POST["data"])) {
        $data = $_POST['data'];
        $videoInfo = json_decode($data, true);
        $thumbnail = $videoInfo["snippet"]["thumbnails"]["default"]["url"];
        $title = $videoInfo['snippet']['title'];
        $channelTitle = $videoInfo['snippet']['channelTitle'];
        $videoId = $videoInfo["id"]["videoId"];
        $link = "https://www.youtube.com/watch?v=" . $videoId;
        // Genero el timestamp del momento en el que se ha visualizado el vídeo.
        $timestamp = date("Y-m-d H:i:s");

        $servidor = "localhost";
        $usuario = "root";
        $password = "";
        $dbname = "youtube";
        $conexion = mysqli_connect($servidor, $usuario, $password, $dbname);

        if (!$conexion) {
            die("Error en la conexión a MySQL: " . mysqli_connect_error());
        }

        // Utilizo parámetros para evitar posibles inyecciones sql
        $sql = "INSERT INTO record (thumbnail, title, channel, link, time_stamp) VALUES (?, ?, ?, ?, ?)";
        $stmt = $conexion->prepare($sql);

        $stmt->bind_param("sssss", $thumbnail, $title, $channelTitle, $link, $timestamp);

        if ($stmt->execute()) {
            echo "Registro insertado correctamente.";
        } else {
            echo "Error: " . $sql . "<br>" . $stmt->error;
        }

        $stmt->close();
        mysqli_close($conexion);
    }
} else if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $servidor = "localhost";
    $usuario = "root";
    $password = "";
    $dbname = "youtube";

    $conexion = new mysqli($servidor, $usuario, $password, $dbname);

    if ($conexion->connect_error) {
        die("Conexión fallida: " . $conexion->connect_error);
    }

    if ($conexion->connect_error) {
        die("Conexión fallida: " . $conexion->connect_error);
    }

    // Hago la petición select de esta manera para que empiece por el último vídeo visualizado.
    $sql = "SELECT thumbnail, title, channel, link, time_stamp FROM record ORDER BY time_stamp DESC";
    $resultado = $conexion->query($sql);

    if ($resultado === false) {
        die("Error en la consulta: " . $conexion->error);
    }

    $record = array();
    while ($fila = $resultado->fetch_assoc()) {
        $record[] = $fila;
    }

    header('Content-Type: application/json');
    echo json_encode($record);

    $conexion->close();
}
?>