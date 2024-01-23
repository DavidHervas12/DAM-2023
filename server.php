<?php
if ($_SERVER['REQUEST_METHOD'] === "POST") {
    if (isset($_POST["data"])) {
        $data = $_POST['data'];
        $videoInfo = json_decode($data, true);
        $channelTitle = $videoInfo['snippet']['channelTitle'];
        $title = $videoInfo['snippet']['title'];
        $description = $videoInfo['snippet']['description'];
        $videoId = $videoInfo["id"]["videoId"];
        $imageUrl = $videoInfo["snippet"]["thumbnails"]["medium"]["url"];
        $publishTime = $videoInfo['snippet']['publishTime'];

        $servidor = "localhost";
        $usuario = "root";
        $password = "";
        $dbname = "youtube";
        $conexion = mysqli_connect($servidor, $usuario, $password, $dbname);

        if (!$conexion) {
            die("Error en la conexión a MySQL: " . mysqli_connect_error());
        }

        $sql = "INSERT INTO record (title, channel, description, videoId, imageUrl, publishTime) VALUES (?, ?, ?, ?, ?, ?)";
        $stmt = $conexion->prepare($sql);

        $stmt->bind_param("ssssss", $title, $channelTitle, $description, $videoId, $imageUrl, $publishTime);

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

    $sql = "SELECT id, nombre, email FROM usuarios";
    $resultado = $conexion->query($sql);

    $usuarios = array();
    while ($fila = $resultado->fetch_assoc()) {
        $usuarios[] = $fila;
    }

    header('Content-Type: application/json');
    echo json_encode($usuarios);

    $conexion->close();
}
?>