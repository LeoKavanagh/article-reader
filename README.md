# Article Reader

Use AWS Lambda, Amazon Polly and Telegram Bot to convert article to mp3 file.

## Step 1: Extract text from web page.

Download the entire webpage source using `requests.get`.
Extract the text, do some small tidying up and return a single string.


