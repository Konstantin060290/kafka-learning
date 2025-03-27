Для развертывания кластера kafka требуется:

1. Получить KAFKA_KRAFT_CLUSTER_ID. Для этого запустить команду docker run --rm apache/kafka:3.7.0 kafka-storage.sh random-uuid
2. Выполнить docker compose up -d в директории с созданным файлом docker-compose.yml
3. Проверить логи контейнеров с kafka с помощью команды docker logs
4. Проверить доступность brokers на странице с kafka ui.
