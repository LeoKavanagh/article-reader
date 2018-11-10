# Article Reader

Use Amazon Polly to convert an article (or the `<p>` tags from any URL) to an mp3 file.

Run the Scala program with
```
sbt "run {{url of the article}}"
```

The article can then be found in a file called `article.mp3`

Compile into a fat jar with
```
sbt assembly
```

The resultant all-in-one jar can be deployed anywhere and run as a normal Java jar
```
java -jar article-reader-fat-jar.jar http://foo.bar/baz
```

