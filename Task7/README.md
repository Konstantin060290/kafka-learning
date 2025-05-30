Задание 1.Развёртывание и настройка Kafka-кластера в Yandex Cloud

В ходе выполнения работы, был развернут кластер 'Managed Service for Kafka' из 3 брокеров в Yandex Cloud.

Характеристики кластера:

![cluster.png](Task7_1/images/cluster.png)

![cluster2.png](Task7_1/images/cluster2.png)

- Был создан топик, настроено время жизни сегмента лога, размер файла сегмента:

![topic.png](Task7_1/images/topic.png)

- Были созданы пользователи, им назначены права на топик test-topic:

![users.png](Task7_1/images/users.png)

- Зарегистрирована схема данных:

![scheme.png](Task7_1/images/scheme.png)

- Ответы get запросов схем:

![scheme_versions.png](Task7_1/images/scheme_versions.png)

![scheme_1.png](Task7_1/images/scheme_1.png)

![scheme_2.png](Task7_1/images/scheme_2.png)

- Были реализованы простой консьюмер и продьюсер, читающий и записывающие соответственно данные в кластер, развернутый в Yandex Cloud.
- Лог продсьюсера:

![producer.png](Task7_1/images/producer.png)

- Лог консьюмера:
![consumer.png](Task7_1/images/consumer.png)

Выводы: Yandex Cloud предлагает удобные и мощные инструменты с простым и понятным UI для разворачивания инфраструктуры в облаке.
Данные инструменты существенно сокращают трудозатраты на разворачивание, сопровождение и поддержку систем, а также сокращают требования
к квалификации задействованного в обслуживании систем персонала.