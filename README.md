**Создал:** Чирков Артём Андреевич
<br>Московский авиационный институт (МАИ)
<br>Институт №8 «Компьютерные науки и прикладная математика»
<br>Кафедра № 806 «Вычислительная математика и программирование» 
Группа М8О-206М-22

## Описание
Инструмент для сборки конвертеров, созданных на основе Coco/R.

## Руководство по использованию
Тебуемые инструменты: Java 21.0.3, Javac 21.0.3, Jar 17.0.10.

Для запуска генератора нужно ввести следующую команду: 
```console
java -jar ConverterGen.jar <args_file>
``` 
В качестве аргумента args_file передается название файла с расширением .json, хранящий все параметры сборки конвертера, либо директория, в которой находится файл args.json. Все пути прописываются относительно расположения файла. 
<br>Пример можно увидеть по пути [converters/Java/C#](./converters/Java/C#).

<details>
  <summary>Параметры запуска</summary>
  
  | Имя параметра    | Тип данных   | Обязательный | Стандартное значение                          | Описание                                                                                               |
|------------------|--------------|--------------|-----------------------------------------------|--------------------------------------------------------------------------------------------------------|
| package          | Строка       | Нет          | "project"                                     | Название пакета в Java                                                                                 |
| jarName          | Строка       | Нет          | "project"                                     | Название формирующегося jar-файла                                                                      |
| grammar          | Строка       | Да           | -                                             | Путь к файлу .atg                                                                                      |
| writers          | Строка       | Да           | -                                             | Название класса, наследующего класс Core.Writers                                                       |
| saveTempFiles    | Булеан       | Нет          | false                                         | Если установлено значение true, программа не будет удалять временные файлы                             |
| frames.directory | Строка       | Нет          | null                                          | Директория, в которой расположены все стандартные фреймы. Если значение null, то параметр игнорируется |
| frames.scanner   | Строка       | Нет          | Путь к файлу Scanner.frame в ресурсах проекта | Путь к файлу, в котором описывается сканер для Coco/R                                                  |
| frames.parser    | Строка       | Нет          | Путь к файлу Parser.frame в ресурсах проекта  | Путь к файлу, в котором описывается парсер для Coco/R                                                  |
| frames.main      | Строка       | Нет          | Путь к файлу Main.frame в русурсах проекта    | Путь к файлу, содержащий главный класс конвертера                                                      |
| frames.core      | Строка       | Нет          | Путь к файлу Core.frame в ресурсах проекта    | Путь к файлу, хранящему все функции и объекты для создания АСД и перевода его в строковый формат       |
| frames.other     | Массив строк | Нет          | Пустой массив                                 | Массив путей к дополнительным фреймам                                                                  |
</details>