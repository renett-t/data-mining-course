import scrapy


class LinksSpider(scrapy.Spider):
    name = "links"
    start_urls = [
        "https://greenpeace.ru/"
    ]
    custom_settings = {
        "DEPTH_LIMIT": 3
    }

    def parse(self, response):
        links = response.css("a::attr(href)").getall()

        for i in range(len(links)):
            if not links[i].startswith("http"):
                links[i] = response.urljoin(links[i])

        yield {
            "parent": response.request.url,
            "children": links
        }
        for link in links:
            yield scrapy.Request(link, callback=self.parse)
