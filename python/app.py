from flask import Flask, request, render_template, redirect
import datetime as dt
import subprocess
app = Flask(__name__)


def run_article_reader(url):

    subprocess.run('rm static/article.mp3'.split())
    cmd = 'java -jar static/article-reader.jar {}'.format(url).split()
    subprocess.run(cmd)
    subprocess.run('mv article.mp3 static/articles'.split())

@app.route('/')
def index():
    return redirect('article')

@app.route('/article', methods=['GET', 'POST'])
def article():

    if request.method == 'POST':
        url = request.form.to_dict()['articleurl']
        print('url is {}'.format(url))
        run_article_reader(url)

        return redirect('play')

    return render_template('article.html')

@app.route('/play')
def play():

    return render_template('play_article.html')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)

