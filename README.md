# Article Reader

Use AWS Lambda, Amazon Polly and Telegram Bot to convert article to mp3 file.

Install AWS CLI with `sudo snap install aws-cli` and accept the risks.

Download the entire webpage source.
Extract the text, do some small tidying up and return a single string.

Run the Scala program with
```
sbt "run {{url of the article}}"
```

Then join all the mp3 files into one with a Bash command
```
cat *.mp3 >> article.mp3
```

