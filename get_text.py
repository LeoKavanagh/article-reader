import sys
import requests
from lxml import html

def get_text(url):
    """
    Read a full web page, extract text from <p> tags
    :param url: string
    :return: string
    """

    page = requests.get(url)
    tree = html.fromstring(page.content)
    
    text = ' '.join(tree.xpath('//p/text()')) \
            .replace('\n', '') \
            .replace('\t', '') \
            .split('\xa0')[0]

    print(text)
    return text

if __name__ == '__main__':
    args = sys.argv[1:]
    get_text(*args)

