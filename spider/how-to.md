# What's this?

- Паук scrapy от Камиля, сяб <3
сохранит всё в json, чтобы изменить глубину измени DEPTH_LIMIT, сам паук quotes_spider

# How to use for the first time?

1. Установить все нужные либы
https://docs.scrapy.org/en/latest/intro/install.html
`sudo apt-get install python3 python3-dev python3-pip libxml2-dev libxslt1-dev zlib1g-dev libffi-dev libssl-dev`

2. Создать virtualenv, находясь в директории spider/
https://docs.python.org/3/tutorial/venv.html#tut-venv
`python3 -m venv pagerank`

3. Перейти в директорию pagerank, активировать virtual environment
`source bin/activate`

4. Установить Scrapy.
`pip install scrapy`

5. Отредактировать файлик quotes_spider с настройками для запуска, изменить start_urls и DEPTH_LIMIT.

6. Запустить паука
`scrapy crawl links -o result.json`

# How to use if already used it? 
(like everything was installed)

Go through
3, 5 and 6 