Для развертывания кластера kafka требуется:

1. Получить KAFKA_KRAFT_CLUSTER_ID. Для этого запустить команду docker run --rm apache/kafka:3.7.0 kafka-storage.sh random-uuid
2. Записать полученыый KAFKA_KRAFT_CLUSTER_ID в docker-compose файл. 
3. Выполнить docker compose up -d в директории с созданным файлом docker-compose.yml
4. Проверить логи контейнеров с kafka с помощью команды docker logs
5. Проверить доступность brokers на странице с kafka ui.
6. Проверить то, что кластер обозначен, как online на странице с kafka ui.
