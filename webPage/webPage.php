<!DOCTYPE html>
<html>
<head>
    <title>Web Page</title>
    <script>
        function atualizarConteudo() {
            var dataHora = new Date();
            var ip = "<?php echo $_SERVER['REMOTE_ADDR']; ?>";
            var data = dataHora.toDateString();
            var hora = dataHora.toLocaleTimeString();

            if ("geolocation" in navigator) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    var latitude = position.coords.latitude;
                    var longitude = position.coords.longitude;

                    document.getElementById('ip').textContent = 'IP: ' + ip;
                    document.getElementById('data').textContent = 'Data: ' + data;
                    document.getElementById('hora').textContent = 'Hora: ' + hora;
                    document.getElementById('localizacao').textContent = 'Localização: ' + latitude + ', ' + longitude;
                });
            } else {
                document.getElementById('localizacao').textContent = 'Localização: Não disponível';
            }
        }
    </script>
</head>
<body onload="atualizarConteudo();">
    <p id="ip">IP: Carregando...</p>
    <p id="data">Data: Carregando...</p>
    <p id="hora">Hora: Carregando...</p>
    <p id="localizacao">Localização: Carregando...</p>
</body>
</html>
